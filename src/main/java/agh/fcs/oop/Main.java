package agh.fcs.oop;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Simulation> simulations = new ArrayList<>();

        Simulation simulation = new Simulation("No variant", "Random gene", 20, 10, 10, 5, 10, 5, 5, 1, 5, 3, 10, 8, 100, false);
        Simulation simulation2 = new Simulation("No variant", "Random gene", 5, 5, 5, 5, 10, 5, 5, 1, 5, 3, 5, 8, 100, false);
        simulations.add(simulation);
        simulations.add(simulation2);

        SimulationEngine engine = new SimulationEngine(simulations);
        engine.runAsync();
    }
}
