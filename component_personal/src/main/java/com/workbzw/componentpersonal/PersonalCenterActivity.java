package com.workbzw.componentpersonal;

import com.workbzw.componentbase.BaseFragmentActivity;
import com.workbzw.componentbase.abstracts.BaseAbstractFragment;
import com.workbzw.componentpersonal.ui.main.PersonalCenterFragment;

public class PersonalCenterActivity extends BaseFragmentActivity {

    @Override
    protected int setLayoutId() {
        return R.layout.personal_center_activity;
    }

    @Override
    protected BaseAbstractFragment setFragmentInstance() {
        return PersonalCenterFragment.newInstance();
    }
}