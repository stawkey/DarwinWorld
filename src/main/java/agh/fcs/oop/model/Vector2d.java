package agh.fcs.oop.model;

import java.util.Objects;

public record Vector2d(int x, int y) {

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean precedes(Vector2d other) {
        return x <= other.x && y <= other.y;
    }

    public boolean follows(Vector2d other) {
        return x >= other.x && y >= other.y;
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(x + other.x, y + other.y);
    }

    public Vector2d subtract(Vector2d other) {
        return new Vector2d(x - other.x, y - other.y);
    }

    public Vector2d upperRight(Vector2d other) {
        int tempX = other.x;
        int tempY = other.y;
        if (x > other.x) {
            tempX = x;
        }
        if (y > other.y) {
            tempY = y;
        }
        return new Vector2d(tempX, tempY);
    }

    public Vector2d lowerLeft(Vector2d other) {
        int tempX = other.x;
        int tempY = other.y;
        if (x < other.x) {
            tempX = x;
        }
        if (y < other.y) {
            tempY = y;
        }
        return new Vector2d(tempX, tempY);
    }

    public Vector2d opposite() {
        return new Vector2d(-x, -y);
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) return true;
//        if (obj == null || getClass() != obj.getClass()) return false;
//        Vector2d vector2d = (Vector2d) obj;
//        return x == vector2d.x && y == vector2d.y;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(x, y);
//    }
}
