package dtu.boardengine;

import dtu.boardengine.layout.BoardLayout;
import dtu.boardengine.layout.EdgeLayout;
import dtu.boardengine.util.ClickListener;
import org.jetbrains.annotations.NotNull;

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
    private final @NotNull List<Field> fields;

    private final @NotNull GameController controller;
    private final Die.@NotNull Factory dieFactory;
    private final @NotNull JLayeredPane dieBox;


    private Board(@NotNull Factory factory, @NotNull GameController controller) {
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

        dieBox = new JLayeredPane();
        dieBox.setLayout(null);
        dieBox.setBounds(0, 0, infobox.getWidth(), infobox.getHeight());
        infobox.add(dieBox);
        infobox.setLayer(dieBox, JLayeredPane.POPUP_LAYER);

        frame.pack();
        frame.setVisible(true);
    }

    private @NotNull ArrayList<Field> setupFields(@NotNull Factory factory, JLayeredPane infobox, @NotNull JLayeredPane base) {
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

    public void displayDies(@NotNull List<Integer> dies) {
        dieBox.removeAll();
        var rnd = new Random();
        for (int eyes : dies) {
            var x = rnd.nextInt(dieBox.getWidth() - 60);
            var y = rnd.nextInt(dieBox.getHeight() - 60);
            var die = dieFactory.setEyes(eyes)
              .setRotation(rnd.nextFloat(2))
              .setPosition(x, y)
              .create(this);
            var label = die.getLabel();
            label.addMouseListener(new ClickListener() {
                @Override
                public void onClick() {
                    controller.clickDie(die);
                    redraw();
                }
            });
            dieBox.add(label);
        }
        dieBox.revalidate();
        dieBox.repaint();
    }


    public static @NotNull Factory make(@NotNull Attributes attributes) throws IOException {
        return new Factory(attributes);
    }

    public static @NotNull Factory make() {
        try {
            return make(Attributes.def());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setFieldTokens(int i, @NotNull List<Token> tokens) {
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

        private @NotNull Dimension dimensions;

        private final Die.@NotNull Factory dieFactory;

        private @NotNull Color background;
        private @NotNull ArrayList<Field.Factory> fields = new ArrayList<>();
        private @NotNull BoardLayout layout = new EdgeLayout();

        public Factory(@NotNull Attributes attrs) throws IOException {
            dimensions = attrs.getWindowDimensions();
            background = attrs.getBoardColor();
            dieFactory = Die.from("Dice.png");
        }


        public @NotNull Factory addField(@NotNull Field.Factory field) {
            fields.add(field);
            return this;
        }

        private @NotNull Factory setFields(@NotNull ArrayList<Field.Factory> fields) {
            this.fields = fields;
            return this;
        }

        public @NotNull Factory setDimensions(@NotNull Dimension dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        public @NotNull Factory setBackground(@NotNull Color background) {
            this.background = background;
            return this;
        }

        private @NotNull Factory setLayout(@NotNull BoardLayout layout) {
            this.layout = layout;
            return this;
        }

        public @NotNull Board done(@NotNull GameController controller) {
            return new Board(this, controller);
        }
    }


}
