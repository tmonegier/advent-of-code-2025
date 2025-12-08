package com.richtirix.adventofcode.days;

import com.richtirix.adventofcode.AbstractDay;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Pattern;

import static java.lang.Math.pow;

public class Day8 extends AbstractDay {

    private final Pattern pointPattern = Pattern.compile("(?<x>\\d*),(?<y>\\d*),(?<z>\\d*)");
    private final List<Circuit> circuits = new ArrayList<>();
    private final List<Distance> sortedDistances;

    public Day8() throws URISyntaxException, IOException {
        super("day8");
        List<Distance> distances = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String startLinePoint = lines.get(i);
            var startPoint = extractPoint(startLinePoint);
            for (int j = i+1; j < lines.size(); j++) {
                String stopLinePoint = lines.get(j);
                var endPoint = extractPoint(stopLinePoint);
                var distance = calculateDistance(startPoint, endPoint);
                distances.add(distance);
            }
        }

        sortedDistances = distances.stream().sorted(Comparator.comparingLong(d -> d.distanceSquared)).toList();
    }

    @Override
    public int getDay() {
        return 8;
    }

    private void init() {
        circuits.clear();
    }

    @Override
    public Long solvePart1() {
        init();
        int MINIMUM_DISTANCES_ARRAY_LENGTH = 10;
        sortedDistances.stream().limit(MINIMUM_DISTANCES_ARRAY_LENGTH).sorted(Comparator.comparingLong(d -> d.distanceSquared)).forEach(
            this::createCircuits
        );

        return (long) circuits.stream()
                .sorted((c1, c2) -> Integer.compare(c2.points.size(), c1.points.size()))
                .limit(3)
                .map(circuit -> circuit.points.size())
                .reduce(1, (i1, i2) -> i1*i2);
    }

    @Override
    public Long solvePart2() {
        init();
        for (Distance currentDistance : sortedDistances) {
            createCircuits(currentDistance);

            if (circuits.size() == 1 && circuits.getFirst().points.size() == lines.size()) {
                return (long) currentDistance.start.x * currentDistance.end.x;
            }
        }
        return 0L;
    }

    private void createCircuits(Distance currentDistance) {
        var tmp = circuits.stream()
                .filter(circuit -> circuit.contains(currentDistance.start) || circuit.contains(currentDistance.end))
                .toList();

        if(tmp.isEmpty()) {
            circuits.add(new Circuit(new HashSet<>(List.of(currentDistance.start, currentDistance.end))));
            return;
        }
        if(tmp.size() == 1) {
            tmp.getFirst().addAll(List.of(currentDistance.start, currentDistance.end));
        }
        if(tmp.size() == 2) {
            tmp.getFirst().merge(tmp.getLast());
            circuits.remove(tmp.getLast());
        }
    }

    private Point extractPoint(String startLinePoint) {
        return pointPattern.matcher(startLinePoint).results().findFirst().map(
            matchResult ->
                new Point(
                    Integer.parseInt(matchResult.group("x")),
                    Integer.parseInt(matchResult.group("y")),
                    Integer.parseInt(matchResult.group("z"))
                )
        ).orElse(null);
    }

    private Distance calculateDistance(Point startPoint, Point endPoint) {
        return new Distance(
            (long) (pow(startPoint.x - endPoint.x,2) + pow(startPoint.y - endPoint.y,2) + pow(startPoint.z - endPoint.z,2)),
            startPoint,
            endPoint
        );
    }

    private record Point (int x, int y, int z){}
    private record Distance(long distanceSquared, Point start, Point end){}
    private record Circuit(Set<Point> points){
        public boolean contains(Point point) {
            return points.contains(point);
        }

        public void merge(Circuit circuit) {
            points.addAll(circuit.points);
        }

        public void addAll(List<Point> pointsToAdd) {
            points.addAll(pointsToAdd);
        }
    }
}
