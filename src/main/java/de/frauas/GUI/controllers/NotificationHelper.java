package de.frauas.GUI.controllers;

import javax.swing.JOptionPane;

public class NotificationHelper {

    public static void showError( String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

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