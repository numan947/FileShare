package coreJava.ServerPackage;

import android.os.Environment;

import com.example.numan947.androidend.ServerActivity;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
    private ServerActivity controller=null;
    FileProcessingThread FPT=null;
    private String savePath=null;


    private static Logger logger=null;
    private void ConfigLog()
    {
        logger=Logger.getLogger(Server.class.getName());
        FileHandler fh= null;
        try {
            fh = new FileHandler(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"Trans"+File.separator+".LOG"+ File.separator+Server.class.getName()+"_logFile.log",true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleFormatter formatter=new SimpleFormatter();
        fh.setFormatter(formatter);
        logger.addHandler(fh);
    }



    public Server(ServerActivity controller1, String savePath) {
        try {
            this.ConfigLog();
            this.controller=controller1;
            this.savePath=savePath;
            this.serverSocket=new ServerSocket(port,1);
            this.thread=new Thread(this);
            thread.start();
        } catch (IOException e) {
            this.controller.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    controller.showMessage("Can't start Server!!", "Are you running another instance of this program?");
                }
            });
            logger.log(Level.SEVERE,"Problem while starting serverSocket!! Can't instantiate ServerSocket "+new Date().toString());
        }
        logger.info("Server session started on "+new Date().toString());
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
