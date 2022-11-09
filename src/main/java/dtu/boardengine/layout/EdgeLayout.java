package dtu.boardengine.layout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EdgeLayout implements BoardLayout {


    @Override
    public void layoutBoard(List<JComponent> components, JComponent infobox, Dimension dims) {
        int fieldsPerSide = Math.max(Math.ceilDiv(components.size(), 4), 2);
        float fieldSizeX = (float) dims.width / ((float) fieldsPerSide + 1);
        float fieldSizeY = (float) dims.height / ((float) fieldsPerSide + 1);
        int margin = 3;
        int x = 0, y = 0;

        infobox.setBounds(
          Math.round(fieldSizeX + margin),
          Math.round(fieldSizeY + margin),
          Math.round((fieldsPerSide - 1) * fieldSizeX - 2 * margin),
          Math.round((fieldsPerSide - 1) * fieldSizeY - 2 * margin)
        );

        for (JComponent component : components) {
            component.setBounds(
              Math.round(x * fieldSizeX + margin),
              Math.round(y * fieldSizeY + margin),
              Math.round(fieldSizeX - 2 * margin),
              Math.round(fieldSizeY - 2 * margin)
            );
            if (y == 0 && x < fieldsPerSide) {
                x += 1;
            } else if (x == fieldsPerSide && y < fieldsPerSide) {
                y += 1;
            } else if (x > 0 && y == fieldsPerSide) {
                x -= 1;
            } else {
                y -= 1;
            }
        }

    }
}
