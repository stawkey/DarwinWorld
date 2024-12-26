package agh.fcs.oop.model;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Animal implements WorldElement {
    private final int geneLength;
    private final int energyUsedForReproduction;
    private final int minMutationCount; // counted from 1 not 0
    private final int maxMutationCount;
    private MapDirection facingDirection;
    private Vector2d position;
    private int energy;
    private ArrayList<Integer> gene = new ArrayList<>();
    private int currGene;
    private int age = 0;
    private int childrenNumber = 0;

    public Animal(Vector2d position, int startAnimalEnergy, int energyUsedForReproduction,
                  int minMutationCount, int maxMutationCount, int geneLength) {
        this.position = position;
        facingDirection = MapDirection.randomDirection();
        energy = startAnimalEnergy;
        this.energyUsedForReproduction = energyUsedForReproduction;
        this.geneLength = geneLength;
        this.minMutationCount = minMutationCount;
        this.maxMutationCount = maxMutationCount;

        currGene = ThreadLocalRandom.current().nextInt(geneLength);
        for (int i = 0; i < this.geneLength; i++) {
            gene.add(ThreadLocalRandom.current().nextInt(8));
        }
    }

    public Animal(Vector2d position, int startAnimalEnergy, int energyUsedForReproduction,
                  int minMutationCount, int maxMutationCount, int geneLength, ArrayList<Integer> gene) {
        this.position = position;
        facingDirection = MapDirection.randomDirection();
        energy = startAnimalEnergy;
        this.energyUsedForReproduction = energyUsedForReproduction;
        this.gene = gene;
        this.geneLength = geneLength;
        this.minMutationCount = minMutationCount;
        this.maxMutationCount = maxMutationCount;
        currGene = ThreadLocalRandom.current().nextInt(geneLength);
    }

    public Vector2d getPosition() {
        return position;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getAge() {
        return age;
    }

    public int getChildrenNumber() {
        return childrenNumber;
    }

    // direction is a number in range 0-7 where 0 represents no rotation, 1 is a rotation by 45 deg
    // then moves forwards by 1 unit vector
    public void move(WorldMap world) {
        int direction = gene.get(currGene);
        currGene++;
        currGene = currGene % gene.size();

        this.facingDirection = this.facingDirection.rotateBy(direction);
        Vector2d newPosition = this.position.add(this.facingDirection.toUnitVector());
        if (world.canMoveTo(newPosition)) {
            this.position = newPosition;
            this.energy--;
            this.age++;
        }
    }

    public void reproduce(Animal partner) {
        int offspringEnergy = (int) (energyUsedForReproduction * 2);
        this.energy -= energyUsedForReproduction;
        partner.energy -= energyUsedForReproduction;

        this.childrenNumber++;
        partner.childrenNumber++;

        double proportion = (double) this.energy / (this.energy + partner.energy);
        int middlePoint = (int) (proportion * geneLength);

        ArrayList<Integer> offspringGene = new ArrayList<>();
        for (int i = 0; i < geneLength; i++) {
            if (i < middlePoint) {
                offspringGene.add(ThreadLocalRandom.current().nextBoolean() ? this.gene.get(i) : partner.gene.get(i));
            } else {
                offspringGene.add(ThreadLocalRandom.current().nextBoolean() ? partner.gene.get(i) : this.gene.get(i));
            }
        }

        int mutations = ThreadLocalRandom.current().nextInt(minMutationCount + 1, maxMutationCount + 1);
        for (int i = 0; i < mutations; i++) {
            int geneIndex = ThreadLocalRandom.current().nextInt(geneLength);
            offspringGene.set(geneIndex, ThreadLocalRandom.current().nextInt(8));
        }

        new Animal(this.position, offspringEnergy, energyUsedForReproduction,
                minMutationCount, maxMutationCount, geneLength, offspringGene);
    }

    public boolean isAlive() {
        return energy > 0;
    }

    @Override
    public String toString() {
        return facingDirection.toString();
    }
}
