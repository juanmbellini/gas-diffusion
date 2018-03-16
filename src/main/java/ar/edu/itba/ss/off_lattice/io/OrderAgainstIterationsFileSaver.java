package ar.edu.itba.ss.off_lattice.io;

import ar.edu.itba.ss.off_lattice.models.Particle;
import ar.edu.itba.ss.off_lattice.models.Space;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * A {@link TextFileSaver} that saves order against iterations files.
 *
 * @implNote It generates a Matlab/Octave file with an array with the values.
 */
@Component
public class OrderAgainstIterationsFileSaver extends TextFileSaver<Space.SpaceState> {

    /**
     * A magic number that indicates in which lines a line break must be inserted.
     */
    private static final int MAGIC_AMOUNT_OF_VALUES_PER_LINE = 25;

    @Override
    void doSave(Writer writer, Queue<Space.SpaceState> simulationStates) throws IOException {
        if (simulationStates.isEmpty()) {
            throw new IllegalArgumentException("The queue must not be empty.");
        }
        final Queue<Double> orderValues = orderValues(simulationStates);
        final StringBuilder builder = new StringBuilder();
        builder.append("result = [").append(Double.toString(orderValues.poll()));
        while (!orderValues.isEmpty()) {
            builder.append(", ").append(Double.toString(orderValues.poll()));
        }
        builder.append("];").append("\n")
                .append("plot(result);").append("\n")
                .append("xlabel(\"Iterations\");").append("\n")
                .append("ylabel(\"Va\");").append("\n");
        writer.append(builder.toString());
    }

    /**
     * Maps the given {@code simulationStates} {@link Queue} into a {@link Double}s {@link Queue},
     * holding the order value for each iteration (i.e each state in the input {@link Queue}).
     *
     * @param simulationStates The {@link Queue} of {@link ar.edu.itba.ss.off_lattice.models.Space.SpaceState}s.
     * @return The {@link Queue} with order values.
     */
    private static Queue<Double> orderValues(Queue<Space.SpaceState> simulationStates) {
        final double speedModule = Optional.ofNullable(simulationStates.peek())
                .map(Space.SpaceState::getParticleStates)
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0))
                .map(Particle.ParticleState::getSpeedModule)
                .orElseThrow(() -> new IllegalArgumentException("Illegal states"));
        final int amount = Optional.ofNullable(simulationStates.peek())
                .map(Space.SpaceState::getParticleStates)
                .map(List::size)
                .orElseThrow(() -> new IllegalArgumentException("Illegal states"));
        final double factor = speedModule * (double) amount;

        final List<Double> orderValues = simulationStates.stream()
                .map(state -> getSumModule(state) / factor)
                .collect(Collectors.toList());

        return new LinkedList<>(orderValues);
    }

    /**
     * Returns the sum of speeds's module.
     *
     * @param spaceState A {@link ar.edu.itba.ss.off_lattice.models.Space.SpaceState}
     *                   holding the {@link ar.edu.itba.ss.off_lattice.models.Particle.ParticleState}
     *                   from which speeds will be taken.
     * @return The sum of the speed's modules.
     */
    private static double getSumModule(Space.SpaceState spaceState) {
        return spaceState.getParticleStates().stream()
                .map(OrderAgainstIterationsFileSaver::getSpeed)
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
    private static CartesianSpeed2DTuple getSpeed(Particle.ParticleState particleState) {
        return CartesianSpeed2DTuple.fromPolar(particleState.getSpeedModule(), particleState.getSpeedAngle());
    }

    /**
     * Bean class holding components of Speed.
     */
    private static final class CartesianSpeed2DTuple {

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
        private CartesianSpeed2DTuple(double xSpeed, double ySpeed) {
            this.xSpeed = xSpeed;
            this.ySpeed = ySpeed;
        }

        /**
         * @return The 'x' component of the speed.
         */
        private double getXSpeed() {
            return xSpeed;
        }

        /**
         * @return The 'y' component of the speed.
         */
        private double getYSpeed() {
            return ySpeed;
        }

        private double getModule() {
            return Math.sqrt(Math.pow(xSpeed, 2) + Math.pow(ySpeed, 2));
        }

        /**
         * Builds a {@link CartesianSpeed2DTuple} from the given polar coordinates.
         *
         * @param speedModule The speed module.
         * @param speedAngle  The speed angle.
         * @return The created {@link CartesianSpeed2DTuple}.
         */
        private static CartesianSpeed2DTuple fromPolar(double speedModule, double speedAngle) {
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
        private static CartesianSpeed2DTuple sum(CartesianSpeed2DTuple first, CartesianSpeed2DTuple second) {
            final double xSpeed = first.getXSpeed() + second.getXSpeed();
            final double ySpeed = first.getYSpeed() + second.getYSpeed();
            return new CartesianSpeed2DTuple(xSpeed, ySpeed);
        }

        /**
         * Builds a {@link CartesianSpeed2DTuple} with components values in 0.
         *
         * @return The neutral element.
         */
        private static CartesianSpeed2DTuple neutral() {
            return new CartesianSpeed2DTuple(0, 0);
        }
    }
}
