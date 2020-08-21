package com.example.wordsearch;

import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import java.io.FileWriter;
import java.lang.StringBuilder;

public class WordSearchGenerator {
    public static void main(String[] args) {
        WordSearch wordsearch = new WordSearch(100, 100);
        wordsearch.GenerateWordSearch(100, 10);

        try {
            FileWriter file = new FileWriter("wordsearch.txt");
            file.write(wordsearch.width + " " + wordsearch.height + " " + wordsearch.words.size() + "\n");
            for (int i = 0; i < wordsearch.width; i++) {
                for (int j = 0; j < wordsearch.height; j++) {
                    file.write(wordsearch.wordsearch[i][j] + " ");
                }
                file.write("\n");
            }

            for (GeneratedWord word : wordsearch.words) {
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

class WordSearch {
    int width;
    int height;
    char[][] wordsearch;
    ArrayList<GeneratedWord> words;
    int minWordLen;

    WordSearch(int w, int h) {
        this.width = w;
        this.height = h;
        this.wordsearch = new char[w][h];
        this.words = new ArrayList<>();
        this.minWordLen = 0;
    }

    public void GenerateWordSearch(int numWords, int minLen) {
        this.minWordLen = minLen;

        Random random = new Random();
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                this.wordsearch[i][j] = (char)('a' + random.nextInt(26));
            }
        }

        for (int i = 0; i < numWords; i++) {
            GenerateWord();
        }
    }

    private void GenerateWord() {
        GeneratedWord word = new GeneratedWord();
        Random random = new Random();
        outer:
        while (true) {
            word.start.SetCoordinates(random.nextInt(this.width), random.nextInt(this.height));
            word.direction.SetCoordinates(random.nextInt(3) - 1, random.nextInt(3) - 1);
            word.length = minWordLen;

            System.out.println(word.CheckValidPlacement(this.width, this.height));
            if (!word.CheckValidPlacement(this.width, this.height) || word.direction.x == 0 && word.direction.y == 0)
                continue;

            System.out.println("Word starts at " + word.start.x + ", " + word.start.y);
            System.out.println("Word travels in the direction of " + word.direction.x + ", " + word.direction.y);

            int maxx = Math.max(this.width, this.height);
            int maxy = maxx;
            if (word.direction.x == 1)
                maxx = this.width - word.start.x;
            else if (word.direction.x == -1)
                maxx = word.start.x + 1;

            if (word.direction.y == 1)
                maxy = this.height - word.start.y;
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
            sb.append(this.wordsearch[word.start.x + word.direction.x * i][word.start.y + word.direction.y * i]);
        }
        word.word = sb.toString();
        this.words.add(word);
    }
}

class GeneratedWord extends Word {
    String word;

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
}

class Word {
    Coordinate start;
    Coordinate direction;
    int length;

    Word() {
        this.start = new Coordinate();
        this.direction = new Coordinate();
    }

    public Coordinate[] GenerateCoordinates() {
        Coordinate[] coordinates = new Coordinate[this.length];
        for (int i = 0; i < this.length; i++) {
            coordinates[i] = new Coordinate(this.start.x + this.direction.x * i, this.start.y + this.direction.y * i);
        }

        return coordinates;
    }

    public boolean CheckValidPlacement(int width, int height) {
        return !((this.start.x <= this.length && this.direction.x < 0) ||
                (this.start.x > width - this.length && this.direction.x > 0) ||
                (this.start.y <= this.length && this.direction.y < 0) ||
                (this.start.y > height - this.length && this.direction.y > 0));
    }
}

class Coordinate {
    int x;
    int y;

    Coordinate(int x, int y) {
        this.SetCoordinates(x, y);
    }

    Coordinate() {
        this.SetCoordinates(0, 0);
    }

    public void SetCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean Equals(Coordinate other) {
        return this.x == other.x && this.y == other.y;
    }
}