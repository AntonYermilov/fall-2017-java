package examples;
import me.eranik.xunit.annotations.*;

public class IncompatibleAnnotations9 {
    @BeforeClass@AfterClass
    void test() {}
}
