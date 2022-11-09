package dtu.boardengine;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ResourceBundle;

public class Attributes {
    private final @NotNull ResourceBundle bundle;

    public Attributes(@NotNull ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public Attributes(String name) {
        this(ResourceBundle.getBundle(name));
    }

    public Attributes() {
        this("default");
    }

    public Dimension getWindowDimensions() {
        int size = Integer.parseInt(bundle.getString("Window.Size"));
        return new Dimension(size, size);
    }

    public Color getColor(String key) {
        return Color.decode(bundle.getString(key));
    }

    public Color getBoardColor() {
        return getColor("Board.Color");
    }



}
