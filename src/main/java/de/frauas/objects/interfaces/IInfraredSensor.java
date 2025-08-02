package de.frauas.objects.interfaces;

import de.frauas.IDrawable;
import de.frauas.objects.trace.ShiftedTrace;

// Author: Infrared-Team. Interface with Observer Pattern to other teams
public interface IInfraredSensor extends IDrawable {
    boolean isOnTrack(ShiftedTrace trace);
}
