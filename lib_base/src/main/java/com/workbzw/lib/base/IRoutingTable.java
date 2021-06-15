package com.workbzw.lib.base;

import java.util.Map;

/**
 * @author bzw [workbzw@outlook.com]
 * @date 2021/6/11 3:16 PM
 * @desc
 */
public interface IRoutingTable {
    void insertInto(Map<String, Class<? extends IService>> routingTable);
}
