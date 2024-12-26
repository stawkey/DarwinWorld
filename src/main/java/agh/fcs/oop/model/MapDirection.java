package agh.fcs.oop.model;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    public String toString() {
        return switch (this) {
            case NORTH -> "North";
            case NORTHEAST -> "Northeast";
            case EAST -> "East";
            case SOUTHEAST -> "Southeast";
            case SOUTH -> "South";
            case SOUTHWEST -> "Southwest";
            case WEST -> "West";
            case NORTHWEST -> "Northwest";
        };
    }

    private static final MapDirection[] vals = values();
    public MapDirection rotateBy(int rotation) {
        return vals[(this.ordinal() + rotation) % vals.length];
    }

    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> new Vector2d(0,1);
            case NORTHEAST -> new Vector2d(1,1);
            case EAST -> new Vector2d(1,0);
            case SOUTHEAST -> new Vector2d(1,-1);
            case SOUTH -> new Vector2d(0,-1);
            case SOUTHWEST -> new Vector2d(-1,-1);
            case WEST -> new Vector2d(-1,0);
            case NORTHWEST -> new Vector2d(-1,1);
        };
    }

    public static MapDirection randomDirection()  {
        MapDirection[] directions = values();
        return directions[ThreadLocalRandom.current().nextInt(directions.length)];
    }
}
