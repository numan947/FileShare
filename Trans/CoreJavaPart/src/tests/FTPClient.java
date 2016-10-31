package tests;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by numan947 on 10/23/16.
 **/
public class FTPClient {

    static byte[] convertToBytes(File f) throws IOException {
        byte[] bb = new byte[(int) f.length()];
        FileInputStream fis = new FileInputStream(f);
        fis.read(bb);
        fis.close();
        return bb;
    }


    public static void main(String[] args) {
        Socket localSocket = null;
        File localFile = null;
        InputStream is = null;
        OutputStream os = null;


        try {
            localSocket = new Socket("localhost", 0);
            System.out.println(localSocket.getPort());
            //localFile = new File("/home/numan947/MyHome/Important/AndroidGraphicsResources/ETC/Holo.png");
            //System.out.println(localFile.exists());

            byte[] myByte = convertToBytes(localFile);


            //is = localSocket.getInputStream();
            //os = localSocket.getOutputStream();
            //is = new BufferedInputStream(is, 1024);
            //os = new BufferedOutputStream(os, myByte.length);

            String fName = localFile.getName() + "$$$$" + myByte.length + '?';

            //os.write(fName.getBytes());
            //os.flush();

            //os.write(myByte);
            //os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
