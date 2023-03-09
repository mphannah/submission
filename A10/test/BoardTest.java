import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

class BoardTest {
    @Test
    void tile () throws InterruptedException {
        Board.Tile tile = new Board.Tile('k', 0, 0);
        Board.Tile.TilePanel panel = new Board.Tile.TilePanel(tile,50);

        JFrame jf = new JFrame("Board.Tile");
        jf.add(panel);
        jf.pack();
        jf.setVisible(true);
        Thread.sleep(4000);
    }

    @Test
    void slow () throws IOException, InterruptedException {
        List<String> words = Files.readAllLines(Paths.get("commonwords.txt"));

        int size = 10;
        Board.Tile[][] tiles = new Board.Tile[size][size];
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                tiles[r][c] = new Board.Tile(r, c);
        Board board = new Board(tiles, new WordList(words));

        JFrame jf = new JFrame("Board");
        JScrollPane panel = new JScrollPane(new Board.BoardPanel(board));
        jf.setLayout(new BorderLayout());
        jf.add(BorderLayout.CENTER, panel);
        jf.pack();
        jf.setVisible(true);

        long t0 = System.currentTimeMillis();
        int i = 0;
        for (String w : board.findWords()) {
            i++;
            System.out.println(w);
        }
        long t1 = System.currentTimeMillis();
        System.out.printf("Found %d words in %d ms!%n", i, t1 - t0);
        Thread.sleep(4000);
    }

    @Test
    void fast () throws IOException, InterruptedException {
        Scanner scanner = new Scanner(Paths.get("commonwords.txt"));
        Trie trie = new Trie();
        while (scanner.hasNext()) trie.insert(scanner.next());

        int size = 10;
        Board.Tile[][] tiles = new Board.Tile[size][size];
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                tiles[r][c] = new Board.Tile(r, c);

        Board board = new Board(tiles, trie);

        JFrame jf = new JFrame("Board");
        JScrollPane panel = new JScrollPane(new Board.BoardPanel(board));
        jf.setLayout(new BorderLayout());
        jf.add(BorderLayout.CENTER, panel);
        jf.pack();
        jf.setVisible(true);

        long t0 = System.currentTimeMillis();
        int i = 0;
        for (String w : board.findWords()) {
            i++;
            System.out.println(w);
        }
        long t1 = System.currentTimeMillis();
        System.out.printf("Found %d words in %d ms!%n", i, t1 - t0);
        Thread.sleep(4000);
    }

    @Test
    void compare () throws IOException, InterruptedException {
        List<String> words = Files.readAllLines(Paths.get("commonwords.txt"));
        Scanner scanner = new Scanner(Paths.get("commonwords.txt"));
        Trie trie = new Trie();
        while (scanner.hasNext()) trie.insert(scanner.next());

        int size = 3;
        String s = "valentine";
        Board.Tile[][] tiles = new Board.Tile[size][size];
        for (int r=0; r<size; r++)
            for (int c=0; c<size; c++)
                tiles[r][c] = new Board.Tile(s.charAt(r*3+c),r,c);
        Board board1 = new Board(tiles,new WordList(words));
        Board board2 = new Board(tiles,trie);

        JFrame jf = new JFrame("Board");
        JScrollPane panel = new JScrollPane(new Board.BoardPanel(board2));
        jf.setLayout(new BorderLayout());
        jf.add(BorderLayout.CENTER, panel);
        jf.pack();
        jf.setVisible(true);

        long t0 = System.currentTimeMillis();
        int i = 0;
        for (String w : board1.findWords()) {
            i++;
            System.out.println(w);
        }
        long t1 = System.currentTimeMillis();
        System.out.printf("slow --- Found %d words in %d ms!%n", i, t1-t0);

        t0 = System.currentTimeMillis();
        i = 0;
        for (String w : board2.findWords()) {
            i++;
            System.out.println(w);
        }
        t1 = System.currentTimeMillis();
        System.out.printf("fast --- Found %d words in %d ms!%n", i, t1-t0);
        Thread.sleep(4000);
    }

    @Test
    public void myTests () throws IOException, InterruptedException {
        Board.Tile[][] tiles =
                {{new Board.Tile('a', 0, 0), new Board.Tile('l', 0, 1), new Board.Tile('e', 0, 2), new Board.Tile('b', 0, 3)},
                        {new Board.Tile('f', 1, 0), new Board.Tile('t', 1, 1), new Board.Tile('u', 1, 2), new Board.Tile('e', 1, 3)},
                        {new Board.Tile('r', 2, 0), new Board.Tile('y', 2, 1), new Board.Tile('o', 2, 2), new Board.Tile('c', 2, 3)},
                        {new Board.Tile('a', 3, 1), new Board.Tile('m', 3, 1), new Board.Tile('d', 3, 2), new Board.Tile('r', 3, 3)}};

        List<String> words = Files.readAllLines(Paths.get("commonwords.txt"));
        Scanner scanner = new Scanner(Paths.get("commonwords.txt"));
        Trie trie = new Trie();
        while (scanner.hasNext()) trie.insert(scanner.next());

        Board board = new Board(tiles, new WordList(words));
        Board board1 = new Board(tiles, trie);

        List<Board.Tile> neighbors = board.getFreshNeighbors(2, 2);
        System.out.println(neighbors + "\n");
//    _______________________________________________________________________________________________

        HashSet<String> wordSet = board.findWordsFromPos(0,0,"");
        System.out.println(wordSet);
        wordSet = board.findWordsFromPos(1,1,"");
        System.out.println(wordSet);
        wordSet = board.findWordsFromPos(2,2,"");
        System.out.println(wordSet);
        wordSet = board.findWordsFromPos(3,3,"");
        System.out.println(wordSet + "\n");
//    _______________________________________________________________________________________________

        HashSet<String> wordSet1 = board1.findWordsFromPos(0,0,"");
        System.out.println(wordSet1);
        wordSet1 = board1.findWordsFromPos(1,1,"");
        System.out.println(wordSet1);
        wordSet1 = board1.findWordsFromPos(2,2,"");
        System.out.println(wordSet1);
        wordSet1 = board1.findWordsFromPos(3,3,"");
        System.out.println(wordSet1);
    }
}