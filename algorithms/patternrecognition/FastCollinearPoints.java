import java.util.Arrays;

public class FastCollinearPoints {

    private Line[] lines;
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
        this.lines = new Line[0];
        this.len = 0;
        findSegments(points);
    }

    private void findSegments(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            Point currPoint = points[i];

            Point[] sorted = Arrays.copyOf(points, points.length);
            Arrays.sort(sorted, currPoint.slopeOrder());

            int j = 0;
            while (j < sorted.length - 1) {
                double slope = currPoint.slopeTo(sorted[j]);
                if (sameSlope(sorted[j], sorted[j + 1], slope)) {

                    Point[] currLine = new Point[4];
                    int currLineIdx = 0;
                    currLine[currLineIdx++] = currPoint;
                    currLine[currLineIdx++] = sorted[j];
                    int k = j + 1;
                    while (k < sorted.length && sameSlope(sorted[j], sorted[k], slope)) {
                        if (currLineIdx >= currLine.length) {
                            currLine = Arrays.copyOf(currLine, currLine.length+1);
                        }
                        currLine[currLineIdx++] = sorted[k];
                        k++;
                    }
                    if (currLineIdx >= 4) {
                        addLineSegment(currLine);
                    }
                    j = k;
                }
                j++;
            }
        }
    }

    private boolean sameSlope(Point p1, Point p2, double slope) {
        double currSlope = p1.slopeTo(p2);
        return currSlope == slope
                || currSlope == Double.NEGATIVE_INFINITY;
    }

    private void addLineSegment(Point[] points) {
        Line newSegment = new Line(points);
        boolean segmentAdded = false;
        for (Line line : lines) {
            if (line.max.equals(newSegment.max) && line.min.equals(newSegment.min)) {
                segmentAdded = true;
                break;
            }
        }
        if (!segmentAdded) {
            Line[] newLines = Arrays.copyOf(lines, ++len);
            newLines[len-1] = newSegment;
            lines = newLines;
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return len;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] segments = new LineSegment[len];
        for (int i = 0; i < len; i++) {
            segments[i] = new LineSegment(lines[i].min, lines[i].max);
        }
        return segments;
    }

    private class Line {
        Point min;
        Point max;

        Line(Point min, Point max) {
            this.min = min;
            this.max = max;
        }

        Line(Point[] points) {
            Arrays.sort(points);
            this.min = points[0];
            this.max = points[points.length-1];
        }
    }
}