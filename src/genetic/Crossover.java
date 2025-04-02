package genetic;

import java.util.*;

public class Crossover {
    public static List<Integer> apply(CrossoverType type, List<Integer> p1, List<Integer> p2) {
        switch (type) {
            case OX: return orderedCrossover(p1, p2);
            case PMX: return pmxCrossover(p1, p2);
            case CX: return cycleCrossover(p1, p2);
            default: return new ArrayList<>(p1);
        }
    }

    private static boolean hasUnvisited(boolean[] visited) {
        for (boolean v : visited) {
            if (!v) return true;
        }
        return false;
    }
    

    // Ordered Crossover (OX)
    private static List<Integer> orderedCrossover(List<Integer> p1, List<Integer> p2) {
        Random rand = new Random();
        int size = p1.size();
        int start = rand.nextInt(size);
        int end = rand.nextInt(size);

        if (start > end) {
            int temp = start;
            start = end;
            end = temp;
        }

        List<Integer> child = new ArrayList<>(Collections.nCopies(size, -1));
        Set<Integer> slice = new HashSet<>();

        // Copy slice from p1
        for (int i = start; i <= end; i++) {
            int gene = p1.get(i);
            child.set(i, gene);
            slice.add(gene);
        }

        // Fill remaining from p2
        int current = (end + 1) % size;
        int p2Index = (end + 1) % size;

        while (child.contains(-1)) {
            int gene = p2.get(p2Index);
            if (!slice.contains(gene)) {
                child.set(current, gene);
                current = (current + 1) % size;
            }
            p2Index = (p2Index + 1) % size;
        }

        return child;
    }

    // Partially Mapped Crossover (PMX)
    private static List<Integer> pmxCrossover(List<Integer> p1, List<Integer> p2) {
        Random rand = new Random();
        int size = p1.size();
        int start = rand.nextInt(size);
        int end = rand.nextInt(size);
        if (start > end) {
            int temp = start; start = end; end = temp;
        }
    
        List<Integer> child = new ArrayList<>(Collections.nCopies(size, -1));
    
        // Create a fast lookup for positions in p1
        Map<Integer, Integer> p1Index = new HashMap<>();
        for (int i = 0; i < size; i++) {
            p1Index.put(p1.get(i), i);
        }
    
        // Step 1: Copy the segment from p1 to child
        for (int i = start; i <= end; i++) {
            child.set(i, p1.get(i));
        }
    
        // Step 2: Map conflicts from p2
        for (int i = start; i <= end; i++) {
            int gene = p2.get(i);
            if (child.contains(gene)) continue;
    
            int pos = i;
            int limit = 0;
    
            while (child.get(pos) != -1) {
                Integer mappedIndex = p1Index.get(gene);
                if (mappedIndex == null) {
                    System.out.println("⚠️ PMX error: gene " + gene + " not found in p1Index.");
                    break;
                }
    
                gene = p2.get(mappedIndex);
    
                if (limit++ > size) {
                    System.out.println("⚠️ PMX loop limit hit. Skipping gene.");
                    pos = -1; // prevent child.set()
                    break;
                }
            }
    
            if (pos >= 0 && pos < size && child.get(pos) == -1) {
                child.set(pos, gene);
            } else {
                System.out.println("⚠️ PMX skipping gene due to bad mapping or position: " + gene);
            }
        }
    
        // Step 3: Fill the rest of the child from p2
        for (int i = 0; i < size; i++) {
            if (child.get(i) == -1) {
                child.set(i, p2.get(i));
            }
        }
    
        return child;
    }
    

    // Cycle Crossover (CX)
    private static List<Integer> cycleCrossover(List<Integer> p1, List<Integer> p2) {
        int size = p1.size();
        List<Integer> child = new ArrayList<>(Collections.nCopies(size, -1));
        boolean[] visited = new boolean[size];
        boolean useP1 = true;

        Map<Integer, Integer> p1Index = new HashMap<>();
        for (int i = 0; i < size; i++) {
            p1Index.put(p1.get(i), i);
        }

        while (hasUnvisited(visited)) {
            int index = 0;
            while (index < size && visited[index]) index++;

            int start = index;
            int loopCount = 0;
            do {
                if (loopCount++ >= size) {
                    System.out.println("⚠️ CX loop limit hit. Breaking cycle.");
                    break;
                }

                child.set(index, useP1 ? p1.get(index) : p2.get(index));
                visited[index] = true;
                index = p1Index.get(p2.get(index));

            } while (index != start);

            useP1 = !useP1;
        }

        return child;
    }
    
}
