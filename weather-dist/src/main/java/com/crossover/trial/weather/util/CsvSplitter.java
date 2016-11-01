package com.crossover.trial.weather.util;

public class CsvSplitter {
    public static String[] split(String line) {
        return line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }
}
