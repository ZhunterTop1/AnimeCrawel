import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * 1. 测试获取到的url是否有效
 * 2. 关键的链接在json中的位置：
 * 3. data.dash.video[0].baseUrl
 * 4. dash.dash.audio[0].baseUrl
 * 5. cookie需要refer
 * 6. cookie设置获取大会员页面
 * 7. 其中的bug等下载m3u8时再处理
 * */
public class ThreadDownTest {
    static final String url = "https://xy36x151x35x230xy.mcdn.bilivideo.cn:4483/upgcxcode/74/03/279490374/279490374_nb2-1-30280.m4s?expires=1613372825&platform=pc&ssig=IufFXGEYf5W9h1eUXlpfgw&oi=2029252057&trid=4f7d0e854b72426da74131090f81dea5p&nfc=1&nfb=maPYqpoel5MI3qOUX6YpRA==&mcdnid=8000097&mid=0&orderid=0,3&agrr=1&logo=A0000080";
    public static void main(String[] args) throws IOException, InterruptedException {
     URLConnection conn = new URL(url).openConnection();
     conn.setRequestProperty("accept", "*/*");
     conn.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8");
     conn.setRequestProperty("Cache-Control","max-age=0");
     conn.setRequestProperty("connection", "Keep-Alive");
     conn.setRequestProperty("user-agent",
             "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
     conn.setRequestProperty("Referer","https://www.bilibili.com/bangumi/play/ep374610/?spm_id_from=333.851.b_7265706f7274466972737431.1");
     conn.setRequestProperty("Cookie", "finger=158939783; _uuid=6A6526E6-34BD-3E26-713F-0A7720D7AC8582774infoc; buvid3=6A43A7B7-BA04-49F9-B035-CE6CD1F4CDB4185002infoc; fingerprint=691bb311e76f14d0c0d180085f16a546; buvid_fp=6A43A7B7-BA04-49F9-B035-CE6CD1F4CDB4185002infoc; buvid_fp_plain=2DE773AD-7542-4785-9FEC-312A1B05B48218552infoc; CURRENT_FNVAL=80; blackside_state=1; SESSDATA=54f82a73%2C1628149578%2C41012%2A21; bili_jct=13130a4ce4bbe458b9540eef6b366e1a; DedeUserID=195588405; DedeUserID__ckMd5=0441c9aa7209ac5b; sid=alsnafg4");
     // 发送POST请求必须设置如下两行
     conn.setDoOutput(true);
     conn.setDoInput(true);
        new Thread(new Runnable() {
                 @Override
                 public void run() {
                     try {
                         InputStream in = conn.getInputStream();
                         Path target = Paths.get("./t.mp4");
                         Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 }
             }).start();


    }
}
