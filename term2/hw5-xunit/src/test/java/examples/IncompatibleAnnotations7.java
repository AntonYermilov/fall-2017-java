package examples;
import me.eranik.xunit.annotations.*;

public class IncompatibleAnnotations7 {
    @Before@AfterClass
    void test() {}
}
