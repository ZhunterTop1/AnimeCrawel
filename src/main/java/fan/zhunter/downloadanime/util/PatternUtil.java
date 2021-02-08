package fan.zhunter.downloadanime.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 1.单纯的选择使用Pattern，就OK
 * 2. 模拟测试API
 *
 * */
public class PatternUtil {
    public static String pattern(String page,String regex) {
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(page);
        String re = null;
        while (matcher.find()){
            re = matcher.group();
            break;
        }
        return re;
    }
}
