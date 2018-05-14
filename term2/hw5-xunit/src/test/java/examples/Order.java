package examples;

import me.eranik.xunit.annotations.*;

import java.io.StringWriter;

public class Order {
    private static StringWriter writer = new StringWriter();

    @BeforeClass
    void beforeClass() {
       writer.append('1');
    }

    @Before
    void before() {
        writer.append('2');
    }

    @Test
    void test1() {
        writer.append('3');
    }

    @Test
    void test2() {
        writer.append('3');
    }

    @Test
    void test3() {
        writer.append('3');
    }

    @After
    void After() {
        writer.append('4');
    }

    @AfterClass
    void AfterClass() {
        writer.append('5');
    }
}
