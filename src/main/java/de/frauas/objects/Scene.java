package de.frauas.objects;

import de.frauas.IDrawable;
import de.frauas.objects.datastructures.Vec3D;
import de.frauas.objects.trace.ShiftedCatmullTrace;
import de.frauas.objects.trace.Trace;
import de.frauas.scenario.dto.Scenario;
import de.frauas.scenario.dto.StartPosition;
import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Scene extends Transformable implements ISdf, IDrawable {
    
    private final Trace trace;
    private final List<Obstacle> obstacles = new ArrayList<>();
    private final Car car;


    public Scene(Scenario scenario) {
        StartPosition startPosition = scenario.getStartPosition();
        car = new Car(this, new Vec3D(startPosition.getX(), startPosition.getY(), 1), startPosition.getHeading());
        trace = new ShiftedCatmullTrace(this);
        trace.addPoint(new Vec3D(startPosition.getX(), startPosition.getY(), 1));
        scenario.getTrace().forEach(point -> trace.addPoint(new Vec3D(point.getX(), point.getY(), 1)));

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

    public void resetCar(){

    }

    public void startCar(){

    }

    public void stopCar(){

    }

    public void pauseCar(){

    }

    public void resumeCar(){

    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        {
            g2.transform(transform.toAffineTransform());
            trace.draw(g2);
            obstacles.forEach(obstacle -> obstacle.draw(g2));
            car.draw(g2);
        }
        g2.dispose();
    }
}
