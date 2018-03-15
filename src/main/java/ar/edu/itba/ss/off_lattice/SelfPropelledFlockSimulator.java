package ar.edu.itba.ss.off_lattice;

import ar.edu.itba.ss.off_lattice.io.OutputSaver;
import ar.edu.itba.ss.off_lattice.io.SimulationArguments;
import ar.edu.itba.ss.off_lattice.models.Space;
import ar.edu.itba.ss.off_lattice.simulation.SimulationEngine;
import ar.edu.itba.ss.off_lattice.simulation.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

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
     * {@link Map} holding {@link OutputSaver}s, together with the path in which each saver must save.
     */
    private final Map<OutputSaver<Space.SpaceState>, String> outputSavers;

    /**
     * Constructor.
     *
     * @param engine    The {@link SimulationEngine} to be used.
     * @param arguments The {@link SimulationArguments} to be used.
     */
    @Autowired
    public SelfPropelledFlockSimulator(SimulationEngine engine, SimulationArguments arguments,
                                       OutputSaver<Space.SpaceState> rawFileSaver,
                                       @Value("${custom.output.raw}") String rawFilePath,
                                       OutputSaver<Space.SpaceState> spaceOvitoFileSaver,
                                       @Value("${custom.output.ovito}") String ovitoFilePath) {
        this.engine = engine;
        this.arguments = arguments;
        this.outputSavers = new HashMap<>();
        this.outputSavers.put(rawFileSaver, rawFilePath);
        this.outputSavers.put(spaceOvitoFileSaver, ovitoFilePath);
    }


    @Override
    public void run(String... args) throws Exception {
        // First, perform simulation
        simulate();
        // Then, save the simulation results
        saveSimulation();
        System.exit(0);
    }

    /**
     * Performs the simulation phase of the program.
     */
    private void simulate() {
        LOGGER.info("Starting simulation...");
        engine.simulate(arguments.getIterations(), arguments.getEta(), arguments.getM());
        LOGGER.info("Finished simulation");
    }

    /**
     * Performs the save phase of the program.
     */
    private <S extends State> void saveSimulation() throws IOException {
        LOGGER.info("Saving output in all formats...");
        try {
            final Queue<Space.SpaceState> simulationOutput = engine.getStates();
            outputSavers.forEach((saver, path) -> saver.save(path, simulationOutput));
        } catch (IllegalStateException e) {
            LOGGER.error("Tried to get simulation results while still simulating");
            System.exit(1);
        }
        LOGGER.info("Finished saving output in all formats.");
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
