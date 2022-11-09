package dtu.boardengine.layout;


import javax.swing.*;
import java.awt.Dimension;
import java.util.List;


/**
 * BoardLayout is an example of a "Command Pattern".
 */
public interface BoardLayout {
    void layoutFields(List<JComponent> components, Dimension dims);
}
