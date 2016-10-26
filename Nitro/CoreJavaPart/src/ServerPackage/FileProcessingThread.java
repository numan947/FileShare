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
    private String fileNames=null;
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

    private void writeToFile(byte[]b)
    {
        try {
            fbuff.write(b);
        } catch (IOException e) {
            System.out.println("Exception In ServerPackage.FileProcessingThread.writeToFile "+e.getMessage());
        }
    }


    @Override
    public void run() {
        try {
            util.writeBuff("requesting total file numbers...".getBytes(encoding));
            util.readBuff(buff);//buff has total number of files
            System.out.println(new String(buff,encoding));
            int totalFiles=Integer.parseInt(new String(buff,encoding));
            /*util.writeBuff(("Will recieve "+totalFiles+" files").getBytes(encoding));
            for(int i=0;i<totalFiles;i++){
                util.readBuff(buff);
                String[]s=(new String(buff,encoding)).split("\\$\\$\\$\\$");
                util.writeBuff(("recieved name "+s[0]+" "+s[1]).getBytes(encoding));
                fbuff=new BufferedOutputStream(new FileOutputStream(DEFAULT_SAVE_FILE_PATH+separator+s[0]));
                int cnt=0;

                while(true){
                    int cc=util.readBuff(buff);
                    if((new String(buff,encoding)).equals("$$$$"))break;
                    cnt+=cc;
                    writeToFile(buff);
                }
                if(cnt!=Integer.parseInt(s[1])){
                    System.out.println("File "+i+" has probably been corrupted");
                }
                util.writeBuff(("File "+(i+1)+" recieved "+cnt+" bytes").getBytes(encoding));
            }*/
        } catch (UnsupportedEncodingException e) {
            System.out.println("Exception In ServerPackage.FileProcessingThread.run "+e.getMessage());
        }

    }
}
