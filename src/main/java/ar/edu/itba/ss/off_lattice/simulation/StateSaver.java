package ar.edu.itba.ss.off_lattice.simulation;

/**
 * Defines behaviour for an object that can save its state.
 */
public interface StateSaver<S extends State> {

    /**
     * Saves the state, returning it.
     *
     * @return The actual state of the object.
     */
    S saveState();
}
