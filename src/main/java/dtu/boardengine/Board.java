package dtu.boardengine;

import dtu.boardengine.layout.BoardLayout;
import dtu.boardengine.layout.EdgeLayout;
import dtu.boardengine.util.ClickListener;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 */
public class Board {
    private final List<Field> fields;

    private final GameController controller;
    private final Die.Factory dieFactory;
    private final JLayeredPane diebox;


    private Board(Factory factory, GameController controller) {
        // this.center = factory.center;
        this.controller = controller;

        this.dieFactory = factory.dieFactory;

        JFrame frame = new JFrame();
        frame.setLocationByPlatform(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        var base = new JLayeredPane();
        base.setPreferredSize(factory.dimensions);
        base.setSize(factory.dimensions);
        base.setLayout(null);
        base.setBackground(factory.background);
        base.setOpaque(true);
        frame.add(base);

        var infobox = new JLayeredPane();
        infobox.setOpaque(true);
        infobox.setLayout(null);

        var board = this;
        infobox.addMouseListener(new ClickListener() {
            @Override
            public void onClick() {
                controller.clickInfoBox(board);
                redraw();
            }
        });
        base.add(infobox);

        this.fields = setupFields(factory, infobox, base);

        diebox = new JLayeredPane();
        diebox.setLayout(null);
        diebox.setBounds(0, 0, infobox.getWidth(), infobox.getHeight());
        infobox.add(diebox);
        infobox.setLayer(diebox, JLayeredPane.POPUP_LAYER);

        frame.pack();
        frame.setVisible(true);
    }

    private ArrayList<Field> setupFields(Factory factory, JLayeredPane infobox, JLayeredPane base) {
        var tmpFields = new ArrayList<Field>();
        var panes = new ArrayList<JComponent>();
        for (Field.Factory ignored : factory.fields) {
            var pane = new JLayeredPane();
            pane.setOpaque(true);
            base.add(pane);
            panes.add(pane);
        }

        factory.layout.layoutBoard(panes, infobox, factory.dimensions);

        for (int i = 0; i < factory.fields.size(); i++) {
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
        return tmpFields;
    }

    private void redraw() {
        SwingUtilities.invokeLater(() -> controller.draw(this));
    }

    public void displayDies(List<Integer> dies) {
        diebox.removeAll();
        for (int eyes : dies) {
            var rnd = new Random();
            var x = rnd.nextInt(diebox.getWidth() - 60);
            var y = rnd.nextInt(diebox.getHeight() - 60);
            var die = dieFactory.setEyes(eyes)
              .setRotation(rnd.nextFloat(2))
              .create();
            die.setBounds(x, y, 60, 60);
            diebox.add(die);
        }
        diebox.revalidate();
        diebox.repaint();
    }


    public static Factory make(Attributes attributes) throws IOException {
        return new Factory(attributes);
    }

    public static Factory make() {
        try {
            return make(Attributes.def());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setFieldTokens(int i, List<Token> tokens) {
        fields.get(i)
          .setTokens(tokens);
    }

    public void clear() {
        for (Field f : fields) {
            f.setTokens(List.of());
        }
    }

    @SuppressWarnings({"UnusedReturnValue", "unused"})
    public static class Factory {

        private Dimension dimensions;

        private final Die.Factory dieFactory;

        private Color background;
        private ArrayList<Field.Factory> fields = new ArrayList<>();
        private BoardLayout layout = new EdgeLayout();
        private GameController controller;

        public Factory(Attributes attrs) throws IOException {
            dimensions = attrs.getWindowDimensions();
            background = attrs.getBoardColor();
            dieFactory = Die.from("Dice.png");
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
