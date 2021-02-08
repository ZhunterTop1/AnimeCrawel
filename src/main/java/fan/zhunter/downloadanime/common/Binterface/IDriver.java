package fan.zhunter.downloadanime.common.Binterface;

import fan.zhunter.downloadanime.common.Env;
import org.openqa.selenium.WebDriver;

import java.util.Map;
/**
 * 1. 只要目的是初始化环境
 * 2. 衔接的参数有：播放链接，是视频播放页还是视频列表页，使用什么类型的Driver，视频的名字,如果是视频播放列表，下载哪几集
 * 3. 思考实现的方法
 * 4. 使用ThreadLocal，设置2中的参数
 * 5. 因为ThreadLocal中只能设置一个参数，将参数设计成一个对象Env
 * 6. Driver中设置参数就是对Env参数的设置
 * 7. IDriver需要实现的主要功能逻辑为生成一个Driver对象&&设置Env到环境中
 * */
public interface IDriver {
    WebDriver getDriver();
    void closeDriver();
    WebDriver getDriver(String BROWSER_PATH, String DriverPath, String UserData);
    void setCookie(Map<String, String> cookie,String url);
}
