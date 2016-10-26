package ClientPackage;

import java.io.*;

/**
 * Created by numan947 on 10/26/16.
 **/
public class Client {
    //buffer related
    private int DEFAULT_BUFFER_SIZE=49152;
    private int BUFFER_SIZE;

    //server & network related
    private int port=46043;
    private String serverAddress=null;
    private NetworkUtil util=null;

    //file transfer related
    private String[]fileAddresses=null;
    private String[]fileNames=null;
    private File selectedFile=null;
    private BufferedInputStream fbuff=null;
    private byte[]buff=null;
    private char separator=File.separatorChar;
    private String encoding="UTF-8";


    public Client(String serverAddress,String[]fileAddresses) {

        this.serverAddress=serverAddress;
        this.fileAddresses=fileAddresses;
        this.util=new NetworkUtil(serverAddress,port);
        this.buff=new byte[DEFAULT_BUFFER_SIZE];

    }

    public Client() {
        util=new NetworkUtil(serverAddress,port);
        buff=new byte[DEFAULT_BUFFER_SIZE];
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

    private void generateNames()
    {
        fileNames=new String[fileAddresses.length];
        int cnt=0;
        for(String i: fileAddresses){
            fileNames[cnt++]=i.substring(i.lastIndexOf(this.separator)+1);
        }
    }



    public void processAndSend()
    {
        try {
            this.generateNames();

            //how many file to send and response from server
            util.readBuff(buff);
            System.out.println(new String(buff));
            util.writeBuff(("" + fileAddresses.length).getBytes());
            util.readBuff(buff);
            System.out.println(new String(buff));

            for (int i = 0; i < fileAddresses.length; i++) {
                try {
                    System.out.println("Sending File " + (i + 1) + "...");
                    selectedFile = new File(fileAddresses[i]);
                    fbuff = new BufferedInputStream(new FileInputStream(selectedFile));

                    //send file name & size
                    util.writeBuff((fileNames[i] + "$$$$" + selectedFile.length()).getBytes(encoding));

                    //response from server
                    util.readBuff(buff);
                    String msg = new String(buff);
                    System.out.println(msg);

                    while (getBuff(buff) > -1) {
                        util.writeBuff(buff);
                    }
                    util.writeBuff("$$$$".getBytes());

                    //response from server
                    util.readBuff(buff);
                    msg = new String(buff);
                    System.out.println(msg);

                    System.out.println("File " + i + " sent in stream");
                } catch (FileNotFoundException | UnsupportedEncodingException e) {
                    System.out.println("Exception In ClientPackage.Client.processAndSend, iteration: " + i + " " + e.getMessage());
                }
            }
            util.readBuff(buff);
            System.out.println(new String(buff));
        } catch (UnsupportedEncodingException e) {
            System.out.println("Exception In ClientPackage.Client.processAndSend "+e.getMessage());
        }
        util.closeAll();
    }

}
