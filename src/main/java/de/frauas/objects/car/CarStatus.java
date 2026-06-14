package de.frauas.objects.car;

/**
 * The CarStatus enum represents the different states a car can be in
 * during its operation. It is used to track and manage the current
 * status of the car during a process or lifecycle.
 * <p>
 * STARTED - Indicates that the car has been started.
 * RUNNING - Denotes that the car is currently in motion or operational.
 * PAUSED - Represents a temporary halt in the car's operation.
 * STOPPED - Indicates the car is no longer in operation.
 * FINISHED - Specifies that the process involving the car has been completed.
 *
 * @author Infrared
 */
public enum CarStatus {
    STARTED,
    RUNNING,
    PAUSED,
    STOPPED,
    FINISHED
}
