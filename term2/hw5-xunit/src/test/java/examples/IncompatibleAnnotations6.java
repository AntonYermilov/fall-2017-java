package examples;
import me.eranik.xunit.annotations.*;

public class IncompatibleAnnotations6 {
    @Before@After
    void test() {}
}
