package fan.zhunter.downloadanime.common.requets;

import java.util.List;
import java.util.Map;

/**
 * 1. 暂时先定这样吧
 * 2. 以一集为一个单位
 * @path : file download path，equal it's father path
 * @name ： file name
 * @urls ： its name and request urls，后缀全变成mp4
 * @refer : download request needs, request header
 * */
public class DownLoadRequest {
    private String path;
    private String name;
    private List<String> url;
    private String refer;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRefer() {
        return refer;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }

    @Override
    public String toString() {
        return "DownLoadRequest{" +
                "path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", url=" + url +
                ", refer='" + refer + '\'' +
                '}';
    }
}
