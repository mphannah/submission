import java.util.Scanner;

class Sudoku {
    private static final int EMPTY = -1;

    private final int dim;
    private final int sdim;
    private final int[][] cells;
    private int backtracking;

    public Sudoku (int[][] cells) {
        this.dim = cells.length;
        this.sdim = (int)Math.sqrt(dim);
        this.cells = cells;
        this.backtracking = 0;
    }

    int getBacktracking () { return backtracking; }

    public boolean isBlank (int row, int col) {
        return cells[row][col] == -1;// TODO (from lab)
    }

    public boolean isValid (int val, int row, int col) {
        return isBlank(row, col) &&
                checkRow(val, row) &&
                checkCol(val, col) &&
                checkBlock(val, row, col); // TODO (from lab)
    }

    private boolean checkRow (int val, int row) {
        for (int i = 0; i < 9; i++) {
            if (cells[row][i] == val) {
                return false;
            }
        }
        return true; // TODO (from lab)
    }

    private boolean checkCol (int val, int col) {
        for (int i = 0; i < 9; i++) {
            if (cells[i][col] == val) {
                return false;
            }
        }
        return true; // TODO (from lab)
    }

    private boolean checkBlock (int val, int row, int col) {
        int startRow = row - row % 3;
        int startCol = col - col % 3;

        for (int i = startRow; i <= startRow+2; i ++) {
            for (int x = startCol; x <= startCol+2; x++) {
                if (cells[i][x] == val) {
                    return false;
                }
            }
        }
        return true; // TODO (from lab)
    }

    public boolean solve () {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (isBlank(row, col)) {
                    for (int guess = 1; guess <= 9; guess++) {
                        if (isValid(guess, row, col)) {
                            cells[row][col] = guess;

                            if (solve()) {
                                return true;
                            } else {
                                cells[row][col] = -1;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true; // TODO
    }

    private boolean tryColumn (int col) {
        return false; // TODO
    }

    private boolean tryCell (int col, int row) {
        return false; // TODO
    }
    
    public String toString () {
        StringBuilder res = new StringBuilder();
        for (int j=0; j<dim; j++) {
            if (j % sdim == 0) res.append("――――――――――――――――――――――\n");
            for (int i=0; i<dim; i++) {
                if (i % sdim == 0) res.append("│");
                int c = cells[i][j];
                res.append(c == EMPTY ? "." : c);
                res.append(" ");
            }
            res.append("│\n");
        }
        res.append("――――――――――――――――――――――\n");
        return res.toString();
    }

    //------------------------------------------------------------

    public static Sudoku read (Scanner s) {
        int dim = s.nextInt();
        int[][] cells = new int[dim][dim];
        for (int j = 0; j < dim; j++)
            for (int i = 0; i < dim; i++) {
                String c = s.next();
                cells[i][j] = c.equals(".") ? EMPTY : Integer.parseInt(c);
            }
        return new Sudoku(cells);
    }
}
