package fan.zhunter.downloadanime.common.Binterface;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.Map;

public abstract class AbstractDriver implements IDriver{
    protected volatile WebDriver driver;
    protected volatile WebDriver logDriver;
    @Override
    /*
    * 1. selenium设置cookie需要，navigate
    * 2. 部分操作不需要LogDriver 使用懒加载
    * **/
    public void setCookie(final Map<String, String> cookie,String url) {
        WebDriver.Navigation navigate = driver.navigate();
        navigate.to(url);
        cookie.keySet().stream().forEach((k)->{
            String v = cookie.get(k);
            driver.manage().addCookie(new Cookie(k, v));
        });
    }
    public void setLogCookie(final Map<String, String> cookie,String url) {
        WebDriver.Navigation navigate = logDriver.navigate();
        navigate.to(url);
        cookie.keySet().stream().forEach((k)->{
            String v = cookie.get(k);
            logDriver.manage().addCookie(new Cookie(k, v));
        });
    }

    public void closeDriver(){
        driver.quit();
        logDriver.quit();
    }
}
