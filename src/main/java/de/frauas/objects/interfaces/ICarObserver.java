package de.frauas.objects.interfaces;

import de.frauas.objects.CarUpdateInformation;

/**
 * An interface representing an observer in the observer pattern for monitoring
 * car-related updates. Classes implementing this interface are notified whenever
 * there are changes in the state or components of the car, encapsulated in a
 * {@link CarUpdateInformation} object.
 * <p>
 * This interface facilitates communication between different components of the
 * system, such as control panels, logging modules, or simulation view panels,
 * by providing an abstraction for responding to updates related to the car's
 * operational status, sensor data, position, and other attributes.
 * <p>
 * Implementers of this interface define custom behavior to handle the car's
 * updated state through the provided method.
 *
 * @author Untrasonic
 */
public interface ICarObserver {
    /**
     * Notifies the observer of an update regarding the car's state. This method is invoked
     * whenever there are changes in the car's operational status, sensor readings, position,
     * heading, or other relevant attributes encapsulated within the provided
     * {@link CarUpdateInformation} object.
     *
     * @param info the {@link CarUpdateInformation} object containing the updated state
     *             and details about the car
     */
    void onCarUpdate(CarUpdateInformation info);
}
