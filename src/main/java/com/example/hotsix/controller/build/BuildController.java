package com.example.hotsix.controller.build;


import com.example.hotsix.dto.build.MemberProjectCredentialDto;
import com.example.hotsix.dto.build.MemberProjectInfoDto;
import com.example.hotsix.service.build.BuildService;
import com.example.hotsix.service.build.BuildServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/build")
@RequiredArgsConstructor
public class BuildController {
    private final BuildService buildService;

    @PostMapping("/{memberId}")
    public String insertMemberProjectCredential(@PathVariable("memberId") Long memberId,
                                                @RequestBody MemberProjectCredentialDto credential,
                                                @RequestBody MemberProjectInfoDto info){
        boolean result = buildService.insertMemberBuildInfo(memberId, credential, info);
        if(result){
            return "success";
        }
        return "fail";
    }
    @GetMapping("/{memberId}")
    public MemberProjectCredentialDto findMemberProjectCredential(@PathVariable("memberId") Long memberId){
        return buildService.getMemberBuildInfo(memberId);
    }

    @GetMapping("/deploy/{memberId}/{projectId}")
    public void deployMemberProject(@PathVariable("memberId")Long memberId, @PathVariable("projectId")Long projectId){
        System.out.println("call deploy start");
        buildService.MemberProjectBuildStart(memberId, projectId);
    }

}
