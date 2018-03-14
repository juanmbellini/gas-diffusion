package ar.edu.itba.ss.off_lattice.io;

import ar.edu.itba.ss.off_lattice.simulation.StateSaver;

import java.util.Queue;

/**
 * Defines behaviour for an object that can save a simulation output in a file.
 */
public interface OutputSaver {

    /**
     * Saves the given {{@code simulationStates} {@link Queue} in a file in the given {@code path}.
     *
     * @param path             The path in which the file will be saved.
     * @param simulationStates The {@link Queue} with the simulation results.
     */
    void save(String path, Queue<StateSaver.State> simulationStates);
}
