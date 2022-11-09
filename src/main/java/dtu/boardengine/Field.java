package dtu.boardengine;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A field is something which you can put tokens on.
 */
public class Field {
    private final Board board;
    private final int id;
    private final JLayeredPane pane;
    private final JLayeredPane tokens;

    private Field(Factory factory, Board board, int id, JLayeredPane pane) {
        this.board = board;
        this.id = id;
        this.pane = pane;
        pane.setLayout(null);
        pane.setBackground(factory.background);
        pane.setOpaque(true);

        var labels = new JLayeredPane();
        pane.add(labels);
        labels.setBounds(0, 0, pane.getWidth(), pane.getHeight());
        labels.add(factory.makeLabel(factory.title));
        labels.add(factory.makeLabel(factory.subtitle));
        labels.add(factory.makeLabel(factory.description));
        labels.setLayout(new GridLayout(3, 1));

        this.tokens = new JLayeredPane();
        pane.add(tokens);
        tokens.setLayout(new GridBagLayout());
        tokens.setBounds(0, 0, pane.getWidth(), pane.getHeight());
        pane.setLayer(this.tokens, JLayeredPane.PALETTE_LAYER);

    }

    public Board getBoard() {
        return board;
    }

    public int getId() {
        return id;
    }


    public static Factory make() {
        return new Factory();
    }

    public void setTokens(List<Token> tokens) {
        this.tokens.removeAll();
        for (Token t : tokens) {
            JLabel label = new JLabel();
            label.setIcon(t.getIcon());
            this.tokens.add(label);
        }
        this.tokens.revalidate();
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

        public Field attach(Board board, int id, JLayeredPane pane) {
            return new Field(this, board, id, pane);
        }
    }


}
