package agh.fcs.oop.model;

import java.util.*;

public class StartingGrassGenerator implements Iterator<Vector2d>, Iterable<Vector2d> {

    protected final int minWidth;
    protected final int minHeight;
    protected final int maxWidth;
    protected final int maxHeight;
    protected final int grassCount;
    protected List<Vector2d> possiblePlaces;
    protected int counter = 0;
    protected Random random = new Random();

    public StartingGrassGenerator(int minWidth, int minHeight, int maxWidth, int maxHeight, int grassCount) {
        this.minWidth = minWidth;
        this.minHeight = minHeight;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.grassCount = grassCount;
        this.possiblePlaces = generatingPossibilities();
        Collections.shuffle(possiblePlaces);
    }

    public int getGrassCount() {
        return grassCount;
    }

    public List<Vector2d> generatingPossibilities() {
        List<Vector2d> positions = new ArrayList<>();
        for(int x = minWidth; x < maxWidth; x++) {
            for(int y = minHeight; y < maxHeight; y++) {
                positions.add(new Vector2d(x, y));
            }
        }
//        System.out.println(positions);
        return positions;
    }

    @Override
    public boolean hasNext() {
        return counter < grassCount && !possiblePlaces.isEmpty();
    }

    @Override
    public Vector2d next() {
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

