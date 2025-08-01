package de.frauas.objects.interfaces;

import de.frauas.IDrawable;
// part of the Ultrasonic Team , Interface with Observer Pattern to other teams
public interface IUltrasonicSensor extends IDrawable {
    int distanceToClosestObstacle();
}
