package cn.rylan.balancer;

import cn.rylan.model.ServiceInstance;

import java.util.List;
import java.util.Random;

public class RandomBalancer implements Balancer{
    private final List<ServiceInstance> instanceList;

    private final Random random = new Random();

    public RandomBalancer(List<ServiceInstance> instanceList) {
        this.instanceList = instanceList;
    }

    @Override
    public ServiceInstance getInstance() {
        int index = random.nextInt(instanceList.size());
        return instanceList.get(index);
    }
}
