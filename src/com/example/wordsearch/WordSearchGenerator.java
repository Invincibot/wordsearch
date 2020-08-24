package com.example.wordsearch;

import java.io.IOException;
import java.util.Random;
import java.io.FileWriter;
import java.lang.StringBuilder;

class WordSearchGenerator extends WordSearch {
    GeneratedWord[] generatedWords;
    int minWordLen;

    public static void main(String[] args) {
        WordSearchGenerator wordsearch = new WordSearchGenerator(10, 10);
        wordsearch.GenerateWordSearch(5, 10);
        wordsearch.WriteToFile();
    }

    WordSearchGenerator(int w, int h) {
        super(w, h);
    }

    public void GenerateWordSearch(int numWords, int minLen) {
        this.generatedWords = new GeneratedWord[numWords];
        this.words = new String[numWords];
        this.minWordLen = minLen;

        Random random = new Random();
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                this.wordsearch[i][j] = (char)('a' + random.nextInt(26));
            }
        }

        for (int i = 0; i < numWords; i++) {
            this.generatedWords[i] = GenerateWord();
            this.words[i] = generatedWords[i].word;
        }
    }

    private GeneratedWord GenerateWord() {
        GeneratedWord word = new GeneratedWord();
        Random random = new Random();
        outer:
        while (true) {
            word.start.SetCoordinates(random.nextInt(this.width), random.nextInt(this.height));
            word.direction.SetCoordinates(random.nextInt(3) - 1, random.nextInt(3) - 1);
            word.length = minWordLen;

            if (!word.CheckValidPlacement(this.width, this.height) || word.direction.x == 0 && word.direction.y == 0)
                continue;

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

            for (Word other : this.generatedWords) {
                if (other == null) {
                    break outer;
                }

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
        return word;
    }
}

class WordSearch {
    int width;
    int height;
    char[][] wordsearch;
    String[] words;

    WordSearch(int w, int h) {
        this.width = w;
        this.height = h;
        this.wordsearch = new char[w][h];
    }

    public void WriteToFile() {
        try {
            FileWriter file = new FileWriter("wordsearch.txt");
            file.write(this.width + " " + this.height + " " + this.words.length + "\n");
            for (int i = 0; i < this.height; i++) {
                for (int j = 0; j < this.width; j++) {
                    file.write(this.wordsearch[j][i] + " ");
                }
                file.write("\n");
            }

            for (String word : this.words) {
                file.write(word + "\n");
            }

            file.close();

            System.out.println("Successfully wrote to file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
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