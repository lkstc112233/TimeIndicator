package com.photoncat.timeindicator.graphics;

import android.opengl.Matrix;

import com.photoncat.timeindicator.math.Mat4;
import com.photoncat.timeindicator.math.Vec3;

public class Camera {
    private final Vec3 up = new Vec3(0, 1, 0);
    private boolean lock = false;
    private Vec3 front = new Vec3(0, 0, 1);
    public Vec3 position = new Vec3(0);

    public void move(Vec3 direction) {
        position.add(direction);
    }

    public void moveForward(float distance) {
        position.add(Vec3.multiply(front, distance));
    }

    public void moveBackward(float distance) {
        moveForward(-distance);
    }

    public void moveLeft(float distance) {
        position.add(Vec3.multiply(Vec3.cross(front, up).normalize(), -distance));  // Right handed.
    }

    public void moveRight(float distance) {
        moveLeft(-distance);
    }

    public void moveUp(float distance) {
        position.add(Vec3.multiply(up, distance));
    }

    public void moveDown(float distance) {
        moveUp(-distance);
    }

    public void turnYaw(float yaw) {
        Mat4 turn = new Mat4();
        Matrix.rotateM(turn.getArray(), 0, yaw, 0, 1, 0);
        float[] frontLast = new float[4];
        frontLast[0] = front.getX();
        frontLast[1] = front.getY();
        frontLast[2] = front.getZ();
        frontLast[3] = 1.0F;
        float[] result = new float[4];
        Matrix.multiplyMV(result, 0, turn.getArray(), 0, frontLast, 0);
        front.setX(result[0]);
        front.setY(result[1]);
        front.setZ(result[2]);
        front.normalize();
    }

    public void turnPitch(float pitch) {
        Mat4 turn = new Mat4();
        Vec3 cross = Vec3.cross(front, up);
        Matrix.rotateM(turn.getArray(), 0, pitch, cross.getX(), cross.getY(), cross.getZ());
        float[] frontLast = new float[4];
        frontLast[0] = front.getX();
        frontLast[1] = front.getY();
        frontLast[2] = front.getZ();
        frontLast[3] = 1.0F;
        float[] result = new float[4];
        Matrix.multiplyMV(result, 0, turn.getArray(), 0, frontLast, 0);
        front.setX(result[0]);
        front.setY(result[1]);
        front.setZ(result[2]);
        front.normalize();
    }

    public void lookAt(Vec3 pos) {
        Vec3 newFront = new Vec3(pos);
        front = Vec3.diff(pos, position).normalize();
    }

    public boolean isViewLocked() {
        return lock;
    }

    public Mat4 getViewMat() {
        if (lock) {
            front = Vec3.diff(target, position).normalize();
        }
        Mat4 result = new Mat4();
        Vec3 center = new Vec3(position);
        center.add(front);
        Matrix.setLookAtM(result.getArray(), 0,
                position.getX(), position.getY(), position.getZ(),
                center.getX(), center.getY(), center.getZ(),
                up.getX(), up.getY(), up.getZ());
        return result;
    }
}
