package de.frauas.objects.interfaces;

import de.frauas.IDrawable;

public interface IUltrasonicSensor extends IDrawable {
    int distanceToClosestObstacle();
    int getDistance();
}
