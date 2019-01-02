package com.photoncat.timeindicator.graphics;

import com.photoncat.timeindicator.math.Vec3;

public class Camera {
    public Vec3 position = new Vec3(0);

    public void move(Vec3 direction) {
        position.add(direction);
    }
}
