import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.core.har.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import java.util.List;


public class BrowserMobProxyTest {

    public void tearDown(WebDriver driver,BrowserMobProxy proxy) {
        if(proxy.isStarted()){
            proxy.stop();
        }
        // 关闭当前窗口
        driver.close();
    }

    public static void main(String[] args) {
        BrowserMobProxyTest browserMobProxyTest =  new BrowserMobProxyTest();
        BrowserProxy browserProxy = BrowserProxy.getInstance();
        WebDriver driver = browserProxy.getDriver();
        BrowserMobProxy proxy = browserProxy.getProxy();
        browserMobProxyTest.testMethod(driver,proxy);
        //browserMobProxyTest.tearDown(driver,proxy);
    }
    public void testMethod(WebDriver driver,BrowserMobProxy proxy) {
        // 这里必需在driver.get(url)之前
        proxy.newHar("test_har");

        driver.get("https://www.bilibili.com/bangumi/play/ep341309");

//        String text = "selenium";

//        driver.findElement(By.xpath("//*[@id=\"source\"]")).sendKeys(text);

        Har har = proxy.getHar();
        List<HarEntry> list =  har.getLog().getEntries();
        for (HarEntry harEntry : list){
            HarResponse harResponse = harEntry.getResponse();
            String responseBody = harResponse.getContent().getText();
            List<HarCookie> cookies = harResponse.getCookies();
            // network response
            System.out.println("responseBody:"+responseBody);
            // 可获取httpOnly 类型的cookie
            System.out.println("cookies:"+cookies);
        }
    }
}