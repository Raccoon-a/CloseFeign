package cn.rylan.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MethodTemplate {

    private Method method;

    private Map<String,Object> paramValues;

    private Map<String,Object> pathValues;

    private Object body;

    private Class<?> returnType;

}
