package fan.zhunter.downloadanime.service.m3u8;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

public class M3U8HandlerTest {
    @Test
    public void test() throws IOException {
        String url = "https://www.nmgxwhz.com:65/20210214/0D09hnVM/index.m3u8";
        HashMap<String, String> map = M3U8Handler.analyM3u8(url);
        map.forEach((k, v)->{
            System.out.println(k +","+v);
        });
    }
}
