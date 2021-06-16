package com.workbzw.lib.base;


import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bzw [workbzw@outlook.com]
 * @date 2021/5/26 8:57 AM
 * @desc
 */
public class ServiceManager {
    /**
     * all of the service which remarked with @Service
     *
     * @see com.workbzw.lib.base.annotation.Service
     */
    private static Map<String, Class<? extends IService>> registry;
    /**
     * after get impl by reflect , put into implCache , get impl with cache at next time
     */
    private static Map<String, IService> implCache;

    static {
        registry = new HashMap<>();
        routingTable();
    }

    public static <T extends IService> T getService(Class<T> tClass) {
        String serviceName = tClass.getCanonicalName();
        T byCache = getByCache(serviceName);
        if (byCache != null) {
            return byCache;
        } else {
            T byReflect = getByReflect(serviceName);
            if (implCache == null)
                implCache = new HashMap<>();
            implCache.put(serviceName, byReflect);
            return byReflect;
        }
    }

    private static <T extends IService> T getByCache(String serviceName) {
        if (implCache == null || implCache.size() == 0) return null;
        IService iService = implCache.get(serviceName);
        return ((T) iService);
    }

    private static <T extends IService> T getByReflect(String serviceName) {
        Class<? extends IService> implClass = ServiceManager.getServiceImpl(serviceName);
        try {
            IService iService = implClass.getConstructor().newInstance();
            return (T) iService;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<? extends IService> getServiceImpl(String name) {
        Class<? extends IService> service = registry.get(name);
        if (service == null)
            throw new IllegalArgumentException("can't find service with this name");
        return service;
    }

    public synchronized static void register(String routingTableClassName) {
        try {
            Class<?> subRoutingTableClass = Class.forName(routingTableClassName);
            Object instance = subRoutingTableClass.getConstructor().newInstance();
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

    public static Map<String, Class<? extends IService>> getRoutingTable() {
        return registry;
    }

    /**
     * init registry() {@link registry()}
     * gradle plugin will insert the bytecode: register(String routingTableClassName)
     */
    public static void routingTable() {
    }
}
