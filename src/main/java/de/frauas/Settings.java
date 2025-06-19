package de.frauas;

import de.frauas.objects.datastructures.Vec3D;

public class Settings {
    
    public static final String TITLE = "Digitaler Zwilling";
    public static final int WIDTH = 1800;
    public static final int HEIGHT = 800;
    
    public static final String SCENARIO_FILE = "Scenario/example.xml";
    public static final int SPLINE_INTERPOLATION_SIZE = 10;
    public static final Vec3D SCENE_CANVAS = new Vec3D(1000, 500, 0);
    public static final Vec3D CAR_SIZE = new Vec3D(75, 150, 0);

    public static final float LINE_WIDTH = 4f;

    public static final boolean DEBUG = true;
    public static final int POINT_DEBUG_RADIUS = 10;
}
