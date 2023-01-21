package cn.rylan.discovery;

import cn.rylan.model.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ServiceDiscovery {

    private final DiscoveryClient discoveryClient;

    public ServiceDiscovery(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public List<ServiceInstance> getAllService(String serviceName) {
        List<org.springframework.cloud.client.ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        return instances.stream().map(instance -> {
            return new ServiceInstance(instance.getHost(), String.valueOf(instance.getPort()));
        }).collect(Collectors.toList());
    }

}
