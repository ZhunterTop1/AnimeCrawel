package fan.zhunter.downloadanime.service.m4s;

import org.junit.Test;

import java.io.IOException;

public class M4sHandlerTest {
    /***
     * video/mp4
     * video/mp4
     * video : 76696465  vide
     * audio : 736F756E  soun
     */
    @Test
    public void test() throws IOException {
        String type = M4sHandler.getType("./t1.mp4");
        System.out.println(type);
    }
}
