package agh.fcs.oop.model;

public class AnimalConfig {
    private final int startAnimalEnergy;
    private final int energyUsedForReproduction;
    private final int minMutationCount;
    private final int maxMutationCount;
    private final int geneLength;

    public int getStartAnimalEnergy() {
        return startAnimalEnergy;
    }

    public int getEnergyUsedForReproduction() {
        return energyUsedForReproduction;
    }

    public int getMinMutationCount() {
        return minMutationCount;
    }

    public int getMaxMutationCount() {
        return maxMutationCount;
    }

    public int getGeneLength() {
        return geneLength;
    }

    public AnimalConfig(int startAnimalEnergy, int energyUsedForReproduction, int minMutationCount,
                        int maxMutationCount, int geneLength) {
        this.startAnimalEnergy = startAnimalEnergy;
        this.energyUsedForReproduction = energyUsedForReproduction;
        this.minMutationCount = minMutationCount;
        this.maxMutationCount = maxMutationCount;
        this.geneLength = geneLength;
    }
}
