package agh.fcs.oop.model;

public class ConsoleMapDisplay implements MapChangeListener {
    private int numberOfChanges = 0;

    @Override
    public void mapChanged(WorldMap map, String messsage) {
        numberOfChanges++;
        System.out.println("Change #" + numberOfChanges + ": " + messsage + ", MapID: " + map.getID());
        System.out.println(map);
    }
}

