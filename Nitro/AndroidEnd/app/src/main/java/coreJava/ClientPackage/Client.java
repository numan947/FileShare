package coreJava.ClientPackage;

import android.os.Environment;

import com.example.numan947.androidend.ClientActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
    private int DEFAULT_BUFFER_SIZE=12000;
    private int BUFFER_SIZE=36000;

    //server & network related
    private int port=46043;
    private String serverAddress=null;
    private NetworkUtil util=null;

    private ArrayList<File>files=null;
    private File selectedFile=null;
    private BufferedInputStream fbuff=null;
    private byte[]buff=null;
    private String encoding="UTF-8";

    //to add gui
    private Thread thread=null;
    private ClientActivity controller=null;
    private boolean stop;

    //statistics flags
    private long startTime;
    private long totalsize;
    private int totalRead;
    private int totalSent;

    private static Logger logger=null;
    private void ConfigLog()
    {
        logger=Logger.getLogger(Client.class.getName());
        FileHandler fh = null;
        try {
            fh = new FileHandler(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"THIS IS SPARTA"+File.separator+".LOG"+ File.separator+Client.class.getName()+"_logFile.log",true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleFormatter formatter=new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.addHandler(fh);
    }



    public Client(ClientActivity controller, String serverAddress, ArrayList<File>files) {

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

        try {

            //how many file to send and response from server
            totalRead=util.readBuff(buff);
            if(totalRead!=-1) {

                logger.info("Server Response : "+new String(buff, 0, totalRead, encoding));

                util.writeBuff(("" + files.size()).getBytes(encoding));
                totalRead = util.readBuff(buff);

                if (totalRead != -1) {
                    logger.info("Server Response : "+new String(buff, 0, totalRead, encoding));

                    while(!files.isEmpty()) {
                        try {
                            selectedFile = files.get(0);
                            logger.info("Sending file...."+selectedFile.getName());
                            fbuff = new BufferedInputStream(new FileInputStream(selectedFile),BUFFER_SIZE);
                            totalsize+=selectedFile.length();
                            //send file name & size
                            util.writeBuff((selectedFile.getName() + "$$$$" + selectedFile.length()).getBytes(encoding));

                            //response from server
                            totalRead = util.readBuff(buff);
                            if (totalRead != -1) {
                                String msg = new String(buff, 0, totalRead, encoding);
                                logger.info("Server Response : "+msg);

                                controller.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        controller.setSecondaryVisualEffect(selectedFile.getName(),selectedFile.length());
                                    }
                                });

                                int tmpCt=0;
                                totalSent=0;
                                while ((totalRead = getBuff(buff)) > -1 && !stop) {
                                    tmpCt+=totalRead;
                                    totalSent+=totalRead;

                                    try {
                                        util.writeBuff(buff, 0, totalRead);
                                    }catch (IOException e){
                                        logger.log(Level.WARNING,"Connection ended by receiver "+new Date().toString(),e.getMessage());


                                        controller.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                controller.changeStates();
                                                controller.clearVisualEffect();
                                                controller.showMessage("ERROR SENDING!!!","Receiver closed the connection!");
                                            }
                                        });

                                        stop=true;
                                        fbuff.close();
                                        return;
                                    }

                                    if(tmpCt>=DEFAULT_BUFFER_SIZE){
                                        util.flushStream();
                                        tmpCt=0;
                                    }
                                    controller.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            controller.setPrimaryVisualEffect(totalSent,selectedFile.length());
                                        }
                                    });
                                }

                                util.flushStream();
                                fbuff.close();

                                if(stop){
                                    controller.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            controller.clearVisualEffect();
                                            controller.showMessage("SENDING INTERRUPTED!!","Did you just hit the stop button? ");
                                        }
                                    });

                                    return;
                                }
                                files.remove(0);
                                controller.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        controller.clearVisualEffect();
                                        controller.updateList();
                                    }
                                });



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
                        logger.info(selectedFile.getName()+" sent");
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

        }catch (IOException ee){
            if(stop)return;
            final String e=ee.getMessage();
            controller.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   controller.showMessage("ERROR WHILE CONNECTING","Can't Connect to host "+e);
                    controller.changeStates();
                }
            });
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

        final double sentInMB=(double)totalsize/(1024*1024);
        final double timetaken=(double)(System.currentTimeMillis()-startTime)/1000;

        controller.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                controller.changeStates();
                controller.showMessage("SUCCESS!!","You sent "+new DecimalFormat("#0.000").format(sentInMB)+" MB in "+new DecimalFormat("#0.000").format(timetaken)+" second(s)");

            }
        });
        util.closeAll();
        logger.info("Session closed at "+new Date().toString());
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
}
