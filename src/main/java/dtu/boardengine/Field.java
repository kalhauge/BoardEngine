package dtu.boardengine;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A field is something which you can put tokens on.
 */
@SuppressWarnings("unused")
public class Field {
    private final Board board;
    private final int id;
    private final JLayeredPane tokens;

    private Field(Factory factory, Board board, int id, JLayeredPane pane) {
        this.board = board;
        this.id = id;
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
        this.tokens.setLayout(new GridLayout(3, 3));
        this.tokens.setBounds(
          factory.tokenMargin,
          factory.tokenMargin,
          pane.getWidth() - factory.tokenMargin * 2,
          pane.getHeight() - factory.tokenMargin * 2
        );
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

    /**
     * Updates the tokens on the field. This will actively also redraw the field.
     *
     * @param tokens a list of tokens to draw
     */
    public void setTokens(List<Token> tokens) {
        this.tokens.removeAll();
        for (Token t : tokens) {
            JLabel label = new JLabel();
            label.setIcon(t.getIcon());
            this.tokens.add(label);
        }
        this.tokens.revalidate();
        this.tokens.repaint();
    }

    @SuppressWarnings("unused")
    public static class Factory {
        public Color foreground = Color.black;
        public int tokenMargin = 20;
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

        public Factory setTokenMargin(int tokenMargin) {
            this.tokenMargin = tokenMargin;
            return this;
        }

        private JLabel makeLabel(String string) {
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
