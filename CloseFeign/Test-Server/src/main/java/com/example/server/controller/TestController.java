/*
 * Copyright (c) 2023 Rylan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 */
package com.example.server.controller;

import com.example.server.feign.CommonReturnType;
import com.example.server.feign.FeignClient;
import com.example.server.feign.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;


import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

@RestController
public class TestController {

    @Autowired
    private FeignClient feignClient;

    @Autowired
    private ExecutorService executor;


    @GetMapping("/test")
    public CommonReturnType test() {
        //test
//        var list = List.of("西红柿", "玉米");
//        var res0 = feignClient.getById(1L);
//        var res1 = feignClient.getByName("西红柿");
//        var res2 = feignClient.getBatch(list); //need auth

        //async test
//        long startTime = System.currentTimeMillis();
//
//        var attributes = RequestContextHolder.getRequestAttributes();
//        var task1 = CompletableFuture.supplyAsync(() -> {
//            RequestContextHolder.setRequestAttributes(attributes);
//            System.out.println("getById");
//            return feignClient.getById(1L);
//        }, executor);
//
//        var task2 = CompletableFuture.supplyAsync(() -> {
//            RequestContextHolder.setRequestAttributes(attributes);
//            System.out.println("getByName");
//            return feignClient.getByName("西红柿");
//        }, executor);
//
//        try {
//            var res3 = task1.get();
//            var res4 = task2.get();
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("time: " + (System.currentTimeMillis() - startTime) + "ms");

        System.out.println(feignClient.getTest(5L));

        System.out.println(feignClient.getMethodList("HTTP_1.1", List.of("GET", "POST", "PUT"), 1));
        return CommonReturnType.create("");
    }

}
