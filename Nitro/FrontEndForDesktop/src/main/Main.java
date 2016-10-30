package main;

import Controller.V1Controller;
import Controller.V2Controller;
import Controller.V3Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main extends Application {

    private static Logger logger=null;
    private static String stateFile="STATE_FILE";
    public static File loggerDir=null;
    public static File initDir;
    public static File saveDir;

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
        URL url = Main.class.getProtectionDomain().getCodeSource().getLocation();
        String jarPath = null;
        try {
            jarPath = URLDecoder.decode(url.getFile(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        loggerDir=new File(new File(jarPath).getParentFile().getAbsolutePath()+File.separator+"logs"+File.separator);

        if(!loggerDir.exists())loggerDir.mkdirs();

        try {
            logger=Logger.getLogger(Main.class.getName());
            FileHandler fh=new FileHandler(loggerDir+File.separator+Main.class.getName()+"_logFile.log",true);
            SimpleFormatter formatter=new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.addHandler(fh);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadFXML()
    {
        this.ConfigLog();
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
            logger.log(Level.SEVERE,"can't load fxmls"+e.getMessage());
            return;
        }
        logger.info("Session started on: "+new Date().toString());
        logger.log(Level.INFO," Successfully loaded FXMLS");
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
        this.loadFXML();
        loadStates();

        this.primaryStage=primaryStage;
        primaryStage.getIcons().add(new Image("/res/images/transfer-files.png"));
        primaryStage.setScene(v1Scene);
        primaryStage.show();
    }

    static void saveStates() throws IOException {
        File f=new File(loggerDir+File.separator+stateFile);
        if(!f.exists())f.createNewFile();
        BufferedWriter writer=new BufferedWriter(new FileWriter(f));
        String s=v2Controller.getInitDir().getAbsolutePath()+"****"+v3Controller.getDefaultSavePath().getAbsolutePath();
        writer.write(s);
        writer.close();
    }

    static private void helperLoad() {
        String path=System.getProperty("user.dir");
        initDir=new File(path);
        saveDir=new File(path);
        v2Controller.setInitDir(initDir);
        v3Controller.setDefaultSavePath(saveDir);
        logger.log(Level.INFO,"Initial and save dirs reset");
    }

    static private void loadStates() throws IOException {
        File f=new File(loggerDir.getAbsolutePath()+File.separator+stateFile);
        if(!f.exists()){
            f.createNewFile();
            helperLoad();
            return;
        }
        BufferedReader reader=new BufferedReader(new FileReader(f));
        String s=reader.readLine();
        reader.close();
        if(s==null){
            helperLoad();
            return;
        }
        String[]dirs=s.split("\\*\\*\\*\\*");
        initDir=new File(dirs[0]);
        saveDir=new File(dirs[1]);
        v2Controller.setInitDir(initDir);
        v3Controller.setDefaultSavePath(saveDir);
        logger.log(Level.INFO,"Initial and save dirs successfully loaded");
    }



    public static void main(String[] args) throws IOException {
        launch(args);
        saveStates();
        logger.log(Level.INFO,"Session ending on "+new Date().toString());
        System.exit(0);
    }
}
