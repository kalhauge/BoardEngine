package dtu.boardengine;

import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
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
        return from(new ImageIcon(url));
    }

    public static Token from(String s, Rectangle dims) {
        URL url = Token.class.getClassLoader().getResource(s);
        if (url == null) {
            throw new InvalidParameterException("The resource is not found: " + s);
        }
        try {
            BufferedImage image = ImageIO.read(url);
            return from(new ImageIcon(image.getSubimage(dims.x,dims.y, dims.width, dims.height)));
        } catch (IOException e) {
            // This is not how we handle exceptions :D
            throw new RuntimeException(e);
        }
    }

    public static Token from(ImageIcon i) {
        return new Token(i);
    }

    public @NotNull Icon getIcon() {
        return icon;
    }
}
