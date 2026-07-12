package com.soundindicators.core;

public final class AngleUtil {

    private AngleUtil() {
    }

    public static double relativeAngleDegrees(double playerYawDegrees,
                                               double playerX, double playerZ,
                                               double soundX, double soundZ) {
        double dx = soundX - playerX;
        double dz = soundZ - playerZ;

        if (dx * dx + dz * dz < 1.0E-6) {
            return 0.0;
        }

        double yawRad = Math.toRadians(playerYawDegrees);
        double lookX = -Math.sin(yawRad);
        double lookZ = Math.cos(yawRad);

        double cross = lookX * dz - lookZ * dx;
        double dot = lookX * dx + lookZ * dz;

        double angle = Math.toDegrees(Math.atan2(cross, dot));
        if (angle < 0) {
            angle += 360.0;
        }
        return angle;
    }

    public static double[] toScreenOffset(double angleDegrees, double radiusPixels) {
        double rad = Math.toRadians(angleDegrees);
        double x = radiusPixels * Math.sin(rad);
        double y = -radiusPixels * Math.cos(rad);
        return new double[]{x, y};
    }
}
