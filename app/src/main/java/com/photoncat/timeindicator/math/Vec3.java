package com.photoncat.timeindicator.math;

public class Vec3 {
    private float x;
    private float y;
    private float z;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3(float same) {
        this(same, same, same);
    }

    public void add(Vec3 direction) {
        x += direction.x;
        y += direction.y;
        z += direction.z;
    }
}
