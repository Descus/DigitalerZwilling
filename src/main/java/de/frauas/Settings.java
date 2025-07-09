package de.frauas;

import de.frauas.objects.datastructures.Vec3D;


public class Settings {
    
    public static class WINDOW {
        public static final String TITLE = "Digitaler Zwilling";
        public static final int WIDTH = 1600;
        public static final int HEIGHT = 1000;
        public static final int TARGET_FPS = 60;
    }
    
    public static class CAR{
        public static final Vec3D SIZE = new Vec3D(150, 240, 0);
        
        public static class MOVEMENT {
            public static final double TURN_DEG = 56;
            public static final double MM_PER_SECOND = 30;
        }
        public static class ULTRASONIC {
            public static final int MAX_DISTANCE = 300;
            public static final int MAX_ANGLE = 15;
        }
        public static class INFRARED {
            
        }
    }
 
    
    public static class SCENE {
        public static String DEFAULT_SCENARIO_FILE = "Scenario/example.xml";
        public static final Vec3D CANVAS = new Vec3D(1000, 500, 0);
        
        public static class TRACE {
            public static final int SPLINE_INTERPOLATION_STEPS = 10;
            public static final float LINE_WIDTH = 30f;
        }
    }
    
    public static class DEBUG {
        public static final boolean ENABLED = false;
        public static final boolean SHOW_RAYS = true;
        public static final int POINT_RADIUS = 10;
    }
}
