package examples;
import me.eranik.xunit.annotations.*;

public class IncompatibleAnnotations3 {
    @Test@After
    void test() {}
}
