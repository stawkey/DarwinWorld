package agh.fcs.oop.GUI;

// this class is responsible for moving parameters from SimulationSetup
// to SimulationPresenter in order to run simulation correctly

public class SimulationConfig {
    private String mapType;
    private String animalType;
    private int width;
    private int height;
    private int grassCount;
    private int animalCount;
    private int animalEnergy;
    private int reproductionMinEnergy;
    private int reproductionUsedEnergy;
    private int minMutationCount;
    private int maxMutationCount;
    private int grassEnergy;
    private int grassGrowth;
    private int geneLength;
    private int sleepDuration;
    private boolean generatingCsv;

    public String getMapType() { return mapType; }
    public void setMapType(String mapType) { this.mapType = mapType; }

    public String getAnimalType() { return animalType; }
    public void setAnimalType(String animalType) { this.animalType = animalType; }

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public int getGrassCount() { return grassCount; }
    public void setGrassCount(int grassCount) { this.grassCount = grassCount; }

    public int getAnimalCount() { return animalCount; }
    public void setAnimalCount(int animalCount) { this.animalCount = animalCount; }

    public int getAnimalEnergy() { return animalEnergy; }
    public void setAnimalEnergy(int animalEnergy) { this.animalEnergy = animalEnergy; }

    public int getReproductionMinEnergy() { return reproductionMinEnergy; }
    public void setReproductionMinEnergy(int reproductionMinEnergy) { this.reproductionMinEnergy = reproductionMinEnergy; }

    public int getReproductionUsedEnergy() { return reproductionUsedEnergy; }
    public void setReproductionUsedEnergy(int reproductionUsedEnergy) { this.reproductionUsedEnergy = reproductionUsedEnergy; }

    public int getMinMutationCount() { return minMutationCount; }
    public void setMinMutationCount(int minMutationCount) { this.minMutationCount = minMutationCount; }

    public int getMaxMutationCount() { return maxMutationCount; }
    public void setMaxMutationCount(int maxMutationCount) { this.maxMutationCount = maxMutationCount; }

    public int getGrassEnergy() { return grassEnergy; }
    public void setGrassEnergy(int grassEnergy) { this.grassEnergy = grassEnergy; }

    public int getGrassGrowth() { return grassGrowth; }
    public void setGrassGrowth(int grassGrowth) { this.grassGrowth = grassGrowth; }

    public int getGeneLength() { return geneLength; }
    public void setGeneLength(int geneLength) { this.geneLength = geneLength; }

    public int getSleepDuration() { return sleepDuration; }
    public void setSleepDuration(int sleepDuration) { this.sleepDuration = sleepDuration; }

    public boolean isGeneratingCsv() { return generatingCsv; }
    public void setGeneratingCsv(boolean generatingCsv) { this.generatingCsv = generatingCsv; }
}
