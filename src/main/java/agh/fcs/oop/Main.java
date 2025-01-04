package agh.fcs.oop;

import agh.fcs.oop.model.Animal;
import agh.fcs.oop.model.Vector2d;
import agh.fcs.oop.model.World;

public class Main {
    public static void main(String[] args) {
        Simulation simulation = new Simulation(5, 5, 5, 5, 10, 5, 5, 1, 5, 3, 5, 8);
        simulation.run();
    }
}
