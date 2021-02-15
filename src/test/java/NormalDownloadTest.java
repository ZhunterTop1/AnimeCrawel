import fan.zhunter.downloadanime.service.builder.NormalDownLoad;
import org.junit.Test;

import java.util.HashMap;

public class NormalDownloadTest {
    @Test
    public void test(){
        NormalDownLoad load = new NormalDownLoad();
        load.setTop(2);
        HashMap<String, String> urls = new HashMap<>();
        urls.put("1e", "https://www.bilibili.com/bangumi/play/ep374644");
        urls.put("2e", "https://www.bilibili.com/bangumi/play/ep374645");
        load.getDownLoadUrl(urls);
    }
}
