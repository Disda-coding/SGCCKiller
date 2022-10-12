package utils;

import java.util.regex.Pattern;

public class Utils {
    public static boolean isNumeric(final CharSequence cs) {
        // 判断是否为空，如果为空则返回false
        if (cs==null||cs.length()==0) {
            return false;
        }
        // 通过 length() 方法计算cs传入进来的字符串的长度，并将字符串长度存放到sz中
        final int sz = cs.length();
        // 通过字符串长度循环
        for (int i = 0; i < sz; i++) {
            // 判断每一个字符是否为数字，如果其中有一个字符不满足，则返回false
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        // 验证全部通过则返回true
        return true;
    }

    public static boolean isNull(String arg){
        if (arg==null||arg.equals("")) return true;
        return false;
    }
    public static String trim(String s){
        String result = "";
        if(null!=s && !"".equals(s)){
            result = s.replaceAll("^[　*| *| *|//s*]*", "").replaceAll("[　*| *| *|//s*]*$", "");
        }
        return result;
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
