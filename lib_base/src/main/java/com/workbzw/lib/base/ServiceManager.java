package com.workbzw.lib.base;


import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author bzw [workbzw@outlook.com]
 * @date 2021/5/26 8:57 AM
 * @desc
 */
public class ServiceManager {
    private static final String TAG = "ServiceManager";
    private static Map<String, Class<? extends IService>> registry = new HashMap<>();

    public static Class<? extends IService> get(String name) {
        Class<? extends IService> service = registry.get(name);
        if (service == null)
            throw new IllegalArgumentException("con't find service with this name");
        return service;
    }

    public synchronized static void register(String className) {
        try {
            Class<?> aClass = Class.forName(className);
            Object instance = aClass.getConstructor().newInstance();
            if (instance instanceof IRoutingTable) {
                registerRoutingTable((IRoutingTable) instance);
            }

        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void registerRoutingTable(IRoutingTable table) {
        table.insertInto(registry);
    }

    public static void printRegistry() {
        Set<String> keySet = registry.keySet();
        for (String key : keySet) {
            Class<? extends IService> service = registry.get(key);

        }
    }

    public static Map<String, Class<? extends IService>> getMap() {
        return registry;
    }

    public static void routingTable() {

    }
}
