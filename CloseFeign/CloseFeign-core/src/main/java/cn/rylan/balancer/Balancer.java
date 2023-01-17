package cn.rylan.balancer;

import cn.rylan.model.ServiceInstance;

public interface Balancer {

    ServiceInstance getInstance();
}
