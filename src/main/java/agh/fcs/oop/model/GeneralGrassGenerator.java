package agh.fcs.oop.model;

import java.util.*;

// TODO: merging both generators into one + bugfixing

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

//    public GeneralGrassGenerator(int minWidth, int minHeight, int maxWidth, int maxHeight, int grassCount, Map<Vector2d, Grass> grassMap) {
//        super(minWidth, minHeight, maxWidth, maxHeight, grassCount);
//        System.out.println(grassMap);
//        this.grassMap = grassMap;
//        System.out.println(grassMap);
//    }


    public List<Vector2d> generatingPossibilities() {
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
