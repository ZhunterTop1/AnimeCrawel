package fan.zhunter.downloadanime.common.Binterface;

import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Map;

public interface IListParse {
    Map<String, String> getPlayList(Map<String, String> url, List<Integer> playList);
}
