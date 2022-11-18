package dtu.boardengine.util;

import javax.swing.*;
import java.awt.*;

public class RotateLabel extends JLabel {
    private double rotation;

    public RotateLabel(double rotation) {
        this.rotation = rotation;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        var icon = this.getIcon();
        assert icon != null;
        g2.rotate(this.rotation, (float) icon.getIconWidth() / 2, (float) icon.getIconHeight() / 2);
        super.paintComponent(g);
    }

    @SuppressWarnings("unused")
    public RotateLabel setRotation(double rotation) {
        this.rotation = rotation;
        return this;
    }
}
