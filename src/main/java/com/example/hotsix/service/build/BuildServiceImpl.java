package com.example.hotsix.service.build;

import com.example.hotsix.dto.build.*;
import com.example.hotsix.model.Member;
import com.example.hotsix.model.TeamProjectCredential;
import com.example.hotsix.model.Team;
import com.example.hotsix.model.project.BackendConfig;
import com.example.hotsix.model.project.DatabaseConfig;
import com.example.hotsix.model.project.FrontendConfig;
import com.example.hotsix.model.project.TeamProjectInfo;
import com.example.hotsix.repository.build.BuildRepository;
import com.example.hotsix.repository.build.ServiceScheduleRepository;
import com.example.hotsix.repository.member.MemberRepository;
import com.example.hotsix.repository.team.TeamProjectInfoRepository;
import com.example.hotsix.repository.team.TeamRepository;
import com.example.hotsix.util.LocalTimeUtil;
import lombok.RequiredArgsConstructor;
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

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BuildServiceImpl implements BuildService{
    private final BuildRepository buildRespository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamProjectInfoRepository teamProjectInfoRepository;
    private final ServiceScheduleRepository serviceScheduleRepository;

    private static final String GIT_TYPE = "git";
    private static final String DOCKER_TYPE = "docker";
    private static final String JOB_NAME = "service-deploy";
    private static final String BUILD_WITH_PARAMETERS_URL = "job/"+JOB_NAME+"/buildWithParameters";

    @Value("${jenkins.url}")
    private String hostJenkinsUrl;

    @Value("${jenkins.username}")
    private String hostJenkinsUsername;

    @Value("${jenkins.token}")
    private String hostJenkinsToken;

    @Override
    public void buildStart(Long teamId, Long teamProjectInfoId){
        TeamProjectInfo teamProjectInfo = teamProjectInfoRepository.findProjectInfoByProjectInfoId(teamProjectInfoId);
        Long emptyServiceId = serviceScheduleRepository.findEmptyService();

        String servicePort = "SERVICE"+emptyServiceId;
        try(CloseableHttpClient httpClient = createHttpClient(hostJenkinsUsername, hostJenkinsToken)){
            String crumb = getCrumb(httpClient, hostJenkinsUrl, hostJenkinsUsername, hostJenkinsToken);
            createJenkinsBackendInstance(teamProjectInfo.getBackendConfigs(), httpClient, crumb, servicePort);


        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    public void createJenkinsBackendInstance(List<BackendConfig> backendConfigs,
                                             CloseableHttpClient httpClient,
                                             String crumb,
                                             String servicePort) throws IOException {
        System.out.println("Crumb = "+crumb);
        String[] crumbParts = crumb.split(":");
        String crumbFieldName = crumbParts[0];
        String crumbValue = crumbParts[1];

        for(BackendConfig config : backendConfigs){
            String hostJobName = "";
            if("Java/Spring".equals(config.getLanguage())){
                hostJobName = hostJenkinsUrl+"job/built_in_backend_docker/buildWithParameters";
            }
            HttpPost httpPost = new HttpPost(hostJobName);
            httpPost.setHeader(crumbFieldName, crumbValue);
            httpPost.setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((hostJenkinsUsername + ":" + hostJenkinsToken).getBytes()));

            String eigenServiceName = UUID.randomUUID().toString();
            List<NameValuePair> params = new ArrayList<>();
            // JDK 17 이런식으로 jdk하는건 아직 구현 안함.
            // Gradle 같이 Build 도구도 아직 구현 안함.
            params.add(new BasicNameValuePair("GIT_URL", config.getGitUrl()));
            params.add(new BasicNameValuePair("GIT_BRANCH", config.getGitBranch()));
            params.add(new BasicNameValuePair("GIT_USERNAME", config.getGitUsername()));
            params.add(new BasicNameValuePair("GIT_ACCESS_TOKEN", config.getGitAccessToken()));
            params.add(new BasicNameValuePair("SAVE_DIRECTORY", servicePort));
            params.add(new BasicNameValuePair("SERVICE_ID", eigenServiceName));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, StandardCharsets.UTF_8);
            httpPost.setEntity(entity);
            System.out.println("Call HttpClient execute");
            HttpResponse response = httpClient.execute(httpPost);
            System.out.println("Response status: " + response.getStatusLine().getStatusCode());
            System.out.println("Response body: " + EntityUtils.toString(response.getEntity()));
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
    public JSONObject createCredentials(TeamProjectCredential credential,
                                        String description, String credentialType) {
        JSONObject credentials = new JSONObject();
        JSONObject credentialsData = new JSONObject();
        String credentialDescription = "Credential of "+credentialType+" for "+credential.getGitUsername();
        String credentialId = credential.getGitUsername()+ "("+LocalTimeUtil.getDateTime()+")("+credentialType+")";
        if("git".equals(credentialType)){
            credential.setGitCredentialId(credentialId);
            credentialsData.put("username", credential.getGitUsername());
            credentialsData.put("password", credential.getGitToken());
        }
        else{
            credential.setDockerCredentialId(credentialId);
            credentialsData.put("username", credential.getDockerUsername());
            credentialsData.put("password", credential.getDockerToken());
        }
        credentialsData.put("scope", "GLOBAL");
        credentialsData.put("id", credentialId);
        credentialsData.put("description", credentialDescription);
        credentialsData.put("$class", "com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl");

        credentials.put("credentials", credentialsData);

        return credentials;
    }

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
