package agh.fcs.oop.GUI;

import agh.fcs.oop.Simulation;
import agh.fcs.oop.SimulationEngine;
import agh.fcs.oop.model.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class SimulationPresenter implements MapChangeListener {
    @FXML
    public Button toggleSimulationButton;
    @FXML
    public Text day;
    @FXML
    private Text selectedAnimalGenome;
    @FXML
    private Text selectedAnimalActiveGene;
    @FXML
    private Text selectedAnimalEnergy;
    @FXML
    private Text selectedAnimalGrassEaten;
    @FXML
    private Text selectedAnimalChildren;
    @FXML
    private Text selectedAnimalAge;
    @FXML
    private Text selectedAnimalDeathDay;
    @FXML
    private Button stopTrackingButton;
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
    public CheckBox highlightPopularGene;
    @FXML
    private Label descriptionLabel;
    @FXML
    private GridPane mapGrid;

    private Animal selectedAnimal;
    private boolean showPopularGene;

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
        Platform.runLater(() -> {
            initializeSimulation();
            Stage stage = (Stage) mapGrid.getScene().getWindow();
            stage.setOnCloseRequest(event -> onCloseWindow());
        });
        stopTrackingButton.setVisible(selectedAnimal != null);
        stopTrackingButton.setManaged(selectedAnimal != null);
    }

    private void initializeSimulation() {
        if (simulation == null) {
            showPopularGene = false;
            simulation = new Simulation(simulationConfig.getWidth(), simulationConfig.getHeight(),
                    simulationConfig.getGrassCount(), simulationConfig.getAnimalCount(),
                    simulationConfig.getAnimalEnergy(), simulationConfig.getReproductionMinEnergy(),
                    simulationConfig.getReproductionUsedEnergy(), simulationConfig.getMinMutationCount(),
                    simulationConfig.getMaxMutationCount(), simulationConfig.getGrassEnergy(),
                    simulationConfig.getGrassGrowth(), simulationConfig.getGeneLength());
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
            updateGeneralStatistics();
            updateSelectedAnimalDetails();
        });
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

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0));
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    public void xyLabel() {
        mapGrid.getColumnConstraints().add(new ColumnConstraints(width));
        mapGrid.getRowConstraints().add(new RowConstraints(height));
        Label label = new Label("y/x");
        mapGrid.add(label, 0, 0);
        GridPane.setHalignment(label, HPos.CENTER);
    }

    public void updateBounds() {
        xMin = 0;
        yMin = 0;
        xMax = world.getWidth() - 1;
        yMax = world.getHeight() - 1;
        mapWidth = xMax + 1;
        mapHeight = yMax + 1;
        width = Math.round((float) maxWidth / mapWidth);
        height = Math.round((float) maxHeight / mapHeight);
        height = Math.min(height, width);
        width = height;
    }

    public void columnsFunction() {
        for (int i = 0; i < mapWidth; i++) {
            Label label = new Label(Integer.toString(i + xMin));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.getColumnConstraints().add(new ColumnConstraints(width));
            mapGrid.add(label, i + 1, 0);
        }
    }

    public void rowsFunction() {
        for (int i = 0; i < mapHeight; i++) {
            Label label = new Label(Integer.toString(yMax - i));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.getRowConstraints().add(new RowConstraints(height));
            mapGrid.add(label, 0, i + 1);
        }
    }

    public void addElements() {
        List<Integer> popularGene = highlightPopularGene.isSelected() ? simulation.getMostPopularGene() : null;

        for (int i = xMin; i <= xMax; i++) {
            for (int j = yMax; j >= yMin; j--) {
                Vector2d pos = new Vector2d(i, j);
                Region cell = new Region();

                if (world.isOccupied(pos)) {
                    WorldElement element = world.objectAt(pos);
                    if (element instanceof Animal isDead && isDead.getEnergy() > 0) {
                        Animal highestEnergyAnimal =
                                simulation.getAnimalList().stream().filter(animal -> animal.getPosition().equals(pos)).toList().getFirst();

                        String colorStyle = getColorStyle(highestEnergyAnimal);
                        cell.setStyle(colorStyle);

                        cell.setOnMouseClicked(event -> selectAnimal(highestEnergyAnimal));
                    } else if (element instanceof Grass) {
                        cell.setStyle("-fx-background-color: rgba(0, 255, 0, 0.5);");
                    }
                } else {
                    // TODO: add równik trawa ciemna
                    cell.setStyle("-fx-background-color: rgba(200, 200, 200, 0.5);");
                }

                // Show animals with most popular gene
                if (popularGene != null) {
                    List<Animal> animalsAtPosition =
                            simulation.getAnimalList().stream().filter(animal -> animal.getPosition().equals(pos)).toList();

                    if (!animalsAtPosition.isEmpty()) {
                        Animal selectedAnimal;
                        selectedAnimal =
                                animalsAtPosition.stream().filter(animal -> animal.getGene().equals(popularGene)).findFirst().orElse(animalsAtPosition.getFirst());

                        if (selectedAnimal.getGene().equals(popularGene)) {
                            cell.setStyle("-fx-background-color: rgba(255, 0, 0, 0.5);");
                        }
                    }
                }

                // Show selected animal
                if (selectedAnimal != null && selectedAnimal.getPosition().equals(pos)) {
                    cell.setStyle("-fx-background-color: rgba(255,255,0,0.5);");
                }

                cell.setPrefSize(width, height);
                mapGrid.add(cell, i - xMin + 1, yMax - j + 1);
                GridPane.setHalignment(cell, HPos.CENTER);
            }
        }
    }

    private static String getColorStyle(Animal animal) {
        double parameter = Math.min(1, animal.getEnergy() / 100.0); // Przy energii > x, parameter = 1

        int blue = (int) (255 * (1 - parameter));
        int redGreen = 0;

        return String.format("-fx-background-color: rgba(%d, %d, %d, 0.7);", redGreen, redGreen, blue);
    }

    private void selectAnimal(Animal animal) {
//        if (simulation.isPaused()) {
        selectedAnimal = animal;
        drawMap();
        updateSelectedAnimalDetails();
        stopTrackingButton.setVisible(selectedAnimal != null);
        stopTrackingButton.setManaged(selectedAnimal != null);
//        }
    }

    @FXML
    private void stopTrackingSelectedAnimal(ActionEvent actionEvent) {
        selectedAnimal = null;
        drawMap();
        clearSelectedAnimalDetails();
        stopTrackingButton.setVisible(selectedAnimal != null);
        stopTrackingButton.setManaged(selectedAnimal != null);
    }

    private void updateGeneralStatistics() {
        day.setText("Day: " + simulation.getDay());
        animalsCount.setText("Animals alive: " + world.animalCount());
        grassCount.setText("Grasses: " + world.grassCount());
        emptyFields.setText("Empty fields: " + (mapHeight * mapWidth - world.takenFields()));
        mostPopularGene.setText("Most popular gene: " + simulation.getMostPopularGene());
        averageEnergy.setText("Average animal energy: " + simulation.getAverageEnergy());
        averageLifespan.setText("Average lifespan: " + simulation.getAverageLifeSpan());
        averageChildren.setText("Average children count: " + simulation.getAverageChildrenCount());
    }

    private void updateSelectedAnimalDetails() {
        if (selectedAnimal != null) {
            selectedAnimalGenome.setText("Genome: " + selectedAnimal.getGene());
            selectedAnimalActiveGene.setText("Active Gene: " + selectedAnimal.getCurrGene());
            selectedAnimalEnergy.setText("Energy: " + selectedAnimal.getEnergy());
            selectedAnimalGrassEaten.setText("Grass Eaten: " + selectedAnimal.getGrassEaten());
            selectedAnimalChildren.setText("Children: " + selectedAnimal.getChildrenNumber());
            selectedAnimalAge.setText("Age: " + selectedAnimal.getAge());
            selectedAnimalDeathDay.setText(selectedAnimal.isAlive() ? "Still alive" :
                    "Died on day: " + selectedAnimal.getDeathDay());
        }
    }

    private void clearSelectedAnimalDetails() {
        selectedAnimalGenome.setText("");
        selectedAnimalActiveGene.setText("");
        selectedAnimalEnergy.setText("");
        selectedAnimalGrassEaten.setText("");
        selectedAnimalChildren.setText("");
        selectedAnimalAge.setText("");
        selectedAnimalDeathDay.setText("");
    }

    public void toggleHighlightPopularGene(ActionEvent actionEvent) {
        showPopularGene = !showPopularGene;
        drawMap();
    }

    public void onCloseWindow() {
        if (simulation != null) {
            simulation.closeCsvWriter();
        }
    }
}
