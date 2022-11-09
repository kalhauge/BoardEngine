package dtu.boardengine;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Die extends JLabel {
    private final float rotation;

    public Die(Factory factory) {
        rotation = factory.rotation;
        setIcon(factory.getIcon());
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        var icon = this.getIcon();
        assert icon != null;
        g2.rotate(this.rotation, (float) icon.getIconWidth() / 2, (float) icon.getIconHeight() / 2);
        super.paintComponent(g);
    }

    public static Die.Factory from(String file) throws IOException {
        URL path = Die.class.getClassLoader().getResource(file);
        assert path != null;
        BufferedImage image = ImageIO.read(path);
        var icons = new ImageIcon[6];
        for (int value = 0; value < 6; value++) {
            int x = 0;
            int y = 55 * value;
            icons[value] = new ImageIcon(image.getSubimage(x, y, 54, 54));
        }
        return new Die.Factory(icons);
    }

    public static class Factory{
        private final ImageIcon[] icons;
        private float rotation;
        private int eyes;

        public Factory(ImageIcon[] icons) {
            this.icons = icons;
        }

        public Factory setRotation(float rotation) {
            this.rotation = rotation;
            return this;
        }

        public Factory setEyes(int eyes) {
            this.eyes = eyes;
            return this;
        }

        public Die create() {
           return new Die(this);
        }
        private ImageIcon getIcon() {
            return icons[eyes - 1];
        }
    }
}
