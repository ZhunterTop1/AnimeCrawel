package fan.zhunter.downloadanime.service;

import fan.zhunter.downloadanime.common.DownLoadRequest;
import fan.zhunter.downloadanime.common.Env;
import fan.zhunter.downloadanime.common.Request;
import fan.zhunter.downloadanime.service.builder.ServiceBuilder;
import fan.zhunter.downloadanime.util.DomainUtils;
import fan.zhunter.downloadanime.util.ThreadLocalUtil;
import fan.zhunter.downloadanime.util.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/***
 * 1. 为了获取所有的downloadurl，存在以下方法
 * 2. 手动解析js逻辑，模仿逻辑发送文件
 *      优点：时间快
 *      缺点：解析复杂，耗时长
 * 3. 从渲染好的html文件中直接获取链接
 *      优点：简单
 *      缺点：获取不全
 * 4. 使用代理
 *      缺点：操作困难，获取不全
 * 5. 使用chrome-network-har
 *      优点：数据全
 *      缺点：数据输入到磁盘，存在延迟&&不好截取有效的url
 * 6. 决定：使用渲染 or chrome-network
 * 7. 对于链接存在于html中的网站，直接从中获取就好
 *      对于链接难以获取的使用chrome-network-har
 * 8. 优化chrome-network-har的逻辑
 * */
public class DefinedPlayService {
    PlayersService playersService = null;
    PlayService playService = null;
    public void service() {
        //设定处理策略
        Env env = ThreadLocalUtil.getEnv();
        ServiceBuilder serviceBuilder = new ServiceBuilder();
        serviceBuilder.setDomain(DomainUtils.getTopDomain(env.getUrl()));
        playService = new PlayService(null);
        playService.setDownParser(serviceBuilder.buildPlayServide());
        playersService = new PlayersService(playService);
        playersService.setListParse(serviceBuilder.buildListParse());
        HashMap<String, String> urls = new HashMap<>();
        urls.put(env.getAname(), env.getUrl());
        Request request = new Request(env.isList(),urls ,env.getList());
        playersService.handler(request);
    }
    public Set<DownLoadRequest> getDownUrl(){
        return playService.downloadUrls;
    }
}
