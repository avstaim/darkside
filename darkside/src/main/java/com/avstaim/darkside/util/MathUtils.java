package com.avstaim.darkside.util;

public final class MathUtils {

    public static final float ACCURACY_001 = 0.001f;

    private MathUtils() {}

    public static boolean isEqual(float f0, float f1) {
        float epsilon = Math.max(Math.ulp(f0), Math.ulp(f1));
        return f0 == f1 || Math.abs(f0 - f1) < epsilon;
    }

    /**
     * Compares two floats with accuracy
     * @param f0 first value
     * @param f1 second value
     * @param accuracy allowable error
     * @return is |f0 - f1| < accuracy ?
     */
    public static boolean isEqual(float f0, float f1, float accuracy) {
        return Math.abs(f0 - f1) < accuracy;
    }
}
