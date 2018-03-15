package ar.edu.itba.ss.off_lattice.io;

import ar.edu.itba.ss.off_lattice.simulation.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Queue;

/**
 * Abstract implementation of {@link OutputSaver}, implementing basic methods.
 *
 * @param <S> A concrete subtype of {@link State}.
 */
/* package */ abstract class FileSaver<S extends State> implements OutputSaver<S> {

    /**
     * The {@link Logger} object.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(FileSaver.class);

    @Override
    public void save(String path, Queue<S> simulationStates) {

        try (final FileOutputStream fileOutputStream = new FileOutputStream(createFile(path))) {
            doSave(fileOutputStream, simulationStates);
            fileOutputStream.flush();
        } catch (IOException e) {
            LOGGER.warn("Could not save {} file", path);
        }
        LOGGER.info("File {} saved successfully", path);
    }

    /**
     * Performs the operation of saving data.
     *
     * @param fileOutputStream The {@link FileOutputStream} used to write the file.
     * @param simulationStates The simulation results to be saved.
     * @throws IOException In case any I/O error occurs while performing the operation.
     */
    /* package */
    abstract void doSave(FileOutputStream fileOutputStream, Queue<S> simulationStates) throws IOException;

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
