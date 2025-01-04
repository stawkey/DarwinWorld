package agh.fcs.oop;

import agh.fcs.oop.model.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Simulation {
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

        this.grassEnergy = grassEnergy;
        this.grassGrowth = grassGrowth;
        this.minEnergyForReproduction = minReproductionEnergy;
    }

    public void run() {
        // Remove dead animals
        animalList.removeIf(animal -> {
            if (!animal.isAlive()) {
                world.removeAnimal(animal);
                animalList.remove(animal);
                return true;
            }
            return false;
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
                    pair.get(0).reproduce(pair.get(1));
                });

        // Generating new grass
//        System.out.println("Before: " + world.getGrassMap());
        world.generatingGrasses(grassGrowth);
//        System.out.println("After: " + world.getGrassMap());
    }
}
