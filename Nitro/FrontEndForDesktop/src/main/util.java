package main;

import java.util.Hashtable;
import java.util.Random;

/**
 * Created by numan947 on 10/29/16.
 **/
public class util {
    private static Random random=new Random();
    public static Hashtable<Long,String>hashtable=new Hashtable<>();

    public static long generateID(String s)
    {
        long key;
        while(hashtable.containsKey(key=random.nextInt(100000))) {
        }
        hashtable.put(key,s);
        return key;
    }

}
