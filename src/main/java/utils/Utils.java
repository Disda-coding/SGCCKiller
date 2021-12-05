package utils;

import java.util.regex.Pattern;

public class Utils {
    public static boolean isNull(String arg){
        if (arg==null&&arg.equals("")) return true;
        return false;
    }
    public static void printLongStuff(String s,int linesize){
        for(int i=0;i<((int)s.length()/linesize)+1;i++){
            if (i==(int)s.length()/linesize){System.out.println(s.substring(i*linesize));}
            else System.out.println(s.substring(i*linesize,(i+1)*linesize));
        }
    }
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}
