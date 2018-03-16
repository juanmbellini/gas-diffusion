package ar.edu.itba.ss.off_lattice.io;

import ar.edu.itba.ss.off_lattice.models.Particle;
import ar.edu.itba.ss.off_lattice.models.Space;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Util class that implements several helper methods.
 */
/* package */ class SpaceStateSaverHelper {

    /**
     * Maps the given {@code simulationStates} {@link Queue} into a {@link Double}s {@link Queue},
     * holding the order value for each iteration (i.e each state in the input {@link Queue}).
     *
     * @param simulationStates The {@link Queue} of {@link ar.edu.itba.ss.off_lattice.models.Space.SpaceState}s.
     * @return The {@link Queue} with order values.
     */
    /* package */
    static Queue<Double> orderValues(Queue<Space.SpaceState> simulationStates) {
        final Space.SpaceState spaceState = Optional.ofNullable(simulationStates.peek())
                .orElseThrow(() -> new IllegalArgumentException("Illegal states"));
        final double factor = getOrderValueFactor(spaceState);

        final List<Double> orderValues = simulationStates.stream()
                .map(state -> getOrderValue(state, factor))
                .collect(Collectors.toList());

        return new LinkedList<>(orderValues);
    }


    /**
     * Calculates the order value from the state, using the given factor (avoids recalculating).
     *
     * @param state  A {@link ar.edu.itba.ss.off_lattice.models.Space.SpaceState}
     *               used to get data from where the calculation will be made.
     * @param factor The order value factor.
     * @return The order value.
     */
    private static double getOrderValue(Space.SpaceState state, double factor) {
        return getSumModule(state) / factor;
    }

    /**
     * Calculates the order value from the state.
     *
     * @param state A {@link ar.edu.itba.ss.off_lattice.models.Space.SpaceState}
     *              used to get data from where the calculation will be made.
     * @return The order value.
     */
    /* package */
    static double getOrderValue(Space.SpaceState state) {
        return getSumModule(state) / getOrderValueFactor(state);
    }

    /**
     * Calculate the factor used to get the order value (i.e amount of particles multiplied by speed module).
     *
     * @param state A {@link ar.edu.itba.ss.off_lattice.models.Space.SpaceState}
     *              used to get data from where the calculation will be made.
     * @return The calculated factor.
     */
    /* package */
    static double getOrderValueFactor(Space.SpaceState state) {
        final List<Particle.ParticleState> particleStates = state.getParticleStates();
        if (particleStates.isEmpty()) {
            throw new IllegalArgumentException("The state has no particles");
        }
        final double speedModule = particleStates.get(0).getSpeedModule();
        final double amount = particleStates.size();
        return speedModule * amount;
    }

    /**
     * Returns the sum of speeds's module.
     *
     * @param spaceState A {@link ar.edu.itba.ss.off_lattice.models.Space.SpaceState}
     *                   holding the {@link ar.edu.itba.ss.off_lattice.models.Particle.ParticleState}
     *                   from which speeds will be taken.
     * @return The sum of the speed's modules.
     */
    /* package */
    static double getSumModule(Space.SpaceState spaceState) {
        return spaceState.getParticleStates().stream()
                .map(SpaceStateSaverHelper::getSpeed)
                .reduce(CartesianSpeed2DTuple.neutral(), CartesianSpeed2DTuple::sum)
                .getModule();
    }

    /**
     * Returns the speed of the given {@link ar.edu.itba.ss.off_lattice.models.Particle.ParticleState}
     *
     * @param particleState The {@link ar.edu.itba.ss.off_lattice.models.Particle.ParticleState}
     *                      from which the speed will be taken.
     * @return The {@link CartesianSpeed2DTuple} representing the speed.
     */
    /* package */
    static CartesianSpeed2DTuple getSpeed(Particle.ParticleState particleState) {
        return CartesianSpeed2DTuple.fromPolar(particleState.getSpeedModule(), particleState.getSpeedAngle());
    }

    /**
     * Bean class holding components of Speed.
     */
    /* package */ static final class CartesianSpeed2DTuple {

        /**
         * The 'x' component of the speed.
         */
        private final double xSpeed;

        /**
         * The 'y' component of the speed.
         */
        private final double ySpeed;


        /**
         * Constructor, from polar coordinates.
         *
         * @param xSpeed The 'x' component of the speed.
         * @param ySpeed The 'y' component of the speed.
         */
        /* package */ CartesianSpeed2DTuple(double xSpeed, double ySpeed) {
            this.xSpeed = xSpeed;
            this.ySpeed = ySpeed;
        }

        /**
         * @return The 'x' component of the speed.
         */
        /* package */ double getXSpeed() {
            return xSpeed;
        }

        /**
         * @return The 'y' component of the speed.
         */
        /* package */ double getYSpeed() {
            return ySpeed;
        }

        /* package */ double getModule() {
            return Math.sqrt(Math.pow(xSpeed, 2) + Math.pow(ySpeed, 2));
        }

        /**
         * Builds a {@link CartesianSpeed2DTuple} from the given polar coordinates.
         *
         * @param speedModule The speed module.
         * @param speedAngle  The speed angle.
         * @return The created {@link CartesianSpeed2DTuple}.
         */
        /* package */
        static CartesianSpeed2DTuple fromPolar(double speedModule, double speedAngle) {
            final double xSpeed = speedModule * Math.cos(speedAngle);
            final double ySpeed = speedModule * Math.sin(speedAngle);
            return new CartesianSpeed2DTuple(xSpeed, ySpeed);
        }

        /**
         * Sums the given {@link CartesianSpeed2DTuple}s.
         *
         * @param first  One of the {@link CartesianSpeed2DTuple} to be summed.
         * @param second The other {@link CartesianSpeed2DTuple} to be summed.
         * @return The result of the sum.
         */
        /* package */
        static CartesianSpeed2DTuple sum(CartesianSpeed2DTuple first, CartesianSpeed2DTuple second) {
            final double xSpeed = first.getXSpeed() + second.getXSpeed();
            final double ySpeed = first.getYSpeed() + second.getYSpeed();
            return new CartesianSpeed2DTuple(xSpeed, ySpeed);
        }

        /**
         * Builds a {@link CartesianSpeed2DTuple} with components values in 0.
         *
         * @return The neutral element.
         */
        /* package */
        static CartesianSpeed2DTuple neutral() {
            return new CartesianSpeed2DTuple(0, 0);
        }
    }
}
