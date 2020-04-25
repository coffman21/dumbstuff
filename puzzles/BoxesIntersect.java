package puzzles;

public class BoxesIntersect {

    public static void main(String[] args) {
        Box testBox = new Box(new Point(2, 2), new Point(5, 7));
        Box boxUpLeft = new Box(new Point(1, 1), new Point(3, 3));
        Box boxUpRight = new Box(new Point(4, 1), new Point(6, 3));
        Box boxDownLeft = new Box(new Point(1, 6), new Point(3, 8));
        Box boxDownRight = new Box(new Point(4, 6), new Point(6, 8));
        Box notIntersectBox = new Box(new Point(15, 15), new Point(20, 20));

        assert testBox.intersects(boxUpLeft);
        assert testBox.intersects(boxUpRight);
        assert testBox.intersects(boxDownLeft);
        assert testBox.intersects(boxDownRight);
        assert !testBox.intersects(notIntersectBox);
        assert boxUpLeft.intersects(testBox);
        assert boxUpRight.intersects(testBox);
        assert boxDownLeft.intersects(testBox);
        assert boxDownRight.intersects(testBox);
        assert !notIntersectBox.intersects(testBox);

    }

    static class Box {
        Point a; // upper left corner
        Point b; // bottom right corner

        public Box(Point a, Point b) {
            assert a.x > 0 && a.y > 0 && b.x > 0 && b.y > 0;
            assert a.x < b.x;
            assert a.y < b.y;
            this.a = a;
            this.b = b;
        }

        public boolean intersects(Box that) {
            return (that.a.in(this) || that.b.in(this)
                    || that.bottomLeft().in(this)
                    || that.upperRight().in(this));
        }

        Point upperRight() { return new Point(b.x, a.y); }
        Point bottomLeft() { return new Point(a.x, b.y); }
    }

    static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean in(Box box) {
            return x < box.a.x && x > box.b.x
                    && y > box.a.y;
        }
    }
}
