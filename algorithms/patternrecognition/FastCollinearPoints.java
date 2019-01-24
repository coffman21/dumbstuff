import java.util.Arrays;

public class FastCollinearPoints {

    private Point[] pts;
    private LineSegment[] lines;
    private int len;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        this.pts = Arrays.copyOf(points, points.length);
        for (int i = 0; i < pts.length; i++) {
            if (pts[i] == null) throw new IllegalArgumentException();
            Point p = pts[i];
            for (int j = 0; j < pts.length; j++) {
                if (j != i && p.compareTo(pts[j]) == 0) throw new IllegalArgumentException();
            }
        }
        this.lines = new LineSegment[0];
        this.len = 0;
        findSegments();
    }

    private void findSegments() {
        for (int i = 0; i < pts.length; i++) {

            Arrays.sort(pts);
            Point currPoint = pts[i];
            Arrays.sort(pts, currPoint.slopeOrder());

            int j = 1;
            while (j < pts.length - 1) {
                int firstPointIdx = j;
                int lastPointIdx = j;
                while (j < pts.length - 1
                        && currPoint.slopeTo(pts[firstPointIdx]) == currPoint.slopeTo(pts[j + 1])) {
                    lastPointIdx = ++j;
                }
                if (lastPointIdx - firstPointIdx + 2 >= 4 && currPoint.compareTo(pts[firstPointIdx]) < 0) {
                    lines = Arrays.copyOf(lines, ++len);
                    lines[len - 1] = new LineSegment(currPoint, pts[lastPointIdx]);
                }
                j++;
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return len;
    }

    // the line segments
    public LineSegment[] segments() {
        return lines;
    }

    public static void main(String[] args) {
        Point[] points = { null };
        FastCollinearPoints fastCollinearPoints = new FastCollinearPoints(points);

    }
}
