package de.frauas;

import de.frauas.objects.datastructures.Vec2D;

public class Settings {
    
    public static final String TITLE = "Digitaler Zwilling";
    public static final int WIDTH = 1800;
    public static final int HEIGHT = 800;
    
    public static final String SCENARIO_FILE = "Scenario/example2.xml";
    public static final int CATMULL_ROM_INTERPOLATION_SIZE = 30;
    public static final Vec2D SCENE_CANVAS = new Vec2D(1000, 500);
    public static final Vec2D CAR_SIZE = new Vec2D(100, 250);

    public static final float LINE_WIDTH = 4f;

    public static final boolean DEBUG = false;
    public static final int POINT_DEBUG_RADIUS = 10;
}
