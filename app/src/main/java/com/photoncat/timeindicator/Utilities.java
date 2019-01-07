package com.photoncat.timeindicator;

import android.content.res.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Utilities {
    private static Resources res;

    public static void setResource(Resources res) {
        Utilities.res = res;
    }

    /**
     * Make a direct NIO FloatBuffer from an array of floats
     *
     * @param arr The array
     * @return The newly created FloatBuffer
     */
    public static FloatBuffer makeFloatBuffer(float[] arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(arr);
        fb.position(0);
        return fb;
    }

    public static String readFileFromResourse(int resId) {
        try (InputStream stream = res.openRawResource(resId);
             java.util.Scanner scanner = new java.util.Scanner(stream).useDelimiter("\\A")) {
            return scanner.hasNext() ? scanner.next() : "";
        } catch (IOException e) {
            // Rethrow the exception since we want the program to break.
            throw new UncheckedIOException(e);
        }
    }
}
