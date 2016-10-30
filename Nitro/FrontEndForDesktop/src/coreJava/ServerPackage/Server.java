package coreJava.ServerPackage;

import Controller.V3Controller;
import javafx.scene.control.Alert;
import main.Main;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by numan947 on 10/26/16.
 **/
public class Server implements Runnable{

    private int port=46043; //my roll numbers combined :)
    private ServerSocket serverSocket=null;
    private Thread thread=null;
    private V3Controller controller=null;
    FileProcessingThread FPT=null;
    private String savePath=null;


    private static Logger logger=null;
    private void ConfigLog()
    {
        try {
            logger=Logger.getLogger(Server.class.getName());
            FileHandler fh=new FileHandler(Main.loggerDir+ File.separator+Server.class.getName()+"_logFile.log",true);
            SimpleFormatter formatter=new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.addHandler(fh);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public Server(V3Controller controller,String savePath) {
        try {
            this.ConfigLog();
            this.controller=controller;
            this.savePath=savePath;
            this.serverSocket=new ServerSocket(port,1);
            this.thread=new Thread(this);
            thread.start();
        } catch (IOException e) {
            controller.showMessage("Can't start Server!!", "Are you running another instance of this program?", Alert.AlertType.ERROR);
            logger.log(Level.SEVERE,"Problem while starting serverSocket!! Can't instantiate ServerSocket "+new Date().toString());
        }
    }

    public void startServer(){
        try {
            Socket socket=serverSocket.accept();
            FPT=new FileProcessingThread(socket,controller,savePath);
            logger.info("Connection with client established, Client address : "+socket.getInetAddress().getHostAddress());
            serverSocket.close();
        } catch (IOException e) {
            logger.log(Level.INFO,"Server stopped "+new Date().toString());
        }



    }

    public void shutdownServer()
    {
        if(!serverSocket.isClosed()) try {
            serverSocket.close();
        } catch (IOException e) {
            logger.log(Level.WARNING,"Exception while closing serverSocket "+new Date().toString());
        }
        else{
            FPT.setStop(true);
        }
    }

    @Override
    public void run() {
        this.startServer();
    }
}
