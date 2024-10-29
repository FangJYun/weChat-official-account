package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 *
 * @author fangjy
 * @date 2021-07-08 13:39
 **/
@RestController
@RequestMapping(value = "/test")
public class TestController {

    @GetMapping("test.do")
    public String test(){
        return "这是个测试接口";
    }
}
