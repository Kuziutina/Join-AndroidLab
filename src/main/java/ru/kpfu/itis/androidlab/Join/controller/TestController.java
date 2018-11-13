package ru.kpfu.itis.androidlab.Join.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
public class TestController {
    @GetMapping
    public String test() {
        return "hello";
    }
}
