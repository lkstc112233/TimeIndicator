package com.photoncat.timeindicator.graphics;

import android.opengl.GLES31;
import android.util.Log;

import com.photoncat.timeindicator.Utilities;
import com.photoncat.timeindicator.math.Mat4;

import java.nio.IntBuffer;
import java.util.List;

public class Shader {
    private static final String LOG_TAG = Shader.class.getName();
    private int programId;
    private boolean valid = true;

    public static SingleShader createVertexShader(int resId) {
        return new SingleShader(resId, GLES31.GL_VERTEX_SHADER);
    }

    public static SingleShader createFragmentShader(int resId) {
        return new SingleShader(resId, GLES31.GL_FRAGMENT_SHADER);
    }

    public static class SingleShader {
        private static final String LOG_TAG = SingleShader.class.getName();
        private int shaderId = 0;
        private boolean valid = true;

        public int getShaderId() {
            return shaderId;
        }

        public boolean isValid() {
            return valid;
        }

        private SingleShader(int resId, int shaderType) {
            shaderId = GLES31.glCreateShader(shaderType);
            GLES31.glShaderSource(shaderId, Utilities.readFileFromResourse(resId));
            GLES31.glCompileShader(shaderId);

            IntBuffer success = IntBuffer.allocate(1);
            GLES31.glGetShaderiv(shaderId, GLES31.GL_COMPILE_STATUS, success);
            if (success.get(0) == 0) {
                Log.e(LOG_TAG, GLES31.glGetShaderInfoLog(shaderId));
                valid = false;
            }
        }
    }

    public Shader(List<SingleShader> list) {
        programId = GLES31.glCreateProgram();
        for (SingleShader shader : list) {
            GLES31.glAttachShader(programId, shader.getShaderId());
        }
        GLES31.glLinkProgram(programId);

        IntBuffer success = IntBuffer.allocate(1);
        GLES31.glGetProgramiv(programId, GLES31.GL_LINK_STATUS, success);
        if (success.get(0) == 0) {
            Log.e(LOG_TAG, GLES31.glGetProgramInfoLog(programId));
        }
    }

    public int getProgramId() {
        return programId;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValue(String key, int i0) {
        GLES31.glProgramUniform1i(getProgramId(), GLES31.glGetUniformLocation(getProgramId(), key), i0);
    }

    public void setValue(String key, float f0) {
        GLES31.glProgramUniform1f(getProgramId(), GLES31.glGetUniformLocation(getProgramId(), key), f0);
    }

    public void setMatrix(String key, Mat4 mat) {
        GLES31.glProgramUniformMatrix4fv(getProgramId(),
                GLES31.glGetUniformLocation(getProgramId(), key),
                1,
                false,
                mat.getArray(),
                0);
    }

    public void use() {
        GLES31.glUseProgram(programId);
    }
}
