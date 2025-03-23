package src.genetic;

import java.util.*;

public class Crossover {

    public static List<Integer> orderedCrossover(List<Integer> parent1, List<Integer> parent2) {
        Random rand = new Random();
        int size = parent1.size();
        List<Integer> child = new ArrayList<>(Collections.nCopies(size, -1));

        int start = rand.nextInt(size);
        int end = rand.nextInt(size);

        if (start > end) { int temp = start; start = end; end = temp; }

        // Copy slice from parent1
        for (int i = start; i <= end; i++) {
            child.set(i, parent1.get(i));
        }

        // Fill remaining from parent2 in order
        int current = (end + 1) % size;
        for (int i = 0; i < size; i++) {
            int candidate = parent2.get((end + 1 + i) % size);
            if (!child.contains(candidate)) {
                child.set(current, candidate);
                current = (current + 1) % size;
            }
        }

        return child;
    }
}
