package ar.edu.itba.ss.off_lattice.io;

import ar.edu.itba.ss.off_lattice.models.Space;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

/**
 * A {@link TextFileSaver} of {@link ar.edu.itba.ss.off_lattice.models.Space.SpaceState} that
 * saves other other (Va and density).
 */
@Component
public class OtherSimulationDataFileSaver extends TextFileSaver<Space.SpaceState> {
    @Override
    void doSave(Writer writer, Queue<Space.SpaceState> simulationStates) throws IOException {
        final Deque<Space.SpaceState> deque = new LinkedList<>(simulationStates);
        final Space.SpaceState state = Optional.ofNullable(deque.peekLast())
                .orElseThrow(() -> new IllegalArgumentException("The queue must not be empty."));
        writer
                .append("density = ").append(Double.toString(calculateDensity(state))).append(";").append("\n")
                .append("order = ").append(Double.toString(calculateOrderValue(state))).append(";").append("\n");
    }

    /**
     * Calculates the density of the system.
     *
     * @param spaceState A {@link ar.edu.itba.ss.off_lattice.models.Space.SpaceState}
     *                   used to get data from where the calculation will be made.
     * @return The calculated density.
     */
    private static double calculateDensity(Space.SpaceState spaceState) {
        return spaceState.getSpaceSideLength() / spaceState.getParticleStates().size();
    }

    /**
     * Calculates the order value from the state, using the given factor (avoids recalculating).
     *
     * @param spaceState A {@link ar.edu.itba.ss.off_lattice.models.Space.SpaceState}
     *                   used to get data from where the calculation will be made.
     * @return The order value.
     */
    private static double calculateOrderValue(Space.SpaceState spaceState) {
        return SpaceStateSaverHelper.getOrderValue(spaceState);
    }
}
