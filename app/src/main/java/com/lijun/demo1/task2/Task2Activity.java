package com.lijun.demo1.task2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.*;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.lijun.demo1.MainActivity;
import com.lijun.demo1.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Creator: yiming
 * FuncDesc:
 * copyright  https://www.jianshu.com/p/475f1a85e8c6    CHSmile
 */
public class Task2Activity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSION = 1001;

    private String[] permissions = new String[] {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    /**被用户拒绝的权限列表*/
    private List<String> mPermissionList = new ArrayList<>();
    private AudioRecord mAudioRecord;
    private boolean isRecording;
    private AudioTrack mAudioTrack;
    private FileInputStream is;
    private Button mAudioControl;
    private Button mPcmConvertWav;
    private Button mPlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task2);

        checkPermissions();

        mAudioControl = findViewById(R.id.btn_control);
        mPcmConvertWav = findViewById(R.id.btn_convert);
        mPlay = findViewById(R.id.btn_play);
        mAudioControl.setOnClickListener(this);
        mPcmConvertWav.setOnClickListener(this);
        mPlay.setOnClickListener(this);
    }

    /**
     * 开始录制
     */
    public void startRecord() {
        final int minBufferSize = AudioRecord.getMinBufferSize(Consts.SAMPLE_RATE_INHZ, Consts.CHANNEL_CONFIG, Consts.AUDIO_FORMAT);
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, Consts.SAMPLE_RATE_INHZ, Consts.CHANNEL_CONFIG
                , Consts.AUDIO_FORMAT, minBufferSize);

        final byte[] data = new byte[minBufferSize];
        final File file = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "test.pcm");

        Toast.makeText(this, file.getAbsolutePath(), Toast.LENGTH_LONG).show();

        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        if (file.exists()) {
            file.delete();
        }

        mAudioRecord.startRecording();
        isRecording = true;

        //TODO pcm 数据无法直接播放， 保存为wav 格式

        new Thread(new Runnable() {
            @Override
            public void run() {
                FileOutputStream os = null;
                try {
                    os = new FileOutputStream(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (os != null) {
                    while (isRecording) {
                        int read = mAudioRecord.read(data, 0, minBufferSize);
                        //如果读取音频数据没有出现错误， 就讲数据写入到文件
                        if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                            try {
                                os.write(data);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        Log.i(TAG, "run: close file output stream !");
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /***
     * 停止录制
     */
    public void stopRecord() {
        isRecording = false;
        //释放资源
        if (mAudioRecord != null) {
            mAudioRecord.stop();
            mAudioRecord.release();
            mAudioRecord = null;
        }
    }

    /***
     * stream 播放
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void playInModeStream() {
        int channelConfig = AudioFormat.CHANNEL_OUT_MONO;
        final int minBufferSize = AudioTrack.getMinBufferSize(Consts.SAMPLE_RATE_INHZ, channelConfig, Consts.AUDIO_FORMAT);
        mAudioTrack = new AudioTrack(
                new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build(),
                new AudioFormat.Builder().setSampleRate(Consts.SAMPLE_RATE_INHZ)
                        .setEncoding(Consts.AUDIO_FORMAT)
                        .setChannelMask(channelConfig)
                        .build(),
                minBufferSize,
                AudioTrack.MODE_STREAM,
                AudioManager.AUDIO_SESSION_ID_GENERATE
        );
        mAudioTrack.play();

        File file = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "test.pcm");
        try {
            is = new FileInputStream(file);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        byte[] tempBuffer = new byte[minBufferSize];
                        while (is.available() > 0) {
                            int readCount = is.read(tempBuffer);
                            if (readCount == AudioTrack.ERROR_INVALID_OPERATION ||
                                    readCount == AudioTrack.ERROR_BAD_VALUE) {
                                continue;
                            }
                            mAudioTrack.write(tempBuffer, 0, readCount);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            for (int i = 0; i < grantResults.length; i ++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, permissions[i] + " 权限被用户禁止！");
                }
            }
        }
    }

    /***
     * 检查权限
     */
    private void checkPermissions() {
        //6.0 动态权限判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < permissions.length; i ++) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), permissions[i])
                        != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permissions[i]);
                }
            }
            if (!mPermissionList.isEmpty()) {
                String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION);
            }
        }
    }

    /**
     * 停止播放
     */
    private void stopPlay() {
        if (mAudioTrack != null) {
            Log.d(TAG, "Stopping");
            mAudioTrack.stop();
            Log.d(TAG, "Releasing");
            mAudioTrack.release();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_control:
                if (mAudioControl.getText().toString().endsWith("开始录音")) {
                    mAudioControl.setText("停止录音");
                    startRecord();
                } else {
                    mAudioControl.setText("开始录音");
                    stopRecord();
                }
                break;
            case R.id.btn_convert:
                PcmToWavUtil pcmToWavUtil = new PcmToWavUtil(Consts.SAMPLE_RATE_INHZ, Consts.CHANNEL_CONFIG, Consts.AUDIO_FORMAT);
                File pcmFile = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "test.pcm");
                File wavFile = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "test.wav");
                if (pcmFile.exists()) {
                    wavFile.delete();
                }
                pcmToWavUtil.pcmToWav(pcmFile.getAbsolutePath(), wavFile.getAbsolutePath());
                break;
            case R.id.btn_play:
                if (mPlay.getText().toString().equals("播放")) {
                    mPlay.setText("停止");
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        playInModeStream();
                    }
                } else {
                    mPlay.setText("播放");
                    stopPlay();
                }
                break;
        }
    }


}
