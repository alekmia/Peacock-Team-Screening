package org.example;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        final long startTime = System.nanoTime();
        LineStat stat = new LineStat(args[0]);
        int groupAmount = stat.calc();
        final long duration = System.nanoTime() - startTime;
        AnswerBundle answer = new AnswerBundle(groupAmount, TimeUnit.NANOSECONDS.toSeconds(duration));
        answer.print();
    }
}