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
package com.example.controller;

import com.example.model.User;
import com.example.response.CommonReturnType;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user")
public class UserController {

    //param 1
    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public CommonReturnType getById(@PathVariable("id") Integer id) {
        if (id == 1)
            return CommonReturnType.create(new User(1, "rylan"));
        else
            return CommonReturnType.create(null, "fail");
    }


    //param [1,2,3]
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public CommonReturnType getBatch(@RequestBody List<Integer> ids) {
        var list = ids.stream().map(id -> {
            return new User(id, "rylan" + id);
        }).collect(Collectors.toList());

        return CommonReturnType.create(list);
    }

    @RequestMapping(value = "/{type}/list", method = RequestMethod.POST)
    public CommonReturnType getMethodList(@PathVariable("type") String type, @RequestBody List<String> methods,@PathParam("id") Integer id) {
        var list = methods.stream().map(method -> {
            return id + ": " + type + ": " + method;
        }).collect(Collectors.toList());

        return CommonReturnType.create(list);
    }
}
