package fan.zhunter.downloadanime.common.Binterface;

import org.openqa.selenium.WebDriver;

import java.util.Map;

public interface IDownUrlParse {
    Map<String, Map<String, String>> getDownLoadUrl(Map<String, String> urls);
}
