import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.StreamSupport;

/**
 * @author kkharitonov
 * @date 24.01.2019
 */
public class Solver {

    private int moves = 0;
    private Node goal;


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

                
        MinPQ<Node> queue = new MinPQ<>();

        Node predecessor = null;
        Board current = initial;

        queue.insert(new Node(initial.manhattan(), initial, predecessor));
        Node currentNode = null;

        while (!current.isGoal()) {
            moves++;
            queue.delMin();

            for (Board board : current.neighbors()) {
                if (predecessor == null || !board.equals(predecessor.board)) {
                    queue.insert(new Node(moves + board.manhattan(), board, predecessor));
                }
            }

            predecessor = new Node(moves - 1 + current.manhattan(), current, predecessor);
            currentNode = queue.min();
            current = currentNode.board;

        }
        goal = currentNode;
    }


    // is the initial board solvable?
    public boolean isSolvable() {
        return moves != -1;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        List<Board> solution = new LinkedList<>();

        Node step = goal;
        while (step != null) {
            solution.add(step.board);
            step = step.predecessor;
        }
        Collections.reverse(solution);
        return solution;
    }

    private class Node implements Comparable<Node> {

        private int move;
        private Board board;
        private Node predecessor;

        private Node(int move, Board board, Node predecessor) {
            this.move = move;
            this.board = board;
            this.predecessor = predecessor;
        }

        @Override
        public int compareTo(Node that) {
            int thisPriority = this.move + this.board.manhattan();
            int thatPriority = that.move + that.board.manhattan();
            return Integer.compare(thisPriority, thatPriority);
        }
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
//        In in = new In(args[0]);
//        int n = in.readInt();
//        int[][] blocks = new int[n][n];
//        for (int i = 0; i < n; i++)
//            for (int j = 0; j < n; j++)
//                blocks[i][j] = in.readInt();
//        Board initial = new Board(blocks);

        Board initial = new Board(new int[][]{{0, 1, 3}, {4, 2, 5}, {7, 8, 6}});
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