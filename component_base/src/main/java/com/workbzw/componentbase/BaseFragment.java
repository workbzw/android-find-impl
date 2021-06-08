package com.workbzw.componentbase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.workbzw.componentbase.abstracts.BaseAbstractFragment;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author bzw [workbzw@outlook.com]
 * @date 2021/5/30 11:35 AM
 * @desc
 */
public abstract class BaseFragment<TDataBinding extends ViewDataBinding, TViewModel extends BaseViewModel> extends BaseAbstractFragment {

    protected abstract int setLayoutId();

    protected abstract void init(Bundle savedInstanceState);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, setLayoutId(), container, false);
        return binding.getRoot();
    }

    protected TDataBinding binding;
    protected TViewModel vm;

    /**
     * 此方法 整个生命周期中只调用一次
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Class clazz;
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            clazz = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
        } else {
            //如果没有指定泛型参数，则默认使用BaseViewModel
            clazz = BaseViewModel.class;
        }
        vm = (TViewModel) new ViewModelProvider(this).get(clazz);
        binding.setVariable(BR.vm, vm);
        binding.setVariable(BR.viewModel, vm);
        binding.setLifecycleOwner(this);
        init(savedInstanceState);
    }
}
