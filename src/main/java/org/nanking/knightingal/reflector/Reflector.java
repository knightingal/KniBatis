package org.nanking.knightingal.reflector;

import org.nanking.knightingal.bean.Employee;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Reflector {
    private final Map<String, Type> setterType = new HashMap<String, Type>();
    private final Map<String, Type> getterType = new HashMap<String, Type>();
    private final Map<String, Method> getterMethod = new HashMap<String, Method>();
    private final Map<String, Method> setterMethod = new HashMap<String, Method>();

    public Constructor getConstructor() {
        return constructor;
    }

    private final Constructor constructor;

    public Reflector(Class<?> clazz) throws NoSuchMethodException {
        Method[] methods = clazz.getMethods();
        constructor = clazz.getConstructor();
        parseSetterMethod(methods);
        parseGetterMethod(methods);
    }

    private void parseSetterMethod(Method[] methods) {
        for (Method method : methods) {
            String methodName = method.getName();
            if (methodName.startsWith("set")
                    && methodName.length() > "set".length()
                    && Character.isUpperCase(methodName.charAt("set".length()))) {
                String fieldName = Character.toLowerCase(methodName.charAt("set".length())) + methodName.substring("set".length() + 1);
                setterMethod.put(fieldName, method);
                setterType.put(fieldName, method.getParameterTypes()[0]);
            }
        }

    }

    private void parseGetterMethod(Method[] methods) {
        for (Method method : methods) {
            String methodName = method.getName();
            if (methodName.startsWith("get")
                    && methodName.length() > "get".length()
                    && Character.isUpperCase(methodName.charAt("get".length()))) {
                String fieldName = Character.toLowerCase(methodName.charAt("get".length())) + methodName.substring("get".length() + 1);
                if (!fieldName.equals("class")) {
                    getterMethod.put(fieldName, method);
                    getterType.put(fieldName, method.getReturnType());
                }
            }
        }

    }

    public Map<String, Type> getSetterType() {
        return setterType;
    }

    public Map<String, Type> getGetterType() {
        return getterType;
    }

    public Map<String, Method> getGetterMethod() {
        return getterMethod;
    }

    public Map<String, Method> getSetterMethod() {
        return setterMethod;
    }
}
