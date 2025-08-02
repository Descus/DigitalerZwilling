package de.frauas.GUI.controllers;

import javax.swing.JOptionPane;

/**
 * NotificationHelper is a utility class to simplify showing popup dialogs
 * for errors and warnings across the application.
 */
public class NotificationHelper {

    public static void showError( String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Displays a warning dialog with Yes/No options.
     * <p>
     * @param message the warning message to be shown
     * @return true if the user clicks "Yes", false otherwise
     */
    public static boolean showWarning( String message) {
        int result = JOptionPane.showConfirmDialog(
                null,
                message,
                "Warning",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }
}