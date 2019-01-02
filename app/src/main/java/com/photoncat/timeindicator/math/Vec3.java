package com.photoncat.timeindicator.math;

public class Vec3 {
    private float x;
    private float y;
    private float z;

    public static Vec3 multiply(Vec3 op1, float op2) {
        Vec3 result = new Vec3(op1);
        result.x *= op2;
        result.y *= op2;
        result.z *= op2;
        return result;
    }

    public static Vec3 cross(Vec3 a, Vec3 b) {
        Vec3 result = new Vec3(0);
        result.x = a.y * b.z - a.z * b.y;
        result.y = a.z * b.x - a.x * b.z;
        result.z = a.x * b.y - a.y * b.x;
        return result;
    }

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

    public Vec3(Vec3 op) {
        x = op.x;
        y = op.y;
        z = op.z;
    }

    public void add(Vec3 direction) {
        x += direction.x;
        y += direction.y;
        z += direction.z;
    }

    public Vec3 normalize() {
        double factor = Math.sqrt(x * x + y * y + z * z);
        x /= factor;
        y /= factor;
        z /= factor;
        return this;
    }
}
