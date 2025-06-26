package de.frauas.objects.interfaces;

import de.frauas.IDrawable;
import de.frauas.objects.datastructures.Line3D;
import de.frauas.objects.trace.ShiftedTrace;


public interface IInfraredSensor extends IDrawable {
    boolean isOnTrack(ShiftedTrace trace);
}
