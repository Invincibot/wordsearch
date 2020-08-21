package com.example.wordsearch;

public class WordSearchTest {
    WordSearchGenerator[] generators;
    WordSearchSolverAless a;
    WordSearchSolverLuigi l;

    public static void main(String[] args) throws TestFailedException {
        final int width = 20;
        final int height = 20;
        final int numWords = 10;
        final int minLen = 5;
        final int numTests = 10000;

        WordSearchTest test = new WordSearchTest(width, height, numWords, minLen, numTests);
        test.RunAlessTests();
        test.RunLuigiTests();
        System.out.println("All tests passed.");
    }

    WordSearchTest(int w, int h, int numWords, int minLen, int numTests) {
        generators = new WordSearchGenerator[numTests];
        for (int i = 0; i < numTests; i++) {
            generators[i] = new WordSearchGenerator(w, h);
            generators[i].GenerateWordSearch(numWords, minLen);
        }

        this.a = new WordSearchSolverAless();
        this.l = new WordSearchSolverLuigi();
    }

    public void RunAlessTests() throws TestFailedException {
        for (WordSearch ws : this.generators) {
            this.a.Solve(ws);
            if (!a.solvable) {
                this.a.WriteToFile();
                throw new TestFailedException("AlessMethod: Got error solving word search");
            }
        }
    }

    public void RunLuigiTests() throws TestFailedException {
        for (WordSearch ws : this.generators) {
            this.l.Solve(ws);
            if (!l.solvable) {
                this.l.WriteToFile();
                throw new TestFailedException("LuigiMethod: Got error solving word search");
            }
        }
    }
}

class TestFailedException extends Exception {
    TestFailedException(String errorMessage) {
        super(errorMessage);
    }
}