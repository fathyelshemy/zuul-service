package com.zuul.limiter.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;

@SpringBootApplication
@EnableZuulProxy
@EnableJpaRepositories
public class ZuulLimiterDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulLimiterDemoApplication.class, args);
    }

    @RestController
    public class sampleController {

        @GetMapping("/welcome")
        public ResponseEntity<String> welcome(){
            return  ResponseEntity.status(HttpStatus.OK).build();
        }

    }
}
