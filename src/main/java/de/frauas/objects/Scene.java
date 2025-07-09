package de.frauas.objects;

import de.frauas.IDrawable;
import de.frauas.Settings;
import de.frauas.objects.car.Car;
import de.frauas.objects.datastructures.Vec3D;
import de.frauas.objects.interfaces.ICarObserver;
import de.frauas.objects.obstacle.ISdf;
import de.frauas.objects.obstacle.Obstacle;
import de.frauas.objects.trace.*;
import de.frauas.scenario.dto.Scenario;
import de.frauas.scenario.dto.StartPosition;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Scene extends Transformable implements ISdf, IDrawable {
    
    private final Trace trace;
    private final List<Obstacle> obstacles = new ArrayList<>();
    private final Car car;
    private final Vec3D startPosition;
    private final double startHeading;

    private final double[][] bakedSdFields = new double[(int) Settings.SCENE.CANVAS.getX()][(int) Settings.SCENE.CANVAS.getY()];
    private boolean isBaked = false;


    public Scene(Scenario scenario) {
        StartPosition startPosition = scenario.getStartPosition();
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

        this.startPosition = new Vec3D(startPosition.getX(), startPosition.getY(), 1);
        this.startHeading = 360 - startPosition.getHeading();
        car = new Car(this, this.startPosition, this.startHeading);

        bakeSdf();
    }


    @Override
    public double getSDF(Vec3D otherPosition) {
        if (!isBaked) {
            bakeSdf();
        }
        return bakedSdFields[
                Math.clamp((int) otherPosition.getX(), 0, (int) Settings.SCENE.CANVAS.getX() - 1)
                ][
                Math.clamp((int) otherPosition.getY(), 0, (int) Settings.SCENE.CANVAS.getY() - 1)
                ];
    }

    private void bakeSdf(){
        isBaked = false;
        for (int i = 0; i < Settings.SCENE.CANVAS.getX(); i++) {
            for (int j = 0; j < Settings.SCENE.CANVAS.getY(); j++) {
                bakedSdFields[i][j] = createSdf(new Vec3D(i, j, 0));
            }
        }
        isBaked = true;
    }

    private double createSdf(Vec3D otherPosition){
        double min = Double.MAX_VALUE;
        for (Obstacle obstacle : obstacles) {
            double sdf = obstacle.getSDF(otherPosition);
            min = Math.min(min, sdf);
        }
        return min;
    }

    public void resetCar(){
        car.reset(startPosition, startHeading);
    }

    public void startCar(){
        this.car.start();
    }

    public void stopCar(){ this.car.stop();}

    public void pauseCar(){ this.car.pause();}

    public void resumeCar(){ this.car.start();}

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
            drawInScene(g2);
        }

        if (Settings.DEBUG.ENABLED) {
            for (int i = 0; i < Settings.WINDOW.WIDTH; i += 2) {
                for (int j = 0; j < Settings.WINDOW.HEIGHT; j += 2) {
                    Vec3D pos = new Vec3D(i, j, 1);
                    pos = toGlobalSpace(pos);
                    double sdf = getSDF(pos);
                    g2.setColor(((ShiftedTrace) trace).isPointBetweenLines(toGlobalSpace(pos)) ? Color.green : sdf >= 0 ? Color.red : Color.blue);
                    g2.fillOval((int) pos.getX(), (int) pos.getY(), 1, 1);
                }
            }
        }
        g2.dispose();
    }
    
    public void addObserverToCar(ICarObserver observer){
        if (car == null) return;
        car.addObserver(observer);
    }
    
    public void removeObserverFromCar(ICarObserver observer){
        if (car == null) return;
        car.removeObserver(observer);
    }

    @Override
    public void drawInScene(Graphics g) {
        trace.drawInScene(g);
        obstacles.forEach(obstacle -> obstacle.drawInScene(g));
        car.drawInScene(g);
    }
}
