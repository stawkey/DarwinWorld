package agh.fcs.oop.GUI;

import agh.fcs.oop.Simulation;
import agh.fcs.oop.SimulationEngine;
import agh.fcs.oop.model.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;


public class SimulationPresenter implements MapChangeListener {
    public Button toggleSimulationButton;
    private Simulation simulation;
    private World world;
    @FXML
    private Label infoLabel;
    @FXML
    private TextField listOfMoves;
    @FXML
    private Label descriptionLabel;
    @FXML
    private GridPane mapGrid;
    private int width = 100;
    private int height = 100;
    private int maxWidth = 700;
    private int maxHeight = 700;

    private int xMin;
    private int yMin;
    private int xMax;
    private int yMax;
    private int mapWidth;
    private int mapHeight;

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
        this.world = simulation.getWorld();
    }

    public void drawMap() {
        clearGrid();
        updateBounds();
        xyLabel();
        columnsFunction();
        rowsFunction();
        addElements();
        mapGrid.setGridLinesVisible(true);
    }

    public void mapChanged(World world, String message) {
        this.world = world;
        Platform.runLater(() -> {
            clearGrid();
            drawMap();
            descriptionLabel.setText(message);
        });
    }

    private void clearGrid(){
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0));
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    public void xyLabel(){
        mapGrid.getColumnConstraints().add(new ColumnConstraints(width));
        mapGrid.getRowConstraints().add(new RowConstraints(height));
        Label label = new Label("y/x");
        mapGrid.add(label, 0, 0);
        GridPane.setHalignment(label, HPos.CENTER);
    }

    public void updateBounds(){
        xMin = 0;
        yMin = 0;
        xMax = world.getWidth() - 1;
        yMax = world.getHeight() - 1;
        mapWidth = xMax + 1;
        mapHeight = yMax + 1;
        width = Math.round(maxWidth/mapWidth);
        height = Math.round(maxHeight/mapHeight);
        height = Math.min(height, width);
        width = height;
    }

    public void columnsFunction(){
        for(int i=0; i<mapWidth; i++){
            Label label = new Label(Integer.toString(i+xMin));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.getColumnConstraints().add(new ColumnConstraints(width));
            mapGrid.add(label, i+1, 0);
        }
    }

    public void rowsFunction(){
        for(int i=0; i<mapHeight; i++){
            Label label = new Label(Integer.toString(yMax-i));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.getRowConstraints().add(new RowConstraints(height));
            mapGrid.add(label, 0, i+1);
        }
    }

    public void addElements(){
        for (int i = xMin; i <= xMax; i++) {
            for (int j = yMax; j >= yMin; j--) {
                Vector2d pos = new Vector2d(i, j);
                if (world.isOccupied(pos)) {
                    mapGrid.add(new Label(world.objectAt(pos).toString()), i - xMin + 1, yMax - j + 1);
                }
                else {
                    mapGrid.add(new Label(" "), i - xMin + 1, yMax - j + 1);
                }
                mapGrid.setHalignment(mapGrid.getChildren().get(mapGrid.getChildren().size() - 1), HPos.CENTER);
            }
        }
    }

    public void toggleSimulation(ActionEvent actionEvent) {
        if(simulation == null) {
            String moveList = listOfMoves.getText();
            simulation = new Simulation(20, 10, 10, 10, 10, 5, 5, 1, 5, 3, 5, 8);
            this.world = simulation.getWorld();
            SimulationEngine engine = new SimulationEngine(List.of(simulation));
            engine.addListener(this);
            descriptionLabel.setText("Simulation started with moves: " + moveList);
            toggleSimulationButton.setText("Pause");
            new Thread(engine::runSync).start();
        }
        else if(simulation.isPaused()) {
            simulation.resume();
            toggleSimulationButton.setText("Pause");
        }
        else {
            simulation.pause();
            toggleSimulationButton.setText("Resume");
        }
    }

    @FXML
    private void newGame(){
        SimulationApp simulationApp = new SimulationApp();

        try {
            simulationApp.start(new Stage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
