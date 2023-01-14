package com.example.server.feign;

import cn.rylan.annotation.FeignClient;
import cn.rylan.annotation.FeignRequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@FeignClient(serviceName = "core-application")
public interface CloseFeignClient {

    @FeignRequestMapping(uri = "/material/id/1", type = "GET")
    public CommonReturnType getById0();
    @FeignRequestMapping(uri = "/material/id/", type = "GET")
    public CommonReturnType getById(Long id);

    @FeignRequestMapping(uri = "/material/names", type = "POST")
    public CommonReturnType getBatch(List<String> names);
}

