package genetic;

public class GAConfig {
    public int popSize;
    public int generations;
    public double Px;
    public double Pm;
    public int tournamentSize;
    public SelectionType selectionType;
    public CrossoverType crossoverType;
    public MutationType mutationType;
    public int elitismCount;

    public GAConfig(int popSize, int generations, double Px, double Pm, int tournamentSize,
                    SelectionType selectionType, CrossoverType crossoverType,
                    MutationType mutationType, int elitismCount) {
        this.popSize = popSize;
        this.generations = generations;
        this.Px = Px;
        this.Pm = Pm;
        this.tournamentSize = tournamentSize;
        this.selectionType = selectionType;
        this.crossoverType = crossoverType;
        this.mutationType = mutationType;
        this.elitismCount = elitismCount;
    }

    public String toShortString() {
        return String.format("pop:%d gen:%d Px:%.2f Pm:%.2f Tour:%d %s/%s/%s EC:%d",
                popSize, generations, Px, Pm, tournamentSize,
                selectionType, crossoverType, mutationType, elitismCount);
    }
}
