package com.photoncat.timeindicator;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class ClockActivity extends AppCompatActivity {
    private static final String LOG_TAG = ClockActivity.class.getName();
    private GLSurfaceView mGLView;

    private boolean hasGLES30() {
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        Log.e(LOG_TAG, "version: " + Integer.toHexString(configurationInfo.reqGlEsVersion));
        return configurationInfo.reqGlEsVersion >= 0x30000;
    }

    private void initialize() {
        if (hasGLES30()) {
            mGLView = new GLSurfaceView(this);
            mGLView.setEGLContextClientVersion(3);
            mGLView.setPreserveEGLContextOnPause(true);
            mGLView.setRenderer(new GLES30Renderer());
        } else {
            // Time to get a new phone, OpenGL ES 3.0 not supported.
            throw new IllegalStateException("GLES 3.0 not supported.");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initialize();
        setContentView(mGLView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }
}
