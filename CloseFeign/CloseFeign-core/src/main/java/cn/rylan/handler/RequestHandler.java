package cn.rylan.handler;

import cn.rylan.http.RequestInterceptor;
import cn.rylan.http.Http;
import cn.rylan.model.MethodTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;


public class RequestHandler {

    private final MethodTemplate methodTemplate;


    public RequestHandler(MethodTemplate methodTemplate) {
        this.methodTemplate = methodTemplate;

    }


    public Object http(String url, String methodType) throws JsonProcessingException {
        Class<?> returnType = methodTemplate.getReturnType();
        Http http = new Http();

        if (methodType.equals("GET")) {
            return http.get(url, returnType);
        }
        if (methodType.equals("POST")) {
            Object body = methodTemplate.getBody();
            return http.post(url, body, returnType);
        }
        return null;
    }


}
