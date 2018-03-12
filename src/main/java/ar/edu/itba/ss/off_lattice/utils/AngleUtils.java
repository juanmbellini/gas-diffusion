package ar.edu.itba.ss.off_lattice.utils;

/**
 * Class implementing some algorithms for angles.
 */
public class AngleUtils {

    /**
     * @return A random angle value, specified in radians.
     */
    public static double randomAngle() {
        return Math.toRadians(Math.random() * 360);
    }
}
