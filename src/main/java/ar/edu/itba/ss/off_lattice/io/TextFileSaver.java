package ar.edu.itba.ss.off_lattice.io;

import ar.edu.itba.ss.off_lattice.simulation.State;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Queue;

/**
 * An {@link OutputSaver} that saves the simulation results in a raw file
 * (i.e without formatting anything, just serializing as Java does).
 *
 * @param <S> A concrete subtype of {@link State}.
 */
@Component
public abstract class TextFileSaver<S extends State> extends FileSaver<S> {

    @Override
    void doSave(File file, Queue<S> simulationStates) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            doSave(writer, simulationStates);
        }
    }

    /**
     * Performs the operation of saving data.
     *
     * @param writer           The {@link FileWriter} used to write a file.
     * @param simulationStates The simulation results to be saved.
     * @throws IOException In case any I/O error occurs while performing the operation.
     */
    abstract void doSave(Writer writer, Queue<S> simulationStates) throws IOException;
}
