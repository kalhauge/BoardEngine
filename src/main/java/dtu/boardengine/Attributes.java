package dtu.boardengine;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ResourceBundle;

public class Attributes {
    private final @NotNull ResourceBundle bundle;

    public Attributes(@NotNull ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public Attributes(@NotNull String name) {
        this(ResourceBundle.getBundle(name));
    }

    public static @NotNull Attributes def() {
        return new Attributes("default");
    }

    public @NotNull Dimension getWindowDimensions() {
        int size = Integer.parseInt(bundle.getString("Window.Size"));
        return new Dimension(size, size);
    }

    public @NotNull Color getColor(@NotNull String key) {
        return Color.decode(bundle.getString(key));
    }

    public @NotNull Color getBoardColor() {
        return getColor("Board.Color");
    }

    public @NotNull Color getForegroundColor() {
        return  getColor("Foreground.Color");
    }
}
