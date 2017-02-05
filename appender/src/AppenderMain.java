import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by numan947 on 2/5/17.
 **/
public class AppenderMain {


    private static String PATH=System.getProperty("user.home")+File.separator+".appender";

    //checks paths and sets them
    private static void setupHOME()
    {
        File f=new File(PATH);

        if(f.exists()&&f.isDirectory()){
            return;
        }
        else if(!f.exists()){
            System.out.println("HELLO");
            System.out.println(f.mkdir());
        }
        else if(!f.isDirectory()){
            System.out.println("HOME IS NOT OKAY! DELETE ANYTHING NAMED <.appender> FROM YOUR HOME");
        }

    }


    public static void main(String[] args) throws IOException {

        setupHOME();
        if(args.length>2||args.length==0){
            System.out.println("ARGUMENTS SIZE ISN'T RIGHT, run --help as arguments to get help 1");
            return;
        }

        //single argument command
        else if(args.length==1){
            if(args[0].equals("--ls")) {
                File f=new File(PATH);
                File[]ff=f.listFiles();
                for(File x:ff){
                    System.out.println(x.getName());
                }
            }
            else if(args[0].equals("--dir")){
                Desktop.getDesktop().open(new File(PATH));
            }
            else if(args[0].equals("--help")){
                System.out.println("CREATE NEW FILE: --new <file name>");
                System.out.println("DELETE FILE: --rm <file name>");
                System.out.println("LIST FILE: --ls");
                System.out.println("OPEN FILE DIRECTORY: --dir");
            }
            else
                System.out.println("ARGUMENTS SIZE ISN'T RIGHT, run --help as arguments to get help 2");
            return;
        }
        //multi argument command
        if(args[0].equals("--rm")&&new File(PATH+File.separator+args[1]).exists()){
            new File(PATH+File.separator+args[1]).delete();
        }
        else if(args[0].equals("--new")) {
            if (new File(PATH + File.separator + args[1]).exists()) {
                System.out.println("FILE ALREADY EXISTS");
            } else {
                if(new File(PATH + File.separator + args[1]).createNewFile()){
                    System.out.println("FILE CREATED");
                }
            }
        }
        else{
            if(!new File(PATH+File.separator+args[0]).exists()){
                System.out.println("FILE DOESN'T EXIST, CREATE BEFORE INSERT");
            }
            else{
                FileWriter fos=new FileWriter(new File(PATH+File.separator+args[0]),true);
                String op=new SimpleDateFormat("dd-mm-yyyy hh:mm a").format(new Date())+"\n\n"+args[1]+"\n\n\n";
                fos.append(op);
                fos.close();
            }
        }
    }
}
