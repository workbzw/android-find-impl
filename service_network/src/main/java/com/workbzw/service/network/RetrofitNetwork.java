package com.workbzw.service.network;

import android.util.Log;

import com.workbzw.lib.base.annotation.Service;

/**
 * @author bzw [workbzw@outlook.com]
 * @date 2021/6/10 8:29 PM
 * @desc
 */
@Service
public class RetrofitNetwork implements NetworkService {
    private static final String TAG = "RetrofitNetwork";

    @Override
    public void request() {
        Log.i(TAG, "request: RetrofitNetwork.request()");
    }
}
