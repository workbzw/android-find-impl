package com.workbzw.baseannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author bzw [workbzw@outlook.com]
 * @date 2021/5/27 11:55 PM
 * @desc
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Action {
    Class<?> actor();

    String name();

    String note();
}
