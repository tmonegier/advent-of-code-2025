package com.richtirix.adventofcode;

import com.richtirix.adventofcode.days.Day6;
import com.richtirix.adventofcode.days.Day7;
import com.richtirix.adventofcode.days.Day8;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class Main {
    private static final List<DaySolution> daySolutions;
    private static final long START = System.nanoTime();
    static {
        try {
            daySolutions = List.of(
                new Day7(),
                new Day6(),
                new Day8()
            );
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void main(String[] args) {
        for (String arg : args) {
            IO.println(getDay(Integer.parseInt(arg)).solvePart1());
            IO.println(getDay(Integer.parseInt(arg)).solvePart2());
        }
        long end = System.nanoTime();
        long elapsedMillis = (end - START) / 1_000_000;
        System.out.println("Execution time: " + elapsedMillis + " ms");
    }

    private static DaySolution getDay(int day) {
        return daySolutions.stream().filter(daySolution -> daySolution.getDay() == day).findFirst().orElseGet(
                () -> new DaySolution() {
                    @Override
                    public int getDay() {
                        return day;
                    }

                    @Override
                    public Long solvePart1() {
                        return 0L;
                    }

                    @Override
                    public Long solvePart2() {
                        return 0L;
                    }
                }
        );
    }
}
