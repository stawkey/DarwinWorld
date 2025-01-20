package agh.fcs.oop;

import agh.fcs.oop.model.MapChangeListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    private final List<Simulation> simulations;
    private final List<Thread> threads = new ArrayList<>();
    ExecutorService executorService = Executors.newFixedThreadPool(4);
    private final List<MapChangeListener> listeners = new ArrayList<>();

    public SimulationEngine(List<Simulation> simulations) {
        this.simulations = simulations;
    }

    public void addListener(MapChangeListener listener) {
        listeners.add(listener);
    }

    public void notifyListeners(Simulation simulation, String message) {
        for (MapChangeListener listener : listeners) {
            listener.mapChanged(simulation.getWorld(), message);
        }
    }

    public void runSync() {
        for (Simulation simulation : simulations) {
            simulation.addListener((world, message) -> notifyListeners(simulation, message));
            simulation.run();
            notifyListeners(simulation, "Simulation step executed synchronously");
        }
    }

    public void runAsync() {
        for (Simulation simulation : simulations) {
            simulation.addListener((world, message) -> notifyListeners(simulation, message));
            Thread thread = new Thread(simulation);
            threads.add(thread);
            thread.start();
            notifyListeners(simulation, "Simulation step executed asynchronously");
        }
        awaitSimulationsEnd();
    }

    public void runAsyncInThreadPool() {
        for (Simulation simulation : simulations) {
            simulation.addListener((world, message) -> notifyListeners(simulation, message));
            executorService.submit(simulation);
            notifyListeners(simulation, "Simulation step executed asynchronously");
        }
        awaitSimulationsEnd();
    }

    public void awaitSimulationsEnd() {
        try {
            for (Thread thread : threads) {
                thread.join();
            }
            executorService.shutdown();
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        }
        catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
