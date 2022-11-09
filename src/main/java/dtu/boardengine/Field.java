package dtu.boardengine;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A field is something which you can put tokens on.
 */
public class Field {
    private final JLayeredPane pane;

    private Field(Factory factory, JLayeredPane pane) {
        this.pane = pane;
        pane.setOpaque(true);
        pane.add(factory.makeLabel(factory.title));
        pane.add(factory.makeLabel(factory.subtitle));
        pane.add(factory.makeLabel(factory.description));
        pane.setLayout(new GridLayout(3, 1));
        pane.setBackground(factory.background);
        pane.setForeground(factory.foreground);
    }


    public static Factory make() {
        return new Factory();
    }

    public void setTokens(List<Token> tokens) {
        for (Token t : tokens) {
            JLabel label = new JLabel();
            label.setIcon(t.getIcon());
            this.pane.add(label);
        }
    }

    public static class Factory {
        public Color foreground = Color.black;
        private Color background = Color.white;
        private String title = "Title";
        private String subtitle = "Subtitle";
        private String description = "Description";

        private Factory() {
        }

        public Factory setTitle(String title) {
            this.title = title;
            return this;
        }

        public Factory setSubtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public Factory setDescription(String description) {
            this.description = description;
            return this;
        }

        public Factory setForeground(Color foreground) {
            this.foreground = foreground;
            return this;
        }

        private JLabel makeLabel(String string)  {
            var label = new JLabel();
            label.setText(string);
            label.setForeground(foreground);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setHorizontalTextPosition(SwingConstants.CENTER);
            label.setBackground(null);
            label.setOpaque(true);
            return label;
        }

        public Factory setBackground(Color background) {
            this.background = background;
            return this;
        }

        public Field attach(JLayeredPane pane) {
            return new Field(this, pane);
        }
    }


}
