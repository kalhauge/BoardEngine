package dtu.boardengine.util;

import java.net.URL;
import java.util.List;

@SuppressWarnings("unused")
public class HtmlBuilder implements HtmlRenderable {
    private final StringBuilder builder;

    public HtmlBuilder(StringBuilder builder) {
        this.builder = builder;
    }

    public static HtmlBuilder make() {
        return new HtmlBuilder(new StringBuilder());
    }

    /**
     * Creates an unnumbered list
     *
     * @param items - the items in the list
     * @return an HtmlBuilder
     */
    public HtmlBuilder ul(List<HtmlRenderable> items) {
        return wrap("ul", b -> {
            for (var item : items) {
                wrap("li", item);
            }
        });
    }

    public HtmlBuilder ol(List<HtmlRenderable> items) {
        return wrap("ol", b -> {
            for (var item : items) {
                wrap("li", item);
            }
        });
    }

    public HtmlBuilder newline() {
        builder.append("<br/>");
        return this;
    }

    public HtmlBuilder text(String hello) {
        builder.append(hello);
        return this;
    }

    public HtmlBuilder b(HtmlRenderable text) {
        return wrap("b", text);
    }
    public HtmlBuilder b(String text) {
        return wrap("b", text);
    }

    public HtmlBuilder i(HtmlRenderable text) {
        return wrap("i", text);
    }
    public HtmlBuilder i(String text) {
        return wrap("i", text);
    }

    public HtmlBuilder img(URL url) {
        builder
          .append("<img src=\"")
          .append(url)
          .append("\"")
          .append("/>");
        return this;
    }

    public HtmlBuilder wrap(String tag, String text) {
        return wrap(tag, HtmlRenderable.fromString(text));
    }
    public HtmlBuilder wrap(String tag, HtmlRenderable render) {
        builder.append("<").append(tag).append(">");
        render.render(this);
        builder.append("</").append(tag).append(">");
        return this;
    }

    public String toHTML() {
        return "<html>" + builder.toString() + "</html>";
    }

    @Override
    public void render(HtmlBuilder b) {
        b.builder.append(this.builder);
    }

    public HtmlBuilder br() {
        return newline();
    }
}
