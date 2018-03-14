package ar.edu.itba.ss.off_lattice.models;

import ar.edu.itba.ss.off_lattice.simulation.StateSaver;
import org.springframework.util.Assert;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a space in which the simulation is done.
 * Note that this is a squared space.
 */
public class Space implements StateSaver {

    /**
     * The length of the side of this space.
     */
    private final double sideLength;

    /**
     * The particles in this space.
     */
    private final List<Particle> particles;

    /**
     * Constructor.
     *
     * @param sideLength The length of the side of this space.
     * @param particles  The particles in this space.
     * @throws IllegalArgumentException If the side length is not positive,
     *                                  if the {@code particles} list is {@code null},
     *                                  or if any particle in the {@code particles} list is not part of this space.
     */
    public Space(final double sideLength, final List<Particle> particles) throws IllegalArgumentException {
        validateSideLength(sideLength);
        validateParticlesList(particles, sideLength);
        this.sideLength = sideLength;
        this.particles = particles;
    }

    /**
     * @return The length of the side of this space.
     */
    public double getSideLength() {
        return sideLength;
    }

    /**
     * @return The particles in this space.
     */
    public List<Particle> getParticles() {
        return new LinkedList<>(particles);
    }

    @Override
    public State saveState() {
        return null;
    }

    /**
     * Bean class that extends {@link StateSaver.State},
     * which stores the actual state of a {@link Space}.
     */
    public static final class SpaceState extends State {
        /**
         * The {@link StateSaver.State} of the {@link Particle}s in the {@link Space}.
         */
        final List<Particle.ParticleState> particleStates;

        /**
         * Constructor.
         *
         * @param space The {@link Space} whose state must be saved.
         */
        public SpaceState(Space space) {
            this.particleStates = space.getParticles().stream()
                    .map(Particle::saveState)
                    .collect(Collectors.toList());
        }
    }


    /**
     * Checks if the given {@code sideLength} value is legal.
     *
     * @param sideLength The value to be validated.
     * @throws IllegalArgumentException In case the value is not legal (i.e is not positive).
     */
    private static void validateSideLength(final double sideLength) throws IllegalArgumentException {
        if (Double.compare(sideLength, 0.0) <= 0) {
            throw new IllegalArgumentException("The side length must be positive");
        }
    }

    /**
     * Checks if the given {@code particles} {@link List} is legal.
     *
     * @param particles  The {@code particles} {@link List} to be validated.
     * @param sideLength The side length, which states a limit for the particles position.
     * @throws IllegalArgumentException In case the list is not valid.
     */
    private static void validateParticlesList(final List<Particle> particles, final double sideLength)
            throws IllegalArgumentException {
        Assert.notNull(particles, "The particles list must not be null.");
        final long legalParticlesAmount = particles.stream()
                .filter(point -> point.getX() >= 0)
                .filter(point -> point.getX() <= sideLength)
                .filter(point -> point.getY() >= 0)
                .filter(point -> point.getY() <= sideLength)
                .count();
        if (legalParticlesAmount != particles.size()) {
            throw new IllegalArgumentException("There are particles that are not part of this space");
        }
    }
}
