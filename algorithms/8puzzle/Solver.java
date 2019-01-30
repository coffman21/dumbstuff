import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author kkharitonov
 * @date 24.01.2019
 */
public class Solver {

    private Node goal;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        MinPQ<Node> queue = new MinPQ<>();
        MinPQ<Node> twinQueue = new MinPQ<>();
        Board current = initial;
        Board twinCurrent = initial.twin();

        queue.insert(new Node(0, current.manhattan(), current, null));
        twinQueue.insert(new Node(0, current.manhattan(), twinCurrent, null));

        while (true) {

            Node currentNode = queue.min();
            Node twinCurrentNode = twinQueue.min();
            queue.delMin();
            twinQueue.delMin();

            Node predecessor = currentNode.parentNode;
            Node twinPredecessor = twinCurrentNode.parentNode;

            current = currentNode.board;
            twinCurrent = twinCurrentNode.board;

            if (current.isGoal() || twinCurrent.isGoal()) {
                if (current.isGoal()) {
                    goal = currentNode;
                }
                break;
            }

            int moves = currentNode.moves;
            int twinMoves = twinCurrentNode.moves;

            for (Board board : current.neighbors()) {
                if (predecessor == null || !board.equals(predecessor.board)) {
                    queue.insert(new Node(moves+1, board.manhattan(), board, currentNode));
                }
            }
            for (Board board : twinCurrent.neighbors()) {
                if (twinPredecessor == null || !board.equals(twinPredecessor.board)) {
                    twinQueue.insert(new Node(twinMoves+1, board.manhattan(), board, twinCurrentNode));
                }
            }
        }

    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return goal != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (goal == null) return -1;
        return goal.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        List<Board> solution = new LinkedList<>();

        Node step = goal;
        while (step != null) {
            solution.add(step.board);
            step = step.parentNode;
        }
        Collections.reverse(solution);
        return solution;
    }

    private class Node implements Comparable<Node> {

        private final int moves;
        private final int manhattan;
        private final Board board;
        private final Node parentNode;

        Node(int moves, int manhattan, Board board, Node parentNode) {
            this.moves = moves;
            this.manhattan = manhattan;
            this.board = board;
            this.parentNode = parentNode;
        }

        @Override
        public int compareTo(Node that) {
            int thisPriority = this.moves + this.manhattan;
            int thatPriority = that.moves + that.manhattan;
            return Integer.compare(thisPriority, thatPriority);
        }
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {

//        Board initial = new Board(new int[][]{{2, 3, 1}, {4, 0, 5}, {7, 8, 6}});

        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
