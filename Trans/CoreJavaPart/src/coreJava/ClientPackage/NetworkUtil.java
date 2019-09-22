package coreJava.ClientPackage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

/**
 * Created by numan947 on 10/26/16.
 **/
public class NetworkUtil {
    private BufferedInputStream is=null;
    private BufferedOutputStream os=null;
    private Socket socket=null;
    private int DEFAULT_BUFFER_SIZE=12000;
    private int BUFFER_SIZE;

    public NetworkUtil(Socket socket) {
        try {
            this.socket = socket;
            this.os=new BufferedOutputStream(socket.getOutputStream(),DEFAULT_BUFFER_SIZE);
            this.is=new BufferedInputStream(socket.getInputStream(),DEFAULT_BUFFER_SIZE);
        }catch (IOException e) {
            System.out.println("Exception In ClientPackage.NetworkUtil.Constructor1 "+e.getMessage());
        }
    }


    public NetworkUtil(String address, int port) {
        try {
            this.socket = new Socket(address,port);
            this.os=new BufferedOutputStream(socket.getOutputStream());
            this.is=new BufferedInputStream(socket.getInputStream());
        }catch (IOException e) {
            System.out.println("Exception In ClientPackage.NetworkUtil.Constructor2 "+e.getMessage());
        }
    }

    public int readBuff(byte []buff) {
        try{
            return is.read(buff);
        } catch (IOException e) {
            System.out.println("Exception In ClientPackage.NetworkUtil.readBuff "+e.getMessage());
        }
        return 0;
    }
    public void writeBuff(byte[]buff,int offset,int length){
        try{
            Random r=new Random();
            os.write(buff,offset,length);
        } catch (IOException e) {
            System.out.println("Exception In ClientPackage.NetworkUtil.writeBuff "+e.getMessage());
        }
    }

    public void flushStream()
    {
        try {
            os.flush();
        } catch (IOException e) {
            System.out.println("Exception In ClientPackage.NetworkUtil.flushStream "+e.getMessage());
        }
    }

    public void writeBuff(byte[]buff){
        try{
            os.write(buff);
            os.flush();
        } catch (IOException e) {
            System.out.println("Exception In ClientPackage.NetworkUtil.writeBuff "+e.getMessage());
        }
    }

    public void closeAll()
    {
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Exception In ClientPackage.NetworkUtil.closeAll "+e.getMessage());
        }

    }
    public Socket getSocket()
    {
        return this.socket;
    }

    public void setBUFFER_SIZE(int BUFFER_SIZE) {
        this.BUFFER_SIZE = BUFFER_SIZE;
        try {
            this.os=new BufferedOutputStream(socket.getOutputStream(),BUFFER_SIZE);
            this.is=new BufferedInputStream(socket.getInputStream(),BUFFER_SIZE);
        } catch (IOException e) {
            System.out.println("Exception In ClientPackage.NetworkUtil.setBUFFER_SIZE "+e.getMessage());
        }
    }
}
