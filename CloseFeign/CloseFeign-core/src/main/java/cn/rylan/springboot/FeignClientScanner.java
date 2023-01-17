package cn.rylan.springboot;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class FeignClientScanner extends ClassPathBeanDefinitionScanner {

    public FeignClientScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    //扫描
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        for (BeanDefinitionHolder holder : beanDefinitions) {
            GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
            //构造注入
            ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
            constructorArgumentValues.addIndexedArgumentValue(0, definition.getBeanClassName());
            definition.getConstructorArgumentValues().addArgumentValues(constructorArgumentValues);
            //设置自己的工厂类
            definition.setBeanClass(CustomizeFactoryBean.class);
        }
        return beanDefinitions;
    }

    protected void registerDefaultFilters() {
        this.addIncludeFilter((metadataReader, metadataReaderFactory) -> {
            if (metadataReader.getClassMetadata().isInterface()) {
                return true;
            }
            return false;
        });
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }
}
