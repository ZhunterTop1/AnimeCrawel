package fan.zhunter.downloadanime.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

/**
 * 1. 直接使用json语法就ok
 * 2. 使用JSONObject
 * 3. 要求初始时为一个Json对象{[[{},{}],[]]}
 * 4. data.dash.audio[0][1].baseUrl
 * 5. or data[dash][audio][0][baseUrl]
 * 6. . == [ ]
 * 7. data.dash[audio[0]]
 * 8. 限制为4
 * 9. 遇到. || [ ，就将之前的属性操作，如果attr = ""; 不操作
 * 10. 技术选型：多态，指针
 * */
public class JsonUtil {
    static JsonParser parser = new JsonParser();
    public static Object parseCore(String raw, String jsonE){
        JsonObject jsonObject = parser.parse(raw).getAsJsonObject();
        char[] chars = jsonE.toCharArray();
        int n = chars.length;
        String attr = "";
        int offset = 0;
        Object re = jsonObject;
        for (int i = 0; i < n; i++) {
            if(chars[i] == '.' || chars[i] == '['){
                if(!"".equals(attr)){
                    re = ((JsonObject) re).get(attr);
                }
                if (chars[i] == '['){
                    while (chars[++i] != ']'){
                        offset = offset*10 + (chars[i] - '0');
                    }
                    re = ((JsonArray) re).get(offset);
                }
                attr = "";
                offset = 0;
            } else {
                attr = attr + chars[i];
            }
        }
        if (!"".equals(attr)){
            re = ((JsonObject) re).get(attr);
        }
        return re;
    }
    public static String parse(String raw, String jsonE){
        return ((JsonPrimitive) parseCore(raw, jsonE)).getAsString();
    }
    public static int getListSize(String json, String jsonE){
        Object arr = parseCore(json, jsonE);
        return ((JsonArray)arr).size();
    }
}