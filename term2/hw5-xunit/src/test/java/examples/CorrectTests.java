package examples;

import me.eranik.xunit.annotations.Before;
import me.eranik.xunit.annotations.BeforeClass;
import me.eranik.xunit.annotations.Test;

public class CorrectTests {

    private int beforeClassValue = 0;
    private int beforeValue = 0;

    @BeforeClass
    void beforeClass() {
        beforeClassValue = 5;
    }

    @Before
    void before() {
        beforeValue = beforeClassValue + 5;
    }

    @Test
    void test1() {
        beforeValue += 5;
        assert beforeValue == 15;
    }

    @Test(expected = AssertionError.class)
    void test2() {
        beforeValue += 10;
        assert beforeValue == 15;
    }

    @Test(ignore = "Invalid test")
    void test3() {
        beforeValue += 15;
        assert beforeValue == 15;
    }
}
