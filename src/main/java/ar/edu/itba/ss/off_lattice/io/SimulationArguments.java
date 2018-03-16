package ar.edu.itba.ss.off_lattice.io;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Bean class used for getting values from properties by injection.
 *
 * @apiNote This is a package visible class in order to be scanned by Spring.
 */
@Component
public final class SimulationArguments {

    /**
     * The amount of iterations.
     */
    private final int iterations;

    /**
     * The 'eta' value, used for calculating noise for updating angles.
     */
    private final double eta;

    /**
     * The 'M' value used by cell index method.
     */
    private final int m;

    /**
     * The speed module used in the simulation.
     */
    private final double speedModule;


    /**
     * @param iterations  The amount of iterations.
     * @param eta         The 'eta' value, used for calculating noise for updating angles.
     * @param m           The 'm' value used by cell index method.
     * @param speedModule The speed module used in the simulation.
     */
    @Autowired
    private SimulationArguments(@Value("${custom.simulation.iterations}") int iterations,
                                @Value("${custom.simulation.eta}") double eta,
                                @Value("${custom.simulation.M}") int m,
                                @Value("${custom.simulation.speed-module}") double speedModule) {
        this.iterations = iterations;
        this.eta = eta;
        this.m = m;
        this.speedModule = speedModule;
    }

    /**
     * @return The amount of iterations.
     */
    public int getIterations() {
        return iterations;
    }

    /**
     * @return The 'eta' value, used for calculating noise for updating angles.
     */
    public double getEta() {
        return eta;
    }

    /**
     * @return The 'M' value used by cell index method.
     */
    public int getM() {
        return m;
    }

    /**
     * @return The speed module used in the simulation.
     */
    public double getSpeedModule() {
        return speedModule;
    }
}
