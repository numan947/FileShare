package Controller;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import main.Main;

public class V1Controller {


    private ContextMenu cm;

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
        v1bp.requestFocus();
        v1bp.setOnMouseClicked(event -> {
            v1bp.requestFocus();
        });

        v1about.setOnAction(event -> {
            Alert dialog = new Alert(Alert.AlertType.INFORMATION);
            dialog.setHeaderText("CREDITS");
            dialog.setContentText("This is prepared by S.Mahmudul Hasan\nBUET batch'13, Level 3/ Term 1\nRoll:1305043\nas a side project :)");
            dialog.setResizable(false);
            dialog.getDialogPane().setPrefSize(270,220);
            dialog.showAndWait();
        });

        cm=new ContextMenu();
        MenuItem changeDest=new MenuItem("Change Destination Folder (RecieverSide)");
        MenuItem changeSrc=new MenuItem("Change Initial Directory (SenderSide)");
        cm.getItems().addAll(changeDest,changeSrc);
        changeDest.setOnAction(event -> {
            DirectoryChooser chooser=new DirectoryChooser();
            File f=chooser.showDialog(main.getPrimaryStage());
            main.setDestDirectory(f);
        });
        changeSrc.setOnAction(event->{
            DirectoryChooser chooser=new DirectoryChooser();
            File f=chooser.showDialog(main.getPrimaryStage());
            main.setInitialDirectory(f);
        });

        v1settings.setOnMouseClicked(event -> {
            if(cm.isShowing())cm.hide();
            else cm.show(v1settings,event.getScreenX(),event.getScreenY());
        });


    }
}
