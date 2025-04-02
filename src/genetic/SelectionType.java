package genetic;

// Selection - To choose individuals (parents) for reproduction based on their fitness (quality).
public enum SelectionType {
    TOURNAMENT, // Randomly selects k individuals and chooses the best (fitness) as the parent.
    ROULETTE // Selects individuals probabilistically, giving higher chances to individuals with better (shorter) distances.
}