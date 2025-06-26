package de.frauas.objects.trace;

import de.frauas.objects.Scene;
import de.frauas.objects.datastructures.Vec3D;
import de.frauas.objects.datastructures.Line3D;
import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ShiftedTrace extends Trace {

    public final List<Line3D> upperLine;
    public final List<Line3D> lowerLine;

    public ShiftedTrace(Scene parent, ArrayList<Vec3D> points) {
        super(parent, points);
        upperLine = new ArrayList<>();
        lowerLine = new ArrayList<>();
        createLines();
    }

    public ShiftedTrace(Scene parent) {
        super(parent);
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

        ArrayList<Vec3D> shiftedPointsUp = new ArrayList<>();
        ArrayList<Vec3D> shiftedPointsDown = new ArrayList<>();

        for (int i = 0; i < points.size(); i++) {
            Vec3D prev = i > 0 ? points.get(i - 1) : points.get(i);
            Vec3D next = i < points.size() - 1 ? points.get(i + 1) : points.get(i);


            // Tangente, direction
            Vec3D tangent = next.subtract(prev).normalize();

            // Orthogonaler Vektor: (-dy, dx)
            Vec3D normal = tangent.perpendicular();

            // Punkte verschieben, jeweils nach unten und oben
            Vec3D shiftedUp = points.get(i).add(normal.scale(offset));
            Vec3D shiftedDown = points.get(i).add(normal.scale(-offset));

            shiftedPointsUp.add(shiftedUp);
            shiftedPointsDown.add(shiftedDown);
        }

        for (int i = 0; i < shiftedPointsUp.size() - 1; i++) {
            upperLine.add(new Line3D(this, shiftedPointsUp.get(i), shiftedPointsUp.get(i + 1)));
            lowerLine.add(new Line3D(this, shiftedPointsDown.get(i), shiftedPointsDown.get(i + 1)));
        }
    }
    
    public boolean isPointBetweenLines(Vec3D globalSpacePoint){
        if (points.size() < 2) return false;

        Vec3D localSpacePoint = toLocalSpace(globalSpacePoint);

        for (int i = 0; i < upperLine.size(); i++) {
            Line3D upper = upperLine.get(i);
            Line3D lower = lowerLine.get(i);

            Line3D leftBound = new Line3D(this, lower.getEnd(), upper.getEnd());
            Line3D rightBound = new Line3D(this, lower.getStart(), upper.getStart());

            if (lower.rightOfLine(localSpacePoint)
                    && !upper.rightOfLine(localSpacePoint)
                    && leftBound.rightOfLine(localSpacePoint)
                    && !rightBound.rightOfLine(localSpacePoint)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void drawLines(Graphics g) {
        upperLine.forEach(l -> l.draw(g));
        lowerLine.forEach(l -> l.draw(g));
    }
}
