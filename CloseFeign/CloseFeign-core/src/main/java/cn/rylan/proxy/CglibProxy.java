package cn.rylan.proxy;

import cn.rylan.annotation.FeignRequestMapping;
import cn.rylan.client.RestTemplateAPI;
import cn.rylan.discovery.ServiceDiscovery;
import cn.rylan.handler.RequestHandler;
import cn.rylan.handler.URLHandler;
import cn.rylan.model.MethodTemplate;
import cn.rylan.model.ServiceInstance;
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
            //TODO 服务发现
            ServiceDiscovery serviceDiscovery = new ServiceDiscovery(discoveryClient);
            ServiceInstance instance = serviceDiscovery.getAllService(serviceName).get(0);

            //TODO URI处理返回完整的URL
            URLHandler urlHandler = new URLHandler(instance,method, objects,uri);
            String URL = urlHandler.getURL();
            System.out.println(type+": " +URL);

            //TODO 远程调用
            MethodTemplate methodTemplate = urlHandler.getMethodTemplate();
            RequestHandler request = new RequestHandler(methodTemplate);
            return request.http(URL, type);
        }
        return null;
    }
}
