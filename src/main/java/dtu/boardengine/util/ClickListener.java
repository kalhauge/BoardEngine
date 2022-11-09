package dtu.boardengine.util;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class ClickListener extends MouseAdapter {

    public abstract void onClick ();

    @Override
    public void mouseReleased(MouseEvent e) {
        onClick();
    }
}
