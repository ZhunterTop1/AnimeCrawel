package fan.zhunter.downloadanime.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 1. 获取视频类型
 * 2. url格式为 XXXX.格式？xxx=...
 * 3. regex = \\.flv || \\.mp4 || \\.ts || \\.m4s
 * */
public class UrlUtil {
    public static String getType(String url) {
        String regex = "\\.(flv|mp4|ts|m4s)";
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(url);
        String re = null;
        while (matcher.find()){
            re =  matcher.group();
            break;
        }
        return re.substring(re.indexOf(".") + 1, re.length());
    }
}
