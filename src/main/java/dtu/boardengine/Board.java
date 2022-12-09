package dtu.boardengine;

import dtu.boardengine.layout.BoardLayout;
import dtu.boardengine.layout.EdgeLayout;
import dtu.boardengine.util.ClickListener;
import dtu.boardengine.util.UniqString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    private final JLabel infoLabel;
    private final JFrame frame;


    private Board(@NotNull Factory factory, @NotNull GameController controller) {
        // this.center = factory.center;
        this.controller = controller;

        this.dieFactory = factory.dieFactory;

        frame = new JFrame();
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

        infoLabel = new JLabel();
        infoLabel.setForeground(factory.foreground);
        infoLabel.setFont(new Font("Sans-Serif", Font.PLAIN, 18));
        infoLabel.setBounds(10, 10, infobox.getWidth() - 10, infobox.getHeight() - 10);
        infobox.add(infoLabel);

        frame.pack();
        frame.setVisible(true);
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

    /**
     * Ask a player a question which has multiple answers.
     *
     * @param question - The question of to ask the players .
     * @param title - The title to display in the info box.
     * @param options - The options to choose between.
     * @return the index of the option choosen.
     */
    @SuppressWarnings("unused")
    public int askOneOf(String question, String title, List<String> options) {
        List<UniqString> items = options.stream()
          .map(UniqString::new)
          .toList();
        //noinspection SuspiciousMethodCalls
        return items.indexOf(JOptionPane.showInputDialog
          (frame, question, title,
           JOptionPane.PLAIN_MESSAGE, null,
           items.toArray(), items.get(0)
          ));
    }

    /**
     * Ask a player a question which can only be answered yes or no.
     *
     * @param question - The question to be answered
     * @param title - The title of the pop-up
     * @param yes - The Yes option
     * @param no - The No option
     * @return true if yes, and false if no.
     */
    @SuppressWarnings("unused")
    public boolean askYesNo(String question, String title, String yes, String no) {
        return 0 != JOptionPane.showOptionDialog(
          frame, question, title, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
          null,
          new String[]{
            no, yes
        }, null);
    }

    /**
     * Ask the players for a text input, optionally requiring it tobe non-empty.
     *
     * @param question - the question to be answered
     * @param title - the title.
     * @param whenEmpty - if not null, requiring the answer to be non-empty, and displaying this string.
     * @return the string typed by the user.
     */
    public String askTextInput(@NotNull String question, @NotNull String title, @Nullable String whenEmpty) {
        final var dialog = new JDialog(frame, title, true);
        final JTextField text = new JTextField();
        final JOptionPane optionPane = new JOptionPane(
             new Object[] {text, question},
          JOptionPane.PLAIN_MESSAGE,
          JOptionPane.DEFAULT_OPTION);
        dialog.setLocationByPlatform(true);
        dialog.setLocationRelativeTo(frame);
        dialog.setContentPane(optionPane);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        optionPane.addPropertyChangeListener(e -> {
            if (dialog.isVisible()
                && e.getSource() == optionPane
                 && e.getPropertyName().equals(JOptionPane.VALUE_PROPERTY)) {
                if (optionPane.getValue().equals(JOptionPane.UNINITIALIZED_VALUE)) {
                    return;
                }
                // Reset the pane for next time
                optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
                if (text.getText().isEmpty() && whenEmpty != null) {
                    text.selectAll();
                    JOptionPane.showMessageDialog(dialog, whenEmpty);
                    return;
                }
                dialog.setVisible(false);
            }
        });
        dialog.pack();
        dialog.setVisible(true);

        return text.getText();
    }

    /**
     * Send a message to the players
     * @param message - the message
     */
    @SuppressWarnings("unused")
    public void sendMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }


    public void setInfo(String a) {
        infoLabel.setText(a);
        infoLabel.repaint();
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

        private final Die.@NotNull Factory dieFactory;
        public @NotNull Color foreground;
        private @NotNull Dimension dimensions;
        private @NotNull Color background;
        private @NotNull ArrayList<Field.Factory> fields = new ArrayList<>();
        private @NotNull BoardLayout layout = new EdgeLayout();

        public Factory(@NotNull Attributes attrs) throws IOException {
            dimensions = attrs.getWindowDimensions();
            background = attrs.getBoardColor();
            foreground = attrs.getForegroundColor();
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
