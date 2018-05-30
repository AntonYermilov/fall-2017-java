package examples;

import me.eranik.xunit.annotations.*;

public class ValidAnnotations1 {
    @BeforeClass
    void beforeClass() {}

    @Before
    void before() {}

    @Test
    void test1() {}

    @Test
    void test2() {}

    @Test
    void test3() {}

    @After
    void After() {}

    @AfterClass
    void AfterClass() {}
}
