package cn.rylan.handler;

import cn.rylan.http.RequestInterceptor;
import cn.rylan.http.RestTemplateAPI;
import cn.rylan.model.MethodTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;


public class RequestHandler {

    private final MethodTemplate methodTemplate;

    private RequestInterceptor interceptor;

    public RequestHandler(MethodTemplate methodTemplate, RequestInterceptor interceptor) {
        this.methodTemplate = methodTemplate;
        this.interceptor = interceptor;
    }


    public Object http(String url, String methodType) throws JsonProcessingException {
        Class<?> returnType = methodTemplate.getReturnType();
        RestTemplateAPI restTemplateAPI = new RestTemplateAPI();

        if (methodType.equals("GET")) {
            return restTemplateAPI.get(url, returnType,interceptor);
        }
        if (methodType.equals("POST")) {
            Object body = methodTemplate.getBody();
            return restTemplateAPI.post(url, body, returnType,interceptor);
        }
        return null;
    }


}
