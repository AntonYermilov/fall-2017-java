package examples;
import me.eranik.xunit.annotations.*;

public class IncompatibleAnnotations1 {
    @Test@Before
    void test() {}
}
