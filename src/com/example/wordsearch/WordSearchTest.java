package com.example.wordsearch;

public class WordSearchTest {
    WordSearchGenerator[] generators;
    WordSearchSolverAless a;
    WordSearchSolverLuigi l;
    WordSearchSolverMixed m;

    public static void main(String[] args) throws TestFailedException {
        final int width = 256;
        final int height = 256;
        final int numWords = 256;
        final int minLen = 128;
        final int numTests = 100;

        WordSearchTest test = new WordSearchTest(width, height, numWords, minLen, numTests);
        test.RunAlessTests();
        test.RunLuigiTests();
        test.RunMixedTests();
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
        this.m = new WordSearchSolverMixed();
    }

    public void RunAlessTests() throws TestFailedException {
        for (WordSearch ws : this.generators) {
            this.a.Solve(ws);
            if (!a.solvable) {
                ws.WriteToFile();
                throw new TestFailedException("AlessMethod: Got error solving word search");
            }
        }
    }

    public void RunLuigiTests() throws TestFailedException {
        for (WordSearch ws : this.generators) {
            this.l.Solve(ws);
            if (!l.solvable) {
                ws.WriteToFile();
                throw new TestFailedException("LuigiMethod: Got error solving word search");
            }
        }
    }

    public void RunMixedTests() throws TestFailedException {
        for (WordSearch ws : this.generators) {
            this.m.Solve(ws);
            if (!m.solvable) {
                ws.WriteToFile();
                throw new TestFailedException("MixedMethod: Got error solving word search");
            }
        }
    }
}

class TestFailedException extends Exception {
    TestFailedException(String errorMessage) {
        super(errorMessage);
    }
}