package ar.edu.itba.ss.off_lattice.simulation;

import ar.edu.itba.ss.off_lattice.models.Particle;
import ar.edu.itba.ss.off_lattice.models.Space;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The main class of the simulation (i.e the simulation performer).
 */
@Component
public class SimulationEngine {

    // ========================================
    // System stuff
    // ========================================

    /**
     * The side length of the {@link Space} in which the simulation is done.
     */
    private final double spaceSideLength;

    /**
     * The amount of {@link Particle}s to be used in the simulation.
     */
    private final int amountOfParticles;

    /**
     * The interaction radius (i.e up to which radius a {@link Particle} is consider a neighbor of another).
     */
    private final double interactionRadius;


    // ========================================
    // Engine stuff
    // ========================================

    /**
     * The states generated in each simulation step.
     */
    private final Queue<StateSaver.State> states;

    /**
     * A flag indicating that this engine is now simulating
     * (i.e used for concurrency stuff without locking, but throwing an exception).
     */
    private boolean simulating;

    /**
     * Constructor.
     *
     * @param spaceSideLength   The side length of the {@link Space} in which the simulation is done.
     * @param amountOfParticles The amount of {@link ar.edu.itba.ss.off_lattice.models.Particle}s
     * @param interactionRadius The interaction radius
     *                          (i.e up to which radius a {@link Particle} is consider a neighbor of another).
     */
    @Autowired
    public SimulationEngine(@Value("${custom.system.length}") double spaceSideLength,
                            @Value("${custom.system.particles}") int amountOfParticles,
                            @Value("${custom.system.interaction-radius}") double interactionRadius) {
        this.spaceSideLength = spaceSideLength;
        this.amountOfParticles = amountOfParticles;
        this.interactionRadius = interactionRadius;
        this.states = new LinkedList<>();
        this.simulating = false;
    }

    /**
     * Starts the simulation.
     *
     * @param iterations The amount of iterations to be performed in the simulation.
     * @throws IllegalStateException In case this engine is now simulating.
     */
    public void simulate(final int iterations, double eta, int M) throws IllegalStateException {
        validateState();
        this.simulating = true;
        final Space space = Initializer.generateInitialSpace(this.spaceSideLength, this.amountOfParticles);
        final Updater updater = new Updater(space, interactionRadius, eta, M);
        this.states.offer(space.saveState());
        for (int iteration = 0; iteration < iterations; iteration++) {
            updater.update();
            states.offer(space.saveState());
        }
    }

    /**
     * Clears this engine
     * (i.e removes all {@link ar.edu.itba.ss.off_lattice.simulation.StateSaver.State}s from the {@link Queue}).
     *
     * @throws IllegalStateException In case this engine is now simulating.
     */
    public void clearEngine() throws IllegalStateException {
        validateState();
        this.states.clear();
    }

    /**
     * Gets the states gotten from a simulation.
     *
     * @return A {@link Queue} with the states gotten in the simulation.
     * @throws IllegalStateException In case this engine is now simulating.
     */
    public Queue<StateSaver.State> getStates() throws IllegalStateException {
        validateState();
        return new LinkedList<>(states);
    }

    /**
     * Checks if this engine is simulating,
     * throwing an {@link IllegalStateException} in case the process is being performed now.
     *
     * @throws IllegalStateException In case this engine is now simulating.
     */
    private void validateState() throws IllegalStateException {
        if (simulating) {
            throw new IllegalStateException("The engine is simulating now.");
        }
    }
}
