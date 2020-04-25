import java.util.Arrays;

class SnailMatrix {
    public static void main(String[] args) {
        printMatrix(snail(0));
        printMatrix(snail(1));
        printMatrix(snail(2));
        printMatrix(snail(3));
        printMatrix(snail(4));
        printMatrix(snail(5));
        printMatrix(snail(-1));
    }

    static int[][] snail(int n) {
        if (n < 0) throw new IllegalArgumentException();

        int[][] res = new int[n][n];
        int value = 0;
        int n1 = n - 1;
        for (int round = 0; round <= n/2; round++) {
            if (round == n/2 && n % 2 == 1) {
                res[round][round] = value + 1;
            }
            for (int col = 0; col < n1 - 2*round; col++) {
                value++;

                res[round][col + round] = value;
                res[col + round][n1 - round] = value + n1 - 2*round;
                res[n1 - round][n1 - col - round] = value + 2*(n1 - 2*round);
                res[n1 - col - round][round] = value + 3*(n1 - 2*round);
            }
            value = (n*n - (n - (round+1)*2)*(n - (round+1)*2));
        }
        return res;
    }

    static void printMatrix(int[][] res) {
        Arrays.stream(res).forEach(ints -> {
            Arrays.stream(ints).forEach(i -> System.out.print(i + "\t"));
            System.out.println();
        });
        System.out.println();
    }
}

