package com.example.server.controller;

import cn.rylan.annotation.FeignClient;
import com.example.server.feign.CommonReturnType;
import com.example.server.feign.CloseFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;

@RestController
public class TestController {

    @Autowired
    CloseFeignClient closeFeignClient;

    @GetMapping("/test")
    public CommonReturnType test() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList("西红柿", "玉米"));
        System.out.println(closeFeignClient.getById(1L));
        System.out.println(closeFeignClient.getById0());
        return closeFeignClient.getBatch(list);
    }

}
