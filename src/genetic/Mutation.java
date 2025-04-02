package genetic;

import java.util.*;

public class Mutation {
    public static void apply(MutationType type, List<Integer> route) {
        switch (type) {
            case SWAP: swap(route); break;
            case INVERSION: inversion(route); break;
        }
    }

    private static void swap(List<Integer> route) {
        Random rand = new Random();
        int i = rand.nextInt(route.size());
        int j = rand.nextInt(route.size());
        Collections.swap(route, i, j);
    }

    private static void inversion(List<Integer> route) {
        Random rand = new Random();
        int i = rand.nextInt(route.size());
        int j = rand.nextInt(route.size());
        if (i > j) { int temp = i; i = j; j = temp; }
        while (i < j) {
            Collections.swap(route, i++, j--);
        }
    }
}