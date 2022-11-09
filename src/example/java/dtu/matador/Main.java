package dtu.matador;

import dtu.boardengine.*;

import javax.swing.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


public class Main extends GameController {
    private final List<Integer> hotelCount;

    public Main() {
        hotelCount = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hotelCount.add(0);
        }

    }

    public Board.Factory setup() {
        Board.Factory bf = Board.make();

        for (Integer _i : hotelCount) {
            bf.addField(Field.make()
                          .setTitle("Field")
                          .setSubtitle("")
                          .setDescription("")
                          .setBackground(Color.darkGray)
                          .setForeground(Color.white));
        }
        return bf;
    }

    @Override
    public void draw(Board board) {
        board.clear();
        Token hotel = Token.from("Hotel.png");
        for (int i = 0; i < hotelCount.size(); i++) {
            ArrayList<Token> tokens = new ArrayList<>();
            for (int j = 0; j < hotelCount.get(i); j++) {
                tokens.add(hotel);
            }
            board.setFieldTokens(i, tokens);
        }
    }

    @Override
    public void clickField(Field field) {
        System.out.println("Printed: " + field);
        var fid = field.getId();
        hotelCount.set(fid, hotelCount.get(fid) + 1);
        SwingUtilities.invokeLater(() -> {
            this.draw(field.getBoard());
        });
    }


    public static void main(String[] args) {
        new Main().runGame();
    }
}
