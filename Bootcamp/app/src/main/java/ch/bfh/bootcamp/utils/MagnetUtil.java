package ch.bfh.bootcamp.utils;

public class MagnetUtil {

    public static int calculateMagnetPercentage(float[] mag, double MagnetSensorMaximumRange) {
        double length = Math.sqrt(mag[0] * mag[0] + mag[1] * mag[1] + mag[2] *
                mag[2]);
        return (int) Math.floor(100 / MagnetSensorMaximumRange * length);
    }

}
