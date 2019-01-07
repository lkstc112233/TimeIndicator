package com.photoncat.timeindicator.math;

import android.opengl.Matrix;

public class Mat4 {
    private float[] data = new float[16];

    public Mat4() {
        Matrix.setIdentityM(data, 0);
    }

    public void set(int x, int y, float data) {
        this.data[x * 4 + y] = data;
    }

    public float get(int x, int y) {
        return data[x * 4 + y];
    }

    public float[] getArray() {
        return data;
    }
}
