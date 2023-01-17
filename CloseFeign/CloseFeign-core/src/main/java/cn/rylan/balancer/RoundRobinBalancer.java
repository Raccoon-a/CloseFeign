package cn.rylan.balancer;

import cn.rylan.model.ServiceInstance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinBalancer implements Balancer {

    private List<ServiceInstance> instanceList;

    private static AtomicInteger index = new AtomicInteger(0);

    public RoundRobinBalancer(List<ServiceInstance> instanceList) {
        this.instanceList = instanceList;
    }

    @Override
    public ServiceInstance getInstance() {
        if (index.get() >= instanceList.size()) {
            index.set(0);
        }
        return instanceList.get(index.getAndIncrement());
    }
}
