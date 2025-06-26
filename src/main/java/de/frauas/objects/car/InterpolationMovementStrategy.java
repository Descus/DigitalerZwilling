package de.frauas.objects.car;

import de.frauas.Settings;
import de.frauas.objects.datastructures.Transform2D;
import de.frauas.objects.trace.RoadTrace;

public class InterpolationMovementStrategy implements IMovementStrategy{
    private final Transform2D transform;
    private final RoadTrace trace;

    public InterpolationMovementStrategy(Transform2D transform, RoadTrace trace) {
        this.transform = transform;
        this.trace = trace;
    }

    @Override
    public void move(double dt) {
    }
}
