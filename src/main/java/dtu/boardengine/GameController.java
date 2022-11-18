package dtu.boardengine;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public abstract class GameController {

    public abstract Board.@NotNull Factory setup();

    public abstract void draw(@NotNull Board board);

    public void clickField(@NotNull Field field) {
        // does nothing override to handle event.
    }

    public void clickInfoBox(@NotNull Board board) {
        // does nothing override to handle event.
    }

    public void clickDie(@NotNull Die die) {
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
