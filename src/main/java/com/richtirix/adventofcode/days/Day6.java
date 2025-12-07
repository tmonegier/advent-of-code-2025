package com.richtirix.adventofcode.days;

import com.richtirix.adventofcode.DaySolution;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

public class Day6 implements DaySolution {

    private static final Pattern FUNCTION_PATTERN = Pattern.compile("[*+]");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");
    private static final Map<String, MathOperation> OPERATIONS = new HashMap<>(){
        {
            put("*", MathOperation.MULTIPLICATION);
            put("+", MathOperation.ADDITION);
        }
    };

    private final List<String> lines;
    private final int functionsIndex;
    private final String functionsString;
    private final List<Long> results = new ArrayList<>();
    private final List<MathOperation> functions = new ArrayList<>();

    public Day6() throws URISyntaxException, IOException {
        var path = Path.of(Objects.requireNonNull(Day6.class.getClassLoader().getResource("day6")).toURI());
        this.lines =  Files.readAllLines(path);
        functionsIndex = lines.size()-1;
        functionsString = lines.get(functionsIndex);
    }

    private void init() {
        var functionsPattern = FUNCTION_PATTERN.matcher(functionsString);
        functions.clear();
        results.clear();
        while(functionsPattern.find()) {
            var operation = OPERATIONS.get(functionsPattern.group());
            functions.add(operation);
            results.add(operation.getNeutralElement());
        }
    }

    @Override
    public Long solvePart1() {
        init();
        for(int index = 0; index < functionsIndex; index++) {
            var currentLine = lines.get(index);
            int i = 0;
            for(var number : NUMBER_PATTERN.matcher(currentLine).results().toList()){
                results.set(i, functions.get(i).getOperation().apply(results.get(i),Long.valueOf(number.group())));
                i++;
            }
        }
        return results.stream().reduce(Long::sum).orElse(0L);
    }

    @Override
    public Long solvePart2()  {
        init();
        int currentFunctionIndex = 0;
        int currentColumn = 0;
        while(currentFunctionIndex < functions.size()) {
            int powerOfTen = 0;
            boolean shouldChangeFunctionIndex = true;
            long currentNumber = 0;

            for(int h = functionsIndex-1; h>=0; h--) {
                char currentChar = currentColumn >= lines.get(h).length() ? ' ' : lines.get(h).charAt(currentColumn);
                if(Character.isDigit(currentChar)) {
                    shouldChangeFunctionIndex = false;
                    currentNumber += (long) (Math.pow(10, powerOfTen) * (currentChar - '0'));
                    powerOfTen++;
                }
            }
            currentColumn++;

            if(shouldChangeFunctionIndex) {
                currentFunctionIndex++;
            } else {
                results.set(currentFunctionIndex, functions.get(currentFunctionIndex).getOperation().apply(results.get(currentFunctionIndex), currentNumber));
            }
        }
        return results.stream().reduce(Long::sum).orElse(0L);
    }


    @Override
    public int getDay() {
        return 6;
    }

    private enum MathOperation {
        ADDITION(Long::sum, 0L),
        MULTIPLICATION((l1, l2) -> l1 * l2, 1L);

        private final BiFunction<Long, Long, Long> operation;
        private final Long neutralElement;

        MathOperation(BiFunction<Long, Long, Long> operation, Long neutralElement) {
            this.operation = operation;
            this.neutralElement = neutralElement;
        }

        public BiFunction<Long, Long, Long> getOperation() {
            return operation;
        }

        public Long getNeutralElement() {
            return neutralElement;
        }
    }
}
