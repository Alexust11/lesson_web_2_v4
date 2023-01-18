package com.example.lesson_web_2_v4.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/")
    public String testApi(){return "WebApp is working!";}
}
