package com.example.hotsix.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MyController {

    @GetMapping("/my")
    public String my() {
        log.info("my");
        return "Hello World";
    }
}
