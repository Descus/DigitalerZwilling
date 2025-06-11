package de.frauas.objects.trace;

import de.frauas.objects.datastructures.Line2D;
import de.frauas.objects.datastructures.Vec2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static de.frauas.Settings.SPLINE_INTERPOLATION_SIZE;

public class ShiftedCatmullTrace extends Trace {

    private final List<Line2D> upperLine;
    private final List<Line2D> lowerLine;
    private static final double OFFSET = 15.0; // Abstand für die Verschiebung

    public ShiftedCatmullTrace(ArrayList<Vec2D> points) {
        super(points);
        this.upperLine = new ArrayList<>();
        this.lowerLine = new ArrayList<>();
        createLines();
    }

    public ShiftedCatmullTrace() {
        super();
        this.upperLine = new ArrayList<>();
        this.lowerLine = new ArrayList<>();
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
        ArrayList<Vec2D> interpolatedPoints = new ArrayList<>();
        Vec2D firstControl = points.get(1).reflect(points.getFirst());
        Vec2D lastControl = points.get(points.size() - 2).reflect(points.getLast());

        interpolatedPoints.add(points.getFirst()); // Startpunkt hinzufügen

        for (int i = 0; i < points.size() - 1; i++) {
            Vec2D p0 = (i > 0) ? points.get(i - 1) : firstControl;
            Vec2D p1 = points.get(i);
            Vec2D p2 = points.get(i + 1);
            Vec2D p3 = (i + 2 < points.size()) ? points.get(i + 2) : lastControl;

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

                interpolatedPoints.add(new Vec2D(x, y));
            }
        }

        // Traces verschieben mit der interpolierten Trace
        ArrayList<Vec2D> shiftedPointsUp = new ArrayList<>();
        ArrayList<Vec2D> shiftedPointsDown = new ArrayList<>();

        for (int i = 0; i < interpolatedPoints.size(); i++) {
            Vec2D current = interpolatedPoints.get(i);
            Vec2D prev = (i > 0) ? interpolatedPoints.get(i - 1) : current;
            Vec2D next = (i < interpolatedPoints.size() - 1) ? interpolatedPoints.get(i + 1) : current;

            Vec2D tangent = next.subtract(prev).normalize();
            Vec2D normal = tangent.perpendicular(); // Orthogonaler Vektor

            shiftedPointsUp.add(current.add(normal.scale(OFFSET)));
            shiftedPointsDown.add(current.add(normal.scale(-OFFSET)));
        }

        // create Lines for both traces
        for (int i = 0; i < interpolatedPoints.size() - 1; i++) {
            upperLine.add(new Line2D(shiftedPointsUp.get(i), shiftedPointsUp.get(i + 1)));

            lowerLine.add(new Line2D(shiftedPointsDown.get(i), shiftedPointsDown.get(i + 1)));
        }
    }

    // Draw Lines
    @Override
    public void drawLines(Graphics g, Function<Vec2D, Vec2D> transformFunction) {
        // Zeichnet die mittlere Linie (gespeichert in der 'lines' Liste der Elternklasse)
        super.drawLines(g, transformFunction);
        // Zeichnet die beiden verschobenen Linien
        upperLine.forEach(l -> l.draw(g, transformFunction));
        lowerLine.forEach(l -> l.draw(g, transformFunction));
    }
}