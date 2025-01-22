package agh.fcs.oop.model;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Animal implements WorldElement {
    private final AnimalConfig animalConfig;
    private MapDirection facingDirection;
    private Vector2d position;
    private final boolean variantSelector;
    private int energy;
    private ArrayList<Integer> gene = new ArrayList<>();
    private int currGene;
    private int age = 0;
    private int childrenNumber = 0;
    private int descendantsNumber = 0;
    private Animal parent1;
    private Animal parent2;
    private int grassEaten = 0;
    private int deathDay = -1;

    public Animal(Vector2d position, AnimalConfig animalConfig, boolean variantSelector) {
        this.position = position;
        facingDirection = MapDirection.randomDirection();

        this.animalConfig = animalConfig;
        energy = animalConfig.getStartAnimalEnergy();

        this.variantSelector = variantSelector;

        currGene = ThreadLocalRandom.current().nextInt(animalConfig.getGeneLength());
        for (int i = 0; i < animalConfig.getGeneLength(); i++) {
            gene.add(ThreadLocalRandom.current().nextInt(8));
        }
    }

    // this constructor is going to be used for creating offspring
    public Animal(Vector2d position, AnimalConfig animalConfig, ArrayList<Integer> gene, boolean variantSelector) {
        this.position = position;
        facingDirection = MapDirection.randomDirection();

        this.animalConfig = animalConfig;
        energy = animalConfig.getEnergyUsedForReproduction() * 2;

        this.variantSelector = variantSelector;

        this.gene = gene;
        currGene = ThreadLocalRandom.current().nextInt(animalConfig.getGeneLength());
    }

    // direction is a number in range 0-7 where 0 represents no rotation, 1 is a rotation by 45 deg
    // then moves forwards by 1 unit vector
    public void move(WorldMap world) {
        int direction = gene.get(currGene);
        if (variantSelector) {
            if (ThreadLocalRandom.current().nextInt(5) % 5 == 0) {
                currGene += ThreadLocalRandom.current().nextInt(animalConfig.getGeneLength() - 1);
            } else {
                currGene++;
            }
        } else {
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

        this.incrementDescendantsNumber();
        partner.incrementDescendantsNumber();

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

        Animal kid = new Animal(this.position, animalConfig, offspringGene, variantSelector);
        kid.setParents(this, partner);
        return kid;
    }


    public void incrementDescendantsNumber() {
        this.descendantsNumber++;
        if(parent1 != null) parent1.incrementDescendantsNumber();
        if(parent2 != null) parent2.incrementDescendantsNumber();
    }

    public Animal getParent1() {
        return parent1;
    }

    public Animal getParent2() {
        return parent2;
    }

    public void setParents(Animal animal1, Animal animal2) {
        parent1 = animal1;
        parent2 = animal2;
    }

    public boolean isAlive() {
        return energy > 0;
    }

    public Vector2d getPosition() {
        return position;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
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

    public void setAge(int age) {
        this.age = age;
    }

    public int getChildrenNumber() {
        return childrenNumber;
    }

    public int getDescendantsNumber() {
        return descendantsNumber;
    }

    public ArrayList<Integer> getGene() {
        return gene;
    }

    public MapDirection getFacingDirection() {
        return facingDirection;
    }

    public void setFacingDirection(MapDirection facingDirection) {
        this.facingDirection = facingDirection;
    }

    public int getCurrGene() {
        return currGene;
    }

    public void incrementGrassEaten() {
        grassEaten++;
    }

    public int getGrassEaten() {
        return grassEaten;
    }

    public void setDeathDay(int day) {
        this.deathDay = day;
    }

    public int getDeathDay() {
        return deathDay;
    }

    @Override
    public String toString() {
        return facingDirection.toString();
    }
}
