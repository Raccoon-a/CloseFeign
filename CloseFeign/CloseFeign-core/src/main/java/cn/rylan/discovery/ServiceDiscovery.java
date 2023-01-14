package cn.rylan.discovery;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class ServiceDiscovery {

    private DiscoveryClient discoveryClient;

    public ServiceDiscovery(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public List<ServiceInstanceInfo> getAllService(String serviceName) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        return instances.stream().map(instance -> {
            return new ServiceInstanceInfo(instance.getHost(), String.valueOf(instance.getPort()));
        }).collect(Collectors.toList());
    }

}
