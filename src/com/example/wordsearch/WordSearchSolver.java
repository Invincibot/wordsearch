package com.example.wordsearch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class WordSearchSolver {
    boolean solvable;
    int width;
    int height;
    char[][] wordsearch;
    SolvedWord[] words;
    ArrayList<SolvedWord> wordsToBeSolved;

    public void ReadFromFile() {
        try {
            File file = new File("wordsearch.txt");
            Scanner scanner = new Scanner(file);
            this.width = scanner.nextInt();
            this.height = scanner.nextInt();
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
            this.wordsToBeSolved = new ArrayList<>(Arrays.asList(this.words));
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

    public void Solve(int width, int height, char[][] wordsearch, String[] words) {
        this.width = width;
        this.height = height;
        this.wordsearch = wordsearch;
        this.words = new SolvedWord[words.length];
        for (int i = 0; i < words.length; i++) {
            this.words[i] = new SolvedWord();
            this.words[i].wordChar = words[i].toCharArray();
            this.words[i].length = this.words[i].wordChar.length;
        }

        this.Solve();
    }

    public void Solve() {}
}

class WordSearchSolverAless extends WordSearchSolver {
    public static void main(String[] args) {
        WordSearchSolverAless cs = new WordSearchSolverAless();
        cs.ReadFromFile();
        cs.Solve();
        cs.WriteToFile();
    }

    public void Solve() {
        outer:
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                if (wordsToBeSolved.size() == 0)
                    break outer;

                for (SolvedWord word : this.wordsToBeSolved) {
                    CheckWordAtPosition(word, i, j);
                }
            }

            for (SolvedWord word : this.words) {
                if (word.solved)
                    this.wordsToBeSolved.remove(word);
            }
        }

        if (wordsToBeSolved.size() != 0)
            this.solvable = false;
    }

    void CheckWordAtPosition(SolvedWord word, int x, int y) {
        word.start.SetCoordinates(x, y);

        boolean[] yDirs = {true, true, true};
        for (int i = -1; i <= 1; i++) {
            if (i == -1 && x <= word.length || i == 1 && x > this.width - word.length)
                continue;

            for (int j = 0; j <= 2; j++) {
                if (!yDirs[j] || i == 0 && j == 1)
                    continue;

                else if (j == 0 && y <= word.length || j == 2 && y > this.height - word.length) {
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

class SolvedWord extends Word {
    boolean solved;
    char[] wordChar;

    SolvedWord() {
        this.solved = false;
    }
}