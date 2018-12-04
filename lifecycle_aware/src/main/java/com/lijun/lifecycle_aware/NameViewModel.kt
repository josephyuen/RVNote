package com.lijun.lifecycle_aware

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Creator: yiming
 * Date: 2018/11/29 5:54 PM
 * FuncDesc:
 * copyright  ©2018-2020 Ququ Technology Corporation. All rights reserved.
 */
class NameViewModel : ViewModel() {

    // Create a LiveData with a String
    val currentName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }


}