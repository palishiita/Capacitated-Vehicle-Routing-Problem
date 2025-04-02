package genetic;

// Crossover -  To combine genes (traits) from two parents to create offspring (new solutions).
public enum CrossoverType {
    OX,    // Ordered Crossover - Keeps a subsequence from one parent and preserves the order of remaining genes from the other.
    PMX,   // Partially Matched Crossover - Swaps segments and creates a gene mapping to resolve conflicts and preserve positions.
    CX     // Cycle Crossover - Preserves absolute positions of genes from both parents by copying genes based on cycles.
}
