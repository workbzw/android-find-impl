package com.workbzw.componentbase;

import android.app.Application;

import androidx.annotation.NonNull;

import com.workbzw.componentbase.abstracts.BaseAbstractViewModel;

/**
 * @author bzw [workbzw@outlook.com]
 * @date 2021/6/1 10:53 PM
 * @desc
 */
public abstract class BaseViewModel extends BaseAbstractViewModel {
    public BaseViewModel(@NonNull Application application) {
        super(application);
    }
}
