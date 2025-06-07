package de.frauas.objects;

import de.frauas.objects.datastructures.Vec3D;
import de.frauas.objects.trace.ShiftedTrace;
import de.frauas.objects.trace.Trace;
import de.frauas.scenario.dto.Scenario;
import de.frauas.scenario.dto.StartPosition;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Scene extends Transformable implements ISdf {
    
    private final Trace trace;
    private final List<Obstacle> obstacles = new ArrayList<>();
    private final Car car;


    public Scene(Scenario scenario) {
        StartPosition startPosition = scenario.getStartPosition();
        car = new Car(this, startPosition.getX(), startPosition.getY(), startPosition.getHeading());
        trace = new ShiftedTrace(this);
        trace.addPoint(new Vec3D(startPosition.getX(), startPosition.getY(), 0));
        scenario.getTrace().forEach(point -> trace.addPoint(new Vec3D(point.getX(), point.getY(), 0)));

        scenario.getObjects().forEach(object -> obstacles.add(new Obstacle(
                this,
                object.getXStart(),
                object.getYStart(),
                object.getXEnd(),
                object.getYEnd(),
                object.getHeight())));
    }


    @Override
    public double getSDF(Vec3D otherPosition) {
        double min = Double.MAX_VALUE;
        for (Obstacle obstacle : obstacles) {
            double sdf = obstacle.getSDF(otherPosition);
            min = Math.min(min, sdf);
        }
        return min;
    }
}
