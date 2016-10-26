import ClientPackage.Client;

import java.io.File;
import java.util.Vector;

/**
 * Created by numan947 on 10/26/16.
 **/
public class ClientTester {


    static void getFiles(File f,Vector<String>v)
    {
        if(f.isDirectory()){
            for(File ff:f.listFiles())getFiles(ff,v);
        }
        if(!f.isDirectory()) v.addElement(f.getAbsolutePath());
    }


    public static void main(String[] args) {
        Vector<String>v=new Vector<>();
        //File ff=new File("/media/numan947/Recreation/Music Videos");
        //getFiles(ff,v);
        //File ff=new File("/media/numan947/Recreation/Anime/Watched");
        File ff=new File("/media/numan947/ETC/Anime/Not Watched/Denpa Teki na Kanojo [720p] mHD");
        getFiles(ff,v);

        String[]ss=new String[v.size()];
        for(int i=0;i<v.size();i++){
            ss[i]=v.get(i);
            System.out.println(v.get(i));
        }

        System.out.println(v.size());

        Client client=new Client("192.168.0.104",ss);
        client.processAndSend();
    }
}
