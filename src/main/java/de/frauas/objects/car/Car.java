package de.frauas.objects.car;

import de.frauas.IDrawable;
import de.frauas.Settings;
import de.frauas.objects.datastructures.Line3D;
import de.frauas.objects.obstacle.ISdf;
import de.frauas.objects.Scene;
import de.frauas.objects.Transformable;
import de.frauas.objects.datastructures.Transform2D;
import de.frauas.objects.datastructures.Vec3D;
import de.frauas.objects.interfaces.IUltrasonicSensor;
import de.frauas.objects.interfaces.IInfraredSensor;
import de.frauas.objects.car.parts.UltrasonicSensor;
import de.frauas.objects.car.parts.InfraredSensor;
import de.frauas.objects.trace.ShiftedCatmullTrace;
import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static de.frauas.Settings.POINT_DEBUG_RADIUS;


@Getter
public class Car extends Transformable implements IDrawable {
    private final double velocity = 100;
    public static final int SENSOR_ANGLE = 30;

    private CarStatus status = CarStatus.STOPPED;
    private final List<IUltrasonicSensor> ultraSonicSensors = new ArrayList<>();
    private final List<IInfraredSensor> infraredSensors = new ArrayList<>();
    
    private final ISdf sceneDistanceField;

    public Car(Scene parent, Vec3D position, double headingDegree){
        this.parent = parent;
        this.sceneDistanceField = parent;
        this.transform = new Transform2D(position, headingDegree);
        Vec3D CarSizeOffset = Settings.CAR_SIZE.scale(0.5);
        Vec3D carCenterWorld = transformPoint(Vec3D.zero);
        ultraSonicSensors.add(new UltrasonicSensor(this, new Vec3D(-CarSizeOffset.getX(),  CarSizeOffset.getY(), 0), 0));
        ultraSonicSensors.add(new UltrasonicSensor(this, new Vec3D(0, CarSizeOffset.getY(), 0), 0));
        ultraSonicSensors.add(new UltrasonicSensor(this, new Vec3D(CarSizeOffset.getX(), CarSizeOffset.getY(), 0), -SENSOR_ANGLE));
        ultraSonicSensors.add(new UltrasonicSensor(this, new Vec3D(-CarSizeOffset.getX(), -CarSizeOffset.getY(), 0), -SENSOR_ANGLE + 180));
        ultraSonicSensors.add(new UltrasonicSensor(this, new Vec3D(0, -CarSizeOffset.getY(), 0), 180));
        ultraSonicSensors.add(new UltrasonicSensor(this, new Vec3D(CarSizeOffset.getX(), -CarSizeOffset.getY(), 0), SENSOR_ANGLE + 180));

        infraredSensors.add(new InfraredSensor(this, new Vec3D(carCenterWorld.getX() + 10 , carCenterWorld.getY()-60, 0)));
        infraredSensors.add(new InfraredSensor(this, new Vec3D(carCenterWorld.getX() + 0, carCenterWorld.getY()-60, 0)));
        infraredSensors.add(new InfraredSensor(this, new Vec3D(carCenterWorld.getX() - 10, carCenterWorld.getY()-60, 0)));

    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        {
            g2.transform(transform.toAffineTransform());

            Vec3D drawPoint = new Vec3D(
                    0 - (Settings.CAR_SIZE.getX() / 2),
                    0 - (Settings.CAR_SIZE.getY() / 2),
                    0
            );

            g2.setColor(Color.RED);
            g2.drawRect(
                    (int) drawPoint.getX(),
                    (int) drawPoint.getY(),
                    (int) Settings.CAR_SIZE.getX(),
                    (int) Settings.CAR_SIZE.getY()
            );
            g2.setColor(Color.BLUE);
            g2.fillOval(
                    (int) (0 - (double) POINT_DEBUG_RADIUS / 2),
                    (int) (0 - (double) POINT_DEBUG_RADIUS / 2),
                    POINT_DEBUG_RADIUS,
                    POINT_DEBUG_RADIUS
            );
            ultraSonicSensors.forEach(s -> s.draw(g2));
            infraredSensors.forEach(s -> s.draw(g2));
        }
        g2.dispose();
    }

    //TODO Check / Drive / Stop logic here
    public void update(ShiftedCatmullTrace trace) {

        for (int i = 0; trace.getUpperLine().size() > i; i++) {
            Line3D lower = trace.getLowerLine().get(i);
            Line3D upper = trace.getUpperLine().get(i);
            Vec3D indexPoint = trace.getLowerLine().get(i).getStart();
            Vec3D nextPoint = trace.getLowerLine().get(i).getEnd();
            double deltaX =  (nextPoint.getX() - indexPoint.getX());
            double deltaY =  (nextPoint.getY() - indexPoint.getY());
            double segLength = Math.sqrt(Math.pow(deltaX,2) + Math.pow(deltaY,2));
            System.out.println(segLength);

            boolean[] sensorStatus = new boolean[infraredSensors.size()];
            for (int s = 0; s < infraredSensors.size(); s++) {
                IInfraredSensor ir = infraredSensors.get(s);
                sensorStatus[s] = ir.isOnTrack(upper, lower);
                //System.out.println(sensorStatus[s]);
            }

            if(sensorStatus[0]==true && sensorStatus[1]==true && sensorStatus[2]==true){


            }



        }




    }
}
