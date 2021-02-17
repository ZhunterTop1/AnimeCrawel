package fan.zhunter.downloadanime.service.worker;

import fan.zhunter.downloadanime.common.APlayService;
import fan.zhunter.downloadanime.common.Binterface.IListParse;
import fan.zhunter.downloadanime.common.requets.Request;

import java.util.List;
import java.util.Map;
/**
 * 1. 基本逻辑使用责任链的设计模式实现完成
 * 2. 本类现在的关键在于获取所有需要的列表， 像樱花动漫网中存在多个list，其中有效list筛选也是其中关键之一
 * 3. 不同网站获取列表的方式或许不同
 * 4. 在test中模拟
 * */
public class PlayersService extends APlayService {
    public PlayersService(APlayService successor) {
        super(successor);
    }
    private IListParse listParse;

    public void setListParse(IListParse listParse) {
        this.listParse = listParse;
    }

    @Override
    public void handler(Request request) {
        Map<String, String> urls = request.getUrls();
        List<Integer> playlist = request.getList();
        if(request.isList()){
            if(listParse == null) {
                System.err.println("IListParse is Null!Please setting it");
            }else {
                urls = listParse.getPlayList(urls, playlist);
                request.isList(false);
                request.setUrls(urls);
            }
        }
        if(successor != null){
            successor.handler(request);
        }
    }
}
