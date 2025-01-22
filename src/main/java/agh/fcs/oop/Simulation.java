package agh.fcs.oop;

import agh.fcs.oop.model.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Simulation implements Runnable {
    private final World world;
    private final ArrayList<Animal> animalList;
    private final ArrayList<Animal> deadAnimals = new ArrayList<>();
    private final List<MapChangeListener> listeners = new ArrayList<>();
    private final int grassEnergy;
    private final int grassGrowth;
    private final int minEnergyForReproduction;
    private final int refreshTime;
    boolean animalFlag;
    boolean generatingCsv;
    private volatile boolean paused = false;
    private int totalDeadAnimalsAge = 0;
    private int day = 1;
    private BufferedWriter csvWriter;

    @Override
    public void run() {
        // Simulation loop
        while (!animalList.isEmpty()) {
            // Pausing
            synchronized (this) {
                while (paused) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            // Simulation logic
            try {
                // Remove dead animals
                List<Animal> toRemove = animalList.stream().filter(animal -> !animal.isAlive()).toList();
                toRemove.forEach(animal -> {
                    world.removeAnimal(animal);
                    animalList.remove(animal);
                    deadAnimals.add(animal);
                    totalDeadAnimalsAge += animal.getAge();
                    animal.setDeathDay(day);
                });

                // Animals movement + world poles
                for (Animal animal : animalList) {
                    world.move(animal);
                    if (world instanceof WorldPoles) {
                        if (animal.getPosition().y() >
                                world.getWidth() - 1 - (((WorldPoles) world).getPoleFields() / 2) ||
                                animal.getPosition().y() < ((WorldPoles) world).getPoleFields() / 2) {
                            animal.setEnergy(animal.getEnergy() - 2);
                        } else if (animal.getPosition().y() >
                                world.getWidth() - 1 - ((WorldPoles) world).getPoleFields() ||
                                animal.getPosition().y() < ((WorldPoles) world).getPoleFields()) {
                            animal.setEnergy(animal.getEnergy() - 1);
                        }
                    }
                }

                // Eating grass
                Map<Vector2d, WorldElement> allElements = world.getElements();
                for (Animal animal : animalList) {
                    Vector2d position = animal.getPosition();
                    if (allElements.get(position) instanceof Grass) {
                        animal.setEnergy(animal.getEnergy() + grassEnergy);
                        animal.incrementGrassEaten();
                        world.removeGrass(position);
                    }
                }

                // Animals reproduction
                Map<Vector2d, List<Animal>> animalsGroupedByPosition =
                        animalList.stream().filter(a -> a.getEnergy() > minEnergyForReproduction)
                                .collect(Collectors.groupingBy(Animal::getPosition));

                animalsGroupedByPosition.values().stream().map(a -> a.stream()
                                .sorted(Comparator.comparingInt(Animal::getEnergy).reversed().thenComparingInt(Animal::getAge)
                                        .reversed().thenComparingInt(Animal::getChildrenNumber).reversed()).limit(2).toList())
                        .filter(pair -> pair.size() == 2).forEach(pair -> {
                            Animal offspring = pair.get(0).reproduce(pair.get(1));
                            animalList.add(offspring);
                            try {
                                world.place(offspring);
                            } catch (IncorrectPositionException e) {
                                System.out.println("Incorrect position: " + offspring.getPosition());
                            }
                        });

                // Grow new grass
                world.generatingGrass(grassGrowth);
                notifySimulationStep("");
                if (generatingCsv) {
                writeStatisticsToCsv(day);
                }
                day++;
                Thread.sleep(refreshTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if (generatingCsv) {
        closeCsvWriter();
        }
    }

    public synchronized void pause() {
        paused = true;
    }

    public synchronized void resume() {
        paused = false;
        notify();
    }

    public boolean isPaused() {
        return paused;
    }

    public ArrayList<Animal> getAnimalList() {
        return animalList;
    }

    public void addListener(MapChangeListener listener) {
        listeners.add(listener);
    }

    private void notifySimulationStep(String message) {
        for (MapChangeListener listener : listeners) {
            listener.mapChanged(world, message);
        }
    }

    public World getWorld() {
        return world;
    }

    public List<Integer> getMostPopularGene() {
        synchronized (animalList) {
            Map<List<Integer>, Integer> geneFrequency = new HashMap<>();

            for (Animal animal : animalList) {
                if (animal.isAlive()) {
                    List<Integer> gene = Collections.unmodifiableList(animal.getGene());
                    geneFrequency.put(gene, geneFrequency.getOrDefault(gene, 0) + 1);
                }
            }

            return geneFrequency.entrySet().stream().max(Map.Entry.comparingByValue())
                    .orElseThrow(() -> new IllegalStateException("No genes found")).getKey();
        }
    }

    public double getAverageEnergy() {
        if (animalList.isEmpty()) {
            return 0;
        }
        int totalEnergy = animalList.stream().mapToInt(Animal::getEnergy).sum();
        return Math.round((double) totalEnergy / animalList.size() * 100.0) / 100.0;
    }

    public double getAverageLifeSpan() {
        if (deadAnimals.isEmpty()) {
            return 0;
        }
        return Math.round((double) totalDeadAnimalsAge / deadAnimals.size() * 100.0) / 100.0;
    }

    public double getAverageChildrenCount() {
        if (animalList.isEmpty()) {
            return 0;
        }
        int totalChildren = animalList.stream().mapToInt(Animal::getChildrenNumber).sum();
        return Math.round((double) totalChildren / animalList.size() * 100.0) / 100.0;
    }

    public int getDay() {
        return day;
    }

    public void initializeCsvWriter() {
        String fileName = "simulation_" + world.hashCode() + ".csv";
        try {
            csvWriter = new BufferedWriter(new FileWriter(fileName));
            csvWriter.write(world.getWidth() + ";" + world.getHeight() + ";" + world.hashCode());
            csvWriter.newLine();
            csvWriter.write("Day;AnimalsCount;GrassCount;EmptyFields;MostPopularGene;AverageEnergy;AverageLifespan;" +
                    "AverageChildrenCount");
            csvWriter.newLine();
        } catch (IOException e) {
            System.err.println("Error initializing CSV writer: " + e.getMessage());
        }
    }

    public void writeStatisticsToCsv(int day) {
        try {
            csvWriter.write(day + ";" + world.animalCount() + ";" + world.grassCount() + ";" +
                    (world.getHeight() * world.getWidth() - world.takenFields()) + ";" + getMostPopularGene() + ";" +
                    getAverageEnergy() + ";" + getAverageLifeSpan() + ";" + getAverageChildrenCount());
            csvWriter.newLine();
            csvWriter.flush();
        } catch (IOException e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
        }
    }

    public void closeCsvWriter() {
        if (csvWriter != null) {
            try {
                csvWriter.close();
                csvWriter = null;
            } catch (IOException e) {
                System.err.println("Error closing CSV writer: " + e.getMessage());
            }
        }
    }

    public Simulation(String mapType, String animalType, int width, int height, int startGrassCount,
                      int startAnimalCount, int startAnimalEnergy, int minReproductionEnergy,
                      int energyUsedForReproduction, int minMutationCount, int maxMutationCount, int grassEnergy,
                      int grassGrowth, int geneLength, int refreshTime, boolean generatingCsv) {
        if (mapType.equals("Globe")) {
            this.world = new WorldGlobe(width, height, startGrassCount);
        } else if (mapType.equals("Poles")) {
            this.world = new WorldPoles(width, height, startGrassCount);
        } else {
            this.world = new World(width, height, startGrassCount);
        }
        this.animalFlag = animalType.equals("Crazy animals");
        ConsoleMapDisplay observer = new ConsoleMapDisplay();
        this.world.addObserver(observer);

        this.animalList = new ArrayList<>();
        AnimalConfig animalConfig =
                new AnimalConfig(startAnimalEnergy, energyUsedForReproduction, minMutationCount, maxMutationCount,
                        geneLength);
        for (int i = 0; i < startAnimalCount; i++) {
            Vector2d position = new Vector2d(ThreadLocalRandom.current().nextInt(0, width - 1),
                    ThreadLocalRandom.current().nextInt(0, height - 1));
            animalList.add(new Animal(position, animalConfig, animalFlag));
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
        this.refreshTime = refreshTime;
        this.generatingCsv = generatingCsv;
        if (generatingCsv) {
            initializeCsvWriter();
        }
    }
}
