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
    public static String getUrl(String context) {
        if(Utils.isEmpty(context) || ! context.contains("url")){
            return null;
        }
        String pattern = PatternUtil.pattern(context,"\"url\":\".*?\"");
        String[] split = pattern.split("\"");
        return split[split.length-1];
    }

    public static String getRefer(String context) {
        if(Utils.isEmpty(context) || ! context.contains("refer")){
            return null;
        }
        String pattern = PatternUtil.pattern(context,"\"referer:.*?\"");
        String[] split = pattern.split("referer: ");
        String s = split[split.length - 1];
        if(s == null){
            return null;
        }
        return s.split("\"")[0];
    }
}
