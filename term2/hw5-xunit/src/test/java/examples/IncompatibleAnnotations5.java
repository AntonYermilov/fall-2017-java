package examples;
import me.eranik.xunit.annotations.*;

public class IncompatibleAnnotations5 {
    @Before@BeforeClass
    void test() {}
}
