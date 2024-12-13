package agh.fcs.oop.model;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Animal implements WorldElement {
    private static final int GENE_SIZE = 10;
    private static final double REPRODUCTION_COST = 0.3;
    private static final double MINIMUM_ENERGY_FOR_REPRODUCTION = 10;
    private MapDirection facingDirection;
    private Vector2d position;
    private int energy;
    private ArrayList<Integer> gene = new ArrayList<>();
    private int currGene;

    public Animal() {
        facingDirection = MapDirection.NORTH;
        position = new Vector2d(2,2);
        energy = 20;
        currGene = 0;
        for (int i = 0; i < GENE_SIZE; i++) {
            gene.add(ThreadLocalRandom.current().nextInt(8));
        }
    }

    public Animal(Vector2d position, MapDirection facingDirection, int energy, ArrayList<Integer> gene) {
        this.position = position;
        this.facingDirection = facingDirection;
        this.energy = energy;
        this.gene = gene;
        currGene = 0;
    }

    public MapDirection getFacingDirection() {
        return facingDirection;
    }

    public void setFacingDirection(MapDirection facingDirection) {
        this.facingDirection = facingDirection;
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

    // direction is a number in range 0-7 where 0 represents no rotation, 1 is a rotation by 45 deg
    // then moves forwards by 1 unit vector
    public void move(WorldMap map, MoveValidator validator) {
        int direction = gene.get(currGene);
        currGene++;
        currGene = currGene % gene.size();

        this.facingDirection = this.facingDirection.rotateBy(direction);
        Vector2d newPosition = this.position.add(this.facingDirection.toUnitVector());
        if(validator.canMoveTo(newPosition)) {
            this.position = newPosition;
            this.energy--;
        }
    }

    public Animal reproduce(Animal partner) {
        if (this.energy < MINIMUM_ENERGY_FOR_REPRODUCTION && partner.energy < MINIMUM_ENERGY_FOR_REPRODUCTION) {
            return null;
        }
        int offspringEnergy = (int)(this.energy * REPRODUCTION_COST + partner.energy * REPRODUCTION_COST);
        this.energy = (int)(this.energy * (1-REPRODUCTION_COST));
        partner.energy = (int)(partner.energy * (1-REPRODUCTION_COST));

        double proportion = (double) this.energy / (this.energy + partner.energy);
        int middlePoint = (int)(proportion * GENE_SIZE);

        ArrayList<Integer> offspringGene = new ArrayList<>();
        for (int i = 0; i < GENE_SIZE; i++) {
            if (i < middlePoint) {
                offspringGene.add(ThreadLocalRandom.current().nextBoolean() ? this.gene.get(i) : partner.gene.get(i));
            } else {
                offspringGene.add(ThreadLocalRandom.current().nextBoolean() ? partner.gene.get(i) : this.gene.get(i));
            }
        }

        int mutations = ThreadLocalRandom.current().nextInt(1, GENE_SIZE + 1);
        for (int i = 0; i < mutations; i++) {
            int geneIndex = ThreadLocalRandom.current().nextInt(GENE_SIZE);
            offspringGene.set(geneIndex, ThreadLocalRandom.current().nextInt(8));
        }

        return new Animal(this.position, MapDirection.NORTH, offspringEnergy, offspringGene);
    }

    @Override
    public String toString() {
        return facingDirection.toString();
    }
}
