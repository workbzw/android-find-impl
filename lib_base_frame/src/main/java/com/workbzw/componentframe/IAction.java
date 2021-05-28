package com.workbzw.componentframe;

/**
 * @Author bzw [workbzw@outlook.com]
 * @CreateDate: 2021/5/19 12:59 PM
 * @Description: {@code ServiceManager}
 */
public interface IAction {
    abstract class Factory {
        abstract <T extends IAction> T get(String name);

        void uu() {
        }
    }
}
