package ar.edu.itba.ss.off_lattice.simulation;

/**
 * Defines behaviour for an object that can save its state.
 */
public interface StateSaver {


    /**
     * Saves the state, returning it.
     *
     * @return The actual state of the object.
     */
    State saveState();


    /**
     * Abstract class representing a state.
     */
    abstract class State {
    }
}
