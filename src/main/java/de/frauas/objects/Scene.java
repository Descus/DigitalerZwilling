package de.frauas.objects;

import de.frauas.IDrawable;
import de.frauas.Settings;
import de.frauas.objects.car.Car;
import de.frauas.objects.car.CarStatus;
import de.frauas.objects.datastructures.Vec3D;
import de.frauas.objects.obstacle.ISdf;
import de.frauas.objects.obstacle.Obstacle;
import de.frauas.objects.trace.*;
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
        trace = new RoadTrace(this);
        trace.addPoint(new Vec3D(startPosition.getX(), startPosition.getY(), 1));
        scenario.getTrace().forEach(point -> trace.addPoint(new Vec3D(point.getX(), point.getY(), 1)));
        
        scenario.getObjects().forEach(object -> obstacles.add(new Obstacle(
                this,
                object.getXStart(),
                object.getYStart(),
                object.getXEnd(),
                object.getYEnd(),
                object.getHeight())));

        this.startPosition = new Vec3D(startPosition.getX(), startPosition.getY(), 1);
        this.startHeading = 360 - startPosition.getHeading();
        car = new Car(this, this.startPosition, this.startHeading);
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

    public void update(double dt) {
        car.update(dt);
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

        if (Settings.DEBUG) {
            for (int i = 0; i < Settings.WIDTH; i += 2) {
                for (int j = 0; j < Settings.HEIGHT; j += 2) {
                    Vec3D pos = new Vec3D(i, j, 1);
                    pos = toGlobalSpace(pos);
                    g2.setColor(((ShiftedTrace) trace).isPointBetweenLines(toGlobalSpace(pos)) ? Color.green : Color.red);
                    g2.fillOval((int) pos.getX(), (int) pos.getY(), 1, 1);
                }
            }
        }
        g2.dispose();
    }
}
