package com.example.hotsix.service.build;

import com.example.hotsix.client.GrafanaClient;
import com.example.hotsix.dto.build.*;
import com.example.hotsix.enums.JenkinsJobType;
import com.example.hotsix.model.ServiceSchedule;
import com.example.hotsix.enums.BuildStatus;
//import com.example.hotsix.model.TeamProjectCredential;
import com.example.hotsix.model.project.*;
import com.example.hotsix.repository.build.*;
import com.example.hotsix.repository.member.MemberRepository;
import com.example.hotsix.repository.team.TeamProjectInfoRepository;
import com.example.hotsix.repository.team.TeamRepository;
import com.example.hotsix.util.TimeUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.offbytwo.jenkins.model.Build;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BuildServiceImpl implements BuildService{
    private final BuildRepository buildRespository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamProjectInfoRepository teamProjectInfoRepository;
    private final ServiceScheduleRepository serviceScheduleRepository;
    private final BuildStageRepository buildStageRepository;
    private final BuildResultRepository buildResultRepository;

    private static final String GIT_TYPE = "git";
    private static final String DOCKER_TYPE = "docker";
    private static final String JOB_NAME = "service-deploy";
    private static final String BUILD_WITH_PARAMETERS_URL = "job/"+JOB_NAME+"/buildWithParameters";
    private final BuildLogRepository buildLogRepository;
    private final GrafanaClient grafanaClient;
    private final BuildJenkinsJobRepository buildJenkinsJobRepository;

    @Value("${jenkins.url}")
    private String hostJenkinsUrl;

    @Value("${jenkins.username}")
    private String hostJenkinsUsername;

    @Value("${jenkins.token}")
    private String hostJenkinsToken;

    // 현재는 쓰지 않는 메서드
    @Override
    @Transactional
    public void buildStart(Long teamId, Long teamProjectInfoId){
        TeamProjectInfo teamProjectInfo = teamProjectInfoRepository.findProjectInfoByProjectInfoId(teamProjectInfoId);
        ServiceSchedule emptyServiceId = serviceScheduleRepository.findEmptyService(teamProjectInfo);
        log.info("empty Service Schedule = {}",emptyServiceId);
        emptyServiceId.setBuildStatus(BuildStatus.PENDING);
        emptyServiceId.setTeam(teamProjectInfo.getTeam());
        emptyServiceId.setTeamProjectInfo(teamProjectInfo);
        serviceScheduleRepository.save(emptyServiceId);
        String servicePort = String.valueOf(emptyServiceId.getId());
        try(CloseableHttpClient httpClient = createHttpClient(hostJenkinsUsername, hostJenkinsToken)){
            String crumb = getCrumb(httpClient, hostJenkinsUrl, hostJenkinsUsername, hostJenkinsToken);


        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void deployStop(Long serviceScheduleId){
        ServiceSchedule serviceSchedule = serviceScheduleRepository.findServiceScheduleByServiceScheduleId(serviceScheduleId);
        serviceSchedule.setTeam(null);
        serviceSchedule.setTeamProjectInfo(null);
        serviceSchedule.setBuildStatus(BuildStatus.EMPTY);

        try(CloseableHttpClient httpClient = createHttpClient(hostJenkinsUsername, hostJenkinsToken)){
            String crumb = getCrumb(httpClient, hostJenkinsUrl, hostJenkinsUsername, hostJenkinsToken);
            String[] crumbParts = crumb.split(":");
            String crumbFieldName = crumbParts[0];
            String crumbValue = crumbParts[1];
            String hostJobName = hostJenkinsUrl+"job/built_in_backend_stop/buildWithParameters";
            HttpPost httpPost = new HttpPost(hostJobName);
            httpPost.setHeader(crumbFieldName, crumbValue);
            httpPost.setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((hostJenkinsUsername + ":" + hostJenkinsToken).getBytes()));
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("SERVICE_NUM", String.valueOf(serviceScheduleId)));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, StandardCharsets.UTF_8);
            httpPost.setEntity(entity);
            System.out.println("Call HttpClient execute");
            HttpResponse response = httpClient.execute(httpPost);
            System.out.println("Response status: " + response.getStatusLine().getStatusCode());
            System.out.println("Response body: " + EntityUtils.toString(response.getEntity()));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public BuildResult addWholeBuildResult(BuildResultDto buildResultDto) throws Exception {
        Long deployNum = buildResultDto.getBuildNum();
        Long teamProjectInfoId = buildResultDto.getTeamProjectInfoId();

        // 1. deployNum 에 해당하는 BuildResult 가 없을경우, 새로 생성
        BuildResult buildResult = buildRespository.findByDeployNumAndTeamProjectInfoId(deployNum, teamProjectInfoId)
                .orElseGet(() -> addBuildResult(deployNum, teamProjectInfoId));

        // 2. BuildJenkinsJob 데이터 생성
        BuildJenkinsJob buildJenkinsJob = BuildJenkinsJob.builder()
                .buildResult(buildResult)
                .buildNum(buildResultDto.getBuildNum())
                .jobName(buildResultDto.getJobName())
                .result(BuildStatus.valueOf(buildResultDto.getResult()))
                .jobType(JenkinsJobType.valueOf(buildResultDto.getJobType().toUpperCase()))
                .build();

        // 2-1. 저장
        buildJenkinsJobRepository.save(buildJenkinsJob);

        // 3. Build 결과를 받아오기 위한 Jenkins API 호출
        String jobName = buildResultDto.getJobName();
        Long buildNum = buildResultDto.getBuildNum();
        String targetUrl = String.format("%sjob/%s/%d/wfapi/describe", hostJenkinsUrl, jobName, buildNum);

        // todo: 다른 데로 옮기기
        // jenkins 서버 모니터링용 grafana dashBoard 추가
        String uId = addGrafanaDashboard(buildResultDto);
//        System.out.println("uId = " + uId);

        System.out.println("targetUrl = " + targetUrl);

        WebClient client = WebClient.builder()
                .baseUrl(targetUrl)
                .build();

        // 4. API 응답 결과를 JsonNode 형식으로 변환
        JsonNode jsonNode = client.get()
                .headers(headers -> headers.setBasicAuth(hostJenkinsUsername, hostJenkinsToken))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        // 5. BuildResult의 Status 변경
//        String wholeStatus = jsonNode.get("status").asText();
//        buildResult.setStatus(BuildStatus.valueOf(wholeStatus.toUpperCase()));

        // 6. Stages 추가
        JsonNode stageNode = jsonNode.get("stages");

        if (stageNode != null && stageNode.isArray()) {
            ArrayNode stagesArrayNode = (ArrayNode) stageNode;
            ArrayList<BuildStage> buildStages = new ArrayList<>();
            ArrayList<BuildLog> buildLogs = new ArrayList<>();

            for (JsonNode stage : stagesArrayNode) {
                long stageId = stage.get("id").asLong();
                String name = stage.get("name").asText();
                int duration = stage.get("durationMillis").asInt();
                String status = stage.get("status").asText();

                BuildStage buildStage = BuildStage.builder()
                        .buildJenkinsJob(buildJenkinsJob)
                        .stageId(stageId)
                        .name(name)
                        .duration(duration)
                        .status(status.toUpperCase())
                        .build();

                buildStages.add(buildStage);

                // 6. 각 stage에 대해, log 결과 저장
                // /job/:job-name/:run-id/execution/node/:node-id/wfapi/log
                String describeUrl = String.format("%sjob/%s/%d/execution/node/%s/wfapi/describe", hostJenkinsUrl, jobName, buildNum, stageId);

                System.out.println("describeUrl = " + describeUrl);

                WebClient describeClient = WebClient.builder()
                        .baseUrl(describeUrl)
                        .build();

                JsonNode describeNode = describeClient.get()
                        .headers(headers -> headers.setBasicAuth(hostJenkinsUsername, hostJenkinsToken))
                        .retrieve()
                        .bodyToMono(JsonNode.class)
                        .block();

                JsonNode logNodes = describeNode.get("stageFlowNodes");
                if (logNodes != null && logNodes.isArray()) {
                    ArrayNode logArrayNode = (ArrayNode) logNodes;

                    for (JsonNode log : logArrayNode) {
                        String title = log.get("name").asText();

                        BuildLog buildLog = BuildLog.builder()
                                .buildStage(buildStage)
                                .title(title)
                                .build();

                        System.out.println("log = " + log);

                        String href = log.get("_links").get("self").get("href").asText();
                        String logUrl = concatenateUrls(hostJenkinsUrl, href);

                        System.out.println("logUrl = " + logUrl);

                        WebClient logClient = WebClient.builder()
                                .baseUrl(logUrl)
                                .build();

                        JsonNode logNode = logClient.get()
                                .headers(headers -> headers.setBasicAuth(hostJenkinsUsername, hostJenkinsToken))
                                .retrieve()
                                .bodyToMono(JsonNode.class)
                                .block();

                        System.out.println("logNode = " + logNode);

                        JsonNode text = logNode.get("text");
                        if (text != null) {
                            String description = text.asText();
                            buildLog.setDescription(description);
                        }

                        buildLogs.add(buildLog);
                    }
                }
            }

            // stages와 logs를 한 번에 저장
            buildStageRepository.saveAll(buildStages);
            buildLogRepository.saveAll(buildLogs);
        }

        buildRespository.save(buildResult);
        return buildResult;
    }

    @Override
    public BuildWholeDto getBuildResultInfo(Long teamProjectInfoId) {
        List<BuildResult> buildResults = buildRespository.findByTeamProjectInfoId(teamProjectInfoId);
        System.out.println("buildResults = " + buildResults.size());


        // todo: 이 부분 수정해야 함
        List<BuildResultInfoDto> buildResultInfoDtos = buildResults.stream()
                .map(buildResult -> {
                    BuildResultInfoDto buildResultInfoDto = BuildResultInfoDto.from(buildResult);
                    buildResultInfoDto.setBuildStages(getBuildStageInfoList(buildResultInfoDto.getBuildId()));
                    return buildResultInfoDto;
                })
                .toList();

        return BuildWholeDto.builder()
                .totalCount(buildResultInfoDtos.size())
                .buildResults(buildResultInfoDtos)
                .build();
    }


    private String addGrafanaDashboard(BuildResultDto buildResultDto) throws Exception {
        return grafanaClient.addGrafanaDashboard(buildResultDto.getServiceNum());
    }

    @Override
    public BuildStartDto wholeBuildStart(Long projectInfoId) {
        TeamProjectInfo teamProjectInfo = teamProjectInfoRepository.findProjectInfoByProjectInfoId(projectInfoId);
        Long teamId = teamProjectInfo.getTeam().getId();
        ServiceSchedule emptyServiceSchedule = serviceScheduleRepository.findEmptyService(teamProjectInfo);

        System.out.println("empty Schedule = "+emptyServiceSchedule);

        // 예외 처리
        // 비어 있는 서비스 포트가 없을 경우
        if (emptyServiceSchedule == null) {
            return BuildStartDto.builder()
                    .buildStatus("FAILED")
                    .build();
        }

        emptyServiceSchedule.setBuildStatus(BuildStatus.PENDING);
        emptyServiceSchedule.setTeamProjectInfo(teamProjectInfo);
        serviceScheduleRepository.save(emptyServiceSchedule);
        Long serviceNum = emptyServiceSchedule.getId();

        try(CloseableHttpClient httpClient = createHttpClient(hostJenkinsUsername, hostJenkinsToken)){
            String crumb = getCrumb(httpClient, hostJenkinsUrl, hostJenkinsUsername, hostJenkinsToken);
            createJenkinsWholeInstance(teamProjectInfo, httpClient, crumb, serviceNum, projectInfoId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    @Transactional
    public BuildCheckDto buildCheck(Long memberId, Long projectInfoId) {
        TeamProjectInfo teamProjectInfo = teamProjectInfoRepository.findProjectInfoByProjectInfoId(projectInfoId);

        System.out.println("teamProjectInfo = " + teamProjectInfo.getId());

        // 1. 현재 teamProjectInfo 기준으로 서비스하고 있는지 여부를 판단
        ServiceSchedule curServiceSchedule = serviceScheduleRepository.findByTeamProjectInfo(teamProjectInfo);
        // 현재 서비스하고 있을 경우
        if (curServiceSchedule != null) {
            return null;
        }

        // 2. 비어 있는 서비스가 있는지 여부를 판단
        ServiceSchedule emptyService = serviceScheduleRepository.findEmptyService(teamProjectInfo);

        // 비어 있는 서비스가 없을 경우
        if (emptyService == null) {
            return null;
        }

        emptyService.setBuildStatus(BuildStatus.PENDING);
        emptyService.setTeam(teamProjectInfo.getTeam());
        emptyService.setTeamProjectInfo(teamProjectInfo);
        serviceScheduleRepository.save(emptyService);

        Long serviceNum = emptyService.getId();

        List<BuildResult> allByTeamProjectInfoOrderByDeployNumDesc = buildResultRepository.findAllByTeamProjectInfoOrderByDeployNumDesc(teamProjectInfo);
        long deployNum = 1L;
        if (!allByTeamProjectInfoOrderByDeployNumDesc.isEmpty()) {
            deployNum = allByTeamProjectInfoOrderByDeployNumDesc.get(0).getDeployNum() + 1;
        }

        return BuildCheckDto.builder()
                .serviceNum(serviceNum)
                .deployNum(deployNum)
                .build();
    }

    @Override
    @Transactional
    public BuildResult insertBuildResult(Long teamProjectInfoId, Long deployNum) {

        log.info("insertBuildResult 실행");

        TeamProjectInfo teamProjectInfo = teamProjectInfoRepository.findProjectInfoByProjectInfoId(teamProjectInfoId);
        BuildResult buildResult = BuildResult.builder()
                .teamProjectInfo(teamProjectInfo)
                .deployNum(deployNum)
                .status(BuildStatus.PENDING)
                .build();

        buildResultRepository.save(buildResult);
        return buildResult;
    }

    @Override
    public void startJenkisJob(DeployConfig deployConfig) {
        JenkinsJobType jobType = deployConfig.getJobType();

        try(CloseableHttpClient httpClient = createHttpClient(hostJenkinsUsername, hostJenkinsToken)){
            String crumb = getCrumb(httpClient, hostJenkinsUrl, hostJenkinsUsername, hostJenkinsToken);

            if (jobType.equals(JenkinsJobType.SETUP)) {
                log.info("setup jenkins job 실행");
                createJenkinsSetupInstance(httpClient, crumb, deployConfig);
            } else if (jobType.equals(JenkinsJobType.BACKEND)) {
                createJenkinsBackendInstance(httpClient, crumb, deployConfig);
            } else if (jobType.equals(JenkinsJobType.DATABASE)) {

            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }



    /*
      시연용으로, built_in_full_docker_copy pipeline을 이용하여, 백 + 프론트 + db 배포를 한꺼번에 수행
     */
    private void createJenkinsWholeInstance(TeamProjectInfo teamProjectInfo, CloseableHttpClient httpClient, String crumb, Long serviceNum, Long projectInfoId) throws IOException {
        Integer serviceIndex = 0;
        System.out.println("Crumb = "+crumb);
        String[] crumbParts = crumb.split(":");
        String crumbFieldName = crumbParts[0];
        String crumbValue = crumbParts[1];

        BackendConfig backendConfig = teamProjectInfo.getBackendConfigs().get(0);
        FrontendConfig frontendConfig = teamProjectInfo.getFrontendConfigs().get(0);
        DatabaseConfig databaseConfig = teamProjectInfo.getDatabaseConfigs().get(0);

        BuildConfigDto buildConfigDto = BuildConfigDto.builder()
                .serviceNum(serviceNum)
                .projectInfoId(projectInfoId)
                .gitBackBranch(backendConfig.getGitBranch())
                .gitBackUsername(backendConfig.getGitUsername())
                .gitBackAccessToken(backendConfig.getGitAccessToken())
                .gitFrontBranch(frontendConfig.getGitBranch())
                .gitFrontUsername(frontendConfig.getGitUsername())
                .gitFrontAccessToken(frontendConfig.getGitAccessToken())
//                .databaseDatabase(databaseConfig.)
                .databaseUsername(databaseConfig.getUsername())
                .databasePassword(databaseConfig.getPassword())
                .build();

        String hostJobName = hostJenkinsUrl + "job/built_in_full_docker_copy/buildWithParameters";

        HttpPost httpPost = new HttpPost(hostJobName);
        httpPost.setHeader(crumbFieldName, crumbValue);
        httpPost.setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((hostJenkinsUsername + ":" + hostJenkinsToken).getBytes()));

        String eigenServiceName = UUID.randomUUID().toString();
        List<NameValuePair> params = new ArrayList<>();
        // JDK 17 이런식으로 jdk하는건 아직 구현 안함.
        // Gradle 같이 Build 도구도 아직 구현 안함.
        params.add(new BasicNameValuePair("SERVICE_NUM", String.valueOf(serviceNum)));
        params.add(new BasicNameValuePair("PROJECT_INFO_ID", String.valueOf(projectInfoId)));
        params.add(new BasicNameValuePair("GIT_BACK_URL", buildConfigDto.getGitBackUrl()));
        params.add(new BasicNameValuePair("GIT_BACK_BRANCH", buildConfigDto.getGitBackBranch()));
        params.add(new BasicNameValuePair("GIT_BACK_USERNAME", buildConfigDto.getGitBackUsername()));
        params.add(new BasicNameValuePair("GIT_BACK_ACCESS_TOKEN", buildConfigDto.getGitBackAccessToken()));
        params.add(new BasicNameValuePair("GIT_FRONT_URL", buildConfigDto.getGitBackUrl()));
        params.add(new BasicNameValuePair("GIT_FRONT_BRANCH", buildConfigDto.getGitBackBranch()));
        params.add(new BasicNameValuePair("GIT_FRONT_USERNAME", buildConfigDto.getGitBackUsername()));
        params.add(new BasicNameValuePair("GIT_FRONT_ACCESS_TOKEN", buildConfigDto.getGitBackAccessToken()));
        params.add(new BasicNameValuePair("GIT_FRONT_USERNAME", buildConfigDto.getGitBackUsername()));
        params.add(new BasicNameValuePair("GIT_FRONT_ACCESS_TOKEN", buildConfigDto.getGitBackAccessToken()));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, StandardCharsets.UTF_8);
        httpPost.setEntity(entity);
        System.out.println("Call HttpClient execute");
        HttpResponse response = httpClient.execute(httpPost);
        System.out.println("Response status: " + response.getStatusLine().getStatusCode());
        System.out.println("Response body: " + EntityUtils.toString(response.getEntity()));
    }

    private List<BuildStageInfoDto> getBuildStageInfoList(Long buildResultId) {
        List<BuildStage> buildStageList = buildStageRepository.findByBuildJenkinsJobId(buildResultId);
        return buildStageList.stream()
                .map(buildStage -> {
                    BuildStageInfoDto buildStageInfoDto = BuildStageInfoDto.from(buildStage);
                    buildStageInfoDto.setBuildLogs(getBuildLogInfoList(buildStageInfoDto.getStageId()));
                    return buildStageInfoDto;
                })
                .toList();
    }

    private List<BuildLogInfoDto> getBuildLogInfoList(Long buildStageId) {
        List<BuildLog> buildLogs = buildLogRepository.findByBuildStageId(buildStageId);
        return buildLogs.stream()
                .map(BuildLogInfoDto::from)
                .toList();
    }


    private String concatenateUrls(String baseUrl, String path) throws URISyntaxException {
        URI baseUri = new URI(baseUrl);
        URI resolvedUri = baseUri.resolve(path);
        return resolvedUri.toString();
    }

    @Transactional
    public BuildResult addBuildResult(Long deployNum, Long teamProjectInfoId) {
        TeamProjectInfo teamProjectInfo = teamProjectInfoRepository.findProjectInfoByProjectInfoId(teamProjectInfoId);

        BuildResult buildResult = BuildResult.builder()
                .deployNum(deployNum)
                .teamProjectInfo(teamProjectInfo)
                .buildTime(TimeUtil.getCurrentTimeMillisSeoul())
                .status(BuildStatus.SUCCESS)
                .build();

        buildRespository.save(buildResult);
        return buildResult;
    }

    private void createJenkinsBackendInstance(CloseableHttpClient httpClient,
                                            String crumb,
                                            DeployConfig deployConfig) {
        System.out.println("Crumb = "+crumb);
        String[] crumbParts = crumb.split(":");
        String crumbFieldName = crumbParts[0];
        String crumbValue = crumbParts[1];

        String hostJobName = hostJenkinsUrl + "job/deploy_setup/buildWithParameters";
        HttpPost httpPost = new HttpPost(hostJobName);
        httpPost.setHeader(crumbFieldName, crumbValue);
        httpPost.setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((hostJenkinsUsername + ":" + hostJenkinsToken).getBytes()));

        System.out.println("deployConfig = " + deployConfig);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("SERVICE_NUM", String.valueOf(deployConfig.getServiceNum())));
        params.add(new BasicNameValuePair("PROJECT_ID", String.valueOf(deployConfig.getTeamProjectInfoId())));
        params.add(new BasicNameValuePair("MEMBER_ID", String.valueOf(deployConfig.getMemberId())));
        params.add(new BasicNameValuePair("DEPLOY_NUM", String.valueOf(deployConfig.getDeployNum())));
        params.add(new BasicNameValuePair("JOB_TYPE", String.valueOf(deployConfig.getJobType())));
        params.add(new BasicNameValuePair("ACCESS_TOKEN", String.valueOf(deployConfig.getAccessToken())));

        // todo: 나중에 여러 개의 jenkinsJob을 build 가능하도록 수정
        // 현재는 첫 번째 세팅만 jenkins build 실행
        BackendConfigDto backendConfigDto = deployConfig.getBackendConfigs().get(0);
        params.add(new BasicNameValuePair("GIT_URL", backendConfigDto.getGitUrl()));
        params.add(new BasicNameValuePair("GIT_USERNAME", backendConfigDto.getGitUsername()));
        params.add(new BasicNameValuePair("GIT_BRANCH", backendConfigDto.getGitBranch()));
        params.add(new BasicNameValuePair("GIT_ACCESS_TOKEN", backendConfigDto.getGitAccessToken()));

        log.info("params: {}", params);

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, StandardCharsets.UTF_8);
        httpPost.setEntity(entity);
        System.out.println("Call HttpClient execute");
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            System.out.println("Response status: " + response.getStatusLine().getStatusCode());
            System.out.println("Response body: " + EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private void createJenkinsSetupInstance(CloseableHttpClient httpClient,
                                            String crumb,
                                            DeployConfig deployConfig) {
        System.out.println("Crumb = "+crumb);
        String[] crumbParts = crumb.split(":");
        String crumbFieldName = crumbParts[0];
        String crumbValue = crumbParts[1];

        String hostJobName = hostJenkinsUrl + "job/deploy_setup/buildWithParameters";
        HttpPost httpPost = new HttpPost(hostJobName);
        httpPost.setHeader(crumbFieldName, crumbValue);
        httpPost.setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((hostJenkinsUsername + ":" + hostJenkinsToken).getBytes()));

        System.out.println("deployConfig = " + deployConfig);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("SERVICE_NUM", String.valueOf(deployConfig.getServiceNum())));
        params.add(new BasicNameValuePair("PROJECT_ID", String.valueOf(deployConfig.getTeamProjectInfoId())));
        params.add(new BasicNameValuePair("MEMBER_ID", String.valueOf(deployConfig.getMemberId())));
        params.add(new BasicNameValuePair("DEPLOY_NUM", String.valueOf(deployConfig.getDeployNum())));
        params.add(new BasicNameValuePair("JOB_TYPE", String.valueOf(deployConfig.getJobType())));
        params.add(new BasicNameValuePair("ACCESS_TOKEN", String.valueOf(deployConfig.getAccessToken())));

        System.out.println("params = " + params);

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, StandardCharsets.UTF_8);
        httpPost.setEntity(entity);
        System.out.println("Call HttpClient execute");
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            System.out.println("Response status: " + response.getStatusLine().getStatusCode());
            System.out.println("Response body: " + EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    @Transactional
    public boolean MemberProjectBuildStart(Long teamId, Long projectInfoId){
//        TeamProjectInfo memberProjectInfo = buildRespository.findTeamProjectInfoByMemberAndInfoId(memberId, projectInfoId);
//        TeamProjectCredential memberProjectCredential = member.getMemberProjectCredential();
//
//        try (CloseableHttpClient httpClient = createHttpClient(hostJenkinsUsername, hostJenkinsToken)){
//            String crumb = getCrumb(httpClient, hostJenkinsUrl, hostJenkinsUsername, hostJenkinsToken);
//
//            ensureCredentials(memberProjectCredential, GIT_TYPE);
//            ensureCredentials(memberProjectCredential, DOCKER_TYPE);
//
//            String[] crumbParts = crumb.split(":");
//            String crumbFieldName = crumbParts[0];
//            String crumbValue = crumbParts[1];
//            System.out.println();
//            HttpPost httpPost = new HttpPost(hostJenkinsUrl+BUILD_WITH_PARAMETERS_URL);
//
//
//            httpPost.setHeader(crumbFieldName, crumbValue);
//            System.out.println("crumb = "+crumb);
//            httpPost.setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((hostJenkinsUsername + ":" + hostJenkinsToken).getBytes()));
//
//            setParametersToHttpPost(httpPost, memberProjectInfo, memberProjectCredential);
//
//            CloseableHttpResponse response = httpClient.execute(httpPost);
//            String responseBody = EntityUtils.toString(response.getEntity());
//            int statusCode = response.getStatusLine().getStatusCode();
//            System.out.println("Response Status: " + statusCode);
//            System.out.println("Response Body: " + responseBody);
//            return true;
//        }catch(Exception e){
//            e.printStackTrace();
//            return false;
//        }
        return true;
    }
//    private void ensureCredentials(TeamProjectCredential credential, String type) throws Exception {
//        String credentialId = GIT_TYPE.equals(type) ? credential.getGitCredentialId() : credential.getDockerCredentialId();
//        if (credentialId == null) {
//            JSONObject newCredential = createCredentials(credential, "", type);
//            addCredentials(newCredential);
//            memberRepository.save(credential.getMember());
//        }
//    }
//
//    public void setParametersToHttpPost(HttpPost httpPost, TeamProjectInfo projectInfo
//                                                        , TeamProjectCredential credential){
//        List<NameValuePair> params = new ArrayList<>();
//        params.add(new BasicNameValuePair("GIT_URL", projectInfo.getBackendGitUrl()));
//        params.add(new BasicNameValuePair("GIT_BRANCH", projectInfo.getBackendGitBranch()));
//        params.add(new BasicNameValuePair("GIT_CREDENTIAL", credential.getGitCredentialId()));
//        params.add(new BasicNameValuePair("DOCKER_CREDENTIAL", credential.getDockerCredentialId()));
//        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, StandardCharsets.UTF_8);
//        httpPost.setEntity(entity);
//    }
    /**
     *
     * @param username
     * @param apiToken
     * @return
     *
     * Username과 Password(Token) 기반으로 Http 연결을 책임질 Client 생성하여 반환.
     */
    public CloseableHttpClient createHttpClient(String username, String apiToken) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, apiToken));

        return HttpClients.custom()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();
    }

    /**
     * @param httpClient
     * @param jenkinsUrl
     * @param username
     * @param token
     * @return
     * @throws IOException
     *
     * Jenkins API로부터 Crunb를 가져오는 메서드 ( csrf 토큰 )
     */
    public String getCrumb(CloseableHttpClient httpClient,
                           String jenkinsUrl, String username, String token) throws IOException {
        String crumbIssuerUrl = jenkinsUrl + "/crumbIssuer/api/xml?xpath=" +
                URLEncoder.encode("concat(//crumbRequestField,\":\",//crumb)", StandardCharsets.UTF_8);
        HttpGet getCrumb = new HttpGet(crumbIssuerUrl);


        String auth = username + ":" + token;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + new String(encodedAuth);
        getCrumb.setHeader("Authorization", authHeader);

        try (CloseableHttpResponse response = httpClient.execute(getCrumb)) {
            return EntityUtils.toString(response.getEntity());
        }
    }



    /**
     * @param description
     * @return
     *
     * 새로운 Credential을 만드는 메서드
     * 현재는 무조건 UsernamePasswordCredential을 생성함.
     * Parameter는 모두 Jenkins의 add credential시 필요한 변수들
     */
//    public JSONObject createCredentials(TeamProjectCredential credential,
//                                        String description, String credentialType) {
//        JSONObject credentials = new JSONObject();
//        JSONObject credentialsData = new JSONObject();
//        String credentialDescription = "Credential of "+credentialType+" for "+credential.getGitUsername();
//        String credentialId = credential.getGitUsername()+ "("+LocalTimeUtil.getDateTime()+")("+credentialType+")";
//        if("git".equals(credentialType)){
//            credential.setGitCredentialId(credentialId);
//            credentialsData.put("username", credential.getGitUsername());
//            credentialsData.put("password", credential.getGitToken());
//        }
//        else{
//            credential.setDockerCredentialId(credentialId);
//            credentialsData.put("username", credential.getDockerUsername());
//            credentialsData.put("password", credential.getDockerToken());
//        }
//        credentialsData.put("scope", "GLOBAL");
//        credentialsData.put("id", credentialId);
//        credentialsData.put("description", credentialDescription);
//        credentialsData.put("$class", "com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl");
//
//        credentials.put("credentials", credentialsData);
//
//        return credentials;
//    }

    /**
     *
     * @param credentials
     * @throws Exception
     *
     * 새로 생성한 Credential을 Job에 적용하는 메서드.
     */
    public void addCredentials(JSONObject credentials) throws Exception {
        String credentialUrl = hostJenkinsUrl + "/manage/credentials/store/system/domain/_/createCredentials";
        HttpPost httpPost = new HttpPost(credentialUrl);

        CloseableHttpClient httpClient = createHttpClient(hostJenkinsUsername, hostJenkinsToken);

        String crumbResponse = getCrumb(httpClient, hostJenkinsUrl, hostJenkinsUsername, hostJenkinsToken);
        System.out.println("credentials crumb = " + crumbResponse);
        String[] crumbParts = crumbResponse.split(":");
        String crumbFieldName = crumbParts[0];
        String crumbValue = crumbParts[1];

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("json", credentials.toString()));
        params.add(new BasicNameValuePair(crumbFieldName, crumbValue));

        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        httpPost.setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((hostJenkinsUsername + ":" + hostJenkinsToken).getBytes()));
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        System.out.println("Request Body: " + EntityUtils.toString(httpPost.getEntity()));

        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("Response Status: " + statusCode);
            System.out.println("Response Body: " + responseBody);
        } finally {
            httpClient.close();
        }
    }
}
