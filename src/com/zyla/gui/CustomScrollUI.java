package com.zyla.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class CustomScrollUI extends BasicScrollBarUI {
    private final Dimension dim = new Dimension();

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return new JButton() {
            @Override
            public Dimension getPreferredSize() {
                return dim;
            }
        };
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return new JButton() {
            @Override
            public Dimension getPreferredSize() {
                return dim;
            }
        };
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        Color Grey3 = new Color (80, 80, 80);

        JScrollBar sb = (JScrollBar) c;

        if (!sb.isEnabled()) {
            return;
        }

        g2.setPaint(Grey3);
        g2.fillRect(r.x, r.y, r.width, r.height);
        g2.drawRect(r.x, r.y, r.width, r.height);
        g2.dispose();
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        Color Grey4 = new Color (95, 95, 95);
        Color Grey5 = new Color (105, 105, 105);

        Color color = null;
        JScrollBar sb = (JScrollBar) c;

        if (!sb.isEnabled() || r.width > r.height) {
            return;
        } else if (isDragging) {
            color = Grey5;
        } else if (isThumbRollover()) {
            color = Grey4;
        } else {
            color = Grey4;
        }

        g2.setPaint(color);
        g2.fillRect(r.x, r.y, r.width, r.height);
        g2.drawRect(r.x, r.y, r.width, r.height);
        g2.dispose();
    }

    @Override
    protected void setThumbBounds(int x, int y, int width, int height) {
        super.setThumbBounds(x, y, width, height);
        scrollbar.repaint();
    }
}