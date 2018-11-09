package com.lijun.demo1.task5;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Creator: yiming
 * FuncDesc: “画布”类 MyGLSurfaceView
 * copyright  ©2018-2020 Ququ Technology Corporation. All rights reserved.
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mRenderer = new MyGLRenderer();

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
    }
}