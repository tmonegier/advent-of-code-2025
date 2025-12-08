package com.richtirix.adventofcode;

import com.richtirix.adventofcode.days.Day8;

import java.io.IOException;
import java.util.List;

public abstract class AbstractDay implements DaySolution {

    protected final List<String> lines;

    public AbstractDay(String resourceName) throws IOException{
        try (var in = Day8.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (in == null) {
                throw new IOException("resource not found on classpath");
            }
            try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(in, java.nio.charset.StandardCharsets.UTF_8))) {
                lines = reader.lines().toList();
            }
        }
    }
}
