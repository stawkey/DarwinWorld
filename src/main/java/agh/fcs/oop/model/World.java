package agh.fcs.oop.model;

import agh.fcs.oop.model.util.MapVisualizer;

import java.util.*;

public class World implements WorldMap {
    protected final int width;
    protected final int height;
    protected final Vector2d bottomLeft;
    protected final Vector2d upperRight;
    protected Vector2d equatorLeftCorner;
    protected Vector2d equatorRightCorner;
    protected final int startingGrassAmount;
    protected Map<Vector2d, Animal> animalMap = new HashMap<>();
    protected Map<Vector2d, Grass> grassMap = new HashMap<>();
    protected Map<Vector2d, WorldElement> allElements = new HashMap<>();
    protected MapVisualizer visualizer = new MapVisualizer(this);
    protected int mapID = this.hashCode();
    protected List<MapChangeListener> observers = new ArrayList<>();

    public World(int width, int height, int startingGrassAmount) {
        this.width = width;
        this.height = height;
        this.bottomLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width - 1, height - 1);
        this.equatorLeftCorner = null;
        this.equatorRightCorner = null;
        if (height == 1 || height == 2) {
            this.equatorLeftCorner = new Vector2d(0,0);
            this.equatorRightCorner = new Vector2d(width - 1, 0);
        }
        else switch (height % 5) {
            case (0) -> {
                this.equatorLeftCorner = new Vector2d(0, (int)(0.4 * height));
                this.equatorRightCorner = new Vector2d(width - 1, equatorLeftCorner.y() + height/5 - 1);
            }
            case (1) -> {
                this.equatorLeftCorner = new Vector2d(0, (int)(0.4 * (height - 1)));
                this.equatorRightCorner = new Vector2d(width - 1, equatorLeftCorner.y() + (height - 1)/5 - 1);
            }
            case (2) -> {
                this.equatorLeftCorner = new Vector2d(0, (int)(0.4 * (height - 2) + 1));
                this.equatorRightCorner = new Vector2d(width - 1, equatorLeftCorner.y() + (height - 2)/5 - 1);
            }
            case (3) -> {
                this.equatorLeftCorner = new Vector2d(0, (int)(0.4 * (height - 3) + 1));
                this.equatorRightCorner = new Vector2d(width - 1, equatorLeftCorner.y() + (height - 3)/5);
            }
            case (4) -> {
                this.equatorLeftCorner = new Vector2d(0, (int)(0.4 * (height - 4) + 1));
                this.equatorRightCorner = new Vector2d(width - 1, equatorLeftCorner.y() + (height - 4)/5);
            }
        }
        this.startingGrassAmount = startingGrassAmount;
//        int equatorStartingGrasses = (int) Math.ceil(startingGrassAmount * 0.8);
//        int otherStartingGrasses = startingGrassAmount - equatorStartingGrasses;
//
//        StartingGrassGenerator equatorPositionGenerator = new StartingGrassGenerator(equatorLeftCorner.x(),
//                equatorLeftCorner.y(), equatorRightCorner.x(), equatorRightCorner.y(), equatorStartingGrasses);
//        for (Vector2d grassPosition : equatorPositionGenerator) {
//            grassMap.put(grassPosition, new Grass(grassPosition));
//        }
//        int upperOtherGrasses = (int) (Math.random() * otherStartingGrasses);
//        int lowerOtherGrasses = otherStartingGrasses = upperOtherGrasses;
//
//        StartingGrassGenerator upperPositionGenerator = new StartingGrassGenerator(0, equatorRightCorner.y() + 1,
//                width - 1, height - 1, upperOtherGrasses);
//        for (Vector2d grassPosition : upperPositionGenerator) {
//            grassMap.put(grassPosition, new Grass(grassPosition));
//        }
//
//        StartingGrassGenerator lowerPositionGenerator = new StartingGrassGenerator(0, 0, width - 1,
//                equatorLeftCorner.y(), lowerOtherGrasses);
//        for (Vector2d grassPosition : lowerPositionGenerator) {
//            grassMap.put(grassPosition, new Grass(grassPosition));
//        }
        generatingGrass(startingGrassAmount);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Vector2d getEquatorLeftCorner() {
        return equatorLeftCorner;
    }

    public Vector2d getEquatorRightCorner() {
        return equatorRightCorner;
    }

    public List<Vector2d> preferredGrassPositions() {
        List<Vector2d> betterGrass = new ArrayList<>();
        for (int i = equatorLeftCorner.x(); i <= equatorRightCorner.x(); i++) {
            for (int j = 0; j <= width - 1; j++) {
                betterGrass.add(new Vector2d(i, j));
            }
        }
        return betterGrass;
    }

    public List<Vector2d> otherGrassPositions() {
        List<Vector2d> worseGrass = new ArrayList<>();
        for (int i = 0; i < equatorLeftCorner.y(); i++) {
            for (int j = 0; j <= width - 1; j++) {
                worseGrass.add(new Vector2d(i, j));
            }
        }
        for (int i = equatorRightCorner.y() + 1; i <= height - 1; i++) {
            for (int j = 0; j <= width - 1; j++) {
                worseGrass.add(new Vector2d(i, j));
            }
        }
        return worseGrass;
    }

    public void generatingGrass(int grassGrowth) {
        int equatorGrass = (int) Math.ceil(grassGrowth * 0.8);
        int tempGrassAmount = grassMap.size();

        GeneralGrassGenerator equatorPositionGenerator = new GeneralGrassGenerator(equatorLeftCorner.x(),
                equatorLeftCorner.y(), equatorRightCorner.x(), equatorRightCorner.y(), equatorGrass, grassMap);
        for (Vector2d grassPosition : equatorPositionGenerator) {
            grassMap.put(grassPosition, new Grass(grassPosition));
        }

        int placedGrass = grassMap.size() - tempGrassAmount;
        int otherGrass = grassGrowth - placedGrass;
        int upperOtherGrass = (int) Math.round(Math.random() * otherGrass);
        tempGrassAmount = grassMap.size();

        GeneralGrassGenerator upperPositionGenerator = new GeneralGrassGenerator(0, equatorRightCorner.y() + 1,
                width - 1, height - 1, upperOtherGrass, grassMap);
        for (Vector2d grassPosition : upperPositionGenerator) {
            grassMap.put(grassPosition, new Grass(grassPosition));
        }
        placedGrass = grassMap.size() - tempGrassAmount;
        int lowerOtherGrasses = otherGrass - placedGrass;

        GeneralGrassGenerator lowerPositionGenerator = new GeneralGrassGenerator(0, 0, width - 1,
                equatorLeftCorner.y() - 1, lowerOtherGrasses, grassMap);
        for (Vector2d grassPosition : lowerPositionGenerator) {
            grassMap.put(grassPosition, new Grass(grassPosition));
        }
    }

    public boolean canMoveTo(Vector2d position) {
        return position.follows(bottomLeft) && position.precedes(upperRight);
    }

    public boolean place(Animal animal) throws IncorrectPositionException {
        if (canMoveTo(animal.getPosition())) {
            animalMap.put(animal.getPosition(), animal);
            return true;
        } else {
            throw new IncorrectPositionException(animal.getPosition());
        }
    }

    public void removeAnimal(Animal animal) {
        if (animal.getPosition() != null) {
            animalMap.remove(animal.getPosition(), animal);
        }
    }

    public void removeGrass(Vector2d position) {
        grassMap.remove(position);
    }

    public WorldElement objectAt(Vector2d position) {
        if (animalMap.get(position) != null) return animalMap.get(position);
        else if (grassMap.get(position) != null) return grassMap.get(position);
        return null;
    }

    @Override
    public void move(Animal animal) {
        Vector2d prevPosition = animal.getPosition();
        MapDirection prevDirection = animal.getFacingDirection();
        animal.move(this);
        animalMap.remove(prevPosition);
        animalMap.put(animal.getPosition(), animal);
    }

    public boolean isOccupied(Vector2d position) {
        return animalMap.containsKey(position) || grassMap.containsKey(position);
    }


    public Map<Vector2d, WorldElement> getElements() {
        observersNotification("");
        allElements.putAll(animalMap);
        allElements.putAll(grassMap);
        return allElements;
    }

    public void observersNotification(String message) {
        for (MapChangeListener observer : observers) {
            observer.mapChanged(this, message);
        }
    }

    public void addObserver(MapChangeListener observer) {
        observers.add(observer);
    }

    public void removeObserver(MapChangeListener observer) {
        observers.remove(observer);
    }

    public int getID() {
        return mapID;
    }

    @Override
    public String toString() {
        return visualizer.draw(bottomLeft, upperRight);
    }

    public int animalCount() {
        return animalMap.size();
    }

    public int grassCount() {
        return grassMap.size();
    }

    public int takenFields() {
        Set<Vector2d> uniqueKeys = new HashSet<>();
        uniqueKeys.addAll(animalMap.keySet());
        uniqueKeys.addAll(grassMap.keySet());
        return uniqueKeys.size();
    }
}
