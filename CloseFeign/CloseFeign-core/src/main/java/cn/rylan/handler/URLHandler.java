package cn.rylan.handler;

import cn.rylan.model.MethodTemplate;
import cn.rylan.model.ServiceInstance;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;


public class URLHandler {

    private ServiceInstance instance;

    private String uri;

    private MethodTemplate methodTemplate;

    private MethodHandler methodHandler;


    public URLHandler(ServiceInstance instance, Method method, Object[] objects, String uri) {
        this.uri = uri;
        this.instance = instance;
        methodHandler = new MethodHandler(method, objects);
        methodTemplate = methodHandler.handler();
    }

    public MethodTemplate getMethodTemplate() {
        return methodTemplate;
    }

    public String getURL() {
        String uri = handler();
        return "http://" + instance.getIp() + ":" + instance.getPort() + uri;
    }

    private String handler() {

        Map<String, Object> paramValues = methodTemplate.getParamValues();

        Map<String, Object> pathValues = methodTemplate.getPathValues();

        Object body = methodTemplate.getBody();

        if (pathValues.size() == 0 && paramValues.size() == 0) {
            return uri;
        }
        if (pathValues.size() > 0) {
            pathValues.forEach((name, value) -> {
                uri = replace(this.uri, name, value.toString());
            });
        }
        if (body != null) {
            return uri;
        }
        if (paramValues.size() > 0) {
            uri = this.uri + "?";
            paramValues.forEach((name, value) -> {
                uri = addParam(uri, name, value.toString());
            });
            uri = StringUtils.removeEnd(uri, "&");
        }
        return uri;
    }

    private String replace(String uri, String target, String param) {
        String s = new String("{" + target + "}");
        return uri.replace(s, param);
    }

    private String addParam(String uri, String param, String value) {
        uri = uri + param + "=" + value + "&";
        return uri;
    }


}
