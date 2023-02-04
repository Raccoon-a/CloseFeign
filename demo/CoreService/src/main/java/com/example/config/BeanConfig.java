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
package com.example.config;

import cn.rylan.http.RequestInterceptor;
import cn.rylan.model.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;
import java.util.Collections;


@Configuration
public class BeanConfig {

    @Bean
    public RequestInterceptor interceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                var requestAttributes = RequestContextHolder.getRequestAttributes();
                assert requestAttributes != null;
                var request = ((ServletRequestAttributes) requestAttributes).getRequest();
                var cookie = request.getHeader("Cookie");
                var headers = new HttpHeaders();
                headers.add("Cookie", cookie);
                headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                headers.setContentType(MediaType.APPLICATION_JSON);
                template.setHeaders(headers);
            }
        };
    }
}
