import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {

    private LineSegment[] lines;
    private int len;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            if (p == null) throw new IllegalArgumentException();
            for (int j = 0; j < points.length; j++) {
                if (j != i && p.compareTo(points[j]) == 0) throw new IllegalArgumentException();
            }
        }
        this.lines = new LineSegment[0];
        this.len = 0;
        findSegments(points);
    }

    private void findSegments(Point[] points) {
        for (int i = 0; i < points.length; i++) {

            Arrays.sort(points);
            Point currPoint = points[i];
            Arrays.sort(points, currPoint.slopeOrder());

            int j = 1;
            while (j < points.length - 1) {
                int firstPointIdx = j;
                int lastPointIdx = j;
                while (j < points.length - 1
                        && currPoint.slopeTo(points[firstPointIdx]) == currPoint.slopeTo(points[j + 1])) {
                    lastPointIdx = ++j;
                }
                if (lastPointIdx - firstPointIdx + 2 >= 4 && currPoint.compareTo(points[firstPointIdx]) < 0) {
                    lines = Arrays.copyOf(lines, ++len);
                    lines[len - 1] = new LineSegment(currPoint, points[lastPointIdx]);
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
}
