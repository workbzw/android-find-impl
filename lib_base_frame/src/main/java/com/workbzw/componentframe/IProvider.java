package com.workbzw.componentframe;

/**
 * @Author bzw [workbzw@outlook.com]
 * @CreateDate: 2021/5/19 1:10 PM
 * @Description: service
 */
public interface IProvider {
    IService get();

    String info();
}
