package agh.fcs.oop.model;

import java.util.*;

public class GeneralGrassGenerator implements Iterator<Vector2d>, Iterable<Vector2d> {
    protected final int minWidth;
    protected final int minHeight;
    protected final int maxWidth;
    protected final int maxHeight;
    protected final int grassCount;
    protected List<Vector2d> possiblePlaces;
    protected int counter = 0;
    protected Random random = new Random();

    private final Map<Vector2d, Grass> grassMap;

    public GeneralGrassGenerator(int minWidth, int minHeight, int maxWidth, int maxHeight,
                                 int grassCount, Map<Vector2d, Grass> grassMap) {
        this.minWidth = minWidth;
        this.minHeight = minHeight;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.grassCount = grassCount;
        this.grassMap = grassMap;
        this.possiblePlaces = generatingPossibilities();
        Collections.shuffle(possiblePlaces);
    }

    public List<Vector2d> generatingPossibilities() {
        // in this method we generate ArrayList with places where we can possibly
        // place grass, but first we check if that position is not in our
        // array with all grass from map
        List<Vector2d> positions = new ArrayList<>();
        for(int x = minWidth; x <= maxWidth; x++) {
            for(int y = minHeight; y <= maxHeight; y++) {
                if (!grassMap.containsKey(new Vector2d(x, y))) {
                    positions.add(new Vector2d(x, y));
                }
            }
        }
        return positions;
    }

    @Override
    public boolean hasNext() {
        return counter < grassCount && !possiblePlaces.isEmpty();
    }

    @Override
    public Vector2d next() {
        // on every iteration we generate new grass position and return it
        // at the same time we remove that position from base array
        // (so it doesn't appear again)
        if (hasNext()) {
            int chosenPlace = random.nextInt(possiblePlaces.size());
            Vector2d generatedVector = possiblePlaces.get(chosenPlace);
            possiblePlaces.remove(chosenPlace);
            counter++;
            return generatedVector;
        }
        throw new UnsupportedOperationException("No more positions to generate");
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return this;
    }

}
