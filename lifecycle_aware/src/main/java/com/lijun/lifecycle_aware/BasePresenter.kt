package com.lijun.lifecycle_aware

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

/**
 * Creator: yiming
 * Date: 2018/11/29 5:08 PM
 * FuncDesc:
 * copyright  ©2018-2020 Ququ Technology Corporation. All rights reserved.
 */
class BasePresenter: IPresenter{

    private val TAG = BasePresenter::class.simpleName

    override fun onCreate(owner: LifecycleOwner) {
        Log.i(TAG,"启动了，哈哈哈 ${owner.lifecycle.currentState}")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        Log.i(TAG,"销毁了，哈哈哈 ${owner.lifecycle.currentState}")
    }

    override fun onLifecycleChanged(owner: LifecycleOwner, event: Lifecycle.Event) {
        Log.i(TAG,"改变了，哈哈哈 ${owner.lifecycle.currentState} ———— ${event.name}")
    }
}