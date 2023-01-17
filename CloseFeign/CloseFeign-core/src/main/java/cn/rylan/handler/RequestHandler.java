package cn.rylan.handler;

import cn.rylan.client.RestTemplateAPI;
import cn.rylan.model.MethodTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;


public class RequestHandler {

    private final MethodTemplate methodTemplate;

    public RequestHandler(MethodTemplate methodTemplate) {
        this.methodTemplate = methodTemplate;
    }


    public Object http(String url, String methodType) throws JsonProcessingException {
        Class<?> returnType = methodTemplate.getReturnType();
        RestTemplateAPI restTemplateAPI = new RestTemplateAPI();

        if (methodType.equals("GET")) {
            return restTemplateAPI.get(url, returnType);
        }
        if (methodType.equals("POST")) {
            Object body = methodTemplate.getBody();
            return restTemplateAPI.post(url, body, returnType);
        }
        return null;
    }


}
