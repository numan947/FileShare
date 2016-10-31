package coreJava.ClientPackage;

import Controller.V2Controller;
import Controller.V3Controller;
import javafx.scene.control.Alert;
import main.Main;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by numan947 on 10/26/16.
 **/
public class Client implements Runnable {
    //buffer related
    private int DEFAULT_BUFFER_SIZE=8192;
    private int BUFFER_SIZE=8192;

    //server & network related
    private int port=46043;
    private String serverAddress=null;
    private NetworkUtil util=null;

    private File[]files=null;
    private File selectedFile=null;
    private BufferedInputStream fbuff=null;
    private byte[]buff=null;
    private String encoding="UTF-8";

    //to add gui
    private Thread thread=null;
    private V2Controller controller=null;
    private boolean stop;

    //statistics flags
    private long startTime;
    private long totalsize;

    private static Logger logger=null;
    private void ConfigLog()
    {
        try {
            logger=Logger.getLogger(Client.class.getName());
            FileHandler fh=new FileHandler(Main.loggerDir+File.separator+Client.class.getName()+"_logFile.log",true);
            SimpleFormatter formatter=new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.addHandler(fh);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public Client(V2Controller controller,String serverAddress,File[]files) {

        this.ConfigLog();
        this.logger.log(Level.INFO,"Client session started on "+new Date().toString());
        this.controller=controller;
        this.serverAddress=serverAddress;
        this.files=files;
        this.buff=new byte[DEFAULT_BUFFER_SIZE];
        this.stop=false;
        this.thread=new Thread(this);
        thread.start();

    }


    private int getBuff(byte[]buff)
    {
        try {
            return fbuff.read(buff);
        } catch (IOException e) {
            logger.log(Level.SEVERE,"Problem while reading file "+new Date().toString());
        }
        return 0;
    }


    public void processAndSend()
    {

        startTime=System.currentTimeMillis();
        int totalRead;
        try {
 
            //how many file to send and response from server
            totalRead=util.readBuff(buff);
            if(totalRead!=-1) {

                logger.info("Server Response : "+new String(buff, 0, totalRead, encoding));

                util.writeBuff(("" + files.length).getBytes(encoding));
                totalRead = util.readBuff(buff);

                if (totalRead != -1) {
                    logger.info("Server Response : "+new String(buff, 0, totalRead, encoding));

                    for (int i = 0; i < files.length; i++) {
                        try {
                            logger.info("Sending file...."+files[i].getName());
                            selectedFile = files[i];
                            fbuff = new BufferedInputStream(new FileInputStream(selectedFile),BUFFER_SIZE);
                            totalsize+=selectedFile.length();
                            //send file name & size
                            util.writeBuff((selectedFile.getName() + "$$$$" + selectedFile.length()).getBytes(encoding));

                            //response from server
                            totalRead = util.readBuff(buff);
                            if (totalRead != -1) {
                                String msg = new String(buff, 0, totalRead, encoding);
                                logger.info("Server Response : "+msg);
                                controller.setSecondaryVisualEffect(selectedFile.getName(),selectedFile.length());

                                int tmpCt=0;
                                int totalSent=0;
                                while ((totalRead = getBuff(buff)) > -1 && !stop) {
                                    tmpCt+=totalRead;
                                    totalSent+=totalRead;

                                    try {
                                        util.writeBuff(buff, 0, totalRead);
                                    }catch (IOException e){
                                        logger.log(Level.WARNING,"Connection ended by receiver "+new Date().toString(),e.getMessage());
                                        controller.changeSendStopButtonStates();
                                        controller.clearVisualEffect();
                                        controller.showMessage("ERROR SENDING!!!","Receiver closed the connection!", Alert.AlertType.ERROR);
                                        stop=true;
                                        fbuff.close();
                                        return;
                                    }

                                    if(tmpCt>=DEFAULT_BUFFER_SIZE){
                                        util.flushStream();
                                        tmpCt=0;
                                    }
                                    controller.setPrimaryVisulaEffect(totalSent,selectedFile.length());
                                }

                                util.flushStream();
                                fbuff.close();

                                if(stop){
                                    controller.clearVisualEffect();
                                    controller.showMessage("SENDING INTERRUPTED!!","Did you just hit the stop button? "+selectedFile.getName()+" not sent", Alert.AlertType.WARNING);
                                    return;
                                }
                                controller.clearVisualEffect();
                                controller.updateLog(selectedFile);


                                //response from server
                                totalRead = util.readBuff(buff);
                                if (totalRead != -1) {
                                    msg = new String(buff, 0, totalRead, encoding);
                                    logger.info("Server Response : "+msg);
                                }
                            }
                        } catch (IOException e) {
                            logger.log(Level.WARNING,"Problem while sending file "+new Date().toString(),e.getMessage());
                        }
                        logger.info(files[i].getName()+" sent");
                    }
                }
                totalRead = util.readBuff(buff);
                if (totalRead != -1)logger.info("Server Response : "+new String(buff, 0, totalRead, encoding));
            }
        } catch (UnsupportedEncodingException ee) {
            System.out.println("Exception In ClientPackage.Client.processAndSend "+ee.getMessage());
        }
        logger.log(Level.INFO,"total time taken to send file: "+(System.currentTimeMillis()-startTime)/1000.0);
        logger.log(Level.INFO,"total sent in MB: "+((double)totalsize/(1024*1024)));
    }



    @Override
    public void run() {

        logger.info("Selected ServerAddress "+serverAddress);
        try {
            this.util = new NetworkUtil(serverAddress, port);

        }catch (IOException e){
            if(stop)return;
            controller.showMessage("ERROR WHILE CONNECTING","Can't Connect to host "+e.getMessage(), Alert.AlertType.ERROR);
            controller.changeSendStopButtonStates();
            return;
        }

        if(stop){
            util.closeAll();
            return;
        }

        this.processAndSend();

        if(stop){
            logger.log(Level.WARNING,"Connection closed by sender "+new Date().toString());
            util.closeAll();
            return;
        }

        double sentInMB=(double)totalsize/(1024*1024);
        double timetaken=(double)(System.currentTimeMillis()-startTime)/1000;

        controller.showMessage("SUCCESS!!","You sent "+new DecimalFormat("#0.000").format(sentInMB)+" MB in "+new DecimalFormat("#0.000").format(timetaken)+" second(s)", Alert.AlertType.INFORMATION);
        controller.changeSendStopButtonStates();
        util.closeAll();
        logger.info("Session closed at "+new Date().toString());
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
}
