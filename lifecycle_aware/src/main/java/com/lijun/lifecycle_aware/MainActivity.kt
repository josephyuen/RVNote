package com.lijun.lifecycle_aware

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

/**
 *  Android  Components
 */
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerPresenter()
        startObserveObj()

    }


    private lateinit var mModel: NameViewModel


    // 开始观测 动态数据
    private fun startObserveObj() {

        // Other code to setup the activity...

        // Get the ViewModel.
        mModel = ViewModelProviders.of(this).get(NameViewModel::class.java)

        //observer，动态数据数据观察者
        val nameObserver = Observer<String> { newName ->
            tv_text.text = newName
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        mModel.currentName.observe(this, nameObserver)


    }


    /*
     * 注册 Presenter 的监听
     */
    private fun registerPresenter() {
        val presenter = BasePresenter()
        lifecycle.addObserver(presenter)
    }


}
