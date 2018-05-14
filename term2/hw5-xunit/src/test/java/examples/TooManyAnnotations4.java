package examples;

import me.eranik.xunit.annotations.AfterClass;

public class TooManyAnnotations4 {
    @AfterClass
    void afterClass1() {}

    @AfterClass
    void afterClass2() {}
}
