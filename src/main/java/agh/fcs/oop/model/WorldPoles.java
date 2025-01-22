package agh.fcs.oop.model;

public class WorldPoles extends World {

    private int poleFields;

    public WorldPoles(int width, int height, int startingGrassAmount) {
        super(width, height, startingGrassAmount);
        this.poleFields = height / 5 + 1;
    }


    public int getPoleFields() {
        return poleFields;
    }
}
