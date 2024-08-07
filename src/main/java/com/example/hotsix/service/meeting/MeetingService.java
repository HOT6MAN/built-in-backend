package com.example.hotsix.service.meeting;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;

public interface MeetingService {
    ResponseEntity<String> initializeSession(Map<String, Object> params)
            throws OpenViduJavaClientException, OpenViduHttpException;
    ResponseEntity<String> getSession(String sessionId, Map<String, Object> params)
            throws OpenViduJavaClientException, OpenViduHttpException;
    ResponseEntity<String> deleteSession(Map<String, Object> params)
            throws OpenViduJavaClientException, OpenViduHttpException;
}


