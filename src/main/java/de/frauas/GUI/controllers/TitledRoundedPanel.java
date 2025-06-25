package de.frauas.GUI.controllers;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class TitledRoundedPanel extends JPanel {
    private String title;
    private Color borderColor;

    public TitledRoundedPanel(String title, Color borderColor,JPanel panel) {
        this.title = title;
        this.borderColor = borderColor;
        setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
        setOpaque(false); // Make transparent to draw custom border

        int padding = 15; // hoặc tùy chỉnh theo độ cong viền và title
        setBorder(new EmptyBorder(30, padding, padding, padding));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int arc = 20; // How rounded the corners are
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

        // Draw title
        drawTitle(g);
    }

    private void drawTitle(Graphics g) {
        if (title == null || title.isEmpty()) return;
        Font font = new Font("Monospaced", Font.BOLD, 18);
        g.setFont(font);
        g.setColor(borderColor);

        int x = 15; // luôn cố định ở bên trái
        int y = g.getFontMetrics().getAscent(); // vị trí dòng đầu tiên

        g.drawString(title, x, y);
    }
}
