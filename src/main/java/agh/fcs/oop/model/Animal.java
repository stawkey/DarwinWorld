package agh.fcs.oop.model;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Animal implements WorldElement {
    private final AnimalConfig animalConfig;
    private MapDirection facingDirection;
    private Vector2d position;
    private int energy;
    private ArrayList<Integer> gene = new ArrayList<>();
    private int currGene;
    private int age = 0;
    private int childrenNumber = 0;

    public Animal(Vector2d position, AnimalConfig animalConfig) {
        this.position = position;
        facingDirection = MapDirection.randomDirection();

        this.animalConfig = animalConfig;
        energy = animalConfig.getStartAnimalEnergy();

        currGene = ThreadLocalRandom.current().nextInt(animalConfig.getGeneLength());
        for (int i = 0; i < animalConfig.getGeneLength(); i++) {
            gene.add(ThreadLocalRandom.current().nextInt(8));
        }
    }

    // this constructor is going to be used for creating offspring
    public Animal(Vector2d position, AnimalConfig animalConfig, ArrayList<Integer> gene) {
        this.position = position;
        facingDirection = MapDirection.randomDirection();

        this.animalConfig = animalConfig;
        energy = animalConfig.getEnergyUsedForReproduction() * 2;

        this.gene = gene;
        currGene = ThreadLocalRandom.current().nextInt(animalConfig.getGeneLength());
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

    public ArrayList<Integer> getGene() {
        return gene;
    }

    public MapDirection getFacingDirection() {
        return facingDirection;
    }

    public int getCurrGene() {
        return currGene;
    }


    // direction is a number in range 0-7 where 0 represents no rotation, 1 is a rotation by 45 deg
    // then moves forwards by 1 unit vector
    public void move(WorldMap world) {
        int direction = gene.get(currGene);

        if(ThreadLocalRandom.current().nextInt(5) % 5 == 0) {
            currGene += ThreadLocalRandom.current().nextInt(animalConfig.getGeneLength() - 1);
        }
        else {
            currGene++;
        }
        currGene = currGene % gene.size();

        facingDirection = facingDirection.rotateBy(direction);
        Vector2d newPosition = this.position.add(facingDirection.toUnitVector());
        if (world.canMoveTo(newPosition)) {
            position = newPosition;
            energy--;
            age++;
        }
    }

    public Animal reproduce(Animal partner) {
        this.energy -= animalConfig.getEnergyUsedForReproduction();
        partner.energy -= animalConfig.getEnergyUsedForReproduction();

        this.childrenNumber++;
        partner.childrenNumber++;

        double proportion = (double) this.energy / (this.energy + partner.energy);
        int middlePoint = (int) (proportion * animalConfig.getGeneLength());

        ArrayList<Integer> offspringGene = new ArrayList<>();
        for (int i = 0; i < animalConfig.getGeneLength(); i++) {
            if (i < middlePoint) {
                offspringGene.add(ThreadLocalRandom.current().nextBoolean() ? this.gene.get(i) : partner.gene.get(i));
            } else {
                offspringGene.add(ThreadLocalRandom.current().nextBoolean() ? partner.gene.get(i) : this.gene.get(i));
            }
        }

        int mutations = ThreadLocalRandom.current().nextInt(animalConfig.getMinMutationCount() + 1,
                animalConfig.getMaxMutationCount() + 1);
        for (int i = 0; i < mutations; i++) {
            int geneIndex = ThreadLocalRandom.current().nextInt(animalConfig.getGeneLength());
            offspringGene.set(geneIndex, ThreadLocalRandom.current().nextInt(8));
        }

        return new Animal(this.position, animalConfig, offspringGene);
    }

    public boolean isAlive() {
        return energy > 0;
    }

    @Override
    public String toString() {
        return facingDirection.toString();
    }
}
