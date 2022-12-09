package dtu.boardengine.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HtmlBuilderTest {

    @Test
    @DisplayName("can bold things")
    void canBoldThings() {
        var b = HtmlBuilder.make();
        b.b("Hello, World!");
        assertEquals("<html><b>Hello, World!</b></html>", b.toHTML());
    }

    @Test
    @DisplayName("can make newlines")
    void canMakeNewlines() {
        var b = HtmlBuilder.make();
        b.text("hello").br().text("world");
        assertEquals("<html>hello<br/>world</html>", b.toHTML());
    }
}