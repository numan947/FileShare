package coreJava.ClientPackage;

import Controller.V2Controller;
import javafx.scene.control.Alert;

import java.io.*;
import java.text.DecimalFormat;

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

    //file transfer related
    private String[]fileAddresses=null;
    private String[]fileNames=null;
    private File[]files=null;
    private File selectedFile=null;
    private BufferedInputStream fbuff=null;
    private byte[]buff=null;
    private char separator=File.separatorChar;
    private String encoding="UTF-8";

    //to add gui
    private Thread thread=null;
    private V2Controller controller=null;
    private boolean stop;

    //statistics flags
    long startTime;
    long totalTime;
    long totalsize;


    public Client(V2Controller controller,String serverAddress,File[]files) {

        this.controller=controller;
        this.serverAddress=serverAddress;
        this.files=files;
        System.out.println(files.length);
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
            System.out.println("Exception In ClientPackage.Client.getBuff "+e.getMessage());
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
                System.out.println(new String(buff, 0, totalRead, encoding));

                util.writeBuff(("" + files.length).getBytes(encoding));
                totalRead = util.readBuff(buff);

                if (totalRead != -1) {
                    System.out.println(new String(buff, 0, totalRead, encoding));

                    for (int i = 0; i < files.length; i++) {
                        try {
                            System.out.println("Sending File " + (i + 1) + "...");
                            selectedFile = files[i];
                            fbuff = new BufferedInputStream(new FileInputStream(selectedFile),BUFFER_SIZE);
                            totalsize+=selectedFile.length();
                            //send file name & size
                            util.writeBuff((selectedFile.getName() + "$$$$" + selectedFile.length()).getBytes(encoding));

                            //response from server
                            totalRead = util.readBuff(buff);
                            if (totalRead != -1) {
                                String msg = new String(buff, 0, totalRead, encoding);

                                System.out.println(msg);

                                controller.setSecondaryVisualEffect(selectedFile.getName(),selectedFile.length());

                                int tmpCt=0;
                                int totalSent=0;
                                while ((totalRead = getBuff(buff)) > -1 && !stop) {
                                    tmpCt+=totalRead;
                                    totalSent+=totalRead;

                                    try {
                                        util.writeBuff(buff, 0, totalRead);
                                    }catch (IOException e){
                                        controller.changeSendStopButtonStates();
                                        controller.clearVisualEffect();
                                        controller.showMessage("ERROR SENDING!!!","Reciever closed the connection!", Alert.AlertType.ERROR);
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
                                    //TODO FIXIT
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

                                    System.out.println(msg);
//TODO cleanup these
/*                                    System.out.println("File " + i + " sent in stream");
                                    System.out.println("Time taken "+(System.currentTimeMillis()-ct)/1000);
                                    ct=System.currentTimeMillis();*/
                                }
                            }
                        } catch (IOException e) {
                            System.out.println("Exception In ClientPackage.Client.processAndSend, iteration: " + i + " " + e.getMessage());
                        }
                    }
                }
                totalRead = util.readBuff(buff);
                if (totalRead != -1) System.out.println(new String(buff, 0, totalRead, encoding));
            }
        } catch (UnsupportedEncodingException ee) {
            System.out.println("Exception In ClientPackage.Client.processAndSend "+ee.getMessage());
        }
        System.out.println("total time taken to send file: "+(System.currentTimeMillis()-startTime)/1000.0);
        System.out.println("total sent in MB: "+((double)totalsize/(1024*1024)));
    }



    @Override
    public void run() {
        //TODO remove
        System.out.println(port+" GOT "+ serverAddress);
        try {
            this.util = new NetworkUtil(serverAddress, port);

        }catch (IOException e){
            if(stop)return;
            controller.showMessage("ERROR WHILE CONNECTING","Can't Connect to host "+e.getMessage(), Alert.AlertType.ERROR);
            controller.changeSendStopButtonStates();
            return;
        }

        if(stop){
            System.out.println("BREAKING THREAD");
            util.closeAll();
            return;
        }

        this.processAndSend();

        if(stop){
            System.out.println("BREAKING THREAD");
            util.closeAll();
            return;
        }

        double sentInMB=(double)totalsize/(1024*1024);
        double timetaken=(double)(System.currentTimeMillis()-startTime)/1000;

        controller.showMessage("SUCCESS!!","You sent "+new DecimalFormat("#0.000").format(sentInMB)+" MB in "+new DecimalFormat("#0.000").format(timetaken)+" second(s)", Alert.AlertType.INFORMATION);
        controller.changeSendStopButtonStates();
        util.closeAll();
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
}
