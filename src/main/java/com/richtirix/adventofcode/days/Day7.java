package com.richtirix.adventofcode.days;

import com.richtirix.adventofcode.AbstractDay;

import java.io.IOException;
import java.util.Arrays;

public class Day7 extends AbstractDay {

    private final long[] beams;
    public Day7() throws IOException {
        super("day7");
        beams = new long[lines.size()];
    }

    @Override
    public int getDay() {
        return 7;
    }

    @Override
    public Long solvePart1() {
        init();
        long res = 0;
        for (String currentLine : lines) {
            for (int j = 0; j < beams.length; j++) {
                if (beams[j] > 0 && currentLine.charAt(j) == '^') {
                    res++;
                    if (j > 0) {
                        beams[j - 1] += beams[j];
                    }
                    if (j < beams.length - 1) {
                        beams[j + 1] += beams[j];
                    }
                    beams[j] = 0;
                }

            }
        }
        return res;
    }

    @Override
    public Long solvePart2() {
        solvePart1();
        return Arrays.stream(beams).reduce(Long::sum).orElse(0);
    }

    private void init() {
        Arrays.fill(beams, 0);
        beams[lines.getFirst().indexOf('S')] = 1;
    }
}