package codechicken.lib.util;

import codechicken.lib.asm.ObfMapping;
import org.objectweb.asm.Type;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

//TODO Clean cruft and move basically everything to OBFMapping.
public class ReflectionManager {

    public static HashMap<Class<?>, Class<?>> primitiveWrappers = new HashMap<Class<?>, Class<?>>();

    static {
        primitiveWrappers.put(Integer.TYPE, Integer.class);
        primitiveWrappers.put(Short.TYPE, Short.class);
        primitiveWrappers.put(Byte.TYPE, Byte.class);
        primitiveWrappers.put(Long.TYPE, Long.class);
        primitiveWrappers.put(Double.TYPE, Double.class);
        primitiveWrappers.put(Float.TYPE, Float.class);
        primitiveWrappers.put(Boolean.TYPE, Boolean.class);
        primitiveWrappers.put(Character.TYPE, Character.class);
    }

    public static boolean isInstance(Class<?> class1, Object obj) {
        Class<?> primitive = primitiveWrappers.get(class1);
        if (primitive != null) {
            if (primitive == Long.class && Long.class.isInstance(obj)) {
                return true;
            }
            if ((primitive == Long.class || primitive == Integer.class) && Integer.class.isInstance(obj)) {
                return true;
            }
            if ((primitive == Long.class || primitive == Integer.class || primitive == Short.class) && Short.class.isInstance(obj)) {
                return true;
            }
            if ((primitive == Long.class || primitive == Integer.class || primitive == Short.class || primitive == Byte.class) && Integer.class.isInstance(obj)) {
                return true;
            }

            if (primitive == Double.class && Double.class.isInstance(obj)) {
                return true;
            }
            if ((primitive == Double.class || primitive == Float.class) && Float.class.isInstance(obj)) {
                return true;
            }

            return primitive.isInstance(obj);
        }
        return class1.isInstance(obj);
    }

    public static Class<?> findClass(String name) {
        return findClass(name, true);
    }

    public static boolean classExists(String name) {
        return findClass(name, false) != null;
    }

    public static Class<?> findClass(ObfMapping mapping) {
        return findClass(mapping, true);
    }

    public static Class<?> findClass(ObfMapping mapping, boolean init) {
        try {
            return Class.forName(mapping.javaClass(), init, ReflectionManager.class.getClassLoader());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?> findClass(String name, boolean init) {
        try {
            return Class.forName(name, init, ReflectionManager.class.getClassLoader());
        } catch (ClassNotFoundException cnfe) {
            try {
                return Class.forName("net.minecraft.src." + name, init, ReflectionManager.class.getClassLoader());
            } catch (ClassNotFoundException cnfe2) {
                return null;
            }
        }
    }

    public static void setField(ObfMapping mapping, Object instance, Object value) {
        try {
            Field field = getField(mapping);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setField(Class<?> class1, Object instance, String name, Object value) throws IllegalArgumentException, IllegalAccessException {
        setField(class1, instance, new String[] { name }, value);
    }

    public static void setField(Class<?> class1, Object instance, String[] names, Object value) throws IllegalArgumentException, IllegalAccessException {
        for (Field field : class1.getDeclaredFields()) {
            boolean match = false;
            for (String name : names) {
                if (field.getName().equals(name)) {
                    match = true;
                    break;
                }
            }
            if (!match) {
                continue;
            }

            field.setAccessible(true);
            field.set(instance, value);
            return;
        }
    }

    public static void setField(Class<?> class1, Object instance, int fieldindex, Object value) throws IllegalArgumentException, IllegalAccessException {
        Field field = class1.getDeclaredFields()[fieldindex];
        field.setAccessible(true);
        field.set(instance, value);
    }

    /**
     * Static function
     * void return type
     * single name
     */
    @Deprecated
    public static void callMethod(Class<?> class1, String name, Object... params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        callMethod(class1, null, new String[] { name }, params);
    }

    /**
     * Static function
     * void return type
     * single name
     */
    @Deprecated
    public static void callMethod(Class<?> class1, String[] names, Object... params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        callMethod(class1, null, names, params);
    }

    /**
     * void return type
     * single name
     */
    @Deprecated
    public static void callMethod(Class<?> class1, Object instance, String name, Object... params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        callMethod(class1, null, instance, new String[] { name }, params);
    }

    /**
     * void return type
     */
    @Deprecated
    public static void callMethod(Class<?> class1, Object instance, String[] names, Object... params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        callMethod(class1, null, instance, names, params);
    }

    /**
     * Static method
     * single name
     */
    @Deprecated
    public static <R> R callMethod(Class<?> class1, Class<R> returntype, String name, Object... params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        return callMethod(class1, returntype, null, new String[] { name }, params);
    }

    /**
     * Static method
     */
    @Deprecated
    public static <R> R callMethod(Class<?> class1, Class<R> returntype, String[] names, Object... params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        return callMethod(class1, returntype, null, names, params);
    }

    /**
     * sinlge name
     */
    @Deprecated
    public static <R> R callMethod(Class<?> class1, Class<R> returntype, Object instance, String name, Object... params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        return callMethod(class1, returntype, instance, new String[] { name }, params);
    }

    @Deprecated
    public static <R> R callMethod(Class<?> class1, Class<R> returntype, Object instance, String[] names, Object... params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        nextMethod:
        for (Method method : class1.getDeclaredMethods()) {
            boolean match = false;
            for (String name : names) {
                if (method.getName().equals(name)) {
                    match = true;
                    break;
                }
            }
            if (!match) {
                continue;
            }

            Class<?>[] paramtypes = method.getParameterTypes();
            if (paramtypes.length != params.length) {
                continue;
            }

            for (int i = 0; i < params.length; i++) {
                if (!isInstance(paramtypes[i], params[i])) {
                    continue nextMethod;
                }
            }

            method.setAccessible(true);
            return (R) method.invoke(instance, params);
        }
        return null;
    }

    public static <R> R callMethod(ObfMapping mapping, Class<R> returnType, Object instance, Object... params) {
        try {
            return callMethod_Unsafe(mapping, returnType, instance, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <R> R callMethod_Unsafe(ObfMapping mapping, Class<R> returnType, Object instance, Object... params) throws InvocationTargetException, IllegalAccessException {
        mapping.toRuntime();
        Class<?> clazz = findClass(mapping);
        Method method = null;
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.getName().equals(mapping.s_name) && Type.getMethodDescriptor(m).equals(mapping.s_desc)) {
                method = m;
                break;
            }
        }
        if (method != null) {
            method.setAccessible(true);
            return (R) method.invoke(instance, params);
        }
        return null;
    }

    public static <T> T getField(Class<?> class1, Class<T> fieldType, Object instance, int fieldIndex) throws IllegalArgumentException, IllegalAccessException {
        Field field = class1.getDeclaredFields()[fieldIndex];
        field.setAccessible(true);
        return (T) field.get(instance);
    }

    public static <T> T getField(Class<?> class1, Class<T> fieldType, Object instance, String fieldName) {
        try {
            Field field = class1.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T newInstance(Class<T> class1, Object... params) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        nextMethod:
        for (Constructor<?> constructor : class1.getDeclaredConstructors()) {
            Class<?>[] paramtypes = constructor.getParameterTypes();
            if (paramtypes.length != params.length) {
                continue;
            }

            for (int i = 0; i < params.length; i++) {
                if (!isInstance(paramtypes[i], params[i])) {
                    continue nextMethod;
                }
            }

            constructor.setAccessible(true);
            return (T) constructor.newInstance(params);
        }
        return null;
    }

    public static boolean hasField(Class<?> class1, String fieldName) {
        try {
            class1.getDeclaredField(fieldName);
            return true;
        } catch (NoSuchFieldException nfe) {
            return false;
        }
    }

    public static <T> T get(Field field, Class<T> class1) {
        return get(field, class1, null);
    }

    public static <T> T get(Field field, Class<T> class1, Object instance) {
        try {
            return (T) field.get(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void set(Field field, Object value) {
        set(field, null, value);
    }

    public static void set(Field field, Object instance, Object value) {
        try {
            field.set(instance, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <V> V getField(ObfMapping mapping, Object instance, Class<V> clazz) {
        try {
            Field field = getField(mapping);
            return (V) field.get(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Field getField(ObfMapping mapping) {
        mapping.toRuntime();

        try {
            Class<?> clazz = ReflectionManager.class.getClassLoader().loadClass(mapping.javaClass());
            Field field = clazz.getDeclaredField(mapping.s_name);
            field.setAccessible(true);
            return field;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
