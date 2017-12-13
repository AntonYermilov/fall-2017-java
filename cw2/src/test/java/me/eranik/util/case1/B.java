package me.eranik.util.case1;

public class B implements iB {
    int x;
    public B() {
        x = 3;
    }

    @Override
    public int getX() {
        return x;
    }
}
