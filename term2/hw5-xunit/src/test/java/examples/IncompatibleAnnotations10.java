package examples;
import me.eranik.xunit.annotations.*;

public class IncompatibleAnnotations10 {
    @After@AfterClass
    void test() {}
}
