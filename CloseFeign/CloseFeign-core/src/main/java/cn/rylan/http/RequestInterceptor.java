package cn.rylan.http;

import cn.rylan.model.RequestTemplate;

public interface RequestInterceptor {

    void apply(RequestTemplate template);
}
