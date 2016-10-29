package Controller;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

import coreJava.ServerPackage.Server;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import main.Main;

public class V3Controller {

    private Main main=null;
    private Server server=null;
    private String ipAddress=null;
    private final long toKB=1024;
    private final long toMB=1048576;
    private File defaultSavePath=null;

    public File getDefaultSavePath() {
        return defaultSavePath;
    }

    public void setDefaultSavePath(File defaultSavePath) {
        this.defaultSavePath = defaultSavePath;
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<File>v3recievelist;

    @FXML
    private Button v3remove;

    @FXML
    private Label v3ipaddress;

    @FXML
    private Label v3total_label;

    @FXML
    private Label v3filename_label;

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

    @FXML
    void startAction(ActionEvent event) {
        findIP();
        this.v3ipaddress.setText(this.ipAddress);

        server=new Server(this,defaultSavePath.getAbsolutePath());
        System.out.println("HELL");
        this.changeStartStopButtonStates();
    }

    @FXML
    void stopAction(ActionEvent event) {
        if(server!=null){
            server.shutdownServer();
            server=null;
        }
        System.out.println("HELL");
        this.changeStartStopButtonStates();
    }

    @FXML
    void removeAction(ActionEvent event) {
        this.v3recievelist.getItems().removeAll(v3recievelist.getSelectionModel().getSelectedItems());
    }

    @FXML
    void backAction(ActionEvent event) {
        main.loadScene1();
    }

    @FXML
    void initialize() {
        assert v3recievelist != null : "fx:id=\"v3recievelist\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3remove != null : "fx:id=\"v3delete\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3ipaddress != null : "fx:id=\"v3ipaddress\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3total_label != null : "fx:id=\"v3total_label\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3filename_label != null : "fx:id=\"v3filename_label\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3done_label != null : "fx:id=\"v3done_label\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3start != null : "fx:id=\"v3start\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3back != null : "fx:id=\"v3back\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3bp != null : "fx:id=\"v3bp\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3pbar != null : "fx:id=\"v3pbar\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3mb_kb_label != null : "fx:id=\"v3mb_kb_label\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3pindicator != null : "fx:id=\"v3pindicator\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3stop != null : "fx:id=\"v3stop\" was not injected: check your FXML file 'view3.fxml'.";

        //setting up initial states
        this.v3recievelist.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        v3stop.setDisable(true);
        clearVisualEffect();
        v3bp.requestFocus();
        v3bp.setOnMouseClicked(event -> {
            v3bp.requestFocus();
        });



    }
    public void setMain(Main main) {
        this.main = main;
    }

    public void changeStartStopButtonStates()
    {
        if(v3start.isDisable())v3start.setDisable(false);
        else v3start.setDisable(true);
        if(v3stop.isDisable())v3stop.setDisable(false);
        else v3stop.setDisable(true);
        if(v3start.isDisable())v3back.setDisable(true);
        else v3back.setDisable(false);
    }


    private void findIP()
    {
        Vector<String>addresses=new Vector<>();
        try {
            Enumeration<NetworkInterface>n=NetworkInterface.getNetworkInterfaces();
            while(n.hasMoreElements()){
                NetworkInterface e=n.nextElement();

                Enumeration<InetAddress>a=e.getInetAddresses();

                while (a.hasMoreElements()){
                    addresses.add(a.nextElement().getHostAddress());
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.ipAddress="NO_NETWORK";

        for(String s:addresses){
            System.out.println(s);
            if(s.matches("10\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])")){
                this.ipAddress=s;
                break;
            }
            else if(s.matches("192.168\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])")){
                this.ipAddress=s;
                break;
            }
            else if(s.matches("172\\.([1][6-9]|[2][0-9]|[3][0-1])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])")){
                this.ipAddress=s;
                break;
            }
        }
    }


    public synchronized void updateFileList(File f)
    {
        Platform.runLater(()->{
            this.v3recievelist.getItems().add(f);
        });
    }


    public synchronized void showMessage(String header, String details, Alert.AlertType f)
    {
        Platform.runLater(()->{
            Alert dialog = new Alert(f);
            dialog.setHeaderText(header);
            dialog.setContentText(details);
            dialog.setResizable(false);
            dialog.getDialogPane().setPrefSize(270,220);
            dialog.showAndWait();
        });
    }


    public synchronized void setPrimaryVisualEffect(long done, long total){
        Platform.runLater(()->{
            if(total>toMB){
                v3done_label.setText(new DecimalFormat("#0.00").format((double) done / toMB));
            }
            else{
                v3done_label.setText(new DecimalFormat("#0.00").format((double) done / toKB));
            }
            double dd=(double)done/total;
            this.v3pbar.setProgress(dd);
            this.v3pindicator.setProgress(dd);
        });
    }

    public void setSecondaryVisualEffect(String fileName,long total)
    {
        Platform.runLater(()->{
            this.v3filename_label.setText(fileName);
            if(total>toMB){
                v3mb_kb_label.setText("MB");
                double dd=(double)total/toMB;
                v3total_label.setText(new DecimalFormat("#0.00").format(dd));
            }
            else{
                v3mb_kb_label.setText("KB");
                double dd=(double)total/toKB;
                v3total_label.setText(new DecimalFormat("#0.00").format(dd));
            }
        });

    }

    public void clearVisualEffect()
    {
        Platform.runLater(()->{
            v3done_label.setText("-");
            v3total_label.setText("-");
            v3mb_kb_label.setText("-");
            v3pbar.setProgress(0);
            v3pindicator.setProgress(0);
            v3filename_label.setText("-");
        });

    }

    public void regenerateServer()
    {
        server=new Server(this,defaultSavePath.getAbsolutePath());
    }


}
