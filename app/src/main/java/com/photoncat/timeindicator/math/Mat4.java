package com.photoncat.timeindicator.math;

public class Mat4 {
    private float[] data = new float[16];

    public void set(int x, int y, float data) {
        this.data[x * 4 + y] = data;
    }

    public float get(int x, int y) {
        return data[x * 4 + y];
    }
}