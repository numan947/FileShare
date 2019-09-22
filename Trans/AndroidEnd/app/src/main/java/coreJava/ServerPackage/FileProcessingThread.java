package coreJava.ServerPackage;

import android.os.Environment;

import com.example.numan947.androidend.ServerActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * Created by numan947 on 10/26/16.
 **/
public class FileProcessingThread implements Runnable {
    //buffer related
    private int NETWORK_BUFFER_SIZE=8192;
    private int FILE_BUFFER_SIZE=8192;

    //server & network related
    private NetworkUtil util=null;
    private  boolean errorFlag;

    //file transfer related
    private String DEFAULT_SAVE_FILE_PATH=null;
    private String fileName=null;
    private int fileSize;
    private BufferedOutputStream fbuff=null;
    private byte[]buff=null;
    private char separator=File.separatorChar;
    private String encoding="UTF-8";
    private File f;


    //gui related
    private ServerActivity controller=null;
    private boolean stop;
    private long totalreceived;
    private long startTime;
    int cnt;

    private static Logger logger=null;
    private void ConfigLog()
    {
            logger=Logger.getLogger(FileProcessingThread.class.getName());
        FileHandler fh = null;
        try {
            fh = new FileHandler(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"Trans"+File.separator+".LOG"+ File.separator+FileProcessingThread.class.getName()+"_logFile.log",true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleFormatter formatter=new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.addHandler(fh);
    }



    public FileProcessingThread(Socket accept, ServerActivity controller,String DEFAULT_SAVE_FILE_PATH) {
        this.ConfigLog();
        this.controller=controller;
        this.DEFAULT_SAVE_FILE_PATH=DEFAULT_SAVE_FILE_PATH;
        this.util=new NetworkUtil(accept);
        this.buff=new byte[NETWORK_BUFFER_SIZE];
        this.stop=false;errorFlag=false;
        Thread thread = new Thread(this);
        thread.start();
    }

    private void writeToFile(byte[] b, int totalRead,int cnt)
    {
        try {
            fbuff.write(b,0,totalRead);
            if(3*cnt>=FILE_BUFFER_SIZE)fbuff.flush();
        } catch (IOException e) {
            logger.log(Level.SEVERE,"Problem while writing to file",e.getMessage());
        }
    }


    public void receiveAndProcess()
    {
        logger.log(Level.INFO,"receiving & processing session started "+new Date().toString());
        totalreceived =0;
        startTime=System.currentTimeMillis();

        int totalRead;
        boolean corrupted = false;
        try {
            util.writeBuff("requesting total file numbers...".getBytes(encoding));
            totalRead=util.readBuff(buff);//buff has total number of files
            if(totalRead!=-1) {
                logger.info("Client Response: "+new String(buff, 0, totalRead, encoding));
                int totalFiles = Integer.parseInt(new String(buff, 0, totalRead, encoding));
                util.writeBuff(("Will receive " + totalFiles + " file(s)").getBytes(encoding));
                for (int i = 0; i < totalFiles; i++) {
                    totalRead = util.readBuff(buff);
                    cnt = 0;
                    if(totalRead!=-1) {
                        getFileNameAndSize(buff, totalRead);
                        totalreceived +=fileSize;
                        util.writeBuff(("received name " + fileName + " " + fileSize).getBytes(encoding));
                        f=new File(DEFAULT_SAVE_FILE_PATH + separator + fileName);
                        int exist=1;
                        if(f.exists()) {
                            while (f.exists())
                            {
                                f = new File(DEFAULT_SAVE_FILE_PATH + separator + "(" + exist + ")" + fileName);
                                exist++;
                            }
                        }
                        fbuff = new BufferedOutputStream(new FileOutputStream(f),FILE_BUFFER_SIZE);


                        controller.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                controller.setSecondaryVisualEffect(fileName,fileSize);
                            }
                        });

                        while (!stop) {
                            if (cnt >= fileSize||totalRead==-1) break;
                            totalRead = util.readBuff(buff);
                            if(totalRead!=-1) {
                                cnt += totalRead;
                                writeToFile(buff, totalRead, cnt);
                                controller.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        controller.setPrimaryVisualEffect(cnt, fileSize);
                                    }
                                });
                            }
                            //System.out.println(fileSize + "  " + totalRead);
                        }

                        fbuff.close();

                        if(cnt < fileSize||totalRead==-1){
                            logger.log(Level.WARNING,"Problem while transferring file..probably file's become corrupted "+f.getName());
                            corrupted =true;
                            f.delete();
                            if(totalRead==-1){
                                logger.info("Connection disconnected by sender");
                                errorFlag=true;
                                controller.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        controller.regenerateServer();
                                        controller.clearVisualEffect();
                                        controller.showMessage("ERROR!!","Problem while receiving"+ f.getName()+" sender disconnected the connection");
                                    }
                                });
                                return;
                            }
                            else if(stop){

                                logger.log(Level.WARNING,"Connection disconnected by receiver");
                                errorFlag=true;
                                controller.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        controller.clearVisualEffect();
                                        controller.showMessage("Receiving stopped","Did you stopped the server?");
                                    }
                                });
                                return;
                            }
                        }


                        //TODO ui update
                        controller.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                controller.clearVisualEffect();
                                controller.updateList(f);
                            }
                        });

                    }else{
                        logger.log(Level.WARNING,"Problem while transferring file..probably file's become corrupted ");
                        corrupted =true;
                    }
                    if(!corrupted)util.writeBuff(("File " + (i + 1) + " received " + cnt + " bytes").getBytes(encoding));
                }
            }else{
                logger.log(Level.WARNING,"Problem while transferring file..probably file's become corrupted ");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE,"Exception in serverpackage.FileProcessingThread ",e.getMessage());
        }        final double dd=(double) totalreceived /(1024*1024);
        if(!errorFlag) {
            final double tt = (double) (System.currentTimeMillis() - startTime) / 1000;
            final String host = util.getSocket().getInetAddress().getHostName();
            controller.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    controller.showMessage("SUCCESS!!", "You received " + new DecimalFormat("#0.00").format(dd) + " in " + tt + " second(s) from " + host);
                }
            });
            logger.log(Level.INFO,"Successfully received "+dd+" MB in "+dd+" second(s)\nSession ended");

        }
        controller.regenerateServer();
    }


    @Override
    public void run() {
        this.receiveAndProcess();
        util.closeAll();
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
