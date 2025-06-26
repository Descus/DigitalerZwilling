package de.frauas.objects.car.parts;

import de.frauas.objects.Transformable;
import de.frauas.objects.datastructures.Transform2D;

import java.util.Random;

public class MotionController extends Transformable {
    private static final double[] TURN_DEG_LEFT;
    private static final double[] TURN_DEG_RIGHT;
    private static final double STEP_MM = 1.4; //Schrittweite geradeaus
    private static final Random RANDOM = new Random();

    private final Transform2D transform;
    private double endOfLineMm = 20;
    //Werte sind in mm angegeben, davor war es cm. jetzt deutlich langsamer

    static {
        TURN_DEG_LEFT = generateRange(0.6, 1, 0.1);
        TURN_DEG_RIGHT = generateRange(0.2, 0.4, 0.1);
    }

    public MotionController(Transform2D transform) {
        this.transform = transform;
    }

    public void forward() {
        transform.translate(transform.forward().normalize().scale(STEP_MM));
    }

    public void endOfLineForward() {
        if (endOfLineMm > 0)
        {
            transform.translate(transform.forward().normalize().scale(STEP_MM));
            endOfLineMm -= STEP_MM;
        }

    }

    public void left() {
        double angel = TURN_DEG_LEFT[RANDOM.nextInt(TURN_DEG_LEFT.length)];
        transform.rotate(+angel);
    }
    public void right() {
        double angel = TURN_DEG_RIGHT[RANDOM.nextInt(TURN_DEG_RIGHT.length)];
        transform.rotate(-angel);
    }

    private static double[] generateRange(double start, double end, double step) {
        //Generiert die Werte für Links/Rechts in einem angegeben Intervall
        int size = (int) ((end - start) / step) + 1;
        double[] range = new double[size];
        for (int i = 0; i < size; i++) {
            range[i] = start + (i * step);
        }
        return range;
    }
}
