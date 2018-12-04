package com.lijun.demo1.task4;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.lijun.demo1.R;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Creator: yiming
 * FuncDesc:    1.MediaExtractor 主要用于多媒体文件的音视频数据的分离。
 *              2.MediaMuxer     作用是生成音频或视频文件；还可以把音频与视频混合成一个音视频文件。
 * copyright  ©2018-2020 Ququ Technology Corporation. All rights reserved.
 */
public class Mp4OperateActivity extends AppCompatActivity {
    private static final String TAG = "Mp4OperateActivity";
    private static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath();
    private static final String INPUT_FILEPATH = SDCARD_PATH + "/doit.mp4";
    private static final String OUTPUT_FILEPATH = SDCARD_PATH + "/xujingli.mp4";

    private ScrollView scrollView;
    private LinearLayout linearView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp4_operate);
        // 获取权限
        int checkWriteExternalPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int checkReadExternalPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (checkWriteExternalPermission != PackageManager.PERMISSION_GRANTED ||
                checkReadExternalPermission != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                try {
                    transcode(INPUT_FILEPATH,OUTPUT_FILEPATH);
                } catch (IOException e) {
                    e.printStackTrace();
                    logout(e.getMessage());
                }
            }
        }).start();

        scrollView = findViewById(R.id.scrollView);
        linearView = findViewById(R.id.linear);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected boolean transcode(String input, String output) throws IOException {
        logout("start processing...");

        MediaMuxer muxer = null;

        MediaExtractor extractor = new MediaExtractor();
        extractor.setDataSource(input);

        logout("start demuxer: " + input);

        int mVideoTrackIndex = 1;
        for (int i =0;i < extractor.getTrackCount(); i++){
            MediaFormat format = extractor.getTrackFormat(i);
            //一个MIME类型包括一个类型（type），一个子类型（subtype）。此外可以加上一个或多个可选参数（optional parameter）。其格式为
            // 类型名 / 子类型名 [ ; 可选参数 ]   目前已被注册的类型名有application、audio、example、image、message、model、multipart、text，以及video
            //常见的mimetype配置列表:
            //Video Type	        Extension	    MIME Type
            //Flash	                .flv	        video/x-flv
            //MPEG-4	            .mp4	        video/mp4
            //iPhone Index	        .m3u8	        application/x-mpegURL
            //iPhone Segment	    .ts	            video/MP2T
            //3GP Mobile	        .3gp	        video/3gpp
            //QuickTime	            .mov	        video/quicktime
            //A/V Interleave	    .avi	        video/x-msvideo
            //Windows Media	        .wmv	        video/x-ms-wmv

            String mime = format.getString(MediaFormat.KEY_MIME);

            if (!mime.startsWith("video")){
                logout("mime not video, continue search");
                continue;
            }
            extractor.selectTrack(i);
            muxer = new MediaMuxer(output, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            mVideoTrackIndex = muxer.addTrack(format);
            muxer.start();
            logout("start muxer: " + output);
        }

        if (muxer == null){
            logout("no video found !");
            return false;
        }

        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        info.presentationTimeUs = 0;
        ByteBuffer buffer = ByteBuffer.allocate(1024*1024*25);

        while (true){
            int sampleSize = extractor.readSampleData(buffer,0);
            if (sampleSize < 0){
                logout("read sample data failed , break !");
                break;
            }
            info.offset = 0;
            info.size = sampleSize;
            info.flags = extractor.getSampleFlags();
            info.presentationTimeUs = extractor.getSampleTime();
            boolean keyframe = (info.flags & MediaCodec.BUFFER_FLAG_SYNC_FRAME) > 0;
            logout("write sample " + keyframe + ", " + sampleSize + ", " + info.presentationTimeUs);
            muxer.writeSampleData(mVideoTrackIndex,buffer,info);
            extractor.advance();
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        extractor.release();

        muxer.stop();
        muxer.release();

        logout("process success !");

        return true;
    }

    private void logout(final String content){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,content);
                final TextView tv = new TextView(Mp4OperateActivity.this);
                tv.setText("\n" + content);
                linearView.addView(tv);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.smoothScrollTo(0,tv.getTop());
                    }
                });
            }
        });
    }
}