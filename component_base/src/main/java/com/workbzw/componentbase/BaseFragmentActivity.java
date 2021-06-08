package com.workbzw.componentbase;

import android.os.Bundle;

import com.workbzw.componentbase.abstracts.BaseAbstractActivity;
import com.workbzw.componentbase.abstracts.BaseAbstractFragment;

/**
 * @author bzw [workbzw@outlook.com]
 * @date 2021/5/30 11:41 AM
 * @desc
 */
public abstract class BaseFragmentActivity extends BaseAbstractActivity {
    protected abstract int setLayoutId();

    protected abstract BaseAbstractFragment setFragmentInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutId());
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, setFragmentInstance())
                    .commitNow();
        }
    }
}
