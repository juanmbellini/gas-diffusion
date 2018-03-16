package ar.edu.itba.ss.off_lattice.io;

import ar.edu.itba.ss.off_lattice.models.Space;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Queue;

/**
 * An {@link OutputSaver} that saves the simulation results in a raw file
 * (i.e without formatting anything, just serializing as Java does).
 */
@Component
public class RawFileSaver extends FileSaver<Space.SpaceState> {

    @Override
    void doSave(File file, Queue<Space.SpaceState> simulationStates) throws IOException {
        final ArrayList<Space.SpaceState> auxList = new ArrayList<>(simulationStates); // Copy into a Serializable List
        try (final FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            final ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(auxList);
        }
    }
}
