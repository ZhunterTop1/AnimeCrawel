package fan.zhunter.downloadanime.application;

import fan.zhunter.downloadanime.common.Aapplication;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application extends Aapplication {
    public static void main(String[] args) {
        //TODO 创建自定义Driver
        //TODO 使用Driver爬取不同网站的播放网址
        //ToDO 根据网址下载视频
        //TODO 后处理视频
        //TODO 关闭
        Application application = new Application();
        String url = "https://www.bilibili.com/bangumi/play/ep374610/?spm_id_from=333.851.b_7265706f7274466972737431.1";
        String aname = "ok";
        Map<String, String> cookie = application.parseCookie();
        boolean useLog = true;
        Class cz = ChromeDriver.class;
        boolean isList = true;
        List<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(1);
        list.add(3);
        application.start(url, aname, cookie, useLog, cz, isList, list);
    }
    public Map<String, String> parseCookie(){
        String all = "finger=158939783; _uuid=6A6526E6-34BD-3E26-713F-0A7720D7AC8582774infoc; buvid3=6A43A7B7-BA04-49F9-B035-CE6CD1F4CDB4185002infoc; fingerprint=691bb311e76f14d0c0d180085f16a546; buvid_fp=6A43A7B7-BA04-49F9-B035-CE6CD1F4CDB4185002infoc; buvid_fp_plain=2DE773AD-7542-4785-9FEC-312A1B05B48218552infoc; CURRENT_FNVAL=80; blackside_state=1; SESSDATA=54f82a73%2C1628149578%2C41012%2A21; bili_jct=13130a4ce4bbe458b9540eef6b366e1a; DedeUserID=195588405; DedeUserID__ckMd5=0441c9aa7209ac5b; sid=alsnafg4; rpdid=|(k|kmklk)k|0J'uYuklm)|JJ";
        String[] split = all.split(";");
        HashMap<String, String> map = new HashMap<>();
        for (String one:
                split) {
            one = one.trim();
            String[] split1 = one.split("=");
            map.put(split1[0].trim(), split1[1].trim());
        }
        return map;
    }
}
