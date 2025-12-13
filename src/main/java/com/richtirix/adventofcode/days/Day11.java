package com.richtirix.adventofcode.days;

import com.richtirix.adventofcode.AbstractDay;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day11 extends AbstractDay {
    private final Map<String, List<Point>> graph = new HashMap<>();
    private final Set<Point> points = new HashSet<>();

    public Day11() throws IOException {
        super("day11");
        for(String line: lines) {
            var pm = Pattern.compile("(?<source>.{3}): (?<targets>.*)").matcher(line);
            boolean ignored = pm.find();
            String source = pm.group("source");
            points.add(new Point(source));
            List<Point> targets = Arrays.stream(pm.group("targets").split(" "))
                    .map(s -> points.stream().filter(new Point(s)::equals).findFirst()
                    .orElse(new Point(s)))
                    .toList();
            points.addAll(targets);
            graph.put(source, targets);
        }
    }

    @Override
    public int getDay() {
        return 11;
    }

    @Override
    public Long solvePart1() {
        if(graph.get("you") != null){
            return getNumberOfPathsToTheEnd(graph, new Point("you"), "out");
        }
        return 0L;
    }

    @Override
    public Long solvePart2() {
        return partialSolvePart2(List.of("svr", "dac", "fft", "out")) + partialSolvePart2(List.of("svr", "fft", "dac", "out"));
    }

    private Long partialSolvePart2(List<String> path) {
        return IntStream.range(0, 3)
                .boxed()
                .filter(i->points.contains(new Point(path.get(i))) && points.contains(new Point(path.get(i+1))))
                .map(i -> {
                    points.forEach(p -> p.setHasBeenVisited(false));
                    return getNumberOfPathsToTheEnd(graph, new Point(path.get(i)), path.get(i+1));
                })
                .reduce(1L, (i1, i2)-> i1*i2);
    }

    private Long getNumberOfPathsToTheEnd(Map<String, List<Point>> graph, Point source, String finalTarget) {
        if(source.hasBeenVisited) {
            return source.getPathSize();
        }
        if("out".equals(source.getKey())) {
            return 0L;
        }
        long tmpNumberOfPaths = 0;
        for(Point nextSource : graph.get(source.getKey())){
            if(finalTarget.equals(nextSource.getKey())) {
                tmpNumberOfPaths++;
            } else {
                tmpNumberOfPaths+= getNumberOfPathsToTheEnd(graph, nextSource, finalTarget);
            }
        }

        source.setHasBeenVisited(true);
        source.setPathSize(tmpNumberOfPaths);
        return tmpNumberOfPaths;
    }


    private static class Point {
        private final String key;
        private boolean hasBeenVisited;
        private long pathSize;

        public Point(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public void setHasBeenVisited(boolean hasBeenVisited) {
            this.hasBeenVisited = hasBeenVisited;
        }

        public long getPathSize() {
            return pathSize;
        }

        public void setPathSize(long pathSize) {
            this.pathSize = pathSize;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return Objects.equals(key, point.key);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(key);
        }
    }
}
