package cn.rylan.springboot;

import cn.rylan.annotation.CloseFeignClient;
import cn.rylan.http.RequestInterceptor;
import cn.rylan.proxy.CglibProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;

@Slf4j
public class CustomizeFactoryBean<T> implements FactoryBean<T> {
    private Class<T> interfaceClass;

    @Autowired
    private DiscoveryClient discoveryClient;



    public CustomizeFactoryBean(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public CustomizeFactoryBean() {
    }

    @Override
    public T getObject() {
        CloseFeignClient closeFeignClient = interfaceClass.getAnnotation(CloseFeignClient.class);
        String serviceName = closeFeignClient.serviceName();
        CglibProxy cglibProxy = new CglibProxy(serviceName, discoveryClient);
        T proxy = (T) cglibProxy.getProxy(interfaceClass);
        if (proxy == null) {
            log.error("{}代理对象,生成失败", interfaceClass.getName());
        }
        log.info("{}代理对象,生成成功", interfaceClass.getName());
        return proxy;
    }

    @Override
    public Class<T> getObjectType() {
        return interfaceClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }


}
