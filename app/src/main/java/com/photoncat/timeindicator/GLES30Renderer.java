package com.photoncat.timeindicator;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.photoncat.timeindicator.graphics.Camera;
import com.photoncat.timeindicator.graphics.Shader;
import com.photoncat.timeindicator.math.Mat4;
import com.photoncat.timeindicator.math.Vec3;

import java.nio.FloatBuffer;
import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.photoncat.timeindicator.graphics.Shader.*;

public class GLES30Renderer implements GLSurfaceView.Renderer {
    private static final String LOG_TAG = GLES30Renderer.class.getName();
    private boolean mFirstDraw;
    private boolean mSurfaceCreated;
    private int mWidth;
    private int mHeight;
    private long mLastTime;
    private int mFPS;
    private Shader shader;
    private final Camera camera = new Camera();

    private final float g_vertex_buffer_data[] = {
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,

            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,

            -1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,

            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,

            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,

            -1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, -1.0f,
    };
    private final FloatBuffer vertexBuffer = Utilities.makeFloatBuffer(g_vertex_buffer_data);

    public GLES30Renderer() {
        mFirstDraw = true;
        mSurfaceCreated = false;
        mWidth = -1;
        mHeight = -1;
        mLastTime = System.currentTimeMillis();
        mFPS = 0;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.i(LOG_TAG, "Surface created.");
        gl.glEnable(gl.GL_DEPTH_TEST);
        gl.glEnable(gl.GL_BLEND);
        gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
        mSurfaceCreated = true;
        mWidth = -1;
        mHeight = -1;
        int[] VAO = new int[1];
        GLES30.glGenVertexArrays(1, VAO, 0);
        GLES30.glBindVertexArray(VAO[0]);
        // This will identify our vertex buffer
        int[] vertexbuffer = new int[1];

        GLES30.glGenBuffers(1, vertexbuffer, 0);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexbuffer[0]);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, (vertexBuffer.capacity() * 4), vertexBuffer, GLES30.GL_STATIC_DRAW);
        GLES30.glEnableVertexAttribArray(0);
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, 0);

        shader = new Shader(Arrays.asList(
                createVertexShader(R.raw.simple_vertex_shader),
                createFragmentShader(R.raw.simple_fragment_shader)
        ));
        Mat4 defaultMatrix = new Mat4();
        shader.setMatrix("transform", defaultMatrix);
        shader.setMatrix("view", defaultMatrix);
        shader.setMatrix("projection", defaultMatrix);
        shader.use();

        camera.position.setX(0);
        camera.position.setY(0);
        camera.position.setZ(-3);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (!mSurfaceCreated && width == mWidth && height == mHeight) {
            Log.i(LOG_TAG, "Surface changed but already handled.");
            return;
        }
        // Android honeycomb has an option to keep the
        // context.
        String msg = "Surface changed width:" + width
                + " height:" + height;
        if (mSurfaceCreated) {
            msg += ", context lost.";
        } else {
            msg += ".";
        }
        Log.i(LOG_TAG, msg);

        mWidth = width;
        mHeight = height;


        Mat4 projection = new Mat4();
        Matrix.perspectiveM(projection.getArray(), 0,
                45, (float) width / height, 0.1F, 1000.0F);
        shader.setMatrix("projection", projection);

        gl.glViewport(0, 0, width, height);

        onCreate(mWidth, mHeight, mSurfaceCreated);
        mSurfaceCreated = false;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        onDrawFrame(mFirstDraw);

        mFPS++;
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastTime >= 1000) {
            mFPS = 0;
            mLastTime = currentTime;
        }

        if (mFirstDraw) {
            mFirstDraw = false;
        }
    }

    public int getFPS() {
        return mFPS;
    }

    public void onCreate(int width, int height,
                         boolean contextLost) {
        GLES30.glClearColor(0.1f, 0f, 0.3f, 1f);
    }

    private void onDrawFrame(boolean firstDraw) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        Mat4 viewMat;
        synchronized (camera) {
            viewMat = camera.getViewMat();
        }
        shader.setMatrix("view", viewMat);

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, g_vertex_buffer_data.length / 3); // Starting from vertex 0; 3 vertices total -> 1 triangle
    }

    public void cameraTo(Vec3 position, Vec3 up) {
        synchronized (camera) {
            camera.position = position;
            camera.up = up;
            camera.lookAt(new Vec3(0));
        }
    }
}
