import ClientPackage.Client;

/**
 * Created by numan947 on 10/26/16.
 **/
public class ClientTester {
    public static void main(String[] args) {
        String[]file=new String[1];
        file[0]="/home/numan947/MyHome/Important/ICONS/board.png";


        Client client=new Client("localhost",file);
        client.processAndSend();
    }
}
