package com.example.server.controller;

import com.example.server.feign.CommonReturnType;
import com.example.server.feign.FeignClient;
import com.example.server.feign.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;

@RestController
public class TestController {

    @Autowired
    FeignClient feignClient;

    @GetMapping("/test")
    public CommonReturnType test() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList("西红柿", "玉米"));
//        System.out.println(feignClient.getById(1L));
//        System.out.println(feignClient.getById0());
//        System.out.println(feignClient.getBatch(list));
        System.out.println(feignClient.getByName("西红柿"));
        return feignClient.test(new Material(1001L, "material", "icon", "分类", "desc"));

    }

}
