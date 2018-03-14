package ar.edu.itba.ss.off_lattice.io;

import ar.edu.itba.ss.off_lattice.simulation.StateSaver;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Queue;

/**
 * An {@link OutputSaver} that saves the simulation results in an Ovito file.
 */
@Component
public class OvitoFileSaver extends FileSaver {


    @Override
    void doSave(FileWriter writer, Queue<StateSaver.State> simulationStates) throws IOException {
        // TODO: Implement ovito file logic
    }
}
