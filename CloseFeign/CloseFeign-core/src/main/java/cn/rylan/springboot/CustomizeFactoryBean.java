package cn.rylan.springboot;

import cn.rylan.annotation.FeignClient;
import cn.rylan.proxy.CglibProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;

public class CustomizeFactoryBean<T> implements FactoryBean<T> {

    private Logger logger = LoggerFactory.getLogger(CustomizeFactoryBean.class);

    private Class<T> interfaceClass;

    @Autowired
    private DiscoveryClient discoveryClient;


    public CustomizeFactoryBean(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public CustomizeFactoryBean() {
    }

    @Override
    public T getObject()  {
        logger.info("正在为{}生成代理对象", interfaceClass.getName());
        FeignClient feignClient = interfaceClass.getAnnotation(FeignClient.class);
        String serviceName = feignClient.serviceName();
        CglibProxy cglibProxy = new CglibProxy(serviceName, discoveryClient);
        T proxy = (T) cglibProxy.getProxy(interfaceClass);
        if (proxy == null) {
            logger.error("{}代理对象,生成失败", interfaceClass.getName());
        }
        logger.info("{}代理对象,生成成功", interfaceClass.getName());
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

    public void setServiceInterface(Class<T> serviceInterface) {
        this.interfaceClass = serviceInterface;
    }
}
