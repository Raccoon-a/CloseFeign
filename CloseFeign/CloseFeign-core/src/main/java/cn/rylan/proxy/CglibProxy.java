package cn.rylan.proxy;

import cn.rylan.annotation.FeignRequestMapping;
import cn.rylan.client.RestTemplateAPI;
import cn.rylan.discovery.ServiceDiscovery;
import cn.rylan.discovery.ServiceInstanceInfo;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CglibProxy implements MethodInterceptor {

    private static final Map<Class<?>, Object> PROXY_MAP = new ConcurrentHashMap<>();

    private String serviceName;

    private DiscoveryClient discoveryClient;

    public CglibProxy(String serviceName, DiscoveryClient discoveryClient) {
        this.serviceName = serviceName;
        this.discoveryClient = discoveryClient;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) PROXY_MAP.computeIfAbsent(clazz, (this::createProxy));
    }


    public Object createProxy(Class<?> clazz) {
        //创建动态代理增强类
        Enhancer enhancer = new Enhancer();
        //设置类加载器
        enhancer.setClassLoader(clazz.getClassLoader());
        //设置被代理类
        enhancer.setSuperclass(clazz);
        //拦截触发回调执行intercept
        enhancer.setCallback(this);
        return enhancer.create();
    }


    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        if (method.isAnnotationPresent(FeignRequestMapping.class)) {
            FeignRequestMapping feignRequestMapping = method.getAnnotation(FeignRequestMapping.class);
            String uri = feignRequestMapping.uri();
            String type = feignRequestMapping.type();
            Class<?> returnType = method.getReturnType();
            //TODO 服务发现
            ServiceDiscovery serviceDiscovery = new ServiceDiscovery(discoveryClient);
            ServiceInstanceInfo instanceInfo = serviceDiscovery.getAllService(serviceName).get(0);
            String address = instanceInfo.getIp();
            String port = instanceInfo.getPort();
            //TODO 远程调用
            RestTemplateAPI restTemplateAPI = new RestTemplateAPI();
            if (type.equals("GET")) {
                if (objects.length == 0)
                    return restTemplateAPI.get(address, port, uri, returnType);
                else
                    return restTemplateAPI.get(address, port, uri, objects, returnType);
            }
            if (type.equals("POST")) {
                return restTemplateAPI.post(address, port, uri, objects[0], returnType);

            }
        }
        return null;
    }
}
