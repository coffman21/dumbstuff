import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double mean;
    private double stddev;
    private double confLow;
    private double confHigh;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        perform(n, trials);
    }

    private void perform(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
        double[] thresholds = new double[trials];

        for (int i = 0; i < trials; i++) {
            double threshold = experiment(n);
            thresholds[i] = threshold;
        }

        mean = StdStats.mean(thresholds);
        stddev = StdStats.stddev(thresholds);

        confLow = mean - ((1.96 * Math.sqrt(stddev)) / Math.sqrt(trials));
        confHigh = mean + ((1.96 * Math.sqrt(stddev)) / Math.sqrt(trials));

    }

    private double experiment(int n) {
        Percolation percolation = new Percolation(n);
        double counter = 0;
        do {
            int c = StdRandom.uniform(1, n+1);
            int d = StdRandom.uniform(1, n+1);
            if (!percolation.isOpen(c, d)) {
                percolation.open(c, d);
                counter++;
            }
        } while (!percolation.percolates());
        double divisor = n * n;
        return counter / divisor;
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return confLow;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confHigh;
    }

    // test client (described below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats percolationStats = new PercolationStats(n, trials);

        System.out.println("mean: " + percolationStats.mean());
        System.out.println("stddev: " + percolationStats.stddev());
        System.out.println("lo: " + percolationStats.confidenceLo());
        System.out.println("hi: " + percolationStats.confidenceHi());
    }

}
