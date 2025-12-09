package com.richtirix.adventofcode.days;

import com.richtirix.adventofcode.AbstractDay;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

public class Day9 extends AbstractDay {

    private final Pattern pointPattern = Pattern.compile("(?<x>\\d*),(?<y>\\d*)");

    @Override
    public int getDay() {
        return 9;
    }

    public Day9()  throws URISyntaxException, IOException  {
        super("day9");
        
    }

    @Override
    public Long solvePart1() {
        long maxArea = 0L;
        for (int i = 0; i < lines.size(); i++) {
            String startLinePoint = lines.get(i);
            var startPoint = extractPoint(startLinePoint);
            for (int j = i+1; j < lines.size(); j++) {
                String stopLinePoint = lines.get(j);
                var endPoint = extractPoint(stopLinePoint);
                var area = calculateArea(startPoint, endPoint);
                if(area > maxArea) {
                    maxArea = area;
                }
            }
        }

        return maxArea;
    }

    private Long calculateArea(Point startPoint, Point endPoint) {
        return (long) Math.abs(startPoint.x+1 - endPoint.x) * Math.abs(startPoint.y+1 - endPoint.y);
    }

    @Override
    public Long solvePart2() {
        var points = lines.stream().map(this::extractPoint).toList();



        for (int i = 0; i < points.size(); i++) {
            grid[points.get(i).y][points.get(i).x] = true;
            for (int j = i; j < points.size(); j++) {
                grid[points.get(j).y][points.get(j).x] = true;
                if(points.get(i).x == points.get(j).x) {
                    for (int k = Math.min(points.get(i).y, points.get(j).y); k < Math.max(points.get(i).y, points.get(j).y); k++) {
                        grid[k][points.get(i).x] = true;
                    }
                }

                if(points.get(i).y == points.get(j).y) {
                    for (int k = Math.min(points.get(i).x, points.get(j).x); k < Math.max(points.get(i).x, points.get(j).x); k++) {
                        grid[points.get(i).y][k] = true;
                    }
                }
            }
        }

        return 0L;
    }

    private Point extractPoint(String startLinePoint) {
        return pointPattern.matcher(startLinePoint).results().findFirst().map(
            matchResult ->
                new Point(
                    Integer.parseInt(matchResult.group("x")),
                    Integer.parseInt(matchResult.group("y"))
                )
        ).orElse(null);
    }

    private record Point(int x, int y) {

    }
}
