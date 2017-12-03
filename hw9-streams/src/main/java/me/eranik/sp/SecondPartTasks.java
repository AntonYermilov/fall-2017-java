package me.eranik.sp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SecondPartTasks {

    private SecondPartTasks() {}

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) {
        return paths.stream().flatMap(name -> {
            try {
                return Files.lines(Paths.get(name));
            } catch (IOException e) {
                return Stream.of("");
            }
        }).flatMap(line -> Arrays.stream(line.split(" ")))
                .filter(str -> str.contains(sequence)).collect(Collectors.toList());
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать, какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        long iterations = (long) 5e6;
        return Stream.generate(new Supplier<Double>() {
            Random r = new Random();
            @Override
            public Double get() {
                return Math.sqrt(Math.pow(r.nextDouble() - 0.5, 2) + Math.pow(r.nextDouble() - 0.5, 2));
            }
        }).limit(iterations).filter(x -> x <= 0.5).count() * 1.0 / iterations;
    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> compositions) {
        return compositions.entrySet().stream().max(Comparator.comparing(author ->
                author.getValue().stream().mapToInt(String::length).sum()))
                .map(Map.Entry::getKey).orElse("");
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        return orders.stream().flatMap(map -> map.entrySet().stream()).collect(Collectors.groupingBy(
                Map.Entry::getKey, Collectors.summingInt(Map.Entry::getValue)));
    }
}