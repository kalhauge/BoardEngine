package dtu.boardengine;

import dtu.boardengine.layout.BoardLayout;
import dtu.boardengine.layout.EdgeLayout;
import dtu.boardengine.util.ClickListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Board {
    private final List<Field> fields;

    private final GameController controller;


    private Board(Factory factory, GameController controller) {
        // this.center = factory.center;
        this.controller = controller;

        // private final BoardCenter center;
        JFrame frame = new JFrame();
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
        for (Field.Factory ignored : factory.fields) {
            var pane = new JLayeredPane();
            pane.setOpaque(true);
            base.add(pane);
            panes.add(pane);
        }
        factory.layout.layoutFields(panes, dimensions);
        for (int i = 0; i < factory.fields.size(); i ++ ) {
            var f = factory.fields.get(i);
            var p = (JLayeredPane) panes.get(i);
            Field field = f.attach(this, i, p);
            tmpFields.add(field);
            p.addMouseListener(new ClickListener() {
                @Override
                public void onClick() {
                    controller.clickField(field);
                    redraw();
                }
            });
        }

        frame.add(base);
        frame.pack();
        frame.setVisible(true);
        this.fields = List.copyOf(tmpFields);
    }

    private void redraw() {
        SwingUtilities.invokeLater(() -> controller.draw(this));
    }


    public static Factory make(Attributes attributes) {
        return new Factory(attributes);
    }

    public static Factory make() {
        return make(Attributes.def());
    }

    public void setFieldTokens(int i, List<Token> tokens) {
        fields.get(i).setTokens(tokens);
    }

    public void clear() {
        for (Field f: fields) {
           f.setTokens(List.of());
        }
    }

    @SuppressWarnings({"UnusedReturnValue", "unused"})
    public static class Factory {
        private final Attributes attrs;

        private Dimension dimensions;


        private Color background;
        private ArrayList<Field.Factory> fields = new ArrayList<>();
        // private BoardCenter center;
        private BoardLayout layout = new EdgeLayout();
        private GameController controller;

        public Factory(Attributes attrs) {
            this.attrs = attrs;
            dimensions = attrs.getWindowDimensions();
            background = attrs.getBoardColor();
        }


        public Factory addField(Field.Factory field) {
            fields.add(field);
            return this;
        }

        private Factory setFields(ArrayList<Field.Factory> fields) {
            this.fields = fields;
            return this;
        }

        public Factory setDimensions(Dimension dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        public Factory setBackground(Color background) {
            this.background = background;
            return this;
        }

        private Factory setLayout(BoardLayout layout) {
            this.layout = layout;
            return this;
        }

        public Board done(GameController controller) {
            return new Board(this, controller);
        }
    }


}
