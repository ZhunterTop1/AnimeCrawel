import fan.zhunter.downloadanime.common.driver.FormatChromeDriver;
import org.junit.Test;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
/**
 * 1. 发现之前思路的bug
 * 2. bug为设置url前需要先navigate to一个url
 * 3. 这个Test成功设置Cookie
 * 4. 先要到主站
 * 5. 之后可以通过get获取对象
 * */
public class CookieTest {
    String url = "https://www.bilibili.com/bangumi/play/ep375398";
    @Test
    public void getHtml() throws InterruptedException {
        FormatChromeDriver driver = new FormatChromeDriver();
        WebDriver driverDriver = driver.getDriver();
        WebDriver.Navigation navigate = driverDriver.navigate();
        navigate.to("https://www.bilibili.com/");
        String all = "finger=158939783; _uuid=6A6526E6-34BD-3E26-713F-0A7720D7AC8582774infoc; buvid3=6A43A7B7-BA04-49F9-B035-CE6CD1F4CDB4185002infoc; fingerprint=691bb311e76f14d0c0d180085f16a546; buvid_fp=6A43A7B7-BA04-49F9-B035-CE6CD1F4CDB4185002infoc; buvid_fp_plain=2DE773AD-7542-4785-9FEC-312A1B05B48218552infoc; CURRENT_FNVAL=80; blackside_state=1; SESSDATA=54f82a73%2C1628149578%2C41012%2A21; bili_jct=13130a4ce4bbe458b9540eef6b366e1a; DedeUserID=195588405; DedeUserID__ckMd5=0441c9aa7209ac5b; sid=alsnafg4; rpdid=|(k|kmklk)k|0J'uYuklm)|JJ";
        String[] split = all.split(";");
        for (String one:
             split) {
            one = one.trim();
            String[] split1 = one.split("=");
            driverDriver.manage().addCookie(new Cookie(split1[0].trim(), split1[1].trim()));

        }
        navigate.refresh();
        Thread.sleep(4000);
        driverDriver.get(url);
        Thread.sleep(2000);
        String pageSource = driverDriver.getPageSource();
        System.out.println(pageSource);
//        String url2 = "https://www.bilibili.com/bangumi/play/ep374533";
//        driverDriver.navigate().to(url2);
        driver.closeDriver();
    }
    /***
     * //定义gecko driver的获取地址
     System.setProperty("webdriver.gecko.driver", "D:\\Firefox\\geckodriver.exe");
     // 指定firefox 安装路径
     System.setProperty("webdriver.firefox.bin","D:\\Firefox\\firefox.exe");
     // 启动firefox浏览器
     WebDriver driver = new FirefoxDriver();
     //指定打开的网址
     Navigation navigation=driver.navigate();
     navigation.to("地址");

     //通过fiddler 找到cookie ，参数名字，参数值
     Cookie c1 = new Cookie("username",");
     Cookie c2 = new Cookie("pwd","");
     driver.manage().addCookie(c1);
     driver.manage().addCookie(c2);
     try {
     Thread.sleep(2000);
     driver.navigate().refresh();
     } catch (InterruptedException e)
     {
     e.printStackTrace();
     }
     }
     }
     *
     * */
}
