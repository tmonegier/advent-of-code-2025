package com.richtirix.adventofcode.days;

import com.richtirix.adventofcode.AbstractDay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static java.lang.Math.pow;

public class Day10 extends AbstractDay {

    private final Pattern TARGET_PATTERN = Pattern.compile("[#.]+");
    private final Pattern SWITCH_PATTERN = Pattern.compile("\\((?<x>(\\d+,?)+)\\)");
    private final Pattern JOLT_PATTERN = Pattern.compile("\\{(?<x>(\\d+,?)+)}");
    private final List<LineDefinition> lineDefinitions;

    public Day10() throws IOException {
        super("day10");
        lineDefinitions = lines.stream().map(
        line -> {
            List<Integer[]> switches = new ArrayList<>();
            var pm = TARGET_PATTERN.matcher(line);
            pm.find();
            var targetArray = pm.group().toCharArray();

            var spm = SWITCH_PATTERN.matcher(line);
            while(spm.find()) {
                List<Integer> list = new ArrayList<>();
                for (String string : spm.group("x").split(",")) {
                    Integer parseInt = Integer.parseInt(string);
                    list.add(parseInt);
                }
                Integer[] s = list.toArray(new Integer[0]);
                switches.add(s);
            }

            return new LineDefinition(targetArray, switches);

        }).toList();
    }

    @Override
    public int getDay() {
        return 10;
    }

    @Override
    public Long solvePart1() {
        int res = 0;
        for(LineDefinition lineDefinition: lineDefinitions) {
            int numberOfSwitches = lineDefinition.switches.size();
            res += IntStream.range(0, (int) pow(2, numberOfSwitches)).boxed().sorted(
                (i1, i2) -> {
                    var b1 = Integer.toBinaryString(i1).chars().map(i -> i - '0').reduce(Integer::sum).orElse(0);
                    var b2 = Integer.toBinaryString(i2).chars().map(i -> i - '0').reduce(Integer::sum).orElse(0);

                    return Integer.compare(b1, b2);
                }
            ).filter(i -> {
                var b = Integer.toBinaryString(i).toCharArray();
                char[] initArray = new char[lineDefinition.target.length];
                Arrays.fill(initArray, '.');
                for(int index = 0; index < b.length; index++) {
                    var correctedIndex = numberOfSwitches - b.length + index;
                    if(b[index] - '0' == 1){
                        var switchToApply = lineDefinition.switches.get(correctedIndex);
                        for(int button : switchToApply) {
                            if(initArray[button] == '.') {
                                initArray[button] = '#';
                            } else {
                                initArray[button] = '.';
                            }
                        }
                    }
                }

                return Arrays.equals(initArray, lineDefinition.target);
            })
            .findFirst()
            .map(Integer::toBinaryString)
            .orElse("")
            .chars()
            .map(i -> i - '0')
            .reduce(Integer::sum)
            .orElse(0);
        }
        return (long) res;
    }

    @Override
    public Long solvePart2() {
        return 0L;
    }

    private record LineDefinition(
        char[] target,
        List<Integer[]> switches
    ) {}
}
