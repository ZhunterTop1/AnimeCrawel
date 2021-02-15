import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import fan.zhunter.downloadanime.util.Utils;
import org.junit.Test;

import java.io.*;

public class FileModTest {
    @Test
    public void test() throws IOException {
        String path = "./p.json";
        File file = new File(path);
        modify(file);
        //将一个文件变成一个字符串
        //file最后可能以,结尾
//        BufferedReader reader = new BufferedReader(new FileReader(file));
//        JsonParser parser = new JsonParser();
////        while (true){
//            try {
//                JsonObject raw = parser.parse(reader).getAsJsonObject();
//            }catch (JsonSyntaxException e){
//                String message = e.getMessage();
//                String s = message.split("line")[1].split("column")[0].trim();
//                System.out.println(s);
//            }
////        }
//        RandomAccessFile rf = new RandomAccessFile(file, "rw");

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

}
