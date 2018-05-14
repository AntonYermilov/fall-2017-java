package examples;
import me.eranik.xunit.annotations.*;

public class IncompatibleAnnotations8 {
    @BeforeClass@After
    void test() {}
}
