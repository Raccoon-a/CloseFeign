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
package cn.rylan.springboot;

import cn.rylan.annotation.EnableCloseFeign;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class FeignClientScan implements ImportBeanDefinitionRegistrar, ApplicationContextAware {
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
        log.info("扫描包：{}", basePackage);
        feignClientScanner.doScan(this.basePackage);
        if (classes != null && classes.length > 0) {
            for (Class clazz : classes) {
                if (!clazz.isInterface()) continue;
                if (!registry.containsBeanDefinition(clazz.getSimpleName())) {
                    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
                    GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
                    definition.getConstructorArgumentValues().addGenericArgumentValue(clazz);
                    definition.setBeanClass(CustomizeFactoryBean.class);
                    //byType方式注入
                    definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
                    registry.registerBeanDefinition(clazz.getSimpleName(), definition);
                    log.info("{}注入完毕", clazz.getSimpleName());
                }
            }

        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
