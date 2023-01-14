package cn.rylan.springboot;

import cn.rylan.annotation.EnableCloseFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

public class FeignClientScan implements ImportBeanDefinitionRegistrar, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(FeignClientScan.class);
    private String[] basePackage;

    private Class[] classes;

    private ApplicationContext applicationContext;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EnableCloseFeign.class.getName());
        this.basePackage = (String[]) annotationAttributes.get("basePackages");
        this.classes = (Class[]) annotationAttributes.get("classes");
        FeignClientScanner feignClientScanner = new FeignClientScanner(registry);
        feignClientScanner.setResourceLoader(this.applicationContext);
        logger.info("扫描包：{}", basePackage);
        feignClientScanner.doScan(this.basePackage);
        logger.info("扫描类：{}", classes);
        if (classes != null && classes.length > 0) {
            //对于class属性就直接手工注册
            for (Class clazz : classes) {
                if (!clazz.isInterface()) continue;
                if (!registry.containsBeanDefinition(clazz.getSimpleName())) {
                    //if(
                    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
                    GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
                    definition.getConstructorArgumentValues().addGenericArgumentValue(clazz);
                    definition.setBeanClass(CustomizeFactoryBean.class);
                    //byType方式注入
                    definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
                    registry.registerBeanDefinition(clazz.getSimpleName(), definition);
                    logger.info("{}注入完毕", clazz.getSimpleName());
                }
            }

        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
