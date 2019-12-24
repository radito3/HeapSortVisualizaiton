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
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class Controller implements Initializable {

    @FXML private BorderPane root_container = new BorderPane();
    @FXML private TextField input_field = new TextField();
    @FXML private Button previousStep = new Button();
    @FXML private Button nextStep = new Button();

    private GraphicsTree graphicsTree;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graphicsTree = new GraphicsTree();
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

    private void alertComplete() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Heap sort complete", ButtonType.CLOSE);
        alert.showAndWait()
             .filter(response -> response == ButtonType.CLOSE)
             .ifPresent(response -> alert.close());
    }

    @FXML private void randomElements(ActionEvent event) {
        Random random = new Random();
        String s = "arr";//format numbers as string
        graphicsTree.createTree(random.ints(15, 1, 50)
                                      .toArray());
        input_field.setText(s);
        input_field.setEditable(false);
    }

    @FXML private void stepByStepSort(ActionEvent event) {
//        graphicsTree.displayCircle(13);
        nextStep.setVisible(true);
        previousStep.setVisible(true);
    }

    @FXML private void continuousSort(ActionEvent event) throws InterruptedException {
        graphicsTree.continuousSort();
        CompletableFuture.runAsync(() -> {
            while (!graphicsTree.isSorted()) ;
            displaySortedArray();
            alertComplete();
        });
    }

    private void displaySortedArray() {
        int[] nums = graphicsTree.getNumbers();
        String s = "kur";
        input_field.setText(s);
    }

    @FXML private void nextStep(ActionEvent event) {
        graphicsTree.nextEvent();
    }

    @FXML private void previousStep(ActionEvent event) {
        graphicsTree.previousEvent();
    }

    @FXML private void clear(ActionEvent event) {
        input_field.setEditable(true);
        input_field.setPromptText("Numbers to sort");
        input_field.setText("");
        graphicsTree.clearCanvas();
        graphicsTree.clearTree();
    }
}
