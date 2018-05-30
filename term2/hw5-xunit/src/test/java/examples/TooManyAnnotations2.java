package examples;

import me.eranik.xunit.annotations.After;

public class TooManyAnnotations2 {
    @After
    void after1() {}

    @After
    void after2() {}
}
