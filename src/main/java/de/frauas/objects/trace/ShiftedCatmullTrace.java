package de.frauas.objects.trace;

import de.frauas.Settings;
import de.frauas.objects.Scene;
import de.frauas.objects.datastructures.Line3D;
import de.frauas.objects.datastructures.Vec3D;

import java.util.ArrayList;

/**
 * Generates the upper and lower lines based on Catmull-Rom spline interpolation
 * and offsets from the interpolated path. This method performs several steps:
 * clearing existing data (line), computing interpolated points using Catmull-Rom splines,
 * calculating displaced points for upper and lower traces, and creating line segments
 * for the traces.
 *
 * The method first interpolates a smooth path based on the given control points
 * using Catmull-Rom splines. To do so, it considers the start and end tangents
 * by reflecting the second and second-to-last points around the first and last
 * control points respectively. It then interpolates intermediate points based
 * on predefined resolution defined by SPLINE_INTERPOLATION_SIZE.
 *
 * Afterwards, for each interpolated point, the method computes normal vectors
 * and offsets (about 15 cm for each trace) them to generate the shifted points for the upper and lower traces.
 * These shifted points are displaced in perpendicular directions by an amount
 * determined by OFFSET, forming parallel paths.
 *
 * Finally, the method connects consecutive shifted points to create line segments
 * for both the upper and lower traces, storing the results in the upperLine and
 * lowerLine collections.
 *
 * If there are fewer than two initial points, this method will return immediately
 * without generating any lines.
 */
public class ShiftedCatmullTrace extends ShiftedTrace {

    public ShiftedCatmullTrace(Scene scene, ArrayList<Vec3D> points) {
        super(scene, points);
        createLines();
    }

    public ShiftedCatmullTrace(Scene scene) {
        super(scene);
    }

    @Override
    public void createLines() {
        // Zuerst alle Daten bzw Listen leeren
        upperLine.clear();
        lowerLine.clear();

        if (points.size() < 2) {
            return;
        }

        // Interpolierte Punkte werden berechnet
        ArrayList<Vec3D> interpolatedPoints = new ArrayList<>();
        Vec3D firstControl = points.get(1).reflect(points.getFirst());
        Vec3D lastControl = points.get(points.size() - 2).reflect(points.getLast());

        // Startpunkt muss seperat ngeholt werden, da es sonst eine Exception gibt
        interpolatedPoints.add(points.getFirst());

        // Hier werden Traces interpoliert alson "Kurvig" gemacht
        // SPLINE_INTERPOLATION_SIZE kann man in SETTINGS ÄNDERN
        for (int i = 0; i < points.size() - 1; i++) {
            Vec3D p0 = (i > 0) ? points.get(i - 1) : firstControl;
            Vec3D p1 = points.get(i);
            Vec3D p2 = points.get(i + 1);
            Vec3D p3 = (i + 2 < points.size()) ? points.get(i + 2) : lastControl;

            for (int j = 1; j <= Settings.SCENE.TRACE.SPLINE_INTERPOLATION_STEPS; j++) {
                double t = (double) j / Settings.SCENE.TRACE.SPLINE_INTERPOLATION_STEPS;
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

            // Tangente, direction
            Vec3D tangent = next.subtract(prev).normalize();

            // Punkte verschieben, jeweils nach unten und oben
            // Orthogonaler Vektor
            Vec3D normal = tangent.perpendicular();

            shiftedPointsUp.add(current.add(normal.scale(Settings.SCENE.TRACE.LINE_WIDTH / 2)));
            shiftedPointsDown.add(current.add(normal.scale(-Settings.SCENE.TRACE.LINE_WIDTH / 2)));
        }

        // create Lines for both traces
        for (int i = 0; i < interpolatedPoints.size() - 1; i++) {
            upperLine.add(new Line3D(this, shiftedPointsUp.get(i), shiftedPointsUp.get(i + 1)));
            lowerLine.add(new Line3D(this, shiftedPointsDown.get(i), shiftedPointsDown.get(i + 1)));
        }
    }
}