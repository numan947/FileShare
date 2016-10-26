import java.io.*;
import java.net.Socket;

/**
 * Created by numan947 on 10/26/16.
 **/
public class NetworkUtil {
    private BufferedInputStream is=null;
    private BufferedOutputStream os=null;
    private Socket socket=null;

    public NetworkUtil(Socket socket) {
        try {
            this.socket = socket;
            this.os=new BufferedOutputStream(socket.getOutputStream());
            this.is=new BufferedInputStream(socket.getInputStream());
        }catch (IOException e) {
            System.out.println("Exception In NetworkUtil.Constructor1 "+e.getMessage());
        }
    }


    public NetworkUtil(String address,int port) {
        try {
            this.socket = new Socket(address,port);
            this.is=new BufferedInputStream(socket.getInputStream());
            this.os=new BufferedOutputStream(socket.getOutputStream());
        }catch (IOException e) {
            System.out.println("Exception In NetworkUtil.Constructor2 "+e.getMessage());
        }
    }

    public int readBuff(byte []buff) {
        try{
            return is.read(buff);
        } catch (IOException e) {
            System.out.println("Exception In NetworkUtil.readBuff "+e.getMessage());
        }
        return 0;
    }
    public void writeBuff(byte[]buff){
        try{
            os.write(buff);
            os.flush();
        } catch (IOException e) {
            System.out.println("Exception In NetworkUtil.writeBuff "+e.getMessage());
        }
    }

    public void closeAll()
    {
        try {
            is.close();
            os.close();
            socket.getInputStream().close();
            socket.getOutputStream().close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Exception In NetworkUtil.closeAll "+e.getMessage());
        }

    }





}
