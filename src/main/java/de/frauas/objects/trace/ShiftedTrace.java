package de.frauas.objects.trace;

import de.frauas.objects.datastructures.Vec2D;
import de.frauas.objects.datastructures.Line2D;
import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
public class ShiftedTrace extends Trace {

    public final List<Line2D> upperLine;
    public final List<Line2D> lowerLine;

    public ShiftedTrace(ArrayList<Vec2D> points) {
        super(points);
        upperLine = new ArrayList<>();
        lowerLine = new ArrayList<>();
        createLines();
    }

    public ShiftedTrace() {super();
        upperLine = new ArrayList<>();
        lowerLine = new ArrayList<>();
        createLines();
    }

    @Override
    public void createLines() {
        upperLine.clear();
        lowerLine.clear();

        if (points.size() < 2) return;

        double offset = 15.0; // Abstand der orthogonalen Verschiebung

        ArrayList<Vec2D> shiftedPointsUp = new ArrayList<>();
        ArrayList<Vec2D> shiftedPointsDown = new ArrayList<>();

        for (int i = 0; i < points.size(); i++) {
            Vec2D prev = i > 0 ? points.get(i - 1) : points.get(i);
            Vec2D next = i < points.size() - 1 ? points.get(i + 1) : points.get(i);


            // Tangente, direction
            Vec2D tangent = next.subtract(prev).normalize();

            // Orthogonaler Vektor: (-dy, dx)
            Vec2D normal = tangent.perpendicular();

            // Punkte verschieben, jeweils nach unten und oben
            Vec2D shiftedUp = points.get(i).add(normal.scale(offset));
            Vec2D shiftedDown = points.get(i).add(normal.scale(-offset));

            shiftedPointsUp.add(shiftedUp);
            shiftedPointsDown.add(shiftedDown);
        }

        for (int i = 0; i < shiftedPointsUp.size() - 1; i++) {
            upperLine.add(new Line2D(shiftedPointsUp.get(i), shiftedPointsUp.get(i + 1)));
            lowerLine.add(new Line2D(shiftedPointsDown.get(i), shiftedPointsDown.get(i + 1)));
        }
    }

    @Override
    public void drawLines(Graphics g, Function<Vec2D, Vec2D> transformFunction) {
        upperLine.forEach(l -> l.draw(g, transformFunction));
        lowerLine.forEach(l -> l.draw(g, transformFunction));
    }
}
