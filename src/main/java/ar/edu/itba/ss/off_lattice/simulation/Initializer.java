package ar.edu.itba.ss.off_lattice.simulation;

import ar.edu.itba.ss.off_lattice.models.Particle;
import ar.edu.itba.ss.off_lattice.models.Space;
import ar.edu.itba.ss.off_lattice.utils.AngleUtils;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Class implementing simulation initialization methods.
 */
public class Initializer {

    /**
     * Generates an initial {@link Space} for the simulation.
     *
     * @param spaceSideLength   The length of the side of the {@link Space} to be generated..
     * @param amountOfParticles The amount of {@link Particle}s to be held in the {@link Space}.
     * @return The created {@link Space}.
     */
    public static Space generateInitialSpace(final double spaceSideLength, final int amountOfParticles) {
        final Random rnd = new Random();
        final List<Particle> particles = IntStream.range(0, amountOfParticles)
                .mapToObj(idx ->
                        new Particle(rnd.nextDouble() * spaceSideLength,
                                rnd.nextDouble() * spaceSideLength,
                                0.03, AngleUtils.randomAngle()))
                .collect(Collectors.toList());

        return new Space(spaceSideLength, particles);
    }
}
