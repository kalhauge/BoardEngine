package dtu.boardengine;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.net.URL;
import java.security.InvalidParameterException;

/**
 * A token is something that can be placed on a field.
 */
public class Token {
    private final @NotNull Icon icon;

    public Token(@NotNull Icon icon) {
        this.icon = icon;
    }

    public static Token from(String s) {
        URL url = Token.class.getClassLoader().getResource(s);
        if (url == null) {
            throw new InvalidParameterException("The resource is not found: " + s);
        }
        return new Token(new ImageIcon(url));
    }

    public static Token from(ImageIcon i) {
        return new Token(i);
    }

    public @NotNull Icon getIcon() {
        return icon;
    }
}
