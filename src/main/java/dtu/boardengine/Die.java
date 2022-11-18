package dtu.boardengine;

import dtu.boardengine.util.RotateLabel;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Die {

    private final @NotNull Board board;
    private final @NotNull RotateLabel label;
    private final int eyes;

    public Die(@NotNull Board board, @NotNull Factory factory) {
        this.eyes = factory.eyes;
        this.board = board;
        this.label = factory.makeLabel();
    }

    public static Die.@NotNull Factory from(String file) throws IOException {
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

    public int getEyes() {
        return eyes;
    }

    public Component getLabel() {
        return label;
    }

    public void setVisible(boolean bool) {
        label.setVisible(bool);
        label.revalidate();
        label.repaint();
    }

    @SuppressWarnings("unused")
    public @NotNull Board getBoard() {
        return board;
    }

    public static class Factory{
        private final @NotNull ImageIcon[] icons;
        private float rotation;
        private int eyes;
        private int x;
        private int y;

        public Factory(ImageIcon[] icons) {
            this.icons = icons;
        }

        public @NotNull Factory setRotation(float rotation) {
            this.rotation = rotation;
            return this;
        }

        public @NotNull Factory setEyes(int eyes) {
            this.eyes = eyes;
            return this;
        }

        public @NotNull Die create(@NotNull Board board) {
           return new Die(board, this);
        }
        private ImageIcon getIcon() {
            return icons[eyes - 1];
        }

        public Factory setPosition(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public RotateLabel makeLabel() {
            var label = new RotateLabel(this.rotation);
            label.setBounds(x, y, 60, 60);
            label.setIcon(getIcon());
            return label;
        }
    }
}
