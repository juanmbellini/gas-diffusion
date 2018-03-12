package ar.edu.itba.ss.off_lattice.simulation;

import ar.edu.itba.ss.off_lattice.models.Particle;
import ar.edu.itba.ss.off_lattice.models.Space;
import ar.edu.itba.ss.off_lattice.utils.NeighborhoodsCalculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.DoubleStream;

/**
 * Object in charge of updating a {@link Space}.
 */
public class Updater {

    /**
     * The {@link Space} to which the updated will be performed.
     */
    private final Space space;

    /**
     * The {@link NeighborhoodsCalculator} used to get a {@link Particle}s neighbors (i.e used for angle updates).
     */
    private final NeighborhoodsCalculator neighborhoodsCalculator;

    private Map<Particle, List<Particle>> stepBeforeNeighborhoods;

    /**
     * The eta value used for noise when updating the angle.
     */
    private final double eta;

    /**
     * Constructor.
     *
     * @param space                   The {@link Space} to which the updated will be performed.
     * @param neighborhoodsCalculator The {@link NeighborhoodsCalculator} used to get a {@link Particle}s neighbors
     *                                (i.e used for angle updates).
     * @param eta                     The eta value used for noise when updating the angle.
     */
    public Updater(Space space, NeighborhoodsCalculator neighborhoodsCalculator, double eta) {
        this.space = space;
        this.neighborhoodsCalculator = neighborhoodsCalculator; // TODO: validate that it is a calculator for the given space
        this.eta = eta;
        this.stepBeforeNeighborhoods = new HashMap<>();
    }

    /**
     * Updates the {@link Space}.
     */
    public void update() {
        // First compute the neighborhoods as the positions are needed before the particles are moved.
        final Map<Particle, List<Particle>> neighborhoods = neighborhoodsCalculator.computeNeighborhoods();
        // Update positions (using the initial positions and the speed).
        updatePositions();
        // Update angles (using the computed neighborhoods).
        updateAngles(neighborhoods);
    }

    /**
     * Makes all {@link Particle}s in the {@link Space} update their positions.
     */
    private void updatePositions() {
        space.getParticles().forEach(particle -> particle.move(space.getSideLength()));
    }

    /**
     * Updates the angles in the neighborhoods of the {@link Space}.
     */
    private void updateAngles(Map<Particle, List<Particle>> neighborhoods) {
        final double upper = this.eta / 2;
        final double lower = -1 * upper;
        final double noise = lower + (new Random().nextDouble() * (upper - lower));
        neighborhoods.forEach((particle, neighbors) -> particle.setSpeedAngle(average(particle, neighbors) + noise));
    }

    /**
     * Calculates the average angle of the given neighborhood (represented as a {@link Particle} and its neighbors).
     *
     * @param particle  The {@link Particle}.
     * @param neighbors The neighbors.
     * @return The calculated average angle.
     */
    private static double average(Particle particle, List<Particle> neighbors) {
        final DoubleStream particleAngleSinStream = DoubleStream.of(particle.getSpeedAngle()).map(Math::sin);
        final DoubleStream neighborsSinStream = neighbors.stream().mapToDouble(Particle::getSpeedAngle).map(Math::sin);
        final double sinAvg = DoubleStream.concat(particleAngleSinStream, neighborsSinStream)
                .average()
                .orElseThrow(() -> new RuntimeException("This should not happen."));

        final DoubleStream particleAngleCosStream = DoubleStream.of(particle.getSpeedAngle()).map(Math::cos);
        final DoubleStream neighborsCosStream = neighbors.stream().mapToDouble(Particle::getSpeedAngle).map(Math::cos);
        final double cosAvg = DoubleStream.concat(particleAngleCosStream, neighborsCosStream)
                .average()
                .orElseThrow(() -> new RuntimeException("This should not happen."));

        return Math.atan2(sinAvg, cosAvg);
    }
}
