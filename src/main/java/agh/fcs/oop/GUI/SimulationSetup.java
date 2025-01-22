package agh.fcs.oop.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


import java.io.*;
import java.util.Objects;

import static java.lang.String.valueOf;

public class SimulationSetup {
    @FXML
    private Text worldChoosing;
    @FXML
    private Text animalChoosing;
    @FXML
    private Text parameters;


    @FXML
    private RadioButton noVariant;
    @FXML
    private RadioButton globe;
    @FXML
    private RadioButton poles;
    @FXML
    private RadioButton random;
    @FXML
    private RadioButton craziness;


    private ToggleGroup toggleGroup;
    private ToggleGroup toggleGroup2;


    @FXML
    private TextField width;
    @FXML
    private TextField height;
    @FXML
    private TextField grassCount;
    @FXML
    private TextField animalCount;
    @FXML
    private TextField animalEnergy;
    @FXML
    private TextField reproductionMinEnergy;
    @FXML
    private TextField reproductionUsedEnergy;
    @FXML
    private TextField minMutationCount;
    @FXML
    private TextField maxMutationCount;
    @FXML
    private TextField grassEnergy;
    @FXML
    private TextField grassGrowth;
    @FXML
    private TextField geneLength;
    @FXML
    private TextField sleepDuration;


    @FXML
    private Button importCustom;
    @FXML
    private Button exportCustom;
    @FXML
    private Button start;

    @FXML
    private CheckBox generatingStats;

    @FXML
    private ImageView owlbear;

    private boolean isCreatingCsv;

    public void setDefault() {
        // after clicking "Set default parameters" in our JavaFX window, this
        // method starts and sets parameters on values below
        int defaultWidth = 10;
        width.setText(valueOf(defaultWidth));
        int defaultHeight = 10;
        height.setText(valueOf(defaultHeight));
        int defaultGrassCount = 10;
        grassCount.setText(valueOf(defaultGrassCount));
        int defaultAnimalCount = 15;
        animalCount.setText(valueOf(defaultAnimalCount));
        int defaultAnimalEnergy = 10;
        animalEnergy.setText(valueOf(defaultAnimalEnergy));
        int defaultReproductionMinEnergy = 5;
        reproductionMinEnergy.setText(valueOf(defaultReproductionMinEnergy));
        int defaultReproductionUsedEnergy = 5;
        reproductionUsedEnergy.setText(valueOf(defaultReproductionUsedEnergy));
        int defaultMinMutationCount = 1;
        minMutationCount.setText(valueOf(defaultMinMutationCount));
        int defaultMaxMutationCount = 5;
        maxMutationCount.setText(valueOf(defaultMaxMutationCount));
        int defaultGrassEnergy = 3;
        grassEnergy.setText(valueOf(defaultGrassEnergy));
        int defaultGrassGrowth = 5;
        grassGrowth.setText(valueOf(defaultGrassGrowth));
        int defaultGeneLength = 8;
        geneLength.setText(valueOf(defaultGeneLength));
        int defaultSleepDuration = 100;
        sleepDuration.setText(valueOf(defaultSleepDuration));
    }

    @FXML
    public void importSettings() {
        // we use this method to import simulation parameters from text file,
        // reading line by line and setting matching parameters
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Settings");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showOpenDialog(importCustom.getScene().getWindow());

        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String worldType = reader.readLine();
                switch (worldType) {
                    case "No variant":
                        noVariant.setSelected(true);
                        break;
                    case "Globe":
                        globe.setSelected(true);
                        break;
                    case "Poles":
                        poles.setSelected(true);
                        break;
                }

                String animalType = reader.readLine();
                if ("Random gene".equals(animalType)) {
                    random.setSelected(true);
                } else if ("Crazy animals".equals(animalType)) {
                    craziness.setSelected(true);
                }

                width.setText(reader.readLine());
                height.setText(reader.readLine());
                grassCount.setText(reader.readLine());
                animalCount.setText(reader.readLine());
                animalEnergy.setText(reader.readLine());
                reproductionMinEnergy.setText(reader.readLine());
                reproductionUsedEnergy.setText(reader.readLine());
                minMutationCount.setText(reader.readLine());
                maxMutationCount.setText(reader.readLine());
                grassEnergy.setText(reader.readLine());
                grassGrowth.setText(reader.readLine());
                geneLength.setText(reader.readLine());
                sleepDuration.setText(reader.readLine());

                System.out.println("Settings successfully imported!");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error reading file: " + e.getMessage());
            }
        }
    }

    @FXML
    public void exportSettings() {
        // this method is analogic to importSettings, but this time it writes
        // current parameters configuration into text file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Settings");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(exportCustom.getScene().getWindow());

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                RadioButton selectedWorldType = (RadioButton) toggleGroup.getSelectedToggle();
                writer.write(selectedWorldType.getText());
                writer.newLine();

                RadioButton selectedAnimalType = (RadioButton) toggleGroup2.getSelectedToggle();
                writer.write(selectedAnimalType.getText());
                writer.newLine();
                writer.write(width.getText());
                writer.newLine();
                writer.write(height.getText());
                writer.newLine();
                writer.write(grassCount.getText());
                writer.newLine();
                writer.write(animalCount.getText());
                writer.newLine();
                writer.write(animalEnergy.getText());
                writer.newLine();
                writer.write(reproductionMinEnergy.getText());
                writer.newLine();
                writer.write(reproductionUsedEnergy.getText());
                writer.newLine();
                writer.write(minMutationCount.getText());
                writer.newLine();
                writer.write(maxMutationCount.getText());
                writer.newLine();
                writer.write(grassEnergy.getText());
                writer.newLine();
                writer.write(grassGrowth.getText());
                writer.newLine();
                writer.write(geneLength.getText());
                writer.newLine();
                writer.write(sleepDuration.getText());
                writer.newLine();

                System.out.println("Settings successfully exported!");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error writing file: " + e.getMessage());
            }
        }
    }

    private boolean validateDimensions() {
        // method responsible for data validation: we want our width and height
        // to be always bigger or equal to 5, but smaller or equal to 30
        // TODO: extending validation for more parameters?
        try {
            int mapWidth = Integer.parseInt(width.getText());
            int mapHeight = Integer.parseInt(height.getText());
            int simMinMutationCount = Integer.parseInt(minMutationCount.getText());
            int simMaxMutationCount = Integer.parseInt(maxMutationCount.getText());
            int simGeneLength = Integer.parseInt(geneLength.getText());

            if (mapWidth < 5 || mapWidth > 30 || mapHeight < 5 || mapHeight > 30) {
                throw new IllegalArgumentException("Width and height must be between 5 and 30.");
            }
            if (simMinMutationCount > simMaxMutationCount) {
                throw new IllegalArgumentException("Min mutation number can't be bigger than max mutation number");
            }
            if (simGeneLength <= 0 || simGeneLength < simMaxMutationCount) {
                throw new IllegalArgumentException("Wrong gene length value");
            }

            return true;

        } catch (NumberFormatException e) {
            System.out.println("Width and height must be valid integers.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @FXML
    private void startSimulation() {
        // after starting simulation, we create config from SimulationConfig class,
        // in order to transfer parameters to SimulationPresenter class
        // thanks to that we can run simulation with parameters from setup window
        
        if (!validateDimensions()) {
            System.out.println("Simulation cannot start due to invalid dimensions.");
            return;
        }
        
        SimulationConfig config = new SimulationConfig();
        config.setMapType(((RadioButton) toggleGroup.getSelectedToggle()).getText());
        config.setAnimalType(((RadioButton) toggleGroup2.getSelectedToggle()).getText());
        config.setWidth(Integer.parseInt(width.getText()));
        config.setHeight(Integer.parseInt(height.getText()));
        config.setGrassCount(Integer.parseInt(grassCount.getText()));
        config.setAnimalCount(Integer.parseInt(animalCount.getText()));
        config.setAnimalEnergy(Integer.parseInt(animalEnergy.getText()));
        config.setReproductionMinEnergy(Integer.parseInt(reproductionMinEnergy.getText()));
        config.setReproductionUsedEnergy(Integer.parseInt(reproductionUsedEnergy.getText()));
        config.setMinMutationCount(Integer.parseInt(minMutationCount.getText()));
        config.setMaxMutationCount(Integer.parseInt(maxMutationCount.getText()));
        config.setGrassEnergy(Integer.parseInt(grassEnergy.getText()));
        config.setGrassGrowth(Integer.parseInt(grassGrowth.getText()));
        config.setGeneLength(Integer.parseInt(geneLength.getText()));
        config.setSleepDuration(Integer.parseInt(sleepDuration.getText()));
        config.setGeneratingCsv(isCreatingCsv);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/simulation.fxml"));
            BorderPane root = loader.load();
            SimulationPresenter presenter = loader.getController();
            presenter.setSimulationConfig(config);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Simulation");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void initialize() {
        // we group radio buttons in toggleGroups, set our owlbear image and
        // set some texts
        worldChoosing.setText("Choose your world type: ");
        animalChoosing.setText("Choose your animal type: ");
        parameters.setText("Set parameters for your map: ");

        isCreatingCsv = false;
        toggleGroup = new ToggleGroup();
        noVariant.setToggleGroup(toggleGroup);
        globe.setToggleGroup(toggleGroup);
        poles.setToggleGroup(toggleGroup);
        noVariant.setSelected(true);

        toggleGroup2 = new ToggleGroup();
        random.setToggleGroup(toggleGroup2);
        craziness.setToggleGroup(toggleGroup2);
        random.setSelected(true);

        owlbear.setImage(new Image(Objects.requireNonNull(getClass().getResource("/owlbear.png")).toExternalForm()));

    }

    public void toggleGeneratingCsv(ActionEvent actionEvent) {
        isCreatingCsv = !isCreatingCsv;
    }
}
