package examples;
import me.eranik.xunit.annotations.*;

public class IncompatibleAnnotations2 {
    @Test@BeforeClass
    void test() {}
}
