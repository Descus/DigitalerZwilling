package de.frauas;

import java.awt.*;

/**
 * Represents an interface for drawable objects. Classes implementing this interface
 * must provide methods to enable rendering of their visual representation
 * either directly or within a scene.
 * <p>
 * @author Gui
 */
public interface IDrawable {
    /**
     * Renders the visual representation of the object on the given Graphics context.
     * This method is responsible for drawing the object but does not apply any scene-specific
     * transformations or scaling.
     * <p>
     * @param g the Graphics context used for rendering the object.
     */
    void draw(Graphics g);
    /**
     * Renders the visual components of the object within a scene. This method is used to
     * display specific elements of the object, such as sensors or additional graphical
     * components, in the context of the larger scene.
     * <p>
     * @param g the Graphics context used for rendering within the scene.
     */
    void drawInScene(Graphics g);
}
