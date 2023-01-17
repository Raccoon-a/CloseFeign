package cn.rylan.handler;

import cn.rylan.model.MethodTemplate;
import lombok.Data;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Data
public class MethodHandler {

    private Map<Parameter, Object> paramMap = new HashMap<>();

    private Method method;

    private Object[] values;


    public MethodHandler(Method method, Object[] values) {
        this.method = method;
        this.values = values;

        //init
        Parameter[] parameters = method.getParameters();
        int index = 0;
        for (Parameter param : parameters) {
            paramMap.put(param,values[index++]);
        }
    }

    public MethodTemplate handler() {
        Class<?> returnType = this.method.getReturnType();
        Map<String, Object> pathValues = new HashMap<>();
        Map<String, Object> paramValues = new HashMap<>();
        AtomicReference<Object> body = new AtomicReference<>();

        paramMap.forEach((key, value) -> {
            if (key.isAnnotationPresent(PathVariable.class)) {
                PathVariable pathVariable = key.getAnnotation(PathVariable.class);
                pathValues.put(pathVariable.value(), value);
            } else if(key.isAnnotationPresent(RequestBody.class)){
                body.set(value);
            }else {
                paramValues.put(key.getName(), value);
            }
        });

        MethodTemplate methodTemplate = new MethodTemplate();
        methodTemplate.setMethod(method);
        methodTemplate.setPathValues(pathValues);
        methodTemplate.setParamValues(paramValues);
        methodTemplate.setBody(body);
        methodTemplate.setReturnType(returnType);
        return methodTemplate;
    }
}
