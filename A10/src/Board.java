import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Board {
    private final Tile[][] tiles;
    private final int boardSize;
    private final WordCollection dict;

    public Board(Tile[][] tiles, WordCollection dict) {
        this.tiles = tiles;
        this.boardSize = tiles.length;
        this.dict = dict;
    }

    public int getBoardSize() { return boardSize; }

    public Tile get(int row, int col) {
        return tiles[row][col];
    }

    /**
     * A position in the board may have up to 8 neighbors, up, down, left, right,
     * and both diagonals. This method returns the neighbors that are not marked
     * as visited, i.e, the ones whose method isFresh returns true.
     */
    public List<Tile> getFreshNeighbors(int r, int c) {
        List<Tile> neighbors = new ArrayList<>();

        // up
        if (r-1 >= 0) {
            if (tiles[r-1][c].isFresh()) {
                neighbors.add(tiles[r-1][c]);
            }
        }

        // down
        if (r+1 < tiles.length) {
            if (tiles[r+1][c].isFresh()) {
                neighbors.add(tiles[r+1][c]);
            }
        }

        // left
        if (c-1 >= 0) {
            if (tiles[r][c-1].isFresh()) {
                neighbors.add(tiles[r][c-1]);
            }
        }

        // right
        if (c+1 < tiles.length) {
            if (tiles[r][c+1].isFresh()) {
                neighbors.add(tiles[r][c+1]);
            }
        }

        // diagonal (\)
        if (r-1 >= 0 && c-1 >= 0) {
            if (tiles[r-1][c-1].isFresh()) {
                neighbors.add(tiles[r-1][c-1]);
            }
        } if (r+1 < tiles.length && c+1 < tiles.length) {
            if (tiles[r+1][c+1].isFresh()) {
                neighbors.add(tiles[r+1][c+1]);
            }
        }

        // diagonal (/)
        if (r-1 >= 0 && c+1 < tiles.length) {
            if (tiles[r-1][c+1].isFresh()) {
                neighbors.add(tiles[r-1][c+1]);
            }
        } if (r+1 < tiles.length && c-1 >= 0) {
            if (tiles[r+1][c-1].isFresh()) {
                neighbors.add(tiles[r+1][c-1]);
            }
        }

        return neighbors; // TODO
    }

    /**
     * This method is the heart of the game.
     *
     * It is given a current string collected so far and attempts to look for
     * words that start with that string by taking steps from the current position.
     * For example, we have the board:
     *
     *     V A L
     *     E N T
     *     I N E
     *
     * We are currently positioned at the bottom-right corner "E" and we have "T"
     * as a current prefix.
     *   - the first thing is we mark the tile "E" as visited so that when we
     *     start searching from a neighbor (e.g. "N") we don't visit it again
     *   - we extend the current prefix with the current letter, so now we have
     *     "TE" as our prefix.
     *   - If "TE" were a full word, we would add it to our result we are
     *     collecting
     *   - If "TE" were not a valid prefix for any word, then we clear the tile
     *     and return
     *   - If "TE" is a valid prefix, then we recursively visit all fresh
     *     neighbors
     *
     */
    public HashSet<String> findWordsFromPos(int r, int c, String s) {
        tiles[r][c].setVisited();
        s = s + tiles[r][c].c;
        HashSet<String> set = new HashSet<>();

        if (!dict.possiblePrefix(s)) {
            tiles[r][c].reset();
            return set;
        } if (dict.contains(s)) {
            set.add(s);
        }

        List<Tile> neighbors = getFreshNeighbors(r, c);

        for (Tile t : neighbors) {
            set.addAll(findWordsFromPos(t.row, t.col, s));
        }

        tiles[r][c].reset();
        return set; // TODO
    }

    public HashSet<String> findWords() {
        HashSet<String> result = new HashSet<>();

        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                result.addAll(findWordsFromPos(r, c, ""));
            }
        }

        return result;
    }

    public void paint(Graphics2D g2, int offset, Dimension dim) {
        Rectangle2D.Double tileBox;
        int tileSize = dim.width / boardSize;

        FontMetrics fm = g2.getFontMetrics();
        int scaleFactor = dim.width / (boardSize * 10 * fm.stringWidth("J"));

        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                tileBox = new Rectangle2D.Double(
                        offset + c * tileSize,
                        offset + r * tileSize,
                        tileSize,
                        tileSize);
                tiles[r][c].paint(g2, tileBox, scaleFactor);
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                sb.append(get(r, c));
                sb.append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public static class BoardPanel extends JPanel {
        private final Board board;

        public BoardPanel(Board board) {
            this.board = board;
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            Dimension dim = getSize();
            dim.setSize(0.8 * dim.getWidth(), 0.8 * dim.getWidth());
            int offset = (int) dim.getWidth() / 10;
            board.paint(g2, offset, dim);
        }

        public Dimension getPreferredSize() {
            int tileSize = 60;
            int boardSize = board.getBoardSize();
            int boardDim = Math.min(tileSize * boardSize, 1200 / boardSize * boardSize);
            return new Dimension(boardDim + 10, boardDim + 10);
        }
    }

    public static class Tile {
        private final char c;
        private final int row, col;
        private boolean visited;

        Tile(char c, int row, int col) {
            this.c = c;
            this.row = row;
            this.col = col;
            reset();
        }

        Tile(int row, int col) {
            Random r = new Random();
            this.c = (char) (r.nextInt(26) + 'a');
            this.row = row;
            this.col = col;
            reset();
        }

        void reset() {
            visited = false;
        }

        boolean isFresh() {
            return !visited;
        }

        void setVisited() {
            this.visited = true;
        }

        int getRow() {
            return row;
        }

        int getCol() {
            return col;
        }

        public String toString() {
            return String.valueOf(Character.toUpperCase(c));
        }

        public void paint(Graphics2D g2, Rectangle2D.Double r, int scaleFactor) {
            g2.draw(r);
            String s = toString();

            Font font = g2.getFont().deriveFont(16f);
            AffineTransform at = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);
            g2.setFont(font.deriveFont(at));

            FontMetrics fm = g2.getFontMetrics();
            double posx = r.x + ((r.width - fm.stringWidth(s)) / 2);
            double posy = r.y + (((r.height - fm.getHeight()) / 2) + fm.getAscent());
            g2.drawString(s, (int) posx, (int) posy);
        }

        public static class TilePanel extends JPanel {
            private final Tile tile;
            private final int sizePixels;

            public TilePanel(Tile tile, int sizePixels) {
                this.tile = tile;
                this.sizePixels = sizePixels;
            }

            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                Rectangle2D.Double box = new Rectangle2D.Double(50, 50, sizePixels, sizePixels);
                tile.paint(g2, box, 3);
            }

            public Dimension getPreferredSize() {
                int size = sizePixels * 4;
                return new Dimension(size, size);
            }
        }
    }
}
