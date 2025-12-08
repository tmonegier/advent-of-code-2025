package com.richtirix.adventofcode;

import com.richtirix.adventofcode.days.Day6;
import com.richtirix.adventofcode.days.Day7;
import com.richtirix.adventofcode.days.Day8;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class Main {
    private static final List<DaySolution> daySolutions;
    static {
        try {
            daySolutions = List.of(
                new Day6(),
                new Day7(),
                new Day8()
            );
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void main(String[] args) {
        IO.println(getDay(Integer.parseInt(args[0])).solvePart1());
        IO.println(getDay(Integer.parseInt(args[0])).solvePart2());
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
