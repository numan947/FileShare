package Controller;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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
    private ListView<File> v3receiveList;

    @FXML
    private Button v3remove;

    @FXML
    private Label v3ipAddress;

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

    private static Logger logger=null;
    private void ConfigLog()
    {
        try {
            logger=Logger.getLogger(V3Controller.class.getName());
            FileHandler fh=new FileHandler(Main.loggerDir+File.separator+V3Controller.class.getName()+"_logFile.log",true);
            SimpleFormatter formatter=new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.addHandler(fh);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void startAction(ActionEvent event) {
        findIP();
        this.v3ipAddress.setText(this.ipAddress);

        server=new Server(this,defaultSavePath.getAbsolutePath());
        logger.log(Level.INFO,"Firing up the server "+new Date().toString());
        this.changeStartStopButtonStates();
    }

    @FXML
    void stopAction(ActionEvent event) {
        if(server!=null){
            logger.log(Level.INFO,"Shutting down server on "+new Date().toString());
            server.shutdownServer();
            server=null;
        }

        this.changeStartStopButtonStates();
    }

    @FXML
    void removeAction(ActionEvent event) {
        this.v3receiveList.getItems().removeAll(v3receiveList.getSelectionModel().getSelectedItems());
    }

    @FXML
    void backAction(ActionEvent event) {
        main.loadScene1();
    }

    @FXML
    void initialize() {
        assert v3receiveList != null : "fx:id=\"v3receiveList\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3remove != null : "fx:id=\"v3delete\" was not injected: check your FXML file 'view3.fxml'.";
        assert v3ipAddress != null : "fx:id=\"v3ipAddress\" was not injected: check your FXML file 'view3.fxml'.";
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

        this.ConfigLog();

        //setting up initial states
        this.v3receiveList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        v3stop.setDisable(true);
        clearVisualEffect();
        v3bp.requestFocus();
        v3bp.setOnMouseClicked(event -> {
            v3bp.requestFocus();
        });
        logger.log(Level.INFO,"Initial setup completed");



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
            logger.log(Level.WARNING,"Problem in NetworkInterfaces "+new Date().toString());
        }
        this.ipAddress="NO_NETWORK";

        for(String s:addresses){

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
            this.v3receiveList.getItems().add(f);
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
