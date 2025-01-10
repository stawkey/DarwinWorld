package agh.fcs.oop;

import agh.fcs.oop.model.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Simulation implements Runnable {
    private final World world;
    private ArrayList<Animal> animalList;
    private final int grassEnergy;
    private final int grassGrowth;
    private final int minEnergyForReproduction;

    public Simulation(int width, int height, int startGrassCount, int startAnimalCount, int startAnimalEnergy,
                      int minReproductionEnergy, int energyUsedForReproduction, int minMutationCount,
                      int maxMutationCount, int grassEnergy, int grassGrowth, int geneLength) {
        this.world = new World(width, height, startGrassCount);
        ConsoleMapDisplay observer = new ConsoleMapDisplay();
        this.world.addObserver(observer);

        this.animalList = new ArrayList<>();
        AnimalConfig animalConfig = new AnimalConfig(startAnimalEnergy, energyUsedForReproduction, minMutationCount,
                maxMutationCount, geneLength);
        for (int i = 0; i < startAnimalCount; i++) {
            Vector2d position = new Vector2d(ThreadLocalRandom.current().nextInt(0, width - 1),
                    ThreadLocalRandom.current().nextInt(0, height - 1));
            animalList.add(new Animal(position, animalConfig));
        }

        for (Animal a : animalList) {
            try {
                world.place(a);
            } catch (IncorrectPositionException e) {
                System.out.println("Incorrect position: " + a.getPosition());
            }
        }

        this.grassEnergy = grassEnergy;
        this.grassGrowth = grassGrowth;
        this.minEnergyForReproduction = minReproductionEnergy;
    }

    @Override
    public void run() {
        while (!animalList.isEmpty()) {
            // Remove dead animals
            List<Animal> toRemove = animalList.stream()
                    .filter(animal -> !animal.isAlive())
                    .toList();

            toRemove.forEach(animal -> {
                world.removeAnimal(animal);
                animalList.remove(animal);
            });

            // Animals movement
            for (Animal animal : animalList) {
                world.move(animal);
            }

            // Eating
            Map<Vector2d, WorldElement> allElements = world.getElements();
            for (Animal animal : animalList) {
                Vector2d position = animal.getPosition();
                if (allElements.get(position) instanceof Grass) {
                    animal.setEnergy(animal.getEnergy() + grassEnergy);
                    world.removeGrass(position);
                }
            }

            // Reproduction
            Map<Vector2d, List<Animal>> animalsGroupedByPosition = animalList.stream()
                    .filter(a -> a.getEnergy() > minEnergyForReproduction)
                    .collect(Collectors.groupingBy(Animal::getPosition));

            animalsGroupedByPosition.values().stream()
                    .map(a -> a.stream()
                            .sorted(Comparator.comparingInt(Animal::getEnergy).reversed()
                                    .thenComparingInt(Animal::getAge).reversed()
                                    .thenComparingInt(Animal::getChildrenNumber).reversed())
                            .limit(2)
                            .toList())
                    .filter(pair -> pair.size() == 2)
                    .forEach(pair -> {
                        Animal offspring = pair.get(0).reproduce(pair.get(1));
                        animalList.add(offspring);
                        try {
                            world.place(offspring);
                        } catch (IncorrectPositionException e) {
                            System.out.println("Incorrect position: " + offspring.getPosition());
                        }
                    });

            // Generating new grass
//        System.out.println("Before: " + world.getGrassMap());
            world.generatingGrass(grassGrowth);
//        System.out.println("After: " + world.getGrassMap());

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.err.println("Simulation interrupted: " + e.getMessage());
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public ArrayList<Animal> getAnimalList() {
        return animalList;
    }
}
