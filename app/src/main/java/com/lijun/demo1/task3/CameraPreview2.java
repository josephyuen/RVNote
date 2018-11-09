package com.lijun.demo1.task3;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.TextureView;

import java.io.IOException;

/**
 * Creator: yiming
 * FuncDesc: camera   TextureView 承载
 * copyright  ©2018-2020 Ququ Technology Corporation. All rights reserved.
 */
public class CameraPreview2 extends TextureView implements TextureView.SurfaceTextureListener {
    private static final String TAG = "rustApp";
    private Camera mCamera;

    public CameraPreview2(Context context) {
        super(context);
    }

    public CameraPreview2(Context context, Camera camera) {
        super(context);
        mCamera = camera;
    }

    public void setCamera(Camera camera) {
        this.mCamera = camera;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.d(TAG, "TextureView onSurfaceTextureAvailable");
        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            mCamera.setDisplayOrientation(90);
        } else {
            mCamera.setDisplayOrientation(0);
        }
        try {
            mCamera.setPreviewCallback(mCameraPreviewCallback);
            mCamera.setPreviewTexture(surface); // 使用SurfaceTexture
            mCamera.startPreview();
        } catch (IOException ioe) {
            // Something bad happened
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        Log.d(TAG, "TextureView onSurfaceTextureSizeChanged"); // Ignored, Camera does all the work for us
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.d(TAG, "TextureView onSurfaceTextureDestroyed");
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // Invoked every time there's a new Camera preview frame
    }

    private Camera.PreviewCallback mCameraPreviewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            Log.d(TAG, "onPreviewFrame: data.length=" + data.length);
        }
    };
}
