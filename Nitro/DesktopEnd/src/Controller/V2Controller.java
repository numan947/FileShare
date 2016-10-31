package Controller;

import coreJava.ClientPackage.Client;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import main.Main;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class V2Controller {

    private File InitDir=null;

    private final long toKB=1024;
    private final long toMB=1048576;

    private Main main=null;

    private Client client=null;

    private FileChooser fileChooser=null;


    @FXML
    private Label v2filenamelabel;

    @FXML
    private ProgressIndicator v2pindicator;

    @FXML
    private Button v2back;

    @FXML
    private Label v2mb_kb_label;

    @FXML
    private Button v2stop;

    @FXML
    private Button v2remove;

    @FXML
    private Label v2donelable;

    @FXML
    private ProgressBar v2pbar;

    @FXML
    private TextField v2destaddress;

    @FXML
    private Button v2selectfile;

    @FXML
    private Button v2send;

    @FXML
    private ListView v2loglist;

    @FXML
    private BorderPane v2bp;

    @FXML
    private Label v2totallabel;

    @FXML
    private ListView <File> v2filelist;


    private static Logger logger=null;
    private void ConfigLog()
    {
        try {
            logger=Logger.getLogger(V2Controller.class.getName());
            FileHandler fh=new FileHandler(Main.loggerDir+File.separator+V3Controller.class.getName()+"_logFile.log",true);
            SimpleFormatter formatter=new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.addHandler(fh);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public File getInitDir() {
        return InitDir;
    }

    public void setInitDir(File initDir) {
        InitDir = initDir;
    }

    @FXML
    void selectFile(ActionEvent event) {
        if(fileChooser==null)fileChooser=new FileChooser();
        fileChooser.setTitle("Select files to send");
        if(InitDir!=null)fileChooser.setInitialDirectory(InitDir);
        List<File> ff =fileChooser.showOpenMultipleDialog(main.getPrimaryStage());
        if(ff!=null) {
            v2filelist.getItems().addAll(ff);
            InitDir=ff.get(0).getAbsoluteFile().getParentFile();
            if(v2remove.isDisable())v2remove.setDisable(false);
        }
    }

    @FXML
    void removeFile(ActionEvent event) {
        ObservableList<File> l2=this.v2filelist.getSelectionModel().getSelectedItems();
        this.v2filelist.getItems().removeAll(l2);
        if(v2filelist.getItems().size()==0)v2remove.setDisable(true);
    }

    @FXML
    void sendFile(ActionEvent event) {

        ObservableList<File>list=this.v2filelist.getItems();
        String address=v2destaddress.getText();

        //validation check
        boolean valid=address.matches("10\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])");
        valid|=address.matches("192.168\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])");
        valid|=address.matches("172\\.([1][6-9]|[2][0-9]|[3][0-1])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])");

        if(!valid||list.isEmpty()){
            this.showMessage("ERROR!! PARAMETERS DON'T MATCH","Is the list empty? Or is the address invalid?", Alert.AlertType.ERROR);
            event.consume();
            logger.log(Level.WARNING,"Invalid arguments while sending files");
            return;
        }


        this.changeSendStopButtonStates();

        File[]ff=new File[list.size()];
        list.toArray(ff);

        if(ff.length>0&&v2destaddress.toString()!=null){
            client=new Client(this,v2destaddress.getText(),ff);
        }
    }

    @FXML
    void stopSending(ActionEvent event) {
        this.changeSendStopButtonStates();
        if(client!=null){
            client.setStop(true);
            client=null;
        }
    }

    @FXML
    void backAction(ActionEvent event) {
        main.loadScene1();
    }


    @FXML
    void initialize() {
        assert v2filenamelabel != null : "fx:id=\"v2filenamelabel\" was not injected: check your FXML file 'view2.fxml'.";
        assert v2pindicator != null : "fx:id=\"v2pindicator\" was not injected: check your FXML file 'view2.fxml'.";
        assert v2back != null : "fx:id=\"v2back\" was not injected: check your FXML file 'view2.fxml'.";
        assert v2mb_kb_label != null : "fx:id=\"v2mb_kb_label\" was not injected: check your FXML file 'view2.fxml'.";
        assert v2stop != null : "fx:id=\"v2stop\" was not injected: check your FXML file 'view2.fxml'.";
        assert v2remove != null : "fx:id=\"v2remove\" was not injected: check your FXML file 'view2.fxml'.";
        assert v2donelable != null : "fx:id=\"v2donelable\" was not injected: check your FXML file 'view2.fxml'.";
        assert v2pbar != null : "fx:id=\"v2pbar\" was not injected: check your FXML file 'view2.fxml'.";
        assert v2destaddress != null : "fx:id=\"v2destaddress\" was not injected: check your FXML file 'view2.fxml'.";
        assert v2selectfile != null : "fx:id=\"v2selectfile\" was not injected: check your FXML file 'view2.fxml'.";
        assert v2send != null : "fx:id=\"v2send\" was not injected: check your FXML file 'view2.fxml'.";
        assert v2filelist != null : "fx:id=\"v2filelist\" was not injected: check your FXML file 'view2.fxml'.";
        assert v2loglist != null : "fx:id=\"v2loglist\" was not injected: check your FXML file 'view2.fxml'.";
        assert v2bp != null : "fx:id=\"v2bp\" was not injected: check your FXML file 'view2.fxml'.";
        assert v2totallabel != null : "fx:id=\"v2totallabel\" was not injected: check your FXML file 'view2.fxml'.";
        this.ConfigLog();

        this.v2filelist.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        v2bp.requestFocus();
        this.v2bp.setOnMouseClicked(event -> {
            v2bp.requestFocus();
        });

        v2stop.setDisable(true);
        v2remove.setDisable(true);
        clearVisualEffect();

        v2filelist.setOnDragOver(event -> {
            Dragboard db=event.getDragboard();
            if(db.hasFiles())event.acceptTransferModes(TransferMode.LINK);
        });

        v2filelist.setOnDragDropped(event->{
            Dragboard db=event.getDragboard();
            if(db.hasFiles()){
                List<File> ff=db.getFiles();
                for(File f:ff){
                    if(f.isDirectory())continue;
                    else this.v2filelist.getItems().add(f);
                }

            }
        });

    }
    public void setMain(Main main) {
        this.main = main;
    }

    public void updateLog(File f)
    {
        Platform.runLater(() -> {
            v2loglist.getItems().add("Sent: "+f.getName());
            v2filelist.getItems().remove(f);
            if(v2filelist.getItems().size()==0)v2remove.setDisable(false);
        });
    }

    public void setPrimaryVisulaEffect(long done,long total){
        Platform.runLater(()->{
            if(total>toMB){
                v2donelable.setText(new DecimalFormat("#0.00").format((double) done / toMB));
            }
            else{
                v2donelable.setText(new DecimalFormat("#0.00").format((double) done / toKB));
            }
            double dd=(double)done/total;
            this.v2pbar.setProgress(dd);
            this.v2pindicator.setProgress(dd);

        });
    }

    public void setSecondaryVisualEffect(String fileName,long total)
    {
        Platform.runLater(()->{
            this.v2filenamelabel.setText(fileName);
            if(total>toMB){
                v2mb_kb_label.setText("MB");
                double dd=(double)total/toMB;
                v2totallabel.setText(new DecimalFormat("#0.00").format(dd));
            }
            else{
                v2mb_kb_label.setText("KB");
                double dd=(double)total/toKB;
                v2totallabel.setText(new DecimalFormat("#0.00").format(dd));
            }
        });
    }

    public void clearVisualEffect()
    {
        Platform.runLater(()->{
            v2donelable.setText("-");
            v2totallabel.setText("-");
            v2mb_kb_label.setText("-");
            v2pbar.setProgress(0);
            v2pindicator.setProgress(0);
            v2filenamelabel.setText("-");
        });

    }

    public void showMessage(String header, String details, Alert.AlertType f)
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

    public void changeSendStopButtonStates()
    {
        if(v2send.isDisable())v2send.setDisable(false);
        else v2send.setDisable(true);
        if(v2stop.isDisable())v2stop.setDisable(false);
        else v2stop.setDisable(true);
        if(v2remove.isDisable())v2remove.setDisable(false);
        else v2remove.setDisable(true);
        if(v2filelist.isDisable())v2filelist.setDisable(false);
        else v2filelist.setDisable(true);
        if(v2send.isDisable())v2back.setDisable(true);
        else v2back.setDisable(false);
        if(v2selectfile.isDisable())v2selectfile.setDisable(false);
        else v2selectfile.setDisable(true);
    }




}
