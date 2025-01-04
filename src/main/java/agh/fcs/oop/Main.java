package agh.fcs.oop;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        Simulation simulation = new Simulation(5, 5, 5, 5, 10, 5, 5, 1, 5, 3, 5, 8);
//        simulation.run();

        List<Simulation> simulations = new ArrayList<>();

        Simulation simulation = new Simulation(5, 5, 5, 5, 10, 5, 5, 1, 5, 3, 5, 8);
        Simulation simulation2 = new Simulation(5, 5, 5, 5, 10, 5, 5, 1, 5, 3, 5, 8);
        simulations.add(simulation);
        simulations.add(simulation2);

        SimulationEngine engine = new SimulationEngine(simulations);
        engine.runAsyncInThreadPool();
    }
}
