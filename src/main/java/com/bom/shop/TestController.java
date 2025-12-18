package com.bom.shop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.ZonedDateTime;

@Controller
public class TestController {

    @GetMapping("/")
    String hello(){
        return "index.html";

    }

    @GetMapping("/about")
    @ResponseBody
    String hello2(){
        return "ㄷㄷㄷ";
    }

    @GetMapping("/date")
    @ResponseBody
    String hello3(){
        return ZonedDateTime.now().toString();

    }
}