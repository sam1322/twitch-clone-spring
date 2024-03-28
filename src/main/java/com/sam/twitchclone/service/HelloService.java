package com.sam.twitchclone.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {
    public HelloService(){
        System.out.println("Hello Service Created");
    }

    public void run(){
        System.out.println("Hello Service Running...");
    }
}
