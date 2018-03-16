package ar.edu.itba.ss.off_lattice.io;

import ar.edu.itba.ss.off_lattice.models.Space;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;
import java.util.Queue;

/**
 * A {@link TextFileSaver} that saves order against iterations files.
 *
 * @implNote It generates a Matlab/Octave file with an array with the values.
 */
@Component
public class OrderAgainstIterationsFileSaver extends TextFileSaver<Space.SpaceState> {

    @Override
    void doSave(Writer writer, Queue<Space.SpaceState> simulationStates) throws IOException {
        if (simulationStates.isEmpty()) {
            throw new IllegalArgumentException("The queue must not be empty.");
        }
        final Queue<Double> orderValues = SpaceStateSaverHelper.orderValues(simulationStates);
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
}
