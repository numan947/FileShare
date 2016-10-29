package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import main.Main;

public class V1Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button v1about;

    @FXML
    private Button v1send;

    @FXML
    private Button v1settings;

    @FXML
    private Button v1recieve;

    @FXML
    private BorderPane v1bp;

    @FXML
    void SettingsAction(ActionEvent event) {

    }

    @FXML
    void AboutAction(ActionEvent event) {

    }

    @FXML
    void SendAction(ActionEvent event) {
        main.loadScene2();
    }

    @FXML
    void RecieveAction(ActionEvent event) {
        main.loadScene3();
    }
    private Main main;

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    void initialize() {
        assert v1about != null : "fx:id=\"v1about\" was not injected: check your FXML file 'view1.fxml'.";
        assert v1send != null : "fx:id=\"v1send\" was not injected: check your FXML file 'view1.fxml'.";
        assert v1settings != null : "fx:id=\"v1settings\" was not injected: check your FXML file 'view1.fxml'.";
        assert v1recieve != null : "fx:id=\"v1recieve\" was not injected: check your FXML file 'view1.fxml'.";
        assert v1bp != null : "fx:id=\"v1bp\" was not injected: check your FXML file 'view1.fxml'.";

    }
}
