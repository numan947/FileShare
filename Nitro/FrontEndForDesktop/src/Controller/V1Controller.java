package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import main.Main;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class V1Controller {
    private static Logger logger=null;

    private ContextMenu cm;

    @FXML
    private Button v1about;

    @FXML
    private Button v1send;

    @FXML
    private Button v1settings;

    @FXML
    private Button v1receive;

    @FXML
    private BorderPane v1bp;


    @FXML
    void AboutAction(ActionEvent event) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setHeaderText("CREDITS");
        dialog.setContentText("This is prepared by S.Mahmudul Hasan\nBUET batch'13, Level 3/ Term 1\nRoll:1305043\nas a side project :)");
        dialog.setResizable(false);
        dialog.getDialogPane().setPrefSize(270,220);
        dialog.showAndWait();
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

    private void ConfigLog()
    {
        try {
            logger=Logger.getLogger(V1Controller.class.getName());
            FileHandler fh=new FileHandler(Main.loggerDir+File.separator+V1Controller.class.getName()+"_logFile.log",true);
            SimpleFormatter formatter=new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.addHandler(fh);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        assert v1about != null : "fx:id=\"v1about\" was not injected: check your FXML file 'view1.fxml'.";
        assert v1send != null : "fx:id=\"v1send\" was not injected: check your FXML file 'view1.fxml'.";
        assert v1settings != null : "fx:id=\"v1settings\" was not injected: check your FXML file 'view1.fxml'.";
        assert v1receive != null : "fx:id=\"v1receive\" was not injected: check your FXML file 'view1.fxml'.";
        assert v1bp != null : "fx:id=\"v1bp\" was not injected: check your FXML file 'view1.fxml'.";

        this.ConfigLog();

        v1bp.requestFocus();
        v1bp.setOnMouseClicked(event -> {
            v1bp.requestFocus();
        });


        cm=new ContextMenu();
        MenuItem changeDest=new MenuItem("Change Destination Folder (ReceiverSide)");
        MenuItem changeSrc=new MenuItem("Change Initial Directory (SenderSide)");
        cm.getItems().addAll(changeDest,changeSrc);
        changeDest.setOnAction(event -> {
            DirectoryChooser chooser=new DirectoryChooser();
            chooser.setTitle("Select default destination");
            if(Main.saveDir!=null)chooser.setInitialDirectory(Main.saveDir.getAbsoluteFile());
            File f=chooser.showDialog(main.getPrimaryStage());
            if(f!=null)main.setDestDirectory(f);
        });
        changeSrc.setOnAction(event->{
            DirectoryChooser chooser=new DirectoryChooser();
            chooser.setTitle("Select default initial directory");
            if(Main.initDir!=null)chooser.setInitialDirectory(Main.initDir.getAbsoluteFile());
            File f=chooser.showDialog(main.getPrimaryStage());
            if(f!=null)main.setInitialDirectory(f);
        });

        v1settings.setOnMouseClicked(event -> {
            if(cm.isShowing())cm.hide();
            else cm.show(v1settings,event.getScreenX(),event.getScreenY());
        });

        logger.log(Level.INFO,"fxml1 successfully initiated "+new Date().toString());


    }
}
