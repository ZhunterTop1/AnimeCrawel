package fan.zhunter.downloadanime.common;

import fan.zhunter.downloadanime.common.Binterface.IPlayService;

import java.util.Map;

public abstract class APlayService implements IPlayService {
    public APlayService successor;
    public Map<String, Map<String, String>> downloadUrls;
    public APlayService(APlayService successor){
        this.successor = successor;
    }
    @Override
    public abstract void handler(Request request);
}
