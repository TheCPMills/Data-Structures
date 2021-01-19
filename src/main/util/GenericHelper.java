package main.util;

import java.lang.reflect.*;

public abstract class GenericHelper<E> {

    public Object o;
    public java.util.ArrayList<String> lst = new java.util.ArrayList<String>();

    public GenericHelper() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Type t = this.getClass().getGenericSuperclass();
        ParameterizedType p = (ParameterizedType) t;
        Type y = p.getActualTypeArguments()[0];

        String name = y.getTypeName();

        int index = name.indexOf("<");

        if (index != -1) {
            String rename;
            do {
                rename = name.substring(0, index);
                lst.add(rename);
                name = name.substring(index + 1, name.length() - 1);
                index = name.indexOf("<");
            } while(index != -1);
        }
        lst.add(name);
    }

    public GenericHelper(E e) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        o = e;
        Type t = o.getClass().getGenericSuperclass();
        ParameterizedType p = (ParameterizedType) t;
        Type y = p.getActualTypeArguments()[0];

        String name = y.getTypeName();

        if(name.length() == 1) {
            lst.add(p.getRawType().getTypeName());
        } else {
            int index = name.indexOf("<");

            if (index != -1) {
                String rename;
                do {
                    rename = name.substring(0, index);
                    lst.add(rename);
                    name = name.substring(index + 1, name.length() - 1);
                    index = name.indexOf("<");
                } while (index != -1);
            }
            lst.add(name);
        }
    }

    public String toString() {
        return lst.toString();
    }

    public Class<?> getMainClass() throws ClassNotFoundException {
        return Class.forName(lst.get(0));
    }
    
    public static void main(String[] args) throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        System.out.println(new GenericHelper<java.util.List<java.util.List<Number>>>() {});
        
    }
}
