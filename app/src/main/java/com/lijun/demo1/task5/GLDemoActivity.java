package com.lijun.demo1.task5;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.lijun.demo1.R;

/**
 * Creator: yiming
 * FuncDesc:
 * copyright  ©2018-2020 Ququ Technology Corporation. All rights reserved.
 */
public class GLDemoActivity extends AppCompatActivity {
    private GLSurfaceView mGLView; // “画布”

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gl_demo);
        mGLView = new MyGLSurfaceView(this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(300,300); // 加入现有视图中
        addContentView(mGLView, layoutParams);
    }
}