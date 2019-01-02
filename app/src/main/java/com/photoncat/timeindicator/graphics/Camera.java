package com.photoncat.timeindicator.graphics;

import com.photoncat.timeindicator.math.Vec3;

public class Camera {
    private final Vec3 up = new Vec3(0, 1, 0);
    private Vec3 front = new Vec3(0, 0, 1);
    public Vec3 position = new Vec3(0);

    public void move(Vec3 direction) {
        position.add(direction);
    }

    public void moveForward(float distance) {
        position.add(Vec3.multiply(front, distance));
    }

    public void moveLeft(float distance) {
        position.add(Vec3.multiply(Vec3.cross(front, up).normalize(), -distance));  // Right handed.
    }
}
