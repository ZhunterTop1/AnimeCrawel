package fan.zhunter.downloadanime.common.driver;

import fan.zhunter.downloadanime.common.Binterface.AbstractDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;

import static fan.zhunter.downloadanime.common.config.ACrawelConfig.*;

public class FormatChromeDriver extends AbstractDriver {
    public WebDriver getDriver() {
        return getDriver(BROWSER_PATH, DriverPath, UserData);
    }
    //多线程安全

    public WebDriver getDriver(String BROWSER_PATH, String DriverPath, String UserData) {
        System.setProperty("webdriver.chrome.bin", BROWSER_PATH);
        System.setProperty("webdriver.chrome.driver",DriverPath);
        if(super.driver == null){
            synchronized (this){
                if (super.driver == null) {
                    ChromeOptions options = new ChromeOptions();

                    if(UserData != null) options.addArguments("user-data-dir=" + UserData);
                    //TODO 测试时将其关闭
                    options.addArguments("--headless");
//                    options.addArguments("--disable-gpu");
                    super.driver = new ChromeDriver(options);
//                    driver.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
                }
            }
        }
        return super.driver;
    }

    public WebDriver getLogDriver() {
        return getLogDriver(BROWSER_PATH, DriverPath, UserData);
    }
    //多线程安全
    public WebDriver getLogDriver(String BROWSER_PATH,String DriverPath,String UserData) {
        System.setProperty("webdriver.chrome.bin", BROWSER_PATH);
        System.setProperty("webdriver.chrome.driver",DriverPath);
        if(super.logDriver == null){
            synchronized (this){
                if (super.logDriver == null) {
                    ChromeOptions options = new ChromeOptions();
                    File file = new File(HarRaw);
                    options.addArguments("--log-net-log=" + file.getAbsolutePath());
                    //TODO 测试时将其关闭
                    options.addArguments("--headless");
                    super.logDriver = new ChromeDriver(options);
                }
            }
        }
        return super.logDriver;
    }
}
