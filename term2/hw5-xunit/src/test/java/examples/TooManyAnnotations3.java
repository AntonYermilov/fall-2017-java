package examples;

import me.eranik.xunit.annotations.BeforeClass;

public class TooManyAnnotations3 {
    @BeforeClass
    void beforeClass1() {}

    @BeforeClass
    void beforeClass2() {}
}
