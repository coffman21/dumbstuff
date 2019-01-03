import java.util.Arrays;

public class ThreeWayPartition {

    int[] arr;
    int mid;

    public ThreeWayPartition(int[] arr, int mid) {
        this.arr = arr;
        this.mid = mid;
    }

    void sort() {
        int n = 0;
        int m = arr.length - 1;

        int i = n;
        while (i <= m) {
            if (arr[i] < mid) {
                swap(i, n);
                n++;
                i++;
            }
            else if (arr[i] > mid) {
                swap(i, m);
                m--;
            } else i++;
            System.out.println(Arrays.toString(arr));
        }
    }

    void swap (int idx1, int idx2) {
        int tmp = arr[idx1];
        arr[idx1] = arr[idx2];
        arr[idx2] = tmp;
    }

    public static void main(String[] args) {
        ThreeWayPartition twp = new ThreeWayPartition(
                new int[] { 1, 3, 2, 1, 1, 2, 3, 2, 1, 2, 1 }, 2);
        twp.sort();
    }
}
