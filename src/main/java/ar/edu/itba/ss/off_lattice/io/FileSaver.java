package ar.edu.itba.ss.off_lattice.io;

import ar.edu.itba.ss.off_lattice.simulation.StateSaver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Queue;

/**
 * Abstract implementation of {@link OutputSaver}, implementing basic methods.
 */
/* package */ abstract class FileSaver implements OutputSaver {

    /**
     * The {@link Logger} object.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(FileSaver.class);

    @Override
    public void save(String path, Queue<StateSaver.State> simulationStates) {

        try (final FileWriter writer = new FileWriter(createFile(path))) {
            doSave(writer, simulationStates);
            writer.flush();
        } catch (IOException e) {
            LOGGER.warn("Could not save {} file", path);
        }
        LOGGER.info("File {} saved successfully", path);
    }

    /**
     * Performs the operation of saving data.
     *
     * @param writer           The {@link FileWriter} used to write a file.
     * @param simulationStates The simulation results to be saved.
     * @throws IOException In case any I/O error occurs while performing the operation.
     */
    /* package */
    abstract void doSave(FileWriter writer, Queue<StateSaver.State> simulationStates) throws IOException;

    /**
     * Creates a {@link File} in the given {@code path}.
     *
     * @param path The path where the {@link File} will be created.
     * @return The created {@link File}.
     * @throws IOException If any I/O error occurs while creating the {@link File}.
     */
    private File createFile(String path) throws IOException {
        final File file = new File(path);
        final File parent = file.getParentFile();
        if (!parent.exists() && !parent.mkdirs()) {
            LOGGER.error("Could not create directories.");
            throw new IOException();
        }
        if (!file.exists() && !file.createNewFile()) {
            LOGGER.error("Could not create file.");
            throw new IOException();
        }
        return file;
    }
}
