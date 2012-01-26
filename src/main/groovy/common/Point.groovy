package common

/**
 * author: ashwin
 * Date: 1/20/12
 */
class Point {

    double x;
    double y

    Point() {
        x = 0
        y = 0
    }

    Point(double x, double y) {
        this.x = x
        this.y = y
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Point point = (Point) o

        if (Double.compare(point.x, x) != 0) return false
        if (Double.compare(point.y, y) != 0) return false

        return true
    }

    int hashCode() {
        int result
        long temp
        temp = x != +0.0d ? Double.doubleToLongBits(x) : 0L
        result = (int) (temp ^ (temp >>> 32))
        temp = y != +0.0d ? Double.doubleToLongBits(y) : 0L
        result = 31 * result + (int) (temp ^ (temp >>> 32))
        return result
    };


    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
