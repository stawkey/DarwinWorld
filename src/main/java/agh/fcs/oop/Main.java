package agh.fcs.oop;

import agh.fcs.oop.model.Animal;
import agh.fcs.oop.model.Vector2d;
import agh.fcs.oop.model.World;

public class Main {
    public static void main(String[] args) {
//        System.out.println("test");
//        World world = new World(3, 5, 5);
        Simulation simulation = new Simulation(10, 10, 5, 5, 20, 10, 5, 1, 5, 5, 5, 8);
        simulation.run();
    }
}
