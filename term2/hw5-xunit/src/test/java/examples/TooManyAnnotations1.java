package examples;

import me.eranik.xunit.annotations.Before;

public class TooManyAnnotations1 {
    @Before
    void before1() {}

    @Before
    void before2() {}
}
