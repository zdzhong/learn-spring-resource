package framework.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtil {
    public static Class<?> resolveType(String resultType) {
        try {
            return Class.forName(resultType);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> getTypeByFileName(String clazzName, String name) {
        try {
            Class<?> clazz = Class.forName(clazzName);
            Field field = clazz.getDeclaredField(name);
            return field.getType();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void invokeMethod(Object bean, String initMethod) {
        Class<?> clazz = bean.getClass();
        try {
            Method method = clazz.getMethod(initMethod);
            method.invoke(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setProperty(Object bean, String name, Object object) {
        try {
            Class<?> clazz = bean.getClass();
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            field.set(bean, object);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Object createObject(Class<?> clazz, Object[] args) {
        try {
            Constructor<?> declaredConstructor = clazz.getDeclaredConstructor();
            return declaredConstructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
