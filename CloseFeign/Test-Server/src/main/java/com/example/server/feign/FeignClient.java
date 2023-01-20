package com.example.server.feign;

import cn.rylan.annotation.FeignRequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

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
}

