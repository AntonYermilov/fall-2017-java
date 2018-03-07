package app.util;

/**
 * Stores pair of coordinates that describe some point.
 */
public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Point) {
            Point p = (Point) o;
            return x == p.x && y == p.y;
        }
        return false;
    }

}
