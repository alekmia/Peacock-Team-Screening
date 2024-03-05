package org.example;

import java.io.*;
import java.util.*;

public class LineStat {
    public LineStat(String inputFilename) {
        this.inputFilename = inputFilename;
    }

    private final String inputFilename;
    private static final Integer MAX_LINES = 1000000;
    private final List<Integer> ranks = new ArrayList<>(Collections.nCopies(MAX_LINES, 0));
    private final List<Integer> parents = new ArrayList<>(MAX_LINES);
    private final List<Map<String, Integer>> places = new ArrayList<>();
    private final List<String> lines = new ArrayList<>(MAX_LINES);
    private final Set<String> uniqueLines = new HashSet<>(MAX_LINES);

    public int calc() {
        String outputFilename = "src/main/resources/out.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilename));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename))) {
            String str;
            int lineAmount = 0;
            while ((str = reader.readLine()) != null) {
                var strings = parseLine(str);
                if (strings == null || uniqueLines.contains(str)) continue;
                saveLine(str, lineAmount);
                checkUnique(strings, lineAmount);
                lineAmount++;
            }
            uniqueLines.clear();
            places.clear();
            Map<Integer, List<Integer>> groupElements = new HashMap<>(MAX_LINES);
            int group = 1;
            var arr = sortedSet(groupElements);
            for (var el : arr) {
                List<Integer> elements = groupElements.get(el.getKey());
                if (elements.size() <= 1) continue;
                writer.write(String.format("Group %d\n", group));
                for (var entry : elements) {
                    writer.write(lines.get(entry) + "\n");
                }
                writer.write("\n");
                group++;
            }
            return --group;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String[] parseLine(String line) {
        if (!validateLine(line)) return null;
        return line.split(";");
    }

    private boolean validateLine(String line) {
        return line.matches("(\"\\d*(.\\d)?\")(;(\"\\d*(.\\d)?\")?)*");
    }

    private int get(int i) {
        if(parents.get(i) != i) {
            parents.set(i, get(parents.get(i)));
        }
        return parents.get(i);
    }

    private void union(int x, int y) {
        x = get(x);
        y = get(y);
        if (x == y) return;
        if (Objects.equals(ranks.get(x), ranks.get(y))) {
            ranks.set(x, get(x)+1);
        }
        if (ranks.get(x) < ranks.get(y)) {
            parents.set(x, y);
        } else {
            parents.set(y, x);
        }
    }

    private void checkUnique(String[] lineStrings, int lineNumber) {
        for (int i = 0; i < lineStrings.length; i++) {
            String cur = lineStrings[i];
            if (i >= places.size()) {
                Map<String, Integer> map = new HashMap<>();
                map.put(cur, lineNumber);
                places.add(map);
            } else if (!checkEmptyString(cur) && places.get(i).containsKey(cur)) {
                int match = places.get(i).get(cur);
                union(match, lineNumber);
            } else {
                places.get(i).put(cur, lineNumber);
            }
        }
    }

    private boolean checkEmptyString(String string) {
        return Objects.equals(string, "") || Objects.equals(string, "\"\"");
    }

    private List<Map.Entry<Integer, Integer>> sortedSet(Map<Integer, List<Integer>> elementsByGroup) {
        Map<Integer, Integer> sizes = new HashMap<>(MAX_LINES);
        for (int i = 0; i < parents.size(); i++) {
            var key = get(i);
            elementsByGroup.computeIfAbsent(key, ignore -> new ArrayList<>()).add(i);
            sizes.put(key, sizes.getOrDefault(key, 0) + 1);
        }
        return sizes.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .toList();
    }

    private void saveLine(String curString, int lineNumberCount) {
        parents.add(lineNumberCount);
        uniqueLines.add(curString);
        lines.add(curString);
    }
}
