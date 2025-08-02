package de.frauas.objects.trace;

import de.frauas.Settings;
import de.frauas.objects.Scene;
import de.frauas.objects.datastructures.Line3D;
import de.frauas.objects.datastructures.Vec3D;

import java.util.ArrayList;

/**
 * Author: Scenario-Group
 *
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

    /**
     * Constructs a ShiftedCatmullTrace with the given scene and control points, and immediately generates the trace lines.
     *
     * @param scene  The scene in which the trace will be rendered.
     * @param points The initial list of control points to define the path.
     */
    public ShiftedCatmullTrace(Scene scene, ArrayList<Vec3D> points) {
        super(scene, points);
        createLines();
    }

    /**
     * Constructs an empty ShiftedCatmullTrace associated with a scene.
     * Points can be added later, and createLines() must be called to generate the trace.
     *
     * @param scene The scene in which the trace will be rendered.
     */
    public ShiftedCatmullTrace(Scene scene) {
        super(scene);
    }

    /**
     * Generates the upper and lower trace lines based on Catmull-Rom spline interpolation.
     * <p>
     * This method performs the following steps:
     * <ol>
     *   <li>Clears any existing line data.</li>
     *   <li>Returns if there are fewer than two control points.</li>
     *   <li>Computes interpolated points for a central path using a Catmull-Rom spline algorithm,
     *       creating virtual start and end control points for a smooth curve.</li>
     *   <li>Calculates shifted points for the upper and lower traces by offsetting the interpolated points
     *       along their normal vectors.</li>
     *   <li>Creates 3D line segments for the upper and lower traces from the shifted points.</li>
     * </ol>
     */
    @Override
    public void createLines() {
        /**
         * Clears all existing data from the upper and lower line lists to prepare for regeneration.
         */
        upperLine.clear();
        lowerLine.clear();

        /**
         * Ensures there are at least two control points, as a line requires a start and an end.
         * If not, the method returns without creating any lines.
         */
        if (points.size() < 2) {
            return;
        }

        /**
         * Initializes a list to store the calculated points for the central path of the trace.
         */
        ArrayList<Vec3D> interpolatedPoints = new ArrayList<>();

        /**
         * Creates virtual control points at the start and end of the path to ensure the Catmull-Rom spline
         * has well-defined tangents at the endpoints, resulting in a smoother curve.
         * The first virtual control point is a reflection of the second point around the first actual point.
         * The last virtual control point is a reflection of the second-to-last point around the last actual point.
         */
        Vec3D firstControl = points.get(1).reflect(points.getFirst());
        Vec3D lastControl = points.get(points.size() - 2).reflect(points.getLast());

        /**
         * The first point of the control list is added separately to the interpolated points
         * as it marks the beginning of the trace.
         */
        interpolatedPoints.add(points.getFirst());

        /**
         * This loop interpolates the path between control points to create a smooth, "curvy" trace
         * using the Catmull-Rom spline algorithm. The number of interpolation steps between each pair
         * of control points can be adjusted in the {@link Settings} class to control the curve's resolution.
         */
        for (int i = 0; i < points.size() - 1; i++) {
            Vec3D p0 = (i > 0) ? points.get(i - 1) : firstControl;
            Vec3D p1 = points.get(i);
            Vec3D p2 = points.get(i + 1);
            Vec3D p3 = (i + 2 < points.size()) ? points.get(i + 2) : lastControl;

            /**
             * Generates a set of intermediate points between p1 and p2 to form a smooth curve segment.
             * The number of points is determined by SPLINE_INTERPOLATION_STEPS.
             */
            for (int j = 1; j <= Settings.SCENE.TRACE.SPLINE_INTERPOLATION_STEPS; j++) {
                double t = (double) j / Settings.SCENE.TRACE.SPLINE_INTERPOLATION_STEPS;
                double t2 = t * t;
                double t3 = t2 * t;

                /**
                 * Calculates the x-coordinate of the interpolated point using the Catmull-Rom spline formula.
                 */
                double x = 0.5 * ((2 * p1.getX()) +
                        (-p0.getX() + p2.getX()) * t +
                        (2 * p0.getX() - 5 * p1.getX() + 4 * p2.getX() - p3.getX()) * t2 +
                        (-p0.getX() + 3 * p1.getX() - 3 * p2.getX() + p3.getX()) * t3);
                /**
                 * Calculates the y-coordinate of the interpolated point using the Catmull-Rom spline formula.
                 */
                double y = 0.5 * ((2 * p1.getY()) +
                        (-p0.getY() + p2.getY()) * t +
                        (2 * p0.getY() - 5 * p1.getY() + 4 * p2.getY() - p3.getY()) * t2 +
                        (-p0.getY() + 3 * p1.getY() - 3 * p2.getY() + p3.getY()) * t3);

                interpolatedPoints.add(new Vec3D(x, y, 1));
            }
        }

        /**
         * Shifts the interpolated central trace to create parallel upper and lower boundary lines.
         * These lists will store the points for the shifted lines.
         */
        ArrayList<Vec3D> shiftedPointsUp = new ArrayList<>();
        ArrayList<Vec3D> shiftedPointsDown = new ArrayList<>();

        for (int i = 0; i < interpolatedPoints.size(); i++) {
            Vec3D current = interpolatedPoints.get(i);

            /**
             * Determines the previous and next points in the interpolated path to calculate the tangent
             * at the current point. For endpoints, the current point is used to avoid index errors.
             */
            Vec3D prev = (i > 0) ? interpolatedPoints.get(i - 1) : current;
            Vec3D next = (i < interpolatedPoints.size() - 1) ? interpolatedPoints.get(i + 1) : current;

            /**
             * The tangent vector represents the direction of the curve at the current point.
             * It is normalized to ensure consistent offset distances.
             */
            Vec3D tangent = next.subtract(prev).normalize();

            /**
             * Calculates the normal vector, which is perpendicular to the tangent. This vector
             * points outwards from the curve and is used to displace the shifted points.
             */
            Vec3D normal = tangent.perpendicular();

            /**
             * Shifts the current point outwards along the normal vector to create the upper and lower points.
             * The distance of the shift is determined by half the line width defined in the settings.
             */
            shiftedPointsUp.add(current.add(normal.scale(Settings.SCENE.TRACE.LINE_WIDTH / 2)));
            shiftedPointsDown.add(current.add(normal.scale(-Settings.SCENE.TRACE.LINE_WIDTH / 2)));
        }

        /**
         * Creates {@link Line3D} segments for both the upper and lower traces by connecting the
         * consecutive shifted points.
         */
        for (int i = 0; i < interpolatedPoints.size() - 1; i++) {
            upperLine.add(new Line3D(this, shiftedPointsUp.get(i), shiftedPointsUp.get(i + 1)));
            lowerLine.add(new Line3D(this, shiftedPointsDown.get(i), shiftedPointsDown.get(i + 1)));
        }
    }
}