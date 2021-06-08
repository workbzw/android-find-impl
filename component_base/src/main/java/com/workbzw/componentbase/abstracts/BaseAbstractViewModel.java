package com.workbzw.componentbase.abstracts;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

/**
 * @author bzw [workbzw@outlook.com]
 * @date 2021/6/1 10:53 PM
 * @desc
 */
public abstract class BaseAbstractViewModel extends AndroidViewModel {
    public BaseAbstractViewModel(@NonNull Application application) {
        super(application);
    }
}
