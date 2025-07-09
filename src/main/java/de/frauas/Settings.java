package de.frauas;

import de.frauas.objects.datastructures.Vec3D;


public class Settings {
    
    public static final String TITLE = "Digitaler Zwilling";
    public static final int WIDTH = 1600;
    public static final int HEIGHT = 1000;
    
    public static String SCENARIO_FILE = "Scenario/example.xml";
    public static final int SPLINE_INTERPOLATION_SIZE = 10;
    public static final Vec3D SCENE_CANVAS = new Vec3D(1000, 500, 0);
    public static final Vec3D CAR_SIZE = new Vec3D(150, 240, 0);
    public static final double TURN_DEG = 12;
    public static final double STEP_MM  = 14;

    public static final float LINE_WIDTH = 30f;

    public static final boolean DEBUG = false;
    public static final boolean SHOW_DEBUG_RAYS = true;
    public static final int POINT_DEBUG_RADIUS = 10;
}
