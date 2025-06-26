package de.frauas.objects;

import de.frauas.IDrawable;
import de.frauas.Settings;
import de.frauas.objects.car.Car;
import de.frauas.objects.car.CarStatus;
import de.frauas.objects.datastructures.Vec3D;
import de.frauas.objects.obstacle.ISdf;
import de.frauas.objects.obstacle.Obstacle;
import de.frauas.objects.trace.ShiftedCatmullTrace;
import de.frauas.objects.trace.ShiftedTrace;
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
    private Vec3D startPosition;
    private double startHeading;


    public Scene(Scenario scenario) {
        StartPosition startPosition = scenario.getStartPosition();
        this.startPosition = new Vec3D(startPosition.getX(), startPosition.getY(), 1);
        this.startHeading = startPosition.getHeading();
        car = new Car(this, this.startPosition, this.startHeading);
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
        car.setStatus(CarStatus.STOPPED);  // Stop the car first
        car.getTransform().setTranslation(startPosition); // Reset position
        car.getTransform().setRotation(startHeading);
    }

    public void startCar(){
        this.car.setStatus(CarStatus.RUNNING);

    }

    public void stopCar(){ this.car.setStatus(CarStatus.STOPPED);}

    public void pauseCar(){ this.car.setStatus(CarStatus.PAUSED);}

    public void resumeCar(){ this.car.setStatus(CarStatus.RUNNING);}

    public void update() {
        car.update((ShiftedCatmullTrace) trace);
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
