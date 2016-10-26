package tests;

import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.net.Socket;
import java.util.Random;

/**
 * Created by numan947 on 10/23/16.
 **/
public class ReadingThread implements Runnable {
    Thread th;
    Socket so;
    InputStream is;
    OutputStream os;

    public ReadingThread(Socket socket) throws IOException {
        this.so = socket;
        is = so.getInputStream();
        os = so.getOutputStream();
        th = new Thread(this);
        th.start();
    }

    @Override
    public void run() {
        Random r=new Random();
        String fileName = "";
        //InputStreamReader iss=new InputStreamReader(is);

        try {
            while (true) {
                char ch=(char)is.read();
                if(ch=='?')break;
                fileName+=ch;
            }
            String[]info=fileName.split("\\$\\$\\$\\$");
            System.out.println(info.length);
            String newFile=r.nextInt()%5000+info[0];
            byte[]buff=new byte[Integer.parseInt(info[1])];
            File ff=new File(newFile);

            System.out.println(fileName);
            System.out.println(is.available());

            BufferedInputStream bis=new BufferedInputStream(is,is.available());
            int read=bis.read(buff);
            System.out.println(read);
            FileOutputStream fos=new FileOutputStream(ff);
            fos.write(buff);
            fos.flush();
            fos.close();

            is.close();
            os.close();
            fos.close();

        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("GETTING OUT");
    }
}
