package dtu.boardengine;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * A token is something that can be placed on a field.
 */
public class Token {
    private final @NotNull Icon icon;

    public Token(@NotNull Icon icon) {
        this.icon = icon;
    }

    public @NotNull Icon getIcon() {
        return icon;
    }
}
