package de.frauas.objects.car.movement;

/**
 * The MovementInstruction enum represents the possible instructions
 * that can be given to an object, such as a vehicle, to control
 * its movement or actions.
 * <p>
 * forward - Indicates that the object should move forward.
 * left - Indicates that the object should turn or move to the left.
 * right - Indicates that the object should turn or move to the right.
 * stop - Indicates that the object should cease movement or actions.
 * <p>
 * @author Infrared
 */
public enum MovementInstruction {
    forward,
    left,
    right,
    stop
}
