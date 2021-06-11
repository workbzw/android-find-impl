package com.workbzw.lib.base;


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
    private static Map<String, IService> registry = new HashMap<>();

    public static IService get(String name) {
        IService service = registry.get(name);
        if (service == null)
            throw new IllegalArgumentException("con't find service with this name");
        return service;
    }

    public synchronized static void register(IService iService) {
    }

    public static void printRegistry() {
        Set<String> keySet = registry.keySet();
        for (String key : keySet) {
            IService service = registry.get(key);
        }
    }
}
