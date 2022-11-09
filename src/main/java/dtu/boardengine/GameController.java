package dtu.boardengine;

import javax.swing.*;

public abstract class GameController {

    public abstract Board.Factory setup();

    public abstract void draw(Board board);

    public void clickField(Field field) {
        // does nothing override to handle event.
    }

    public void clickInfoBox(Board board) {
        // does nothing override to handle event.
    }

    public void runGame() {
        // Needed to make the swing application perform correctly
        SwingUtilities.invokeLater(() -> {
            Board.Factory bf = setup();
            Board board = bf.done(this);
            draw(board);
        });
    }

}
