package examples;

import me.eranik.xunit.annotations.AfterClass;
import me.eranik.xunit.annotations.Before;
import me.eranik.xunit.annotations.Test;

public class ValidAnnotations3 {
    @Before
    void before() {}

    @Test
    void test() {}

    @AfterClass
    void afterClass() {}
}
