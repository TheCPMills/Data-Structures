package main.util;

import main.*;
import java.lang.reflect.*;
import java.util.*;

public class Utilities {
    public static String getMethodName(Method method) {
        String name = method.getName();
        Class<?>[] params = method.getParameterTypes();
        String paramNames = "";
        for (Class<?> c : params) {
            paramNames += c.toString() + ", ";
        }
        paramNames = (paramNames.length() == 0) ? "" : paramNames.substring(0, paramNames.length() - 2);
        return name + "(" + paramNames + ")";
    }

    public static Class<?> getGenericParameterType(Field field) {
        ParameterizedType fieldType = (ParameterizedType) field.getGenericType();
        Class<?> fieldClass = (Class<?>) fieldType.getActualTypeArguments()[0];
        return fieldClass;
    }

    public static <T> Class<?> getGenericParameterArgument(T t) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return new GenericHelper<T>(t){}.getMainClass();
    }

    public static Class<?> getGenericParameterType(Class<?> c, String fieldName) throws NoSuchFieldException, SecurityException {
        Field field = c.getDeclaredField(fieldName);
        return getGenericParameterType(field);
    }

    public static Class<?> getGenericParameterMethod(Class<?> c, String methodName) throws NoSuchMethodException, SecurityException {
        Method method = c.getDeclaredMethod(methodName);
        Type[] types = method.getGenericParameterTypes();
        ParameterizedType pType = (ParameterizedType) types[0];
        Class<?> clazz = (Class<?>) pType.getActualTypeArguments()[0];
        return clazz;
    }
    
    public static boolean isRelatedTo(Class<?> sub, Class<?> sup) {
        return sup.isAssignableFrom(sub);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static int compare(Object o1, Object o2) {
        if (o1.getClass() != o2.getClass()) {
            throw new IllegalArgumentException(o1.getClass() + " cannot be compared to " + o2.getClass());
        } else {
            int result;
            Class<?> typeClass;
            boolean hasParam;

            if (GenericTest.isGeneric(o1)) {
                try {
                    typeClass = Utilities.getGenericParameterArgument(o1);
                } catch (SecurityException | ClassNotFoundException | InstantiationException
                        | IllegalAccessException e) {
                    typeClass = null;
                }
                hasParam = true;
            } else {
                typeClass = o1.getClass();
                hasParam = false;
            }

            if (Utilities.isRelatedTo(typeClass, Comparator.class)) {
                Comparator<Object> obj;
                try {
                    obj = (Comparator<Object>) typeClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    obj = (Comparator<Object>) ((Structure) (o1)).clone();
                }

                return obj.compare(o1, o2);
            } else if (Utilities.isRelatedTo(typeClass, Comparable.class)) {
                Comparable<Object> obj1;
                Object obj2;
                if (hasParam) {
                    Structure ob1 = (Structure) o1;
                    Structure ob2 = (Structure) o2;
                    Iterator<Object> i1 = ob1.iterator();
                    Iterator<Object> i2 = ob2.iterator();
                    int smallerSize = Math.min(ob1.size(), ob2.size());
                    for (int i = 0; i < smallerSize; i++) {
                        obj1 = (Comparable<Object>) i1.next();
                        obj2 = i2.next();
                        result = obj1.compareTo(obj2);
                        if (result != 0) {
                            return result;
                        }
                    }
                    return Integer.compare(ob1.size(), ob2.size());
                } else {
                    obj1 = (Comparable<Object>) o1;
                    obj2 = (Comparable<Object>) o2;
                    return obj1.compareTo(obj2);
                }
            } else {
                String s = typeClass.getName();
                throw new IllegalArgumentException(s.substring(s.indexOf("class ")) + "'s cannot be compared");
            }
        }
    }
}