package de.frauas.objects.interfaces;

import de.frauas.IDrawable;
import de.frauas.objects.ISdf;
import de.frauas.objects.datastructures.Vec3D;

public interface IUltrasonicSensor extends IDrawable {
    Vec3D isObstacleInRange(ISdf ISDF);
}
