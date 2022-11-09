package dtu.matador;

import dtu.boardengine.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


public class Main extends GameController {
    private final List<Integer> houseCount;

    public Main() {
        houseCount = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            houseCount.add(0);
        }

    }

    public Board.Factory setup() {
        Board.Factory bf = Board.make();

        for (Integer ignored : houseCount) {
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
        Token house = Token.from("House.png");
        for (int i = 0; i < houseCount.size(); i++) {
            ArrayList<Token> tokens = new ArrayList<>();
            int j = houseCount.get(i);
            while (j > 0) {
                if (j >= 5) {
                    j -= 5;
                    tokens.add(hotel);
                } else {
                    j -= 1;
                    tokens.add(house);
                }
            }
            System.out.println(i +  ": " + tokens.size());
            board.setFieldTokens(i, tokens);
        }
    }

    @Override
    public void clickField(Field field) {
        var fid = field.getId();
        System.out.println(houseCount.get(fid));
        houseCount.set(fid, (houseCount.get(fid) + 1));
    }

    public void clickInfoBox(Board board) {
        // reset house count
        for (int i = 0; i < houseCount.size(); i++) {
            houseCount.set(i, 0);
        }
        board.displayDies(List.of(1, 3));
    }


    public static void main(String[] args) {
        new Main().runGame();
    }
}
