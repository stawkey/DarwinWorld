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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;


public class SimulationPresenter implements MapChangeListener {
    @FXML
    public Button toggleSimulationButton;
    @FXML
    public Text animalsCount;
    @FXML
    public Text grassCount;
    @FXML
    public Text emptyFields;
    @FXML
    public Text mostPopularGene;
    @FXML
    public Text averageEnergy;
    @FXML
    public Text averageLifespan;
    @FXML
    public Text averageChildren;
    @FXML
    private Label descriptionLabel;
    @FXML
    private GridPane mapGrid;

    private Simulation simulation;
    private World world;
    private int width = 100;
    private int height = 100;
    private int maxWidth = 300;
    private int maxHeight = 300;

    private int xMin;
    private int yMin;
    private int xMax;
    private int yMax;
    private int mapWidth;
    private int mapHeight;

    private SimulationConfig simulationConfig;

    public void setSimulationConfig(SimulationConfig config) {
        this.simulationConfig = config;
    }

    public void initialize() {
        Platform.runLater(this::initializeSimulation);
    }

    private void initializeSimulation() {
        if (simulation == null) {
            simulation = new Simulation(
                    simulationConfig.getWidth(), simulationConfig.getHeight(), simulationConfig.getGrassCount(),
                    simulationConfig.getAnimalCount(), simulationConfig.getAnimalEnergy(),
                    simulationConfig.getReproductionMinEnergy(), simulationConfig.getReproductionUsedEnergy(),
                    simulationConfig.getMinMutationCount(), simulationConfig.getMaxMutationCount(),
                    simulationConfig.getGrassEnergy(), simulationConfig.getGrassGrowth(), simulationConfig.getGeneLength()
            );
            this.world = simulation.getWorld();
            SimulationEngine engine = new SimulationEngine(List.of(simulation));
            engine.addListener(this);

            new Thread(engine::runSync).start();
        }
    }

    public void toggleSimulation(ActionEvent actionEvent) {
        if (simulation.isPaused()) {
            simulation.resume();
            toggleSimulationButton.setText("Pause");
        } else {
            simulation.pause();
            toggleSimulationButton.setText("Resume");
        }
    }

    public void mapChanged(World world, String message) {
        this.world = world;
        Platform.runLater(() -> {
            clearGrid();
            drawMap();
            descriptionLabel.setText(message);

            // Statistics
            animalsCount.setText("Animals alive: " + world.animalCount());
            grassCount.setText("Grasses: " + world.grassCount());
            emptyFields.setText("Empty fields: " + (mapHeight*mapWidth - world.takenFields()));
            mostPopularGene.setText("Most popular gene: " + simulation.getMostPopularGene());
            averageEnergy.setText("Average animal energy: " + simulation.getAverageEnergy());
            averageLifespan.setText("Average lifespan: " + simulation.getAverageLifeSpan());
            averageChildren.setText("Average children count: " + simulation.getAverageChildrenCount());
        });
    }

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
}
