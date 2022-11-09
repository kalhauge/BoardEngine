package dtu.boardengine;

import javax.swing.*;

public abstract class GameController implements EventHandler {

    public abstract Board.Factory setup();

    public abstract void draw(Board board);

    public void runGame() {
        // Needed to make the swing application perform correctly
        SwingUtilities.invokeLater(() -> {
            Board.Factory bf = setup();
            bf.setEventHandler(this);
            Board board = bf.done();
            draw(board);
        });
    }
}