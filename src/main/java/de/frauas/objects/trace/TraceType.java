package de.frauas.objects.trace;

/**
 * The TraceType enumeration defines the types of traces that can be used within the application.
 * These trace types are utilized to specify the purpose or context of certain trace objects.
 *
 * @author Scenario
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