package genetic;

// Mutation - To make small random changes to a solution to introduce new genetic material.
public enum MutationType {
    SWAP, // Randomly selects two positions in the route and swaps them.
    INVERSION // Randomly selects a sub-segment of the route and reverses it.
}