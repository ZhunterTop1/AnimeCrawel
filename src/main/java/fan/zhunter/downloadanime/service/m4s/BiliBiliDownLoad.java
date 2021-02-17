package fan.zhunter.downloadanime.service.m4s;

import fan.zhunter.downloadanime.common.Binterface.IDownUrlParse;
import fan.zhunter.downloadanime.common.Binterface.IDriver;
import fan.zhunter.downloadanime.common.requets.DownLoadRequest;
import fan.zhunter.downloadanime.util.JsonUtil;
import fan.zhunter.downloadanime.util.PatternUtil;
import fan.zhunter.downloadanime.util.ThreadLocalUtil;
import fan.zhunter.downloadanime.util.Utils;
import org.openqa.selenium.WebDriver;

import java.util.*;

/**
1. 实现根据播放链接获取downloadurl的操作
 2. 是driver的循环操作
 3. 方法抽象
 4. 实验获取到的url是否可以下载文件
 * */
public class BiliBiliDownLoad implements IDownUrlParse {
    WebDriver driver;
    @Override
    public Set<DownLoadRequest> getDownLoadUrl(Map<String, String> urls) {//cookie在一开始就设置
        IDriver iDriver = ThreadLocalUtil.getEnv().getDriver();
        driver = iDriver.getDriver();
        Set<DownLoadRequest> re = new HashSet<>();
        for (String name : urls.keySet()){
            Map<String, String> singleDownUrl = getSingleDownUrl(urls.get(name));
            if (singleDownUrl.size() > 0) {
                DownLoadRequest request = new DownLoadRequest();
                request.setUrl(new ArrayList<>(singleDownUrl.keySet()));
                request.setPath("./" + name + "/");
                re.add(request);
            }
        }
        return re;
    }
    /**
     * 1. 核心为json处理过程
     * 2. 目前主流的json处理工具类为：Gson，FastJson，Jackson，Json-lib。
     * 3. Json-lib为性能极弱，排除
     * 4. FastJson: 复杂的bean转换可能出现错误，排除
     * 5. Gson, Jackson:优秀，选择Gson来解析json
     * 6. 将Json处理变成一个Util
     * 7. 使用Json表达式获取值

     * */
    public Map<String, String> getSingleDownUrl(String url){
        driver.get(url);
        String page = driver.getPageSource();
        String regex = "<script>window.__playinfo__=(\\{.*?\\})</script>";
        String tmp = PatternUtil.pattern(page, regex);
        String json = tmp.substring(tmp.indexOf("{"),tmp.lastIndexOf("}") + 1);
        String videoJ = "data.dash.video[0].baseUrl";
        String audioJ = "data.dash.audio[0].baseUrl";
        HashMap<String, String> re = new HashMap<>();
        if(Utils.isNotEmpty(json) && Utils.isNotEmpty(videoJ) && Utils.isNotEmpty(audioJ)){
            String videoUrl = JsonUtil.parse(json, videoJ);
            String audioUrl = JsonUtil.parse(json, audioJ);
            re.put("video.m4s", videoUrl);
            re.put("audio.m4s", audioUrl);
        }
        return re;
    }
}
