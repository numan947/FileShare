package coreJava.ServerPackage;

import Controller.V3Controller;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.io.*;
import java.net.Socket;
import java.text.DecimalFormat;

/**
 * Created by numan947 on 10/26/16.
 **/
public class FileProcessingThread implements Runnable {
    //buffer related
    private int NETWORK_BUFFER_SIZE=12000;
    private int FILE_BUFFER_SIZE=36000;

    //server & network related
    private NetworkUtil util=null;

    //file transfer related
    private String DEFAULT_SAVE_FILE_PATH=null;
    private String saveFilePath=null;
    private String fileName=null;
    private int fileSize;
    private BufferedOutputStream fbuff=null;
    private byte[]buff=null;
    private char separator=File.separatorChar;
    private String encoding="UTF-8";

    //Thread related
    private Thread thread=null;
    private boolean corrupted;


    //gui related
    private V3Controller controller=null;
    private boolean stop;
    long totalrecieved;
    long startTime;


    public FileProcessingThread(Socket accept, V3Controller controller,String DEFAULT_SAVE_FILE_PATH) {
        this.controller=controller;
        this.DEFAULT_SAVE_FILE_PATH=DEFAULT_SAVE_FILE_PATH;
        this.util=new NetworkUtil(accept);
        this.buff=new byte[NETWORK_BUFFER_SIZE];
        this.stop=false;
        this.thread=new Thread(this);
        thread.start();
    }

    private void writeToFile(byte[] b, int totalRead,int cnt)
    {
        try {
            fbuff.write(b,0,totalRead);
            if(3*cnt>=FILE_BUFFER_SIZE)fbuff.flush();
        } catch (IOException e) {
            System.out.println("Exception In ServerPackage.FileProcessingThread.writeToFile "+e.getMessage());
        }
    }


    public void receiveAndProcess()
    {
        totalrecieved=0;
        startTime=System.currentTimeMillis();

        int totalRead;
        corrupted=false;
        try {
            util.writeBuff("requesting total file numbers...".getBytes(encoding));
            totalRead=util.readBuff(buff);//buff has total number of files
            if(totalRead!=-1) {
                System.out.println(new String(buff, 0, totalRead, encoding));
                int totalFiles = Integer.parseInt(new String(buff, 0, totalRead, encoding));
                util.writeBuff(("Will recieve " + totalFiles + " file(s)").getBytes(encoding));
                for (int i = 0; i < totalFiles; i++) {
                    totalRead = util.readBuff(buff);
                    int cnt = 0;
                    if(totalRead!=-1) {
                        getFileNameAndSize(buff, totalRead);
                        totalrecieved+=fileSize;
                        util.writeBuff(("recieved name " + fileName + " " + fileSize).getBytes(encoding));
                        File f=new File(DEFAULT_SAVE_FILE_PATH + separator + fileName);
                        int exist=1;
                        if(f.exists()) {
                            while (f.exists())
                            {
                                f = new File(DEFAULT_SAVE_FILE_PATH + separator + "(" + exist + ")" + fileName);
                                exist++;
                            }
                        }
                        fbuff = new BufferedOutputStream(new FileOutputStream(f),FILE_BUFFER_SIZE);
                        //TODO gui update
                        controller.setSecondaryVisualEffect(fileName,fileSize);

                        while (!stop) {
                            if (cnt >= fileSize||totalRead==-1) break;
                            totalRead = util.readBuff(buff);
                            if(totalRead!=-1) {
                                cnt += totalRead;
                                writeToFile(buff, totalRead, cnt);
                                //TODO gui
                                controller.setPrimaryVisualEffect(cnt, fileSize);
                            }
                            System.out.println(fileSize + "  " + totalRead);
                        }

                        fbuff.close();

                        if(cnt < fileSize||totalRead==-1){
                            System.out.println("Problem while transferring file..probably file's become corrupted");
                            corrupted=true;
                            f.delete();
                            if(totalRead==-1){
                                //TODO gui
                                controller.regenerateServer();
                                controller.clearVisualEffect();
                                controller.showMessage("ERROR!!","Problem while receiving"+ f.getName()+" device disconnected the connection", Alert.AlertType.ERROR);
                                return;
                            }
                            else if(stop){
                                //TODo gui
                                controller.clearVisualEffect();
                                controller.showMessage("Receiving stopped","Did you stopped the server?", Alert.AlertType.WARNING);
                                return;
                            }
                        }


                        //TODO ui update
                        controller.clearVisualEffect();
                        controller.updateFileList(f);
                    }else{
                        System.out.println("Problem while transferring file..probably file's become corrupted");
                        corrupted=true;
                    }
                    if(!corrupted)util.writeBuff(("File " + (i + 1) + " recieved " + cnt + " bytes").getBytes(encoding));
                }
            }else{
                System.out.println("Problem while transferring file..probably file's become corrupted");
                corrupted=true;
            }
        } catch (IOException e) {
            System.out.println("Exception In ServerPackage.FileProcessingThread.run "+e.getMessage());
        }
        controller.regenerateServer();
    }


    @Override
    public void run() {
        this.receiveAndProcess();
        util.closeAll();
        double dd=(double)totalrecieved/(1024*1024);
        double tt=(double)(System.currentTimeMillis()-startTime)/1000;
        controller.showMessage("SUCCESS!!","You recieved "+new DecimalFormat("#0.00").format(dd)+" in "+tt+" second(s) from "+util.getSocket().getInetAddress().getHostName(), Alert.AlertType.INFORMATION);
    }

    private void getFileNameAndSize(byte[] buff,int totalRead) {
        try {
            String[]s=(new String(buff,0,totalRead,encoding)).split("\\$\\$\\$\\$");
            this.fileName=s[0];
            this.fileSize = Integer.parseInt(s[1]);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
}
