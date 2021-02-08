package fan.zhunter.downloadanime.service;

import fan.zhunter.downloadanime.common.Env;
import fan.zhunter.downloadanime.common.Request;
import fan.zhunter.downloadanime.service.builder.ServiceBuilder;
import fan.zhunter.downloadanime.util.DomainUtils;
import fan.zhunter.downloadanime.util.ThreadLocalUtil;
import fan.zhunter.downloadanime.util.Utils;

import java.util.HashMap;
import java.util.Map;

public class DefinedPlayService {
    public Map<String, Map<String, String>> service() {
        //设定处理策略
        Env env = ThreadLocalUtil.getEnv();
        ServiceBuilder serviceBuilder = new ServiceBuilder();
        serviceBuilder.setDomain(DomainUtils.getTopDomain(env.getUrl()));
        PlayService playService = new PlayService(null);
        playService.setDownParser(serviceBuilder.buildPlayServide());
        PlayersService playersService = new PlayersService(playService);
        playersService.setListParse(serviceBuilder.buildListParse());
        HashMap<String, String> urls = new HashMap<>();
        urls.put(env.getAname(), env.getUrl());
        Request request = new Request(env.isList(),urls ,env.getList());
        playersService.handler(request);

        return playService.downloadUrls;
    }
}
