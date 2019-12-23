package sample.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import sample.shape.Circle;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private BorderPane root_container = new BorderPane();
    @FXML private TextField input_field = new TextField();

    private GraphicsTree graphicsTree;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graphicsTree = new GraphicsTree();
        root_container.setCenter(graphicsTree);

        input_field.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                graphicsTree.createTree(processInput(input_field.getText()));
            }
        });

        graphicsTree.widthProperty().bind(root_container.widthProperty());
        graphicsTree.heightProperty().bind(root_container.heightProperty().subtract(50));
    }

    private Integer[] processInput(String userInput) {
        String[] elems = userInput.split("\\s");
        return Arrays.stream(elems)
                     .map(Integer::valueOf)
                     .toArray(Integer[]::new);
    }

    private void alert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to empty the tree?", ButtonType.OK);
        alert.showAndWait()
             .filter(response -> response == ButtonType.OK)
             .ifPresent(response -> alert.close());
    }

    /**
     *  Performs the action when the search button is clicked.
     */
    @FXML private void searchOnAction(ActionEvent event) {
        int key = processInput(input_field.getText())[0];
        graphicsTree.search(key);
    }

    /**
     * Performs the action when the delete button is clicked.
     */
    @FXML private void deleteOnAction(ActionEvent event) {

    }

    /**
     *  Performs the action when the insert button is clicked.
     */
    @FXML private void insertOnAction(ActionEvent event) {

    }
}
