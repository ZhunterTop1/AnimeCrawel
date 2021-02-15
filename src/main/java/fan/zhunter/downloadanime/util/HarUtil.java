package fan.zhunter.downloadanime.util;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.*;

public class HarUtil {
    //public static long offset = 10000000000000l;
    public static Map<Long, String> getHarMap() throws IOException {
        String path = "./p.json";
        File file = new File(path);
        //将一个文件变成一个字符串
        //file最后可能以,结尾
        modify(file);
        BufferedReader reader = new BufferedReader(new FileReader("./a.json"));
        JsonParser parser = new JsonParser();
        JsonObject raw = parser.parse(reader).getAsJsonObject();
        JsonArray events = raw.getAsJsonArray("events");
        long start = raw.getAsJsonObject("constants").get("timeTickOffset").getAsLong();
        int n = events.size();
        HashMap<Long, String> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            JsonObject tmp = events.get(i).getAsJsonObject();
            String val = tmp.toString();
            if (val.contains("params") && (hasVideo(val) || val.contains("headers"))) {
                long id = tmp.getAsJsonObject("source").get("id").getAsLong();
                long start_time = tmp.getAsJsonObject("source").get("start_time").getAsLong();
//                long key = id * offset + (start + start_time);
                String params = tmp.getAsJsonObject("params").toString();
                String orDefault = map.getOrDefault(id,"");
                if("".equals(orDefault)){
                    orDefault += (start + start_time);
                }
                map.put(id, orDefault + "\t" + params);
            }
        }
        ArrayList<Long> longs = new ArrayList<>(map.keySet());
        longs.sort(null);
        HashSet<String> set = new HashSet<>();
        Map<Long, String> re = new TreeMap<>();
        for (Long key : longs) {
            String s = map.get(key);
            if(useful(s)){
                String url = UrlUtil.getUrl(s);
                if(!set.contains(url)){
                    set.add(url);
                    re.put(key, s);
//                    System.out.println(key + "." + s);
                }
            }
            //filter
        }
        return re;
    }
    public static boolean useful(String v){
        return v.contains("MEDIUM");
    }

    public static void modify(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./a.json")));
        String p = null;
        String before = null;
        while ((p = reader.readLine()) != null){
            if((p.startsWith("{")&&(p.endsWith("}") || p.endsWith(","))) || p.startsWith("\"events\"")){
                writer.write(p);
                writer.write("\n");
                writer.flush();
                before = p;
            }
        }
        if(before.endsWith(",")){
            writer.write("{}]}");
            writer.flush();
        }
    }

    public static boolean hasVideo(String raw){
        if(Utils.isEmpty(raw)) return false;
        boolean isVideo = raw.contains(".mp4") || raw.contains(".m4s") || raw.contains(",m3u8")
                || raw.contains(".flv");
        return isVideo && raw.contains("MEDIUM");
    }

    public static void main(String[] args) throws IOException {
        HarUtil.getHarMap();
    }
}
