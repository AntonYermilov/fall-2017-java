package examples;
import me.eranik.xunit.annotations.*;

public class IncompatibleAnnotations4 {
    @Test@AfterClass
    void test() {}
}
