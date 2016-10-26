package ServerPackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by numan947 on 10/26/16.
 **/
public class Server {

    private static Logger LOGGER=Logger.getLogger(Server.class.getName()+"LogFile");
    private static String logHelper="Exception in Server";
    private int port=46043; //my roll numbers combined :)
    ServerSocket serverSocket=null;
    private boolean serverFlag;

    public Server() {
        try {
            serverSocket=new ServerSocket(port,20);
            serverFlag=true;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE,logHelper+".Constructor1 "+e.getMessage());
            System.out.println(logHelper+".Constructor1 "+e.getMessage());
        }
    }

    public void startServer(){
        System.out.println("Server Starting Up.....");
        while(serverFlag){

            try {
                new FileProcessingThread(serverSocket.accept());
                System.out.println("New client accepted");
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE,logHelper+".startServer "+e.getMessage());
                System.out.println(logHelper+".startServer "+e.getMessage());
            }
        }

        System.out.println("Shutting Down Server.....");
    }

    public void setServerFlag(boolean flag){
        this.serverFlag=flag;
    }

}
