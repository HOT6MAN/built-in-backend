
package com.example.hotsix.controller.auth;


import com.example.hotsix.dto.auth.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MyController {

    @GetMapping("/my")
    public LoginResponse my() {
        log.info("MyController.my 호출 - 시간: {}", System.currentTimeMillis());
        LoginResponse loginResponse = new LoginResponse("ok");
        return loginResponse;
        //return "my";
    }
}
