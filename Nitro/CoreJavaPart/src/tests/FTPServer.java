import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by numan947 on 10/23/16.
 **/
public class FTPServer {
    public static void main(String[] args) {
        ServerSocket socket=null;
        Socket clientSide=null;
        try {
            socket=new ServerSocket(1080);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true){
            try {
                new ReadingThread(socket.accept());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }





    }




}
