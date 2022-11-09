package dtu.boardengine;

import javax.swing.*;

public abstract class GameController {

    public abstract Board.Factory setup();

    public abstract void draw(Board board);

    public abstract void clickField(Field field);

    public void runGame() {
        // Needed to make the swing application perform correctly
        SwingUtilities.invokeLater(() -> {
            Board.Factory bf = setup();
            Board board = bf.done(this);
            draw(board);
        });
    }
}
