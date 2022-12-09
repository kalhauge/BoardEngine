package dtu.boardengine.util;

public interface HtmlRenderable {
    void render(HtmlBuilder b);

    static HtmlRenderable fromString(String text) {
       return b -> b.text(text);
    }
}
