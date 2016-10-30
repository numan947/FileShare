package main;

import Controller.V1Controller;
import Controller.V2Controller;
import Controller.V3Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main extends Application {

    private static Logger logger=Logger.getLogger(Main.class.getName());
    private static final String logmsg="Exception in main.Main.class ";
    private static String stateFile="STATE_FILE";

    private Stage primaryStage=null;

    private static V1Controller v1Controller=null;
    private static V2Controller v2Controller=null;
    private static V3Controller v3Controller=null;

    private Scene v1Scene=null;
    private Scene v2Scene=null;
    private Scene v3Scene=null;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void ConfigLog()
    {
        try {
            FileHandler fh=new FileHandler("logFile.log",true);
            SimpleFormatter formatter=new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.addHandler(fh);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadFXML()
    {
        FXMLLoader loader=null;
        Parent parent=null;
        try {
            //load view1
            loader=new FXMLLoader(getClass().getResource("/res/fxmls/view1.fxml"));
            parent=loader.load();
            this.v1Scene=new Scene(parent,600,400);
            this.v1Controller=loader.getController();
            v1Controller.setMain(this);


            //load view2
            loader=new FXMLLoader(getClass().getResource("/res/fxmls/view2.fxml"));
            parent=loader.load();
            this.v2Scene=new Scene(parent,600,400);
            this.v2Controller=loader.getController();
            v2Controller.setMain(this);


            //load view3
            loader=new FXMLLoader(getClass().getResource("/res/fxmls/view3.fxml"));
            parent=loader.load();
            this.v3Scene=new Scene(parent,600,400);
            this.v3Controller=loader.getController();
            v3Controller.setMain(this);

        } catch (IOException e) {
            logger.log(Level.SEVERE,logmsg+"loadFXML() "+e.getMessage());
        }
    }

    public void loadScene1()
    {
        primaryStage.setScene(v1Scene);
    }
    public void loadScene2()
    {
        primaryStage.setScene(v2Scene);
    }
    public void loadScene3()
    {
        primaryStage.setScene(v3Scene);
    }

    public void setInitialDirectory(File f)
    {
        v2Controller.setInitDir(f);
    }

    public void setDestDirectory(File f)
    {
        v3Controller.setDefaultSavePath(f);
    }



    @Override
    public void start(Stage primaryStage) throws Exception{
        this.ConfigLog();
        this.loadFXML();

        this.primaryStage=primaryStage;
        primaryStage.setScene(v1Scene);
        primaryStage.show();
    }

    static void saveStates() throws IOException {
        File f=new File(stateFile);
        if(!f.exists())f.createNewFile();
        BufferedWriter writer=new BufferedWriter(new FileWriter(f));
        writer.write(v2Controller.getInitDir().getAbsolutePath());
        writer.write(v3Controller.getDefaultSavePath().getAbsolutePath());
        writer.close();
    }

    static void loadStates() throws IOException {
        File f=new File(stateFile);
        if(!f.exists()){
            f.createNewFile();
            return;
        }
        BufferedReader reader=new BufferedReader(new FileReader(f));
        String s=reader.readLine();
        System.out.println(s);
        s=reader.readLine();
        System.out.println(s);
        reader.close();
    }



    public static void main(String[] args) throws IOException {
        launch(args);
        saveStates();
        System.exit(0);
    }
}
