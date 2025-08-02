package de.frauas.objects.trace;

/**
 * @Author Scenario-Group
 * Specifies the type of a trace, which can be used to differentiate
 * between traces for debugging purposes and those used in the main application.
 */

public enum TraceType {
    /**
     * A trace used for debugging visualizations.
     * These traces are typically not used for the car's navigation logic
     * but are rendered to assist developers.
     */
    DEBUG,
    /**
     * A trace that represents the actual path for the car to follow.
     * These are used in the application's core logic, such as for the
     * infrared sensors to detect if the car is on track.
     */
    WORKING
}