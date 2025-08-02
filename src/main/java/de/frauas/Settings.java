package de.frauas;

import de.frauas.objects.datastructures.Vec3D;


/**
 * The Settings class encapsulates all the configurable parameters and constants
 * used by the application. It is organized into inner static classes to group
 * related settings for various components and aspects of the system.
 * <p>
 * @author Scenario
 */
public class Settings {
    
    /**
     * Represents the window configuration settings used by the application.
     * This class contains constants that define the default properties
     * of the main application window, including its title, dimensions,
     * and target frame rate.
     */
    public static class WINDOW {
        /**
         * Defines the title of the main application window.
         * This value is used to set the title displayed in the title bar
         * of the application's graphical user interface.
         */
        public static final String TITLE = "Digitaler Zwilling";
        /**
         * Specifies the default width of the main application window.
         * This constant is used to define the horizontal dimension of
         * the window during initialization.
         */
        public static final int WIDTH = 1600;
        /**
         * Specifies the default height of the main application window.
         * This constant is used to define the vertical dimension of
         * the window during initialization.
         */
        public static final int HEIGHT = 1000;
        /**
         * Specifies the target frames per second (FPS) for rendering within the application.
         * This value is used to control the refresh rate of graphical updates, ensuring
         * smooth performance and consistent visual output in the main application window.
         */
        public static final int TARGET_FPS = 60;
    }
    
    /**
     * The CAR class is a static container for constants related to car settings and attributes.
     * It provides a structure to store information about car dimensions, movement properties,
     * and sensor configurations, such as ultrasonic and infrared parameters.
     */
    public static class CAR{
        /**
         * Represents the dimensions of a car in millimeters, specifically its width, length, and height.
         * The vector contains three components:
         * - x: The width of the car.
         * - y: The length of the car.
         * - z: The height of the car, which is set to zero as height is not considered in this context.
         * <p>
         * This constant is used to define the physical size of the car in a 2D plane.
         */
        public static final Vec3D SIZE = new Vec3D(150, 240, 0);
        
        /**
         * The MOVEMENT class is a static container for constants related to the movement
         * properties of a car, such as its speed and turn speed. These values define
         * the physical movement characteristics used in driving simulations or control logic.
         */
        public static class MOVEMENT {
            /**
             * Defines the angular turning speed of a car in degrees per second.
             * This constant is used to control the car's rotation behavior,
             * such as determining how quickly it can change its orientation
             * during simulations or control logic operations.
             */
            public static final double TURN_SPEED_DEG_P_S = 56;
            /**
             * Represents the linear movement speed of a car in millimeters per second.
             * This constant is used to define the physical movement characteristics of the car,
             * such as its forward or reverse speed, during driving simulations or motion planning.
             */
            public static final double SPEED_MM_P_S = 300;
        }
        
        /**
         * The ULTRASONIC class serves as a static container for constants related to the configuration
         * and operation of ultrasonic sensors. The constants defined within this class are used to
         * determine the sensor's detection range, angular scanning range, operational timings, and
         * measurement granularity. These parameters aid in simulating or configuring ultrasonic-based
         * environment sensing, such as detecting obstacles or mapping surroundings.
         */
        public static class ULTRASONIC {
            /**
             * Represents the maximum distance (in centimeters) that can be measured by the
             * ultrasonic sensor. This value defines the upper limit of the sensor's detection range
             * and is used to filter out measurements that exceed this threshold.
             */
            public static final int MAX_DISTANCE = 300;
            /**
             * Represents the maximum angular range (in degrees) of the ultrasonic sensor's
             * scanning capability. This value defines the largest angle that the sensor
             * can cover during its operational sweep, restricting the detection field for objects
             * or surroundings. Used in context with configuring and simulating ultrasonic-based
             * sensing systems.
             */
            public static final int MAX_ANGLE = 15;
            /**
             * Indicates the time interval, in milliseconds, between consecutive checks or updates
             * for the ultrasonic sensor. This parameter is used to control the frequency
             * of sensor operation or simulation, balancing responsiveness and resource utilization.
             */
            public static final int CHECK_DELAY_MS = 10;
            /**
             * Defines the minimum angular step in degrees that the ultrasonic sensor covers during
             * its scanning or rotational operation. This value specifies the granularity of the
             * scanning process, determining the level of detail or resolution available in the
             * sensor's sweep. Lower values allow finer measurements, while higher values result
             * in coarser detection.
             */
            public static final double STEP_SIZE = 0.1;
        }
        /**
         * The INFRARED class provides configuration settings related to the
         * car's infrared sensor system for monitoring its environment. This class
         * contains constants that define timing parameters for sensor updates.
         * <p>
         * CHECK_DELAY_MS specifies the delay, in milliseconds, between consecutive
         * checks or updates performed by the infrared sensors. This value is used
         * to control and scale the movement of the car based on sensor input.
         */
        public static class INFRARED {
            /**
             * Specifies the delay interval between consecutive infrared sensor checks in milliseconds.
             * This constant defines the time period the system waits before performing the next
             * sensor update, crucial for managing the frequency of sensor data processing and
             * environmental monitoring by the infrared system.
             */
            public static final int CHECK_DELAY_MS = 7;
        }
    }
    
    /**
     * Represents the SCENE configuration containing constants and nested settings
     * related to visual scene representation and rendering.
     */
    public static class SCENE {

        /**
         * The DEFAULT_SCENARIO_FILE variable defines the default file path
         * for the scenario configuration, typically stored in XML format.
         * This file contains data necessary for initializing or rendering
         * a visual scene, including object positions, trace points, and
         * other scene-specific settings.
         */
        public static String DEFAULT_SCENARIO_FILE = "Scenario/ScenarioRightTurnObstacle.xml";
        /**
         * Defines the dimensions of the canvas used for visual rendering
         * and scene representation in the application.
         * <p>
         * This constant represents the width, height, and depth of the canvas.
         * The x-coordinate corresponds to the canvas width, the y-coordinate
         * to the canvas height, and the z-coordinate to the depth (defaulted to 0
         * for 2D representation). It is utilized in graphical scenes to establish
         * rendering boundaries and scaling.
         */
        public static final Vec3D CANVAS = new Vec3D(1000, 500, 0);
        
        /**
         * Defines constants related to trace rendering and configuration within a visual scene.
         * This class is typically used to store parameters influencing the appearance
         * of trace elements, such as lines or splines, in the rendered scene.
         */
        public static class TRACE {
            /**
             * Defines the number of steps used for spline interpolation within a graphical trace representation.
             * This constant determines the resolution of the interpolated spline, influencing the smoothness
             * of curves or paths rendered in the visual scene. A higher value results in a smoother spline
             * at the cost of increased computational overhead.
             */
            public static final int SPLINE_INTERPOLATION_STEPS = 10;
            /**
             * Defines the width of lines used in trace rendering within a graphical visualization.
             * This constant determines the thickness of lines or trace elements, influencing their
             * appearance and prominence in the rendered scene. The value is specified in floating-point
             * units, allowing for precise control over the line width.
             */
            public static final float LINE_WIDTH = 30f;
        }
    }
    
    /**
     * The DEBUG class contains constants that control debugging-related settings
     * for various functional or graphical purposes within the application. These
     * constants are primarily used to enable or disable specific debugging features
     * and to configure visual elements related to debugging.
     * <p>
     * FIELD DESCRIPTIONS:
     * <p>
     * ENABLED: A boolean constant specifying whether debugging features are globally enabled.
     * SHOW_RAYS: A boolean constant indicating whether graphical rays should be rendered during debugging.
     * SHOW_MAP_OVERLAY: A boolean constant indicating whether the map overlay should be displayed during debugging.
     * POINT_RADIUS: An integer constant defining the radius of points used in debugging visualizations.
     * <p>
     * This class is designed as a static utility within the configuration of the application and
     * does not require instantiation.
     */
    public static class DEBUG {
        /**
         * A boolean constant that indicates whether debugging features are globally enabled in the application.
         * When set to {@code true}, all debugging-related functionality, such as rendering specific debugging
         * visuals or enabling debug-specific behaviors, will be active. When set to {@code false}, these
         * features are disabled.
         */
        public static final boolean ENABLED = false;
        /**
         * A boolean constant that determines whether graphical rays are rendered for
         * visualization purposes during debugging. When set to {@code true}, rays
         * (such as those representing light paths, line of sight, or other directional
         * indicators) will be displayed in the graphical interface. When set to
         * {@code false}, these rays will be hidden.
         */
        public static final boolean SHOW_RAYS = true;
        /**
         * A boolean constant that determines whether the map overlay feature
         * is displayed in the graphical interface during debugging. When set to {@code true},
         * the map overlay will be rendered to aid in visualization and analysis. When set to
         * {@code false}, the map overlay will not be displayed. This setting is primarily
         * intended for debugging purposes and may provide additional context or information
         * useful during the development or testing phases.
         */
        public static final boolean SHOW_MAP_OVERLAY = false;
        /**
         * Defines the radius, in pixels, of points used in debugging visualizations.
         * This constant is used for rendering circular graphical representations of
         * points in the debugging interface.
         * <p>
         * The value of this constant determines the size of the points, allowing
         * developers to control and customize their appearance during debugging tasks.
         */
        public static final int POINT_RADIUS = 10;
    }
}
