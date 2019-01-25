import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author kkharitonov
 * @date 24.01.2019
 */
public class Board {

    private int[][] blocks;
    private int dimension;
    private int manhattanDistance;

    // construct a board from an n-by-n array of blocks (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        this.blocks = blocks;
        this.dimension = blocks.length;
        this.manhattanDistance = findManhattanDistance();
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of blocks out of place
    public int hamming() {
        int sum = 0;
        int inPlaceBlock = 1;
        for (int[] blockLine : blocks) {
            for (int block : blockLine) {
                if (block != inPlaceBlock && block != 0) {
                    sum++;
                }
                inPlaceBlock++;
            }
        }
        return sum;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return manhattanDistance;
    }

    private int findManhattanDistance() {
        int sum = 0;
        int inPlaceBlock = 1;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int block = blocks[i][j];
                if (block != 0 && block != inPlaceBlock) {
                    int expectedXDistance = (block - 1) / dimension;
                    int expectedYDistance = (block - 1) % dimension;
                    sum += Math.abs(expectedXDistance - i) + Math.abs(expectedYDistance - j);
                }
                inPlaceBlock++;
            }
        }
        return sum;
    }

    // is this board the goal board?
    public boolean isGoal() {
        int inPlaceBlock = 1;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (blocks[i][j] != 0 && blocks[i][j] != inPlaceBlock) return false;
                inPlaceBlock++;
            }
        }
        return true;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        if (blocks[0][0] != 0 && blocks[0][1] != 0) return createNeighbourBoard(0, 0, Direction.RIGHT);
        else return createNeighbourBoard(1, 0, Direction.RIGHT);
    }

    // does this board equal y?
    public boolean equals(Board that) {
        if (this.dimension != that.dimension) return false;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (blocks[i][j] != that.blocks[i][j]) return false;
            }
        }
        return true;
    }

    private enum Direction {
        LEFT, RIGHT, UP, DOWN
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> boards = new ArrayList<>();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (blocks[i][j] == 0) {
                    if (i != 0) boards.add(createNeighbourBoard(i, j, Direction.LEFT));
                    if (i != dimension-1) boards.add(createNeighbourBoard(i, j, Direction.RIGHT));
                    if (j != 0) boards.add(createNeighbourBoard(i, j, Direction.UP));
                    if (j != dimension-1) boards.add(createNeighbourBoard(i, j, Direction.DOWN));
                }
            }
        }
        return boards;
    }

    private Board createNeighbourBoard(int i, int j, Direction d) {
        int[][] newBlocks = Arrays.stream(blocks)
                .map(int[]::clone)
                .toArray(int[][]::new);
        int tmp = newBlocks[i][j];
        switch (d) {
            case LEFT:
                newBlocks[i][j] = newBlocks[i][j-1];
                newBlocks[i][j-1] = tmp;
                break;
            case RIGHT:
                newBlocks[i][j] = newBlocks[i][j+1];
                newBlocks[i][j+1] = tmp;
                break;
            case UP:
                newBlocks[i][j] = newBlocks[i-1][j];
                newBlocks[i-1][j] = tmp;
                break;
            case DOWN:
                newBlocks[i][j] = newBlocks[i+1][j];
                newBlocks[i+1][j] = tmp;
                break;
        }
        return new Board(newBlocks);
    }

    // string representation of this board (in the output format specified below)
    //
    //3
    // 0  1  3
    // 4  2  5
    // 7  8  6

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dimension).append("\n");
        Arrays.stream(blocks)
                .forEach(blockLine -> {
                    Arrays.stream(blockLine)
                            .forEach(b -> sb
                                    .append(" ")
                                    .append(b));
                    sb.append("\n");
                });
        return sb.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {
        Board solved = new Board(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 0}});
        Board initial = new Board(new int[][]{{8, 1, 3}, {4, 0, 2}, {7, 6, 5}});

        System.out.println(solved.toString());

        System.out.println("Solved dimensions: " + solved.dimension());

        System.out.println("Solved hamming: " + solved.hamming());
        System.out.println("Initial hamming: " + initial.hamming());

        System.out.println("Solved manhattan: " + solved.manhattan());
        System.out.println("Initial manhattan: " + initial.manhattan());

        System.out.println("Solved neigbours: ");
        solved.neighbors().forEach(System.out::println);
        System.out.println("Initial neigbours: ");
        initial.neighbors().forEach(System.out::println);

        System.out.println("Solved is goal: " + solved.isGoal());
        System.out.println("Initial is goal: " + initial.isGoal());

        System.out.println("Solved twin:");
        System.out.println(solved.twin());
        System.out.println("Initial twin:");
        System.out.println(initial.twin());
    }
}
