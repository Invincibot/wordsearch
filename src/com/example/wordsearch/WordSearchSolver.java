package com.example.wordsearch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class WordSearchSolver {
    boolean solvable;
    int width;
    int height;
    char[][] wordsearch;
    SolvedWord[] words;

    public void ReadFromFile() {
        try {
            File file = new File("wordsearch.txt");
            Scanner scanner = new Scanner(file);
            // Necessary swap due to elements being printed to match human 'width' and 'height'
            this.height = scanner.nextInt();
            this.width = scanner.nextInt();
            this.words = new SolvedWord[scanner.nextInt()];
            this.wordsearch = new char[this.width][this.height];
            scanner.nextLine();
            for (int i = 0; i < this.width; i++) {
                this.wordsearch[i] = scanner.nextLine().replaceAll(" ", "").toCharArray();
            }
            for (int i = 0; i < this.words.length; i++) {
                this.words[i] = new SolvedWord();
                this.words[i].wordChar = scanner.next().toCharArray();
                this.words[i].length = this.words[i].wordChar.length;
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void WriteToFile() {
        try {
            FileWriter file = new FileWriter("solution.txt");

            if (this.solvable) {
                for (SolvedWord word : this.words) {
                    file.write(new String(word.wordChar) +
                            " Start (" + word.start.x + ", " + word.start.y +
                            ") End (" + (word.start.x + word.direction.x * word.length) +
                            ", " + (word.start.y + word.direction.y * word.length) + ")\n");
                }
            } else
                file.write("Word search cannot be solved.");

            file.close();

            System.out.println("Successfully wrote to file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void Solve(WordSearch ws) {
        this.width = ws.width;
        this.height = ws.height;
        this.wordsearch = ws.wordsearch;
        this.words = new SolvedWord[ws.words.length];
        for (int i = 0; i < ws.words.length; i++) {
            this.words[i] = new SolvedWord();
            this.words[i].wordChar = ws.words[i].toCharArray();
            this.words[i].length = this.words[i].wordChar.length;
        }

        this.Solve();
    }

    void Solve() {}

    void CheckWordAtPosition(SolvedWord word, int x, int y) {
        word.start.SetCoordinates(x, y);

        boolean[] yDirs = {true, true, true};
        for (int i = -1; i <= 1; i++) {
            if (i == -1 && x < word.length - 1 || i == 1 && x > this.width - word.length)
                continue;

            for (int j = 0; j <= 2; j++) {
                if (!yDirs[j] || i == 0 && j == 1)
                    continue;

                else if (j == 0 && y < word.length - 1 || j == 2 && y > this.height - word.length) {
                    yDirs[j] = false;
                    continue;
                }

                word.direction.SetCoordinates(i, j - 1);
                if (CheckWordToLetters(word)) {
                    word.solved = true;
                    return;
                }
            }
        }
    }

    boolean CheckWordToLetters(SolvedWord word) {
        for (int i = 0; i < word.length; i++) {
            if (word.wordChar[i] != wordsearch[word.start.x + word.direction.x * i][word.start.y + word.direction.y * i])
                return false;
        }
        return true;
    }
}

class WordSearchSolverAless extends WordSearchSolver {
    public static void main(String[] args) {
        WordSearchSolverAless cs = new WordSearchSolverAless();
        cs.ReadFromFile();
        cs.Solve();
        cs.WriteToFile();
    }

    public void Solve() {
        this.solvable = true;

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                for (SolvedWord word : this.words) {
                    if (word.solved)
                        continue;

                    CheckWordAtPosition(word, i, j);
                }
            }
        }

        for (SolvedWord word : this.words) {
            if (!word.solved) {
                this.solvable = false;
                return;
            }
        }
    }
}

class WordSearchSolverLuigi extends WordSearchSolver {
    public static void main(String[] args) {
        WordSearchSolverLuigi cs = new WordSearchSolverLuigi();
        cs.ReadFromFile();
        cs.Solve();
        cs.WriteToFile();
    }

    public void Solve() {
        this.solvable = true;

        outer:
        for (SolvedWord word : this.words) {
            for (int i = 0; i < this.width; i++) {
                for(int j = 0; j < this.height; j++) {
                    CheckWordAtPosition(word, i, j);
                    if (word.solved)
                        continue outer;
                }
            }

            this.solvable = false;
            return;
        }
    }
}

class SolvedWord extends Word {
    boolean solved;
    char[] wordChar;
}