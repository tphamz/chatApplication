package com.example.api_server.service;
import java.lang.reflect.Method;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseService <T>{
    Logger logger = LoggerFactory.getLogger(BaseService.class);
    public T mapData(T source, T target){
        for(Method method: source.getClass().getMethods()){
            String name = method.getName();
            try {
                if(name.contains("get") && Objects.nonNull(method.invoke(source))){
                    try {
                        String mappingKey = "set" + name.substring("get".length());
                        target.getClass().getMethod(mappingKey, method.getReturnType()).invoke(target, method.invoke(source));
                    } catch (Exception e) {}
                }
            } catch (Exception e) {}
        }
        return target;
    }
}
