package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.fluentd.logger.FluentLogger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private static FluentLogger log = FluentLogger.getLogger("demo.TestController","localhost",24224);


    @GetMapping("/")
    public void test() {
      log.log("test", "warn", "111111111111");
    }

}
