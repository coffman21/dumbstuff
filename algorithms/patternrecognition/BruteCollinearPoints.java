import java.util.Arrays;

public class BruteCollinearPoints {

    private LineSegment[] segs;
    private int len;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
            Point p = points[i];
            for (int j = 0; j < points.length; j++) {
                if (j != i && p.compareTo(points[j]) == 0) throw new IllegalArgumentException();
            }
        }
        this.segs = new LineSegment[10];
        this.len = 0;
        findSegments(points);
    }

    private void findSegments(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int p = k + 1; p < points.length; p++) {
                        double slopeIJ = points[i].slopeTo(points[j]);
                        double slopeJK = points[j].slopeTo(points[k]);
                        double slopeKP = points[k].slopeTo(points[p]);
                        if (slopeIJ == slopeJK && slopeJK == slopeKP) {
                            addLineSegment(points[i],
                                           points[j],
                                           points[k],
                                           points[p]);
                        }
                    }
                }
            }
        }
    }

    private void addLineSegment(Point i, Point j, Point k, Point p) {
        resize();
        Point[] points = { i, j, k, p };
        Arrays.sort(points);
        int minIdx = 0;
        int maxIdx = 3;
        segs[len++] = new LineSegment(points[minIdx], points[maxIdx]);
    }

    private void resize() {
        if (len == segs.length) segs = Arrays.copyOf(segs, len * 2);
    }

    // the number of line segments
    public int numberOfSegments() {
        return len;
    }

    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(segs, len);
    }
}
