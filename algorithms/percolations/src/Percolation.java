import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF uf;
    private boolean[][] sites;
    private int n;
    private int size;
    private int openSites;

    // create n-by-n grid, with all sites blocked
    // throws if n <= 0
    public Percolation(int n) throws IllegalArgumentException {
        if (n <= 0) throw new IllegalArgumentException("illegal n : " + n);

        this.n = n;
        this.size = n * n + 1;

        this.uf = new WeightedQuickUnionUF(this.size + 1);
        this.openSites = 0;

        this.sites = new boolean[n][];
        for (int i = 0; i < n; i++) {
            sites[i] = new boolean[n];
            for (int j = 0; j < n; j++) {
                sites[i][j] = false;
            }
        }
    }

    private int getPtr(int r, int c) {
        // if site does not exists
        if (r < 0 || c < 0 || r >= n || c >= n) return -1;

        int ptr = (n) * r + c + 1;

        if (ptr > size) throw new IndexOutOfBoundsException(
                "ptr : " + ptr + ", size : " + size);
        return ptr;
    }


    // open site (row, col) if it is not open already
    public void open(int row, int col) throws IllegalArgumentException {
        reallyOpen(row - 1, col - 1);
    }

    private void connectIfExists(int r1, int c1, int r2, int c2) {

        int ptr1 = getPtr(r1, c1);
        int ptr2 = getPtr(r2, c2);

        if (ptr1 == -1 || ptr2 == -1) {
            return;
        }
        if (r1 == 0) uf.union(0, ptr1);
        else if (r1 == n-1) uf.union(size, ptr1);
        if (sites[r2][c2]) uf.union(ptr1, ptr2);

    }

    private void reallyOpen(int r, int c) {
        if (!sites[r][c]) {
            sites[r][c] = true;
            openSites++;
        }

        connectIfExists(r, c, r-1, c);
        connectIfExists(r, c, r+1, c);
        connectIfExists(r, c, r, c-1);
        connectIfExists(r, c, r, c+1);
    }

    // is site (row, col) open?
    // row and column are between 1 and n
    public boolean isOpen(int row, int col) throws IllegalArgumentException {
        return isReallyOpen(row - 1, col - 1);
    }

    private boolean isReallyOpen(int r, int c) {
        return sites[r][c];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) throws IllegalArgumentException {
        return isReallyFull(row - 1, col - 1);
    }

    private boolean isReallyFull(int r, int c) {
        return this.uf.connected(getPtr(r, c), 0);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.connected(0, size);
    }

    // test client (optional)
    public static void main(String[] args) {
    }
}
