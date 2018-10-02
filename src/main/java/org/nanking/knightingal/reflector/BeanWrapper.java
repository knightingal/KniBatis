package org.nanking.knightingal.reflector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanWrapper {

    private final Object originObject;

    private final Reflector reflector;

    public BeanWrapper(Object originObject) throws NoSuchMethodException {
        this.originObject = originObject;
        this.reflector = new Reflector(originObject.getClass());
    }

    public Object valueByName(String name)  {
        Method getMethod = reflector.getGetterMethod().get(name);
        try {
            return getMethod.invoke(originObject);
        } catch (Exception e) {
            return null;
        }
    }
}
