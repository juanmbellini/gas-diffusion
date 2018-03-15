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
     * @param speedModule       The speed module used in the simulation.
     * @return The created {@link Space}.
     */
    public static Space generateInitialSpace(double spaceSideLength, int amountOfParticles, double speedModule) {
        final List<Particle> particles = IntStream.range(0, amountOfParticles)
                .mapToObj(idx ->
                        new Particle(new Random().nextDouble() * spaceSideLength,
                                new Random().nextDouble() * spaceSideLength,
                                speedModule, AngleUtils.randomAngle()))
                .collect(Collectors.toList());

        return new Space(spaceSideLength, particles);
    }
}
