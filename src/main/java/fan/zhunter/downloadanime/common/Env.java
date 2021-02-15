package fan.zhunter.downloadanime.common;

import fan.zhunter.downloadanime.common.Binterface.IDriver;

import java.util.List;
import java.util.Map;

/**
 * 1. 面向对象的思维
 * 2. 将对象的属性设置
 * */
public class Env {
    private String url;
    private String aname;
    private IDriver driver;
    private List<Integer> list;
    private boolean isList;
    private Map<String, String> cookie;

    public Map<String, String> getCookie() {
        return cookie;
    }

    public void setCookie(Map<String, String> cookie) {
        this.cookie = cookie;
    }

    public String getUrl() {
        return url;
    }

    public String getAname() {
        return aname;
    }

    public IDriver getDriver() {
        return driver;
    }
    public List<Integer> getList() {
        return list;
    }

    public boolean isList() {
        return isList;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAname(String aname) {
        this.aname = aname;
    }

    public void setDriver(IDriver driver) {
        this.driver = driver;
    }

    public void isList(boolean isList) {
        this.isList = isList;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }
}
