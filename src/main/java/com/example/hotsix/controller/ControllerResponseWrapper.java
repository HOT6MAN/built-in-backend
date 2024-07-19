package com.example.hotsix.controller;

import com.example.hotsix.dto.common.APIResponse;
import com.example.hotsix.dto.common.ErrorResponse;
import com.example.hotsix.dto.common.ProcessResponse;
import com.example.hotsix.enums.Process;
import com.example.hotsix.exception.BuiltInException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * RestController의 응답을, 우리가 원하는 객체 형식으로 변환
 */

@RestControllerAdvice(
        basePackages = "com.example.hotsix.controller"
)
@Slf4j
public class ControllerResponseWrapper implements ResponseBodyAdvice<Object> {

    /**
     *
     * @param e : 발생한 사용자 정의 예외(BuiltInException)
     * @return ErrorResponse 객체 반환
     */
    @ExceptionHandler(BuiltInException.class)
    private ErrorResponse handleBuiltInException(BuiltInException e) {
        return ErrorResponse.builder()
                .processResponse(ProcessResponse.from(e.getProcess()))
                .build();
    }

    // Controller에 대해, Wrapping을 할지 말지를 결정
    // 객체 형식으로 반환된 값에 대해서만 wrapping 실행
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
    }

    // Wrapping 실행
    // ErrorResponse 객체를 받은 경우와, 정상 응답을 받은 경우를 구분해서 실행
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        if (body instanceof ErrorResponse errorResponse) {
            return APIResponse.builder()
                    .process(errorResponse.getProcessResponse())
                    .build();
        }

        return APIResponse.builder()
                .process(ProcessResponse.from(Process.NORMAL_RESPONSE))
                .data(body)
                .build();
    }

}