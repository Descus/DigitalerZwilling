package de.frauas;

import de.frauas.scenario.primitives.Vec2F;

public class Settings {
    
    public static final String TITLE = "Digitaler Zwilling";
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int TARGET_FPS = 60;
    
    public static final String SCENARIO_FILE = "Scenario/example.xml";
    public static final Vec2F SCENE_CANVAS = new Vec2F(1000, 500);
    public static final Vec2F CAR_SIZE = new Vec2F(20, 40);

    public static final float LINE_WIDTH = 4f;

    public static final boolean DEBUG = false;
    public static final int POINT_DEBUG_RADIUS = 10;
}
