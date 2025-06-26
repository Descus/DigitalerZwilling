package de.frauas.objects.car;

import de.frauas.Settings;
import de.frauas.objects.datastructures.Transform2D;
import de.frauas.objects.interfaces.IInfraredSensor;
import de.frauas.objects.trace.ShiftedTrace;

import java.util.List;


public class SensorMovementStrategy implements IMovementStrategy{


    private final Transform2D carTransform;
    private final ShiftedTrace trace;
    private final List<IInfraredSensor> sensors;

    public SensorMovementStrategy(Transform2D carTransform, ShiftedTrace trace, List<IInfraredSensor> sensors) {
        this.carTransform = carTransform;
        this.trace = trace;
        this.sensors = sensors;
    }
    
    @Override
    public void move(double dt) {
        boolean[] irHit = new boolean[sensors.size()];
        for (int s = 0; s < sensors.size(); s++) {
            IInfraredSensor ir = sensors.get(s);
            irHit[s] = ir.isOnTrack(trace);
        }

        MovementInstruction direction = getDirection(irHit);
        System.out.println(direction);

        //Fahrbefehle ausführen
        switch (direction) {
            case forward -> carTransform.translate(carTransform.forward().normalize().scale(Settings.STEP_MM * dt));
            case left -> carTransform.rotate(Settings.TURN_DEG * dt);
            case right -> carTransform.rotate(-Settings.TURN_DEG * dt);
            default -> {}
        }
    }

    private MovementInstruction getDirection(boolean[] sensorStatus) {
        boolean L = sensorStatus[2];
        boolean M = sensorStatus[1];
        boolean R = sensorStatus[0];

        if (L && !R) {
            return MovementInstruction.left; // links abbiegen
        } else if (!L && R) {
            return MovementInstruction.right; // rechts abbiegen
        } else { // wenn links und rechts false
            return M ? MovementInstruction.forward // falls mitte true
                    : MovementInstruction.stop; // falls mitte false
        }
    }
}
