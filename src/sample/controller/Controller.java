package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

public class Controller implements Initializable {

    @FXML private BorderPane root_container = new BorderPane();
    @FXML private TextField input_field = new TextField();
    @FXML private Button previousStep = new Button();
    @FXML private Button nextStep = new Button();

    private GraphicsTree graphicsTree;
    private ScheduledExecutorService service;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graphicsTree = new GraphicsTree();
        service = AsyncServiceSupplier.get();
        root_container.setCenter(graphicsTree);

        input_field.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                graphicsTree.createTree(processInput(input_field.getText()));
                input_field.setEditable(false);
            }
        });

        graphicsTree.widthProperty().bind(root_container.widthProperty());
        graphicsTree.heightProperty().bind(root_container.heightProperty().subtract(50));
    }

    private int[] processInput(String userInput) {
        String[] elems = userInput.split("\\s");
        int[] nums = new int[elems.length];
        int i = 0;
        for (String elem : elems) {
            nums[i++] = Integer.parseInt(elem);
        }
        return nums;
    }

    @FXML private void randomElements(ActionEvent event) {
        Random random = new Random();
        int[] arr = random.ints(15, 1, 200)
                          .toArray();
        String numbersStr = Arrays.stream(arr)
                                  .mapToObj(String::valueOf)
                                  .collect(Collectors.joining(" "));
        graphicsTree.createTree(arr);
        input_field.setText(numbersStr);
        input_field.setEditable(false);
    }

    @FXML private void stepByStepSort(ActionEvent event) {
        if (!graphicsTree.isInitialized()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Array to sort is not initialized", ButtonType.OK);
            alert.showAndWait()
                 .filter(response -> response == ButtonType.OK)
                 .ifPresent(response -> alert.close());
            return;
        }

        graphicsTree.stepByStepSort();

        nextStep.setVisible(true);
        previousStep.setVisible(true);
    }

    @FXML private void continuousSort(ActionEvent event) {
        if (!graphicsTree.isInitialized()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Array to sort is not initialized", ButtonType.OK);
            alert.showAndWait()
                 .filter(response -> response == ButtonType.OK)
                 .ifPresent(response -> alert.close());

            if (!service.isShutdown()) {
                service.shutdown();
            }
            return;
        }

        graphicsTree.continuousSort();

        if (!service.isShutdown())
            service.shutdown();
    }

    private void displaySortedArray() {
        int[] nums = graphicsTree.getNumbers();
        String numbersStr = Arrays.stream(nums)
                                  .mapToObj(String::valueOf)
                                  .collect(Collectors.joining(" "));
        input_field.setText(numbersStr);
    }

    @FXML private void nextStep(ActionEvent event) {
        if (graphicsTree.nextEvent()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Heap sort complete", ButtonType.OK);
            alert.showAndWait()
                 .filter(response -> response == ButtonType.OK)
                 .ifPresent(response -> alert.close());

            displaySortedArray();

            if (!service.isShutdown())
                service.shutdown();
        }
    }

    @FXML private void previousStep(ActionEvent event) {
        if (graphicsTree.previousEvent()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "This is the first step", ButtonType.OK);
            alert.showAndWait()
                 .filter(response -> response == ButtonType.OK)
                 .ifPresent(response -> alert.close());
        }
    }

    @FXML private void clear(ActionEvent event) {
        input_field.setEditable(true);
        input_field.setPromptText("Numbers to sort");
        input_field.setText("");

        graphicsTree.clearCanvas();
        graphicsTree.clearTree();

        nextStep.setVisible(false);
        previousStep.setVisible(false);

        if (!service.isShutdown())
            service.shutdown();
    }
}
