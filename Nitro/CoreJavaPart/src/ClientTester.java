import ClientPackage.Client;

import java.io.File;
import java.util.Vector;

/**
 * Created by numan947 on 10/26/16.
 **/
public class ClientTester {
    public static void main(String[] args) {
        Vector<String>v=new Vector<>();
        File ff=new File("/media/numan947/Recreation/Music Videos");
        for(File f:ff.listFiles()){
            v.addElement(f.getAbsolutePath());
        }
        String[]ss=new String[v.size()];
        for(int i=0;i<v.size();i++)ss[i]=v.get(i);



        Client client=new Client("localhost",ss);
        client.processAndSend();
    }
}
