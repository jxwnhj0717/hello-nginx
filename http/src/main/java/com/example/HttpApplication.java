package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@Slf4j
public class HttpApplication {

    public static void main(String[] args) {
        SpringApplication.run(HttpApplication.class, args);
    }

    @Autowired
    private Environment env;

    @GetMapping("/echo/{msg}")
    public String echo(@PathVariable String msg) {
        String port = env.getProperty("local.server.port");
        log.info("echo[{}] {}", port, msg);
        return "reply " + port + ":" + msg + "\r\n";
    }

}
