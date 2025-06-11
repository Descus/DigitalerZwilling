package de.frauas.objects.trace;

import de.frauas.objects.Scene;
import de.frauas.objects.datastructures.Line3D;
import de.frauas.objects.datastructures.Vec3D;

import java.util.ArrayList;

import static de.frauas.Settings.SPLINE_INTERPOLATION_SIZE;

public class ShiftedCatmullTrace extends ShiftedTrace {

    private static final double OFFSET = 15.0; // Abstand für die Verschiebung

    public ShiftedCatmullTrace(Scene scene, ArrayList<Vec3D> points) {
        super(scene, points);
        createLines();
    }

    public ShiftedCatmullTrace(Scene scene) {
        super(scene);
    }

    @Override
    public void createLines() {
        // Zuerst alle Listen leeren
        upperLine.clear();
        lowerLine.clear();

        if (points.size() < 2) {
            return;
        }

        // Interpolierte Punkte berechnen
        ArrayList<Vec3D> interpolatedPoints = new ArrayList<>();
        Vec3D firstControl = points.get(1).reflect(points.getFirst());
        Vec3D lastControl = points.get(points.size() - 2).reflect(points.getLast());

        interpolatedPoints.add(points.getFirst()); // Startpunkt hinzufügen

        for (int i = 0; i < points.size() - 1; i++) {
            Vec3D p0 = (i > 0) ? points.get(i - 1) : firstControl;
            Vec3D p1 = points.get(i);
            Vec3D p2 = points.get(i + 1);
            Vec3D p3 = (i + 2 < points.size()) ? points.get(i + 2) : lastControl;

            for (int j = 1; j <= SPLINE_INTERPOLATION_SIZE; j++) {
                double t = (double) j / SPLINE_INTERPOLATION_SIZE;
                double t2 = t * t;
                double t3 = t2 * t;

                double x = 0.5 * ((2 * p1.getX()) +
                        (-p0.getX() + p2.getX()) * t +
                        (2 * p0.getX() - 5 * p1.getX() + 4 * p2.getX() - p3.getX()) * t2 +
                        (-p0.getX() + 3 * p1.getX() - 3 * p2.getX() + p3.getX()) * t3);
                double y = 0.5 * ((2 * p1.getY()) +
                        (-p0.getY() + p2.getY()) * t +
                        (2 * p0.getY() - 5 * p1.getY() + 4 * p2.getY() - p3.getY()) * t2 +
                        (-p0.getY() + 3 * p1.getY() - 3 * p2.getY() + p3.getY()) * t3);

                interpolatedPoints.add(new Vec3D(x, y, 1));
            }
        }

        // Traces verschieben mit der interpolierten Trace
        ArrayList<Vec3D> shiftedPointsUp = new ArrayList<>();
        ArrayList<Vec3D> shiftedPointsDown = new ArrayList<>();

        for (int i = 0; i < interpolatedPoints.size(); i++) {
            Vec3D current = interpolatedPoints.get(i);
            Vec3D prev = (i > 0) ? interpolatedPoints.get(i - 1) : current;
            Vec3D next = (i < interpolatedPoints.size() - 1) ? interpolatedPoints.get(i + 1) : current;

            Vec3D tangent = next.subtract(prev).normalize();
            Vec3D normal = tangent.perpendicular(); // Orthogonaler Vektor

            shiftedPointsUp.add(current.add(normal.scale(OFFSET)));
            shiftedPointsDown.add(current.add(normal.scale(-OFFSET)));
        }

        // create Lines for both traces
        for (int i = 0; i < interpolatedPoints.size() - 1; i++) {
            upperLine.add(new Line3D(this, shiftedPointsUp.get(i), shiftedPointsUp.get(i + 1)));
            lowerLine.add(new Line3D(this, shiftedPointsDown.get(i), shiftedPointsDown.get(i + 1)));
        }
    }
}