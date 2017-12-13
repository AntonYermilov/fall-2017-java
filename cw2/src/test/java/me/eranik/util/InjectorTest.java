package me.eranik.util;

import me.eranik.util.case1.A;
import me.eranik.util.exceptions.InjectionCycleException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InjectorTest {
    @Test
    void testInitialize() {
        try {
            A a = (A) Injector.initialize("me.eranik.util.case1.A",
                    new Class<?>[]{
                            me.eranik.util.case1.A.class,
                            me.eranik.util.case1.B.class,
                            me.eranik.util.case1.C.class,
                            me.eranik.util.case1.D.class,
                            me.eranik.util.case1.iB.class}
            );
            assertEquals(6, a.x);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    void testInitializeWithCycle() {
        try {
            assertThrows(InjectionCycleException.class,
                    () -> Injector.initialize("me.eranik.util.case2.A",
                            new Class<?>[]{
                                    me.eranik.util.case2.A.class,
                                    me.eranik.util.case2.B.class,
                                    me.eranik.util.case2.C.class,
                                    me.eranik.util.case2.D.class,
                                    me.eranik.util.case2.iB.class
                            })
            );
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

}