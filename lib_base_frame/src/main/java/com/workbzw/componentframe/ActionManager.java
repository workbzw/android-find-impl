package com.workbzw.componentframe;

import com.workbzw.componentframe.annotation.Action;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author bzw [workbzw@outlook.com]
 * @date 2021/5/26 8:57 AM
 * @desc
 */
public class ActionManager {
    private static final String TAG = "ServiceManager";
    private static Map<String, IAction> registry = new HashMap<>();

    public static IAction get(String name) {
        IAction service = registry.get(name);
        if (service == null)
            throw new IllegalArgumentException("con't find service with this name");
        return service;
    }

    public synchronized static void register(IAction iAction) {
        String name = iAction.name();
        if (name == null || name.equals(""))
            throw new IllegalArgumentException("you should init name for service");
        IAction service = registry.get(iAction.name());
        if (service != null)
            throw new IllegalArgumentException("this service have been registered , don't register one more");
        registry.put(iAction.name(), iAction);
    }

    public static void printRegistry() {
        Set<String> keySet = registry.keySet();
        for (String key : keySet) {
            IAction service = registry.get(key);
            System.out.println(TAG + "service registry-->" + service.name());
        }
    }
}
