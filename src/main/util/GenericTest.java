package main.util;

import java.lang.reflect.*;

public class GenericTest {

    public static void isGeneric(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        GenericTest.isGeneric(Class.forName(className));
    }

    public static boolean isGeneric(Object o) {
        return isGeneric(o.getClass());
    }

    public static boolean isGeneric(Class<?> c) {
        boolean hasTypeParameters = hasTypeParameters(c);
        boolean hasGenericSuperclass = hasGenericSuperclass(c);
        // boolean hasGenericSuperinterface = hasGenericSuperinterface(c);
        // boolean isGeneric = hasTypeParameters || hasGenericSuperclass || hasGenericSuperinterface;
        boolean isGeneric = hasTypeParameters || hasGenericSuperclass;

        return isGeneric;
    }

    public static boolean hasTypeParameters(Class<?> c) {
        return c.getTypeParameters().length > 0;
    }

    public static boolean hasGenericSuperclass(Class<?> c) {
        Class<?> testClass = c;
        while (testClass != null) {
            Type t = testClass.getGenericSuperclass();
            if (t instanceof ParameterizedType) {
                return true;
            }
            testClass = testClass.getSuperclass();
        }
        return false;
    }

    public static boolean hasGenericSuperinterface(Class<?> c) {
        for (Type t : c.getGenericInterfaces()) {
            if (t instanceof ParameterizedType) {
                return true;
            }
        }
        return false;
    }
}