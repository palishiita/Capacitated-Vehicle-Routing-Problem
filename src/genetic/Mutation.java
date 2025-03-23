package src.genetic;

import java.util.*;

public class Mutation {

    // Swap Mutation: randomly swaps two cities in route
    public static void swap(List<Integer> route) {
        Random rand = new Random();
        int i = rand.nextInt(route.size());
        int j = rand.nextInt(route.size());
        Collections.swap(route, i, j);
    }

    // Inversion Mutation: reverses a sublist between two points
    public static void inversion(List<Integer> route) {
        Random rand = new Random();
        int i = rand.nextInt(route.size());
        int j = rand.nextInt(route.size());

        if (i > j) { int temp = i; i = j; j = temp; }

        List<Integer> sublist = route.subList(i, j + 1);
        Collections.reverse(sublist);
    }
}
