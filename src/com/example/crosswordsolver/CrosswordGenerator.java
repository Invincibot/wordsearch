package com.example.crosswordsolver;

import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import java.io.FileWriter;
import java.lang.StringBuilder;

public class CrosswordGenerator {
    public static void main(String[] args) {
        Crossword crossword = new Crossword(15, 15);
        crossword.GenerateCrossword(5, 10);

        try {
            FileWriter file = new FileWriter("crossword.txt");
            file.write("10 10\n");
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    file.write(crossword.crossword[i][j] + " ");
                }
                file.write("\n");
            }

            for (Word word : crossword.words) {
                file.write(word.word + "\n");
            }

            file.close();

            System.out.println("Successfully wrote to file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

class Crossword {
    int width;
    int height;
    char[][] crossword;
    ArrayList<Word> words;
    int minWordLen;

    Crossword(int w, int h) {
        this.width = w;
        this.height = h;
        this.crossword = new char[w][h];
        this.words = new ArrayList<>();
        this.minWordLen = 0;
    }

    public void GenerateCrossword(int numWords, int minLen) {
        this.minWordLen = minLen;

        Random random = new Random();
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                this.crossword[i][j] = (char)('a' + random.nextInt(26));
            }
        }

        for (int i = 0; i < numWords; i++) {
            GenerateWord();
        }
    }

    private void GenerateWord() {
        Word word = new Word();
        Random random = new Random();
        outer:
        while (true) {
            word.start.x = random.nextInt(this.width);
            word.start.y = random.nextInt(this.height);
            word.direction.x = random.nextInt(3) - 1;
            word.direction.y = random.nextInt(3) - 1;
            if (
                    (word.start.x < minWordLen - 1 && word.direction.x < 0) ||
                    (word.start.x > this.width - minWordLen && word.direction.x > 0) ||
                    (word.start.y < minWordLen - 1 && word.direction.y < 0) ||
                    (word.start.y > this.height - minWordLen && word.direction.y > 0) ||
                    (word.direction.x == 0 && word.direction.y == 0)
            ) {
                System.out.println("continuing...");
                continue;
            }

            System.out.println("Not continuing...");

            System.out.println("Word starts at " + word.start.x + ", " + word.start.y);
            System.out.println("Word travels in the direction of " + word.direction.x + ", " + word.direction.y);

            int maxx = this.width, maxy = this.height;
            if (word.direction.x == 1)
                maxx = this.width - word.start.x + 1;
            else if (word.direction.x == -1)
                maxx = word.start.x + 1;

            if (word.direction.y == 1)
                maxy = this.height - word.start.y + 1;
            else if (word.direction.y == -1)
                maxy = word.start.y + 1;

            int maxLen = Math.min(maxx, maxy);
            word.length = random.nextInt(maxLen - this.minWordLen + 1) + this.minWordLen;


            System.out.println("Word has length of " + word.length);

            for (Word other : this.words) {
                if (word.CollidesWith(other)) {
                    continue outer;
                }
            }
            break;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < word.length; i++) {
            sb.append(this.crossword[word.start.x + word.direction.x * i][word.start.y + word.direction.y * i]);
        }
        this.words.add(word);
    }
}

class Word {
    Coordinate start;
    Coordinate direction;
    int length;
    String word;

    Word() {
        start = new Coordinate(0, 0);
        direction = new Coordinate(0, 0);
        length = 0;
        word = "";
    }

    public boolean CollidesWith(Word other) {
        Coordinate[] coords1 = this.GenerateCoordinates();
        Coordinate[] coords2 = other.GenerateCoordinates();
        boolean collided = false;

        outer:
        for (Coordinate coord1 : coords1) {
            for (Coordinate coord2 : coords2) {
                if (coord1.Equals(coord2)) {
                    if (!collided) {
                        collided = true;
                        continue outer;
                    } else {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public Coordinate[] GenerateCoordinates() {
        Coordinate[] coordinates = new Coordinate[this.length];
        for (int i = 0; i < this.length; i++) {
            coordinates[i] = new Coordinate(this.start.x + this.direction.x * i, this.start.y + this.direction.y * i);
        }

        return coordinates;
    }
}

class Coordinate {
    int x;
    int y;

    Coordinate(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public boolean Equals(Coordinate other) {
        return this.x == other.x && this.y == other.y;
    }
}