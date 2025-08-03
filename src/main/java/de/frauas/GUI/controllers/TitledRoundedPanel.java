package de.frauas.GUI.controllers;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * TitledRoundedPanel is a custom Swing panel that draws a rounded rectangle
 * around its content and displays a styled title at the top.
 * <p>
 * It enhances visual grouping of components in the GUI by providing consistent
 * spacing, padding, and aesthetics across all panels that extend this class.
 * </p>
 *
 * <p>Features:
 * <ul>
 *     <li>Rounded border with anti-aliasing</li>
 *     <li>Custom title with consistent font and color</li>
 *     <li>Padding and layout pre-configured</li>
 * </ul>
 * </p>
 *
 * @author GUI-Group
 */
public class TitledRoundedPanel extends JPanel {
    private String title;
    private Color borderColor;

    /**
     * Constructs a TitledRoundedPanel with a title, border color, and inner content panel.
     * <p>
     * @param title       The title text displayed at the top of the panel.
     * @param borderColor The color used for the border and title text.
     */
    public TitledRoundedPanel(String title, Color borderColor) {
        int padding = 15;
        this.title = title;
        this.borderColor = borderColor;
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(30, padding, padding, padding));
    }

    /**
     * Paints the rounded border and title over the panel.
     *
     * @param g the Graphics context used for drawing
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int arc = 20; //radius of corner rounding
        int margin = 5;

        FontMetrics fm = g.getFontMetrics();
        int titleHeight = fm.getHeight();

        int width = getWidth() - 2 * margin;
        int height = getHeight() - 2 * margin - titleHeight;
        int x = margin;
        int y = margin + titleHeight;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(2));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.draw(new RoundRectangle2D.Double(x, y, width, height, arc, arc));
        g2.dispose();

        // Draw title text on top
        drawTitle(g);
    }

    /**
     * Draws the title text at the top-left corner of the panel.
     *
     * @param g the Graphics context used for drawing
     */
    private void drawTitle(Graphics g) {
        if (title == null || title.isEmpty()) return;
        Font font = new Font("Monospaced", Font.BOLD, 18);
        g.setFont(font);
        g.setColor(borderColor);
        int x = 15;
        int y = g.getFontMetrics().getAscent();
        g.drawString(title, x, y);
    }
}
