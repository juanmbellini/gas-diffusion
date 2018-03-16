package ar.edu.itba.ss.off_lattice.io;

import ar.edu.itba.ss.off_lattice.models.Particle;
import ar.edu.itba.ss.off_lattice.models.Space;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An {@link OvitoFileSaver} that saves {@link ar.edu.itba.ss.off_lattice.models.Space.SpaceState}s.
 */
@Component
public class SpaceOvitoFileSaver extends OvitoFileSaver<Space.SpaceState> {

    @Override
    void saveState(Writer writer, Space.SpaceState state, int frame) throws IOException {

        final List<Particle.ParticleState> finalList = Stream
                .concat(getLimitStream(state.getSpaceSideLength()), state.getParticleStates().stream())
                .collect(Collectors.toList());
        final String stringRepresentation = finalList.stream()
                .map(particle ->
                        new StringBuilder()
                                .append(particle.getX())
                                .append(" ")
                                .append(particle.getY())
                                .append(" ")
                                .append(particle.getSpeedModule() * Math.cos(particle.getSpeedAngle()))
                                .append(" ")
                                .append(particle.getSpeedModule() * Math.sin(particle.getSpeedAngle()))
                )
                .collect(Collectors.joining("\n"));
        writer.append(Integer.toString(finalList.size())).append("\n")
                .append(Integer.toString(frame)).append("\n")
                .append(stringRepresentation).append("\n");
    }

    /**
     * Returns a {@link Stream} of {@link ar.edu.itba.ss.off_lattice.models.Particle.ParticleState}
     * used to delimit the space (i.e the simulation cell).
     *
     * @param spaceSideLength The length of the sides of the space.
     * @return The created {@link Stream}.
     */
    private static Stream<Particle.ParticleState> getLimitStream(double spaceSideLength) {
        final Particle lowerLeft = new Particle(0, 0, 0, 0);
        final Particle upperLeft = new Particle(0, spaceSideLength, 0, 0);
        final Particle lowerRight = new Particle(spaceSideLength, 0, 0, 0);
        final Particle upperRight = new Particle(spaceSideLength, spaceSideLength, 0, 0);

        return Stream.of(lowerLeft, lowerRight, upperLeft, upperRight).map(Particle.ParticleState::new);
    }
}
