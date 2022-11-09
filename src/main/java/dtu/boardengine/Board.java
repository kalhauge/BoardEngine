package dtu.boardengine;

import dtu.boardengine.layout.BoardLayout;
import dtu.boardengine.layout.EdgeLayout;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Board {
    private final List<Field> fields;

    // private final BoardCenter center;
    private final JFrame frame;

    private Board(Factory factory) {
        // this.center = factory.center;

        this.frame = new JFrame();
        frame.setLocationByPlatform(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        var base = new JLayeredPane();
        var dimensions = factory.attrs.getWindowDimensions();
        base.setPreferredSize(dimensions);
        base.setLayout(null);
        base.setBackground(factory.attrs.getBoardColor());
        base.setOpaque(true);

        var tmpFields = new ArrayList<Field>();
        var panes = new ArrayList<JComponent>();
        for (Field.Factory f : factory.fields) {
            var pane = new JLayeredPane();
            pane.setOpaque(true);
            base.add(pane);
            tmpFields.add(f.attach(pane));
            panes.add(pane);
        }
        factory.layout.layoutFields(panes, dimensions);

        frame.add(base);
        frame.pack();
        frame.setVisible(true);
        this.fields = List.copyOf(tmpFields);
    }


    public static Factory make(Attributes attributes) {
        return new Factory(attributes);
    }

    public void setFieldTokens(int i, List<Token> tokens) {
        fields.get(i).setTokens(tokens);
    }

    public static class Factory {
        private final Attributes attrs;
        private ArrayList<Field.Factory> fields = new ArrayList<>();
        private BoardCenter center;
        private BoardLayout layout = new EdgeLayout();

        public Factory(Attributes attrs) {
            this.attrs = attrs;
        }

        public Factory addField(Field.Factory field) {
            fields.add(field);
            return this;
        }

        @SuppressWarnings("unused")
        private Factory setFields(ArrayList<Field.Factory> fields) {
            this.fields = fields;
            return this;
        }

        @SuppressWarnings("unused")
        private Factory setCenter(BoardCenter center) {
            this.center = center;
            return this;
        }

        @SuppressWarnings("unused")
        private Factory setLayout(BoardLayout layout) {
            this.layout = layout;
            return this;
        }

        public Board done() {
            return new Board(this);
        }

    }


}
