package agh.fcs.oop;

public class Main {
    public static void main(String[] args) {
        Simulation simulation = new Simulation(5, 5, 30, 30, 20, 10, 5, 1, 5, 5, 5, 8);
        simulation.run();
    }
}
