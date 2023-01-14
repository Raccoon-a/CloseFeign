package com.example.server;

import cn.rylan.annotation.EnableCloseFeign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
@EnableCloseFeign(basePackages = {"com.example.server.feign"})
public class TestServerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TestServerApplication.class, args);
//        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
//        provider.addIncludeFilter(new AnnotationTypeFilter(FeignClient.class));
//        Set<BeanDefinition> beanDefinitions = provider.findCandidateComponents("com.example.server.feign");
//        System.out.println(beanDefinitions);
    }

}
