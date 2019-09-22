package ServerPackage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by numan947 on 10/26/16.
 **/
public class NetworkUtil {
    private BufferedInputStream is=null;
    private BufferedOutputStream os=null;
    private Socket socket=null;
    private int NETWORK_BUFFER_SIZE=12000;
    private int FILE_BUFFER_SIZE=36000;

    public NetworkUtil(Socket socket) {
        try {
            this.socket = socket;
            this.os=new BufferedOutputStream(socket.getOutputStream(),NETWORK_BUFFER_SIZE);
            this.is=new BufferedInputStream(socket.getInputStream(),NETWORK_BUFFER_SIZE);
        }catch (IOException e) {
            System.out.println("Exception In ServerPackage.NetworkUtil.Constructor1 "+e.getMessage());
        }
    }


    public NetworkUtil(String address,int port) {
        try {
            this.socket = new Socket(address,port);
            this.os=new BufferedOutputStream(socket.getOutputStream());
            this.is=new BufferedInputStream(socket.getInputStream());
        }catch (IOException e) {
            System.out.println("Exception In ServerPackage.NetworkUtil.Constructor2 "+e.getMessage());
        }
    }

    public int readBuff(byte []buff) {
        try{
            return is.read(buff);
        } catch (IOException e) {
            System.out.println("Exception In ServerPackage.NetworkUtil.readBuff "+e.getMessage());
        }
        return 0;
    }
    public void writeBuff(byte[]buff,int offset,int length){
        try{
            os.write(buff,offset,length);
            os.flush();
        } catch (IOException e) {
            System.out.println("Exception In ServerPackage.NetworkUtil.writeBuff "+e.getMessage());
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
            System.out.println("Exception In ServerPackage.NetworkUtil.closeAll "+e.getMessage());
        }
    }

    public Socket getSocket()
    {
        return this.socket;
    }


}
