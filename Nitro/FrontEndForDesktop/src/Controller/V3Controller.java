package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import coreJava.ServerPackage.Server;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import main.Main;


public class V3Controller {
    private Server server;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<?> v3filetable;

    @FXML
    private Button v3opendest;

    @FXML
    private Button v3delete;

    @FXML
    private Label v3ipaddress;

    @FXML
    private Label v3total_label;

    @FXML
    private Label v3filename_label;

    @FXML
    private ListView<?> v3errorlist;

    @FXML
    private Label v3done_label;

    @FXML
    private Button v3start;

    @FXML
    private Button v3back;

    @FXML
    private BorderPane v3bp;

    @FXML
    private ProgressBar v3pbar;

    @FXML
    private Label v3mb_kb_label;

    @FXML
    private ProgressIndicator v3pindicator;

    @FXML
    private Button v3stop;


    private Main main;

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    void initialize() {
        assert v3filetable != null : "fx:id=\"v3filetable\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3opendest != null : "fx:id=\"v3opendest\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3delete != null : "fx:id=\"v3delete\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3ipaddress != null : "fx:id=\"v3ipaddress\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3total_label != null : "fx:id=\"v3total_label\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3filename_label != null : "fx:id=\"v3filename_label\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3errorlist != null : "fx:id=\"v3errorlist\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3done_label != null : "fx:id=\"v3done_label\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3start != null : "fx:id=\"v3start\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3back != null : "fx:id=\"v3back\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3bp != null : "fx:id=\"v3bp\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3pbar != null : "fx:id=\"v3pbar\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3mb_kb_label != null : "fx:id=\"v3mb_kb_label\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3pindicator != null : "fx:id=\"v3pindicator\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3stop != null : "fx:id=\"v3stop\" was not injected: check your FXML file 'view3.fxml'.";

    }
}
