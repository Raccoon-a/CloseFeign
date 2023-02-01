/*
 * Copyright (c) 2023 Rylan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 */
package cn.rylan.proxy;

import cn.rylan.SPI.ExtensionLoad;
import cn.rylan.annotation.FeignRequestMapping;
import cn.rylan.balancer.Balancer;
import cn.rylan.discovery.ServiceDiscovery;
import cn.rylan.handler.RequestHandler;
import cn.rylan.handler.URLHandler;
import cn.rylan.model.MethodTemplate;
import cn.rylan.model.ServiceInstance;
import cn.rylan.springboot.Properties;
import cn.rylan.springboot.SpringBeanFactory;
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


    public CglibProxy(String serviceName, DiscoveryClient discoveryClient) {
        init(serviceName, discoveryClient);
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


    private void init(String serviceName, DiscoveryClient discoveryClient) {
        //服务发现
        ServiceDiscovery serviceDiscovery = new ServiceDiscovery(discoveryClient);
        List<ServiceInstance> instances = serviceDiscovery.getAllService(serviceName);

        String balanceType = SpringBeanFactory.getBean(Properties.class).getBalancer();
        //SPI获取负载均衡器
        this.balancer = ExtensionLoad.getExtensionLoader(Balancer.class)
                .getExtension(balanceType, new Object[]{instances}, List.class);
    }
}

