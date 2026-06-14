package de.frauas.GUI.controllers.observer;

/**
 * Represents an observer in the simulation system, which can be notified of
 * simulation updates. Classes implementing this interface should define the
 * behavior to execute when the simulation is updated.
 */
public interface ISimulationObserver {
    /**
     * This method is invoked whenever the simulation state is updated.
     * It defines the actions to be performed by an observer during a simulation update.
     * Implementing classes should override this method to specify the desired behavior
     * when the simulation progresses.
     */
    void onSimulationUpdate();
}
