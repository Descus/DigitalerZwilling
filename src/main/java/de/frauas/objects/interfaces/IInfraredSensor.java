package de.frauas.objects.interfaces;

import de.frauas.IDrawable;
import de.frauas.objects.datastructures.Line3D;


public interface IInfraredSensor extends IDrawable {
    boolean isOnTrack(Line3D upper, Line3D lower);
}
