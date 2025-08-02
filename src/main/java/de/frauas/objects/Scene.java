package de.frauas.objects;

import de.frauas.GUI.controllers.NotificationHelper;
import de.frauas.IDrawable;
import de.frauas.Settings;
import de.frauas.objects.car.Car;
import de.frauas.objects.datastructures.Vec3D;
import de.frauas.objects.interfaces.ICarObserver;
import de.frauas.objects.obstacle.ISdf;
import de.frauas.objects.obstacle.Obstacle;
import de.frauas.objects.trace.*;
import de.frauas.scenario.dto.Scenario;
import de.frauas.scenario.dto.StartPosition;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a scene containing a car, obstacles, and a trace that the car follows.
 * The scene can be visualized and manipulated in a 2D space and includes functionality
 * for managing spatial distance fields (SDFs) for collision detection.
 * It extends {@code Transformable} to allow hierarchical transformations
 * and implements {@code ISdf} and {@code IDrawable} to provide signed distance field
 * calculations and rendering functionality.
 * <p>
 * @author Scenario
 */
@Getter
@Setter
public class Scene extends Transformable implements ISdf, IDrawable {
    
    /**
     * Represents the trace of an object within a scene.
     * This field stores a reference to a {@code Trace} instance, which can be used for
     * managing and visualizing the path or trajectory of an object in the {@code Scene}.
     * The {@code Trace} can contain a series of points defining the path, along with methods
     * to render or update it.
     * <p>
     * In the context of this {@code Scene} class, the {@code trace} can be used
     * to define the course or history of an object's movement or its spatial trajectory.
     * This may include functionalities such as computing the length of the path,
     * drawing the lines and points for visualization, or interacting with other components
     * of the scene for rendering or data processing purposes.
     * <p>
     * The initial value of {@code trace} is {@code null}, indicating that it is
     * not initialized or associated with any specific {@code Trace} object.
     */
    private Trace trace = null;
    /**
     * A list of obstacles present in the scene.
     * Each obstacle represents a physical barrier or structure that can interact with
     * other elements, such as the car, in the scene. The obstacles are defined with
     * properties such as their position, dimensions, and height, and can be represented
     * visually or used in various calculations, such as distance fields or path planning.
     * <p>
     * This list is initialized as empty and can be populated with obstacles
     * using specific methods in the class.
     */
    private final List<Obstacle> obstacles = new ArrayList<>();
    /**
     * Represents the car object used in the scene.
     * This variable is used to manage and interact with the car instance
     * within the scene, including its position, control, and observers.
     * Initially set to null, it must be initialized before use.
     */
    private Car car = null;
    /**
     * Represents the initial position of the scene's car in 3D space.
     * It is stored as a Vec3D object and can be used to define or reset the starting location of the car.
     * The position is defined by three coordinates (x, y, z).
     */
    private Vec3D startPosition = null;
    /**
     * Represents the initial heading angle of the car at the start of the simulation or scenario.
     * The heading is specified in radians and determines the orientation of the car in 2D space.
     * It is used as a reference for resetting or initializing the car's orientation within the scene.
     */
    private double startHeading = 0;

    /**
     * A 2D array used to store the precomputed signed distance field (SDF) values
     * for the scene's canvas. The dimensions of the array are derived from the
     * width and height of the scene's canvas as defined in {@code Settings.SCENE.CANVAS}.
     * Each element of the array represents the signed distance value at a specific
     * position within the canvas, which is used to determine the proximity of that
     * position to the nearest obstacle or boundary.
     * <p>
     * This array is primarily used to optimize the performance of signed distance
     * calculations by caching precomputed values, which can be looked up instead
     * of recalculating them during runtime.
     * <p>
     * The baking of the SDF values into this array is typically handled by invoking
     * specialized methods within the class, and the data in this array is utilized
     * by various scene rendering and simulation functionalities.
     */
    private final double[][] bakedSdFields = new double[(int) Settings.SCENE.CANVAS.getX()][(int) Settings.SCENE.CANVAS.getY()];
    /**
     * Indicates whether the scene's signed distance field (SDF) calculations have been completed
     * and cached (baked). When true, the SDF is prepared and can be used for collision detection
     * or other spatial queries. When false, the SDF is not yet prepared and must be computed
     * using the appropriate method before it can be utilized.
     */
    private boolean isBaked = false;


    /**
     * Constructs a Scene object using the provided Scenario.
     * Initializes the starting position, trace, obstacles, and car properties.
     * Displays error or warning messages if the given Scenario contains invalid or missing data.
     * Terminates the program if critical data is missing or the user cancels the action.
     * <p>
     * @param scenario the Scenario instance containing the starting position, trace points,
     *                 and objects to initialize this Scene.
     */
    public Scene(Scenario scenario) {
        if (scenario.getStartPosition() == null || scenario.getTrace() == null) {
            if (scenario.getStartPosition() == null) {
                NotificationHelper.showError("Scenario start position could not be parsed");
            } else if (scenario.getTrace() == null) {
                NotificationHelper.showError("Trace could not be parsed");
            }
            System.exit(0);
        }
        if (scenario.getObjects() == null) {
            if(!NotificationHelper.showWarning("There is no Object in the file. Do you want to continue?")){
                System.exit(0);
            }
        } else {
            scenario.getObjects().forEach(object -> obstacles.add(new Obstacle(
                    this,
                    object.getXStart(),
                    object.getYStart(),
                    object.getXEnd(),
                    object.getYEnd(),
                    object.getHeight())));
        }

        StartPosition startPosition = scenario.getStartPosition();
        trace = new ShiftedCatmullTrace(this);
        trace.addPoint(new Vec3D(startPosition.getX(), startPosition.getY(), 1));
        scenario.getTrace().forEach(point -> trace.addPoint(new Vec3D(point.getX(), point.getY(), 1)));
        this.startPosition = new Vec3D(startPosition.getX(), startPosition.getY(), 1);
        this.startHeading = 360 - startPosition.getHeading();
        car = new Car(this, this.startPosition, this.startHeading);
        bakeSdf();
    }


    /**
     * Retrieves the Signed Distance Field (SDF) value for a specified position within the scene.
     * If the SDF is not baked, it computes and initializes the SDF fields before returning the value.
     * <p>
     * @param otherPosition the position as a Vec3D object for which the SDF value is to be retrieved.
     *                      Only the x and y components of the position are used, and they are clamped
     *                      based on the scene's canvas size.
     * @return the SDF value corresponding to the clamped x and y coordinates of the given position.
     */
    @Override
    public double getSDF(Vec3D otherPosition) {
        if (!isBaked) {
            bakeSdf();
        }
        return bakedSdFields[
                Math.clamp((int) otherPosition.getX(), 0, (int) Settings.SCENE.CANVAS.getX() - 1)
                ][
                Math.clamp((int) otherPosition.getY(), 0, (int) Settings.SCENE.CANVAS.getY() - 1)
                ];
    }

    /**
     * Generates and initializes the baked Signed Distance Fields (SDF) for the entire scene canvas.
     * This method iterates over all positions within the scene's defined canvas dimensions
     * and calculates the SDF value for each position using the {@code createSdf} method.
     * Once all values are computed, it marks the SDF process as completed by setting the
     * {@code isBaked} flag to {@code true}.
     * <p>
     * The baked SDF values are stored in a 2D array, {@code bakedSdFields}, where each entry
     * corresponds to a specific position defined by the x and y coordinates of the canvas.
     * <p>
     * This method modifies the {@code isBaked} field, ensuring that it accurately reflects
     * whether the SDF baking process has been finalized.
     */
    private void bakeSdf(){
        isBaked = false;
        for (int i = 0; i < Settings.SCENE.CANVAS.getX(); i++) {
            for (int j = 0; j < Settings.SCENE.CANVAS.getY(); j++) {
                bakedSdFields[i][j] = createSdf(new Vec3D(i, j, 0));
            }
        }
        isBaked = true;
    }

    /**
     * Calculates the minimum Signed Distance Field (SDF) value between a specified position
     * and all obstacles present in the scene. The SDF value indicates the shortest distance
     * to any obstacle and is computed by iterating over all obstacles and evaluating their SDF values.
     * <p>
     * @param otherPosition a Vec3D object representing the position for which the SDF value is calculated.
     *                      Only the x and y components of the position are considered in the computation.
     * @return the minimum SDF value between the provided position and all obstacles.
     */
    private double createSdf(Vec3D otherPosition){
        double min = Double.MAX_VALUE;
        for (Obstacle obstacle : obstacles) {
            double sdf = obstacle.getSDF(otherPosition);
            min = Math.min(min, sdf);
        }
        return min;
    }

    /**
     * Resets the car to a predefined starting position and heading.
     * This method updates the car's internal state and transforms, clearing its sensors and measurements,
     * and resets the starting conditions for motion and observation.
     * It utilizes the previously initialized start position and heading values of the scene.
     */
    public void resetCar(){
        car.reset(startPosition, startHeading);
    }

    /**
     * Starts the car within the scene.
     * This method initializes the car's internal state by invoking its {@code start} method,
     * which sets the car's status to {@code RUNNING}.
     */
    public void startCar(){
        this.car.start();
    }

    /**
     * Stops the car in the scene by invoking its internal stop method.
     * Changes the car's status to {@code STOPPED}, effectively halting
     * any ongoing motion or operations.
     */
    public void stopCar(){ this.car.stop();}

    /**
     * Pauses the car within the scene.
     * This method invokes the car's internal {@code pause} method, changing
     * its status to {@code PAUSED}. All car operations and motion are temporarily halted
     * until resumed.
     */
    public void pauseCar(){ this.car.pause();}

    /**
     * Resumes the car's operation within the scene.
     * <p>
     * This method invokes the {@code start} method of the car object,
     * setting its status to {@code RUNNING}. It reinitializes the
     * car's active state, allowing it to proceed from a paused or
     * stopped condition.
     */
    public void resumeCar(){ this.car.start();}

    /**
     * Renders the current scene, including the trace, obstacles, car, and additional overlays, onto the provided Graphics context.
     * This method applies transformations, draws multiple scene components, and optionally displays debug overlays based on settings.
     * <p>
     * @param g the Graphics context used for rendering the scene and all its elements.
     */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        {
            g2.transform(transform.toAffineTransform());
            trace.draw(g2);
            obstacles.forEach(obstacle -> obstacle.draw(g2));
            car.draw(g2);
            drawInScene(g2);
        }

        if (Settings.DEBUG.SHOW_MAP_OVERLAY) {
            for (int i = 0; i < Settings.WINDOW.WIDTH; i += 2) {
                for (int j = 0; j < Settings.WINDOW.HEIGHT; j += 2) {
                    Vec3D pos = new Vec3D(i, j, 1);
                    pos = toGlobalSpace(pos);
                    double sdf = getSDF(pos);
                    g2.setColor(((ShiftedTrace) trace).isPointBetweenLines(toGlobalSpace(pos)) ? Color.green : sdf >= 0 ? Color.red : Color.blue);
                    g2.fillOval((int) pos.getX(), (int) pos.getY(), 1, 1);
                }
            }
        }
        g2.dispose();
    }
    
    /**
     * Adds an observer to the car in the scene. The observer will be notified
     * of updates and changes from the car.
     * <p>
     * @param observer an implementation of the {@code ICarObserver} interface
     *                 that will receive update notifications from the car.
     */
    public void addObserverToCar(ICarObserver observer){
        if (car == null) return;
        car.addObserver(observer);
    }
    
    /**
     * Removes an observer from the car in the scene. The observer will no longer
     * receive updates or notifications from the car.
     * <p>
     * @param observer an implementation of the {@code ICarObserver} interface
     *                 that is to be removed from the car's list of observers.
     */
    public void removeObserverFromCar(ICarObserver observer){
        if (car == null) return;
        car.removeObserver(observer);
    }

    /**
     * Renders the scene by drawing its components, including the trace, obstacles, and the car,
     * onto the provided Graphics context.
     * <p>
     * @param g the Graphics context used for rendering the scene and its elements.
     */
    @Override
    public void drawInScene(Graphics g) {
        trace.drawInScene(g);
        obstacles.forEach(obstacle -> obstacle.drawInScene(g));
        car.drawInScene(g);
    }
}
