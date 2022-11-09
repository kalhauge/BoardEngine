package dtu.matador;

import dtu.boardengine.Attributes;
import dtu.boardengine.Board;
import dtu.boardengine.Field;
import dtu.boardengine.Token;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        Attributes attr = new Attributes();
        SwingUtilities.invokeAndWait(() -> {
            Board board = Board.make(attr)
              .addField(Field.make()
                          .setBackground(Color.blue)
                          .setForeground(Color.white)
                          .setTitle("Start")
                          .setSubtitle("")
                          .setDescription(""))
              .addField(Field.make()
                          .setForeground(Color.white)
                          .setBackground(Color.darkGray))
              .addField(Field.make())
              .addField(Field.make())
              .addField(Field.make())
              .addField(Field.make())
              .addField(Field.make())
              .addField(Field.make())
              .addField(Field.make())
              .addField(Field.make())
              .addField(Field.make())
              .addField(Field.make())
              .done();

            URL url = Token.class.getClassLoader()
              .getResource("Hotel.png");
            assert url != null;
            Token hotelToken = new Token(new ImageIcon(url));
            board.setFieldTokens(0, List.of(hotelToken, hotelToken));
        });


    }
}
