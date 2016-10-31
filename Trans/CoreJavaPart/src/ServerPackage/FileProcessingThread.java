package ServerPackage;

import java.io.*;
import java.net.Socket;

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
    private boolean corrupted;


    public FileProcessingThread(Socket accept) {
        this.util=new NetworkUtil(accept);
        this.buff=new byte[NETWORK_BUFFER_SIZE];
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


    @Override
    public void run() {
        long totalSizetoRecieve=0;
        long ct=System.currentTimeMillis();
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
                        totalSizetoRecieve+=fileSize;
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


                        while (true) {
                            if (cnt >= fileSize||totalRead==-1) break;
                            totalRead = util.readBuff(buff);
                            cnt += totalRead;
                            writeToFile(buff, totalRead,cnt);
                            System.out.println(fileSize + "  " + totalRead);
                        }
                        if(cnt < fileSize||totalRead==-1){
                            System.out.println("Problem while transferring file..probably file's become corrupted");
                            corrupted=true;
                            f.delete();
                        }
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
            util.closeAll();
            System.out.println("total time taken to receive file: "+(System.currentTimeMillis()-ct));
            System.out.println("total recieved in MB: "+(totalSizetoRecieve/(1024*1024)));
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
