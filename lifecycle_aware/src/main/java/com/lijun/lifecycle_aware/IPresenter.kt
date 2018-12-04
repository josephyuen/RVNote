package com.lijun.lifecycle_aware

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import org.jetbrains.annotations.NotNull

/**
 * Creator: yiming
 * Date: 2018/11/29 5:03 PM
 * FuncDesc:
 * copyright  ©2018-2020 Ququ Technology Corporation. All rights reserved.
 */
interface IPresenter: LifecycleObserver{


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(owner: LifecycleOwner)

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner:LifecycleOwner)

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onLifecycleChanged(owner:LifecycleOwner,event:Lifecycle.Event)




}