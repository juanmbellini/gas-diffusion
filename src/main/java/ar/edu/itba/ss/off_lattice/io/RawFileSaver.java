package ar.edu.itba.ss.off_lattice.io;

import ar.edu.itba.ss.off_lattice.simulation.StateSaver;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Queue;

/**
 * An {@link OutputSaver} that saves the simulation results in a raw file
 * (i.e without formatting anything, just serializing as Java does).
 */
@Component
public class RawFileSaver extends FileSaver {

    @Override
    void doSave(FileWriter writer, Queue<StateSaver.State> simulationStates) throws IOException {
        // TODO: implement logic
    }
}
