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
package com.example.server.feign;

import cn.rylan.annotation.FeignRequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.websocket.server.PathParam;
import java.util.List;


@cn.rylan.annotation.CloseFeignClient(serviceName = "core-application")
public interface FeignClient {

    @FeignRequestMapping(uri = "/material/id/1", type = "GET")
    CommonReturnType getById0();

    @FeignRequestMapping(uri = "/material/id/{id}", type = "GET")
    CommonReturnType getById(@PathVariable("id") Long id);

    @FeignRequestMapping(uri = "/material/names", type = "POST")
    CommonReturnType getBatch(@RequestBody List<String> names);

    @FeignRequestMapping(uri = "/material/test", type = "POST")
    CommonReturnType test(@RequestBody Material material);

    @FeignRequestMapping(uri = "/material/name/{name}", type = "GET")
    CommonReturnType getByName(@PathVariable("name") String name);

    @FeignRequestMapping(uri = "/recipe/name/{name}", type = "GET")
    CommonReturnType getRecipeByName(@PathVariable("name") String name);

    @FeignRequestMapping(uri = "/material/test/id", type = "GET")
    CommonReturnType getTest(@PathParam("id") Long id);

    @FeignRequestMapping(uri = "/material/{type}/list", type = "POST")
    CommonReturnType getMethodList(@PathVariable("type") String type, @RequestBody List<String> methods, @PathParam("id") Integer id);
}

