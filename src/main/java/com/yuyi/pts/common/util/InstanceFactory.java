package com.yuyi.pts.common.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 多例对象工厂
 *
 * @author greyson
 * @since 2021/5/17
 */
public class InstanceFactory<T> {

    private Map<String, Constructor> constructorMap;
    private Map<String, Map<Constructor, T>> instanceMap = new HashMap<>();

    private InstanceFactory() {
    }

    public static InstanceFactory init(Class targetClass){
        Constructor[] declaredConstructors = targetClass.getDeclaredConstructors();
        InstanceFactory factory = new InstanceFactory();
        factory.constructorMap = Arrays.stream(declaredConstructors).collect(Collectors.toMap(InstanceFactory::toConstructorMapKey, Function.identity()));
        return factory;
    }

    public T get(Object ... params){
        String key = toConstructorMapKey(params);
        Constructor<T> constructor = constructorMap.get(key);

        String instanceKey = toInstanceMapKey(params);
        if (instanceMap.containsKey(instanceKey) && instanceMap.get(instanceKey).containsKey(constructor)){
            return instanceMap.get(instanceKey).get(constructor);
        }

        try {
            T t = constructor.newInstance(params);
            this.putToInstanceMap(instanceKey, constructor, t);
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }



    private void putToInstanceMap(String key, Constructor<T> constructor, T t) {
        Map<Constructor, T> constructorTMap = this.instanceMap.getOrDefault(key, new HashMap());
        constructorTMap.put(constructor, t);
        this.instanceMap.put(key, constructorTMap);
    }

    private String toInstanceMapKey(Object ... paramArray) {
        if (paramArray == null || paramArray.length == 0) return "";
        String key = Arrays.stream(paramArray).map(Object::toString).collect(Collectors.joining("-"));
        return key;
    }

    private static String toConstructorMapKey(Constructor constructor){
        Class[] parameterTypes = constructor.getParameterTypes();
        String key = toConstructorMapKey(parameterTypes);
        return key;
    }

    private static String toConstructorMapKey(Class ... paramClassArray){
        if (paramClassArray == null || paramClassArray.length == 0) return "";
        String key = Arrays.stream(paramClassArray).map(Class::getName).collect(Collectors.joining("-"));
        return key;
    }

    private static String toConstructorMapKey(Object ... paramArray){
        if (paramArray == null || paramArray.length == 0) return "";
        Class[] paramClassArray = Arrays.stream(paramArray).map(Object::getClass).toArray(Class[]::new);
        return toConstructorMapKey(paramClassArray);
    }
}
