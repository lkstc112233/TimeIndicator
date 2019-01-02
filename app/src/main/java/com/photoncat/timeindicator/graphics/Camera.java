package com.photoncat.timeindicator.graphics;

import com.photoncat.timeindicator.math.Vec3;

public class Camera {
    private Vec3 front = new Vec3(0, 0, 1);
    public Vec3 position = new Vec3(0);

    public void move(Vec3 direction) {
        position.add(direction);
    }

    public void moveForward(float distance) {
        position.add(Vec3.multiply(front, distance));
    }
}
