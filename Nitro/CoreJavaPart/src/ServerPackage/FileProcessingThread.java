package ServerPackage;

import java.io.*;
import java.net.Socket;

/**
 * Created by numan947 on 10/26/16.
 **/
public class FileProcessingThread implements Runnable {
    //buffer related
    private int DEFAULT_BUFFER_SIZE=49152;
    private int BUFFER_SIZE;

    //server & network related
    private NetworkUtil util=null;

    //file transfer related
    private String DEFAULT_SAVE_FILE_PATH="/home/numan947/MyHome/Important/JAVA/testsForNitro";
    private String saveFilePath=null;
    private String fileName=null;
    private int fileSize;
    private BufferedOutputStream fbuff=null;
    private byte[]buff=null;
    private char separator=File.separatorChar;
    private String encoding="UTF-8";

    //Thread related
    Thread thread=null;


    public FileProcessingThread(Socket accept) {
        this.util=new NetworkUtil(accept);
        this.buff=new byte[DEFAULT_BUFFER_SIZE];
        this.thread=new Thread(this);
        thread.start();
    }

    private void writeToFile(byte[] b, int totalRead)
    {
        try {
            fbuff.write(b,0,totalRead);
        } catch (IOException e) {
            System.out.println("Exception In ServerPackage.FileProcessingThread.writeToFile "+e.getMessage());
        }
    }


    @Override
    public void run() {
        int totalRead;
        try {
            util.writeBuff("requesting total file numbers...".getBytes(encoding));
            totalRead=util.readBuff(buff);//buff has total number of files
            System.out.println(new String(buff,0,totalRead,encoding));
            int totalFiles=Integer.parseInt(new String(buff,0,totalRead,encoding));
            util.writeBuff(("Will recieve "+totalFiles+" file(s)").getBytes(encoding));
            for(int i=0;i<totalFiles;i++){
                totalRead=util.readBuff(buff);
                getFileNameAndSize(buff,totalRead);
                util.writeBuff(("recieved name "+fileName+" "+fileSize).getBytes(encoding));
                fbuff=new BufferedOutputStream(new FileOutputStream(DEFAULT_SAVE_FILE_PATH+separator+fileName));
                int cnt=0;

                while(true){
                    if(cnt>=fileSize)break;
                    totalRead=util.readBuff(buff);
                    cnt+=totalRead;
                    writeToFile(buff,totalRead);
                    System.out.println(fileSize+"  "+totalRead);
                }
                util.writeBuff(("File "+(i+1)+" recieved "+cnt+" bytes").getBytes(encoding));
            }

        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            System.out.println("Exception In ServerPackage.FileProcessingThread.run "+e.getMessage());
        }

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
}
