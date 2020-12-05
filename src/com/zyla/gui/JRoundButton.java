package com.zyla.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import javax.swing.JButton;

public class JRoundButton extends JButton {

    private static final long serialVersionUID = 1L;

    private int radius;
    private boolean mouseOver = false;
    private boolean mousePressed = false;
    private Color colorPressed;
    private Color topBarColor;
    private Shape shape;

    public JRoundButton(String text) {
        super(text);
        this.radius = Math.min(getWidth(), getHeight());

        MouseAdapter mouseListener = new MouseAdapter(){

            @Override
            public void mousePressed (MouseEvent me) {
                if(contains(me.getX(), me.getY())){
                    mousePressed = true;
                    repaint();
                }
            }

            @Override
            public void mouseReleased (MouseEvent me) {
                mousePressed = false;
                repaint();
            }

            @Override
            public void mouseExited (MouseEvent me) {
                mouseOver = false;
                mousePressed = false;
                repaint();
            }

            @Override
            public void mouseMoved (MouseEvent me) {
                mouseOver = contains(me.getX(), me.getY());
                repaint();
            }
        };

        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
    }

    public void setColorPressed(Color colorPressed) {
        this.colorPressed = colorPressed;
    }

    public void setTopBarColor(Color topBarColor) {
        this.topBarColor = topBarColor;
    }

    @Override
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
        }
        return shape.contains(x, y);
    }

    @Override
    public void paintComponent (Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(topBarColor);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (mouseOver) {
            g2d.setColor(colorPressed);
        } else {
            g2d.setColor(getBackground());
        }

        if (mousePressed) {
            g2d.setColor(colorPressed);
        } else {
            g2d.setColor(getBackground());
        }

        g2d.fillOval(0, 0, getSize().width - 1, getSize().height - 1);

        g2d.setColor(Color.WHITE);

        g2d.setFont(getFont());

        FontMetrics fm = g2d.getFontMetrics(getFont());

        int stringWidth = fm.stringWidth(getText());
        int stringHeight = fm.getHeight();

        g2d.drawString(getText(), getWidth() / 2 - stringWidth / 2 - 1, getHeight() / 2 + stringHeight / 4 - 1);
        g2d.dispose();
        g.dispose();
    }
}
