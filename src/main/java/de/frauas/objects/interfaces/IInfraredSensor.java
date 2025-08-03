package de.frauas.objects.interfaces;

import de.frauas.IDrawable;
import de.frauas.objects.trace.ShiftedTrace;

/**
 * Interface for an infrared sensor, used as part of an observer pattern for integration
 * with other components in a system. The IInfraredSensor is designed to interact with
 * a track representation and determine its position relative to the boundaries of the track.
 * <p>
 * This interface extends the IDrawable interface, enabling the infrared sensor's visual
 * representation to be rendered as part of a scene or directly.
 *
 * @author Infrared
 */
public interface IInfraredSensor extends IDrawable {
    /**
     * Determines whether the infrared sensor is currently positioned on the track
     * defined by the given ShiftedTrace object. The determination is based on the
     * boundaries of the track (upper and lower lines) provided by the ShiftedTrace.
     *
     * @param trace the ShiftedTrace object representing the track boundaries to check against
     * @return true if the infrared sensor is on the track, false otherwise
     */
    boolean isOnTrack(ShiftedTrace trace);
}
