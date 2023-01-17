package cn.rylan.proxy;

import cn.rylan.SPI.ExtensionLoad;
import cn.rylan.annotation.FeignRequestMapping;
import cn.rylan.balancer.Balancer;
import cn.rylan.discovery.ServiceDiscovery;
import cn.rylan.handler.RequestHandler;
import cn.rylan.handler.URLHandler;
import cn.rylan.model.MethodTemplate;
import cn.rylan.model.ServiceInstance;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class CglibProxy implements MethodInterceptor {

    private static final Map<Class<?>, Object> PROXY_MAP = new ConcurrentHashMap<>();

    private Balancer balancer;

    public CglibProxy(String serviceName, DiscoveryClient discoveryClient, String balanceType) {
        init(serviceName, discoveryClient, balanceType);
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
            //服务发现 - 负载均衡
            ServiceInstance instance = balancer.getInstance();
            // URI处理返回完整的URL
            URLHandler urlHandler = new URLHandler(instance, method, objects, uri);
            String URL = urlHandler.getURL();
            log.info(type + ": => {}", URL);
            //远程调用
            MethodTemplate methodTemplate = urlHandler.getMethodTemplate();
            RequestHandler request = new RequestHandler(methodTemplate);
            return request.http(URL, type);
        }
        return null;
    }


    private void init(String serviceName, DiscoveryClient discoveryClient, String balanceType) {
        //服务发现
        ServiceDiscovery serviceDiscovery = new ServiceDiscovery(discoveryClient);
        List<ServiceInstance> instances = serviceDiscovery.getAllService(serviceName);

        //SPI获取负载均衡器
        this.balancer = ExtensionLoad.getExtensionLoader(Balancer.class)
                .getExtension(balanceType, new Object[]{instances}, List.class);
    }
}

