package com.workbzw.componentpersonal.ui.main;

import android.os.Bundle;

import com.workbzw.componentbase.BaseFragment;
import com.workbzw.componentpersonal.R;
import com.workbzw.componentpersonal.databinding.MainFragmentBinding;

public class PersonalCenterFragment extends BaseFragment<MainFragmentBinding, MainViewModel> {

    public static PersonalCenterFragment newInstance() {
        return new PersonalCenterFragment();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.main_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }
}