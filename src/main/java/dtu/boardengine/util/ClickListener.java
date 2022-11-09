package dtu.boardengine.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class ClickListener implements MouseListener {

    public abstract void onClick ();

    @Override
    public void mouseClicked(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        onClick();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // do nothing
    }
}
