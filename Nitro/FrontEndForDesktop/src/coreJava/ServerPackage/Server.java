package coreJava.ServerPackage;

import Controller.V3Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by numan947 on 10/26/16.
 **/
public class Server implements Runnable{

    private static Logger LOGGER=Logger.getLogger(Server.class.getName()+"LogFile");
    private static String logHelper="Exception in Server";
    private int port=46043; //my roll numbers combined :)
    private ServerSocket serverSocket=null;
    private Thread thread=null;
    private V3Controller controller=null;
    FileProcessingThread FPT=null;
    private String savePath=null;


    public Server(V3Controller controller,String savePath) {
        try {
            this.controller=controller;
            this.savePath=savePath;
            this.serverSocket=new ServerSocket(port,1);
            this.thread=new Thread(this);
            thread.start();
        } catch (IOException e) {
            System.out.println(logHelper+".Constructor1 "+e.getMessage());
        }
    }

    public void startServer(){
        System.out.println("Server Starting Up.....");
        try {
            Socket socket=serverSocket.accept();
            FPT=new FileProcessingThread(socket,controller,savePath);
            System.out.println("New client accepted");
            serverSocket.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE,logHelper+".startServer "+e.getMessage());
            System.out.println(logHelper+".startServer "+e.getMessage());
            System.out.println("Shutting Down Server.....");
        }



    }

    public void shutdownServer()
    {
        if(!serverSocket.isClosed()) try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
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
