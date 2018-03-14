package ar.edu.itba.ss.off_lattice;

import ar.edu.itba.ss.off_lattice.io.SimulationArguments;
import ar.edu.itba.ss.off_lattice.simulation.SimulationEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
     * Entry point.
     *
     * @param args Program arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(SelfPropelledFlockSimulator.class, args);
    }
}
