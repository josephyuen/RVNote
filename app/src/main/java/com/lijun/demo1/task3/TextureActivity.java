package com.lijun.demo1.task3;

import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.lijun.demo1.R;

/**
 * Creator: yiming
 * FuncDesc:   TextureView 预览 Camera，取到NV21数据
 * copyright  ©2018-2020 Ququ Technology Corporation. All rights reserved.
 */
public class TextureActivity extends AppCompatActivity {
    private static final String TAG = "rustApp";
    private Camera mCamera;
    private CameraPreview2 mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        new InitCameraThread().start();
    }

    @Override
    protected void onResume() {
        if (null == mCamera) {
            if (mPreview != null && safeCameraOpen()) {
                mPreview.setCamera(mCamera); // 重新获取camera操作权
            } else {
                Log.e(TAG, "无法操作camera");
            }
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
    }

    private boolean safeCameraOpen() {
        boolean qOpened = false;
        try {
            releaseCamera();
            mCamera = Camera.open();
            qOpened = (mCamera != null);
        } catch (Exception e) {
            Log.e(TAG, "failed to open Camera");
            e.printStackTrace();
        }
        return qOpened;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    private class InitCameraThread extends Thread {
        @Override
        public void run() {
            super.run();
            if (safeCameraOpen()) {
                Log.d(TAG, "TextureActivity 开启摄像头");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPreview = new CameraPreview2(TextureActivity.this, mCamera);
                        mPreview.setSurfaceTextureListener(mPreview);
                        FrameLayout preview = findViewById(R.id.camera_preview);
                        preview.addView(mPreview);
                        mPreview.setCamera(mCamera); // 获取camera操作权
                    }
                });
            }
        }
    }
}
