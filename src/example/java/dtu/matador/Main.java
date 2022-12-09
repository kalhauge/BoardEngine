package dtu.matador;

import dtu.boardengine.*;
import dtu.boardengine.util.HtmlBuilder;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Main extends GameController {
    private final @NotNull List<Integer> houseCount;
    private final @NotNull Random rnd;
    private int carPosition;
    private String player_name;
    private int activeDie = 0;

    public Main() {
        houseCount = new ArrayList<>();
        rnd = new Random();
        for (int i = 0; i < 24; i++) {
            houseCount.add(0);
        }

    }

    public static void main(String[] args) {
        new Main().runGame();
    }

    public Board.@NotNull Factory setup() {
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

    public void onStart(@NotNull Board board) {
       player_name = board.askTextInput(
         "Player 1:",
         "Name your player!",
         "Your player needs a name!"
       );
    }

    @Override
    public void draw(@NotNull Board board) {
        board.clear();
        Token hotel = Token.from("Hotel.png");
        Token house = Token.from("House.png");
        Token car = Token.from("Cars.png", new Rectangle(0, 0, 40, 20));
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
            if (i == carPosition) {
                tokens.add(car);
            }
            board.setFieldTokens(i, tokens);
        }
        URL url = Token.class.getClassLoader().getResource("Cars.png");
        board.setInfo(
          HtmlBuilder.make()
            .b("The Current Score is:")
            .ul(List.of(
                html -> html.img(url).text(player_name).text(": " + carPosition)
            )).toHTML()
        );
    }

    @Override
    public void clickField(@NotNull Field field) {
        var fid = field.getId();
        houseCount.set(fid, (houseCount.get(fid) + 1));
    }

    public void clickInfoBox(@NotNull Board board) {
        // reset house count
        for (int i = 0; i < houseCount.size(); i++) {
            houseCount.set(i, 0);
        }

        // if there are no active die, reroll
        if (activeDie == 0) {
            var fst = rnd.nextInt(6) + 1;
            var snd = rnd.nextInt(6) + 1;

            board.displayDies(List.of(fst, snd));
            activeDie = 2;
        }
    }

    @Override
    public void clickDie(@NotNull Die die) {
        activeDie -= 1;
        carPosition = (carPosition + die.getEyes()) % houseCount.size();
        die.setVisible(false);
    }
}
