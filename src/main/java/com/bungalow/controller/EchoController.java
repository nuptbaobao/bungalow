package com.bungalow.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EchoController {
    @RequestMapping("/{msg}")
    public String index(@PathVariable("msg") String msg) {
        return msg;
    }
    
}
