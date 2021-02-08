package fan.zhunter.downloadanime.common;

import fan.zhunter.downloadanime.common.Binterface.IDriver;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Collections;
import java.util.Map;

import static fan.zhunter.downloadanime.common.ACrawelConfig.*;

public class FormatChromeDriver implements IDriver {
    private volatile WebDriver normalDriver = null;
    public WebDriver getDriver() {
        return getDriver(BROWSER_PATH, DriverPath, UserData);
    }
    //多线程安全

    public WebDriver getDriver(String BROWSER_PATH, String DriverPath, String UserData) {
        System.setProperty("webdriver.chrome.bin", BROWSER_PATH);
        System.setProperty("webdriver.chrome.driver",DriverPath);
        if(normalDriver == null){
            synchronized (this){
                if (normalDriver == null) {
                    ChromeOptions options = new ChromeOptions();
//                    if(UserData != null) options.addArguments("user-data-dir=" + UserData);
                    //TODO 测试时将其关闭
//                    options.addArguments("--headless");
                    options.addArguments("--disable-gpu");
                    normalDriver = new ChromeDriver(options);
//                    normalDriver.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
                }
            }
        }
        return normalDriver;
    }

    @Override
    /*
    * 1. selenium设置cookie需要，navigate
    *
    * **/
    public void setCookie(final Map<String, String> cookie, String url) {
        WebDriver.Navigation navigate = normalDriver.navigate();
        navigate.to(url);
        cookie.keySet().stream().forEach((k)->{
            String v = cookie.get(k);
            normalDriver.manage().addCookie(new Cookie(k, v));
        });
    }

    public void closeDriver(){
        normalDriver.quit();
    }

}
