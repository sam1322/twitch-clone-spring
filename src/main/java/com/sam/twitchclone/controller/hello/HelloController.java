package com.sam.twitchclone.controller.hello;

import com.sam.twitchclone.service.HelloService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class HelloController {

    private final HelloService helloService;

//    public HelloController(HelloService helloService){
//        System.out.println("Creating Hello Controller Object");
//        this.helloService = helloService;
//    }

    @GetMapping("/v1/hello")
    public String Hello() {
//        log.info("Printing hello world");
        System.out.println("myBean hashcode = "+helloService.hashCode());

        return "Hello World";
    }

    @PostMapping("/v1/hello")
    public String PostHello() {
//        log.info("messing around with database");
        helloService.run();
        return "Hello World";
    }
}
