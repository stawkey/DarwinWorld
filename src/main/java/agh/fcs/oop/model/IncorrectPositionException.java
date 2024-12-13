package agh.fcs.oop.model;

public class IncorrectPositionException extends Exception {
    public IncorrectPositionException(Vector2d vector) {
        super("Position " + vector + " is not correct");
    }
}
