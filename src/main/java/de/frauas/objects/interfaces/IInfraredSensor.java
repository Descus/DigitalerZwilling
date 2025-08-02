package de.frauas.objects.interfaces;

import de.frauas.IDrawable;
import de.frauas.objects.trace.ShiftedTrace;

/**
 * @author Infrared-Team, Interface with Observer Pattern to facilitate collaboration with other teams.
 * Defines the abstract method isOnTrack(), which checks whether the sensor is currently
 * positioned on the virtual track defined by the ShiftedTrace.
 */
public interface IInfraredSensor extends IDrawable {
    boolean isOnTrack(ShiftedTrace trace);
}
