package agh.fcs.oop.model;

public class ConsoleMapDisplay implements MapChangeListener {
    private int numberOfChanges = 0;

    @Override
    public void mapChanged(WorldMap map, String message) {
        synchronized (this) {
            numberOfChanges++;
            System.out.println(map.getID());
            System.out.println("Change #" + numberOfChanges + ": " + message);
            System.out.println(map);
        }
    }
}

