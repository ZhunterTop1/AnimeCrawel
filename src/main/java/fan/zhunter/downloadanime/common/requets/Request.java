package fan.zhunter.downloadanime.common.requets;

import fan.zhunter.downloadanime.common.Binterface.IDriver;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Map;

public class Request {
    private boolean isList;
    private Map<String, String> urls;
    private List<Integer> list;
    public Request(){}

    public Request(boolean isList,Map<String, String> urls,List<Integer> list){
        this.isList = isList;
        this.urls = urls;
        this.list = list;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }

    public boolean isList() {
        return isList;
    }

    public void isList(boolean list) {
        isList = list;
    }

    public Map<String, String> getUrls() {
        return urls;
    }

    public void setUrls(Map<String, String> urls) {
        this.urls = urls;
    }

    public List<Integer> getList() {
        return list;
    }
}
