package com.example.server.config;

import cn.rylan.http.RequestInterceptor;
import cn.rylan.model.RequestTemplate;
import org.bouncycastle.util.encoders.UTF8;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class BeanConfig {

    @Bean
    public RequestInterceptor interceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
                String cookie = request.getHeader("Cookie");
                HttpHeaders headers = new HttpHeaders();
                headers.add("Cookie", cookie);
                headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                headers.setContentType(MediaType.APPLICATION_JSON);
                template.setHeaders(headers);
            }
        };
    }
}
