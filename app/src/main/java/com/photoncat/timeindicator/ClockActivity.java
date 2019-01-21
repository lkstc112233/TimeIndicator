package com.photoncat.timeindicator;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEventListener;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.photoncat.timeindicator.math.Vec3;

import java.util.Arrays;

public class ClockActivity extends AppCompatActivity {
    private static final String LOG_TAG = ClockActivity.class.getName();
    private GLSurfaceView mGLView;
    private GLES30Renderer renderer;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private TriggerEventListener mTriggerEventListener;

    private boolean hasGLES30() {
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        Log.e(LOG_TAG, "version: " + Integer.toHexString(configurationInfo.reqGlEsVersion));
        return configurationInfo.reqGlEsVersion >= 0x30001;
    }

    private void initialize() {
        if (hasGLES30()) {
            mGLView = new GLSurfaceView(this);
            mGLView.setEGLContextClientVersion(3);
            mGLView.setPreserveEGLContextOnPause(true);
            mGLView.setRenderer(renderer = new GLES30Renderer());
        } else {
            // Time to get a new phone, OpenGL ES 3.1 not supported.
            throw new IllegalStateException("GLES 3.1 not supported.");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utilities.setResource(getResources());
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        final SensorEventListener mEventListener = new SensorEventListener() {
            public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
            }

            private float[] rotateMatrix = new float[16];
            private float[] rotateMatrixTemp = new float[32];
            private float[] up = new float[]{0, 1, 0, 1, 0, 0, 0, 0};
            private float[] front = new float[]{0, 0, -10, 1, 0, 0, 0, 0};
            private float[] inverseXZ = new float[]{-1, 0, 0, 0, 0, 1, 0, 0, 0, 0, -1, 0, 0, 0, 0, 1};
            private float[] quaternion = new float[4];

            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                    SensorManager.getRotationMatrixFromVector(rotateMatrix, event.values);
                    SensorManager.getQuaternionFromVector(quaternion, event.values);

                    // Trick to switch the matrix.
                    Matrix.multiplyMM(rotateMatrixTemp, 0, inverseXZ, 0, rotateMatrix, 0);
                    Matrix.multiplyMM(rotateMatrixTemp, 16, rotateMatrixTemp, 0, inverseXZ, 0);
                    Matrix.transposeM(rotateMatrix, 0, rotateMatrixTemp, 16);
                    Matrix.multiplyMV(up, 4, rotateMatrix, 0, up, 0);
                    Matrix.multiplyMV(front, 4, rotateMatrix, 0, front, 0);

                    renderer.cameraTo(new Vec3(front[4], front[5], front[6]), new Vec3(up[4], up[5], up[6]));
                }
            }
        };
        mSensorManager.registerListener(mEventListener, mSensor, SensorManager.SENSOR_DELAY_FASTEST, new Handler());
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
