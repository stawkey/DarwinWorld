package agh.fcs.oop.model;

public class ConsoleMapDisplay implements MapChangeListener {
    private int numberOfChanges = 0;

    @Override
    public void mapChanged(World world, String message) {
        synchronized (this.getClass()) {
            numberOfChanges++;
            System.out.println(world.getID());
            System.out.println("Change #" + numberOfChanges + ": " + message);
            System.out.println(world);
        }
    }
}

