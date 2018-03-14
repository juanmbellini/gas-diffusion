package ar.edu.itba.ss.off_lattice;

import ar.edu.itba.ss.off_lattice.simulation.SimulationEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

/**
 * Main class.
 */
@SpringBootApplication
public class SelfPropelledFlockSimulator implements CommandLineRunner {

    /**
     * The {@link Logger} instance.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(SelfPropelledFlockSimulator.class);

    /**
     * The {@link SimulationEngine} to be used.
     */
    private final SimulationEngine engine;

    /**
     * The {@link SimulationArguments} to be used.
     */
    private final SimulationArguments arguments;

    /**
     * Constructor.
     *
     * @param engine    The {@link SimulationEngine} to be used.
     * @param arguments The {@link SimulationArguments} to be used.
     */
    @Autowired
    public SelfPropelledFlockSimulator(SimulationEngine engine, SimulationArguments arguments) {
        this.engine = engine;
        this.arguments = arguments;
    }


    @Override
    public void run(String... args) throws Exception {
        // First, perform simulation
        simulate();
        // Then, save the simulation results
        saveSimulation();
    }

    /**
     * Performs the simulation phase of the program.
     */
    private void simulate() {
        LOGGER.info("Starting simulation...");
        this.engine.simulate(arguments.getIterations(), arguments.getEta(), arguments.getM());
        LOGGER.info("Finished simulation");
    }

    /**
     * Performs the save phase of the program.
     */
    private void saveSimulation() {
        // TODO: implement raw file (to be used in case anything else must be calculated)
        // TODO: implement Ovito file
        // TODO: implement matlab/octave files
    }

    /**
     * Bean class used for getting values from properties by injection.
     *
     * @apiNote This is a package visible class in order to be scanned by Spring.
     */
    @Component
    /* package */ static final class SimulationArguments {

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
         * @param iterations The amount of iterations.
         * @param eta        The 'eta' value, used for calculating noise for updating angles.
         * @param m          The 'm' value used by cell index method.
         */
        @Autowired
        private SimulationArguments(@Value("${custom.simulation.iterations}") int iterations,
                                    @Value("${custom.simulation.eta}") double eta,
                                    @Value("${custom.simulation.M}") int m) {
            this.iterations = iterations;
            this.eta = eta;
            this.m = m;
        }

        /**
         * @return The amount of iterations.
         */
        private int getIterations() {
            return iterations;
        }

        /**
         * @return The 'eta' value, used for calculating noise for updating angles.
         */
        private double getEta() {
            return eta;
        }

        /**
         * @return The 'M' value used by cell index method.
         */
        private int getM() {
            return m;
        }
    }

    /**
     * Entry point.
     *
     * @param args Program arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(SelfPropelledFlockSimulator.class, args);
    }
}
