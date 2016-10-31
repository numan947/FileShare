import coreJava.ClientPackage.Client;

import java.io.File;
import java.util.Vector;
import java.util.concurrent.RunnableFuture;

/**
 * Created by numan947 on 10/26/16.
 **/
public class ClientTester {


    static void getFiles(File f,Vector<File>v)
    {
        if(f.isDirectory()){
            for(File ff:f.listFiles())getFiles(ff,v);
        }
        if(!f.isDirectory()) v.addElement(f);
    }


    public static void main(String[] args) {
        Vector<File>v=new Vector<>();
        //File ff=new File("/media/numan947/Recreation/Music Videos");
        //getFiles(ff,v);
        //File ff=new File("/media/numan947/Recreation/Anime/Watched");
        File ff=new File("/media/numan947/Recreation/Light Novel/Tate no Yuusha no Nariagari (Completed)");
        getFiles(ff,v);

        File[]t= new File[v.size()];
        v.toArray(t);
        for(File tt:t) System.out.println(tt.getName());

        Client client=new Client("192.168.0.104", t);

    }
}
