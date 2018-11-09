package com.lijun.demo1.task5;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.opengles.GL10;

/**
 * Creator: yiming
 * FuncDesc: 渲染器类MyGLRenderer (openGL画笔)
 * copyright  ©2018-2020 Ququ Technology Corporation. All rights reserved.
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {
    private Triangle mTriangle;

    /**
     * @param type       GLES20.GL_VERTEX_SHADER or GLES20.GL_FRAGMENT_SHADER
     * @param shaderCode shader code string
     * @return GLES20.glCreateShader(type)
     */
    public static int loadShader(int type, String shaderCode) {
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, javax.microedition.khronos.egl.EGLConfig config) {
        mTriangle = new Triangle(); // 创建时初始化三角形实例
        GLES20.glClearColor(-255.5f, 0.0f, 0.0f, 1.0f);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT); // 每次都重绘背景
        mTriangle.draw(); // 直接调用draw()方法
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }
}