package fan.zhunter.downloadanime.service.m3u8;

import fan.zhunter.downloadanime.service.builder.IDownLoadHandler;
import fan.zhunter.downloadanime.util.Utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import static fan.zhunter.downloadanime.common.config.ACrawelConfig.Workplace;

public class M3U8Handler implements IDownLoadHandler {
    @Override
    public boolean typeCheck (String msg){
        return Utils.isNotEmpty(msg) && msg.contains(".m3u8");
    }
    public static HashMap<String, String> analyM3u8(String url) throws IOException {
        String root = url.split("index")[0];
        InputStream in = null;
        String filePath = Workplace + "\\" + Utils.getMD5(url) + ".m3u8";
        File file1 = new File(filePath);
        FileOutputStream f = null;
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.setRequestProperty("accept","*/*");
            conn.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8");
            conn.setRequestProperty("Cache-Control","max-age=0");
            conn.setRequestProperty("connection","Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3970.5 Safari/537.36");
            in = conn.getInputStream();
            if (!file1.getParentFile().exists()) {
                file1.getParentFile().mkdirs();
            }

            f = new FileOutputStream(filePath);
            byte[] bb = new byte[1024];
            int len;
            while ((len = in.read(bb)) > 0) {
                f.write(bb,0,len);
                f.flush();
            }
            f.close();
            in.close();
        } catch (Exception e) {
            return null;
        }
        String rowUrl = url;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));
            String val = null;
            while ((val = reader.readLine()) != null) {
                if (val.endsWith(".ts")) {
                    break;
                }
                if (val.endsWith(".m3u8")) {
                    root = mergeIndex(rowUrl,val);
                    URLConnection urlConnection = new URL(root).openConnection();
                    urlConnection.setRequestProperty("accept","*/*");
                    urlConnection.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8");
                    urlConnection.setRequestProperty("Cache-Control","max-age=0");
                    urlConnection.setRequestProperty("connection","Keep-Alive");
                    urlConnection.setRequestProperty("user-agent",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3970.5 Safari/537.36");
                    InputStream in2 = urlConnection.getInputStream(); //创建连接、输入copy流
                    FileOutputStream f2 = new FileOutputStream(filePath);//创建文百件度输出流
                    byte[] bb2 = new byte[1024]; //接收缓存
                    int len2 = 0;
                    while ((len2 = in2.read(bb2)) > 0) { //接收
                        f2.write(bb2,0,len2); //写入问文件
                    }
                    in.close();
                    in2.close();
                    f2.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(filePath);
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        HashMap<String, String> map = new HashMap<>();

        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String info = null;
        String suffix = ".ts";
        int i = 0;
        while ((info = bufferedReader.readLine()) != null) {
            if (info.endsWith(".ts")) {
                map.put(++i + suffix,mergeIndex(root,info));
            }
        }
        bufferedReader.close();
        return map;
    }

    private static String mergeIndex(String index,String end) {
        index = index.replaceAll("//","/");
        index = index.replaceAll(":/","://");
        end = end.replaceAll("//","/");
        end = end.replaceAll(":/","://");
        String s = index.substring(0,index.lastIndexOf("/"));
        s = deleteSlash(s);
        String b = end.substring(0,end.lastIndexOf("/"));
        b = deleteSlash(b);
        while (!s.endsWith(b)) {
            b = b.substring(0,b.lastIndexOf("/"));
            if (b == null || "".equals(b)) {
                break;
            }
            b = deleteSlash(b);
        }
        String ret = null;
        if (b != null && !"".equals(b)) {
            s = s.substring(0,s.lastIndexOf(b));
        }
        ret = s + end;
        return ret;
    }
    private static String deleteSlash(String b) {
        while (b.endsWith("/")) {
            b = b.substring(0, b.lastIndexOf("/"));
        }
        return b;
    }
}
