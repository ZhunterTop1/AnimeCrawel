package fan.zhunter.downloadanime.util;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.*;

import static fan.zhunter.downloadanime.common.config.ACrawelConfig.HarMod;
import static fan.zhunter.downloadanime.common.config.ACrawelConfig.HarRaw;

public class HarUtil {
    public static Map<Long, String> getHarMap() throws IOException{
        return getHarMap(null);
    }
    public static Map<Long, String> getHarMap(TreeMap<Long, Integer> timeline) throws IOException {
        File in = new File(HarRaw);
        File out = new File(HarMod);
        modify(in, out);
        BufferedReader reader = new BufferedReader(new FileReader(HarMod));
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
                String params = tmp.getAsJsonObject("params").toString();
                String orDefault = map.getOrDefault(id,"");
                if("".equals(orDefault)){
                    orDefault += (start + start_time);
                }
                map.put(id, orDefault + "\t" + params);
            }
        }
        ArrayList<Long> longs = new ArrayList<>(map.keySet());
        ArrayList<Long> buckets = new ArrayList<>();
        if(Utils.isNotEmpty(timeline)){
            buckets = new ArrayList<>(timeline.keySet());
            Collections.sort(buckets);
        }
        longs.sort(null);
        Map<Long, String> re = new TreeMap<>();
        for (Long key : longs) {
            String content = map.get(key);
            if(hasVideo(content)){
                if(Utils.isNotEmpty(timeline)){
                    long startTime = Long.parseLong(content.split("\t")[0]);
                    if(startTime < timeline.firstKey()) continue;
                    long bucket = getBucket(buckets, startTime);
                    System.out.println(bucket + "," + startTime + "," + content);
                }
            }
        }
        return re;
    }
    public static boolean useful(String v){
//        return v.contains("MEDIUM");
        return v.contains(".m3u8");
    }

    public static void modify(File in, File out) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(in));
        BufferedWriter writer = new BufferedWriter(new FileWriter(out));
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
        return isVideo;
    }
    private static long getBucket(ArrayList<Long> longs,long time) {
        int n = longs.size();
        int l=0,r=n-1;
        while (l < r){
            int mid = l + (r-l+1)/2;
            if(longs.get(mid) > time){
                r = mid -1;
            }else{
                l = mid;
            }
        }
        return longs.get(l);
    }

    public static void main(String[] args) throws IOException {
        HarUtil.getHarMap();
    }
}
