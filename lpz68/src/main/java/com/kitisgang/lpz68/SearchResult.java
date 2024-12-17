package com.kitisgang.lpz68;

public class SearchResult {
    private final int count;
    private final double frequency;
    private final int totalWords;

    public SearchResult(int count, double frequency, int totalWords) {
        this.count = count;
        this.frequency = frequency;
        this.totalWords = totalWords;
    }

    public int getCount() {
        return count;
    }

    public double getFrequency() {
        return Math.round(frequency * 100.0) / 100.0;
    }

    public int getTotalWords() {
        return totalWords;
    }
}