package fan.zhunter.downloadanime.common;

import fan.zhunter.downloadanime.common.Binterface.IController;
import fan.zhunter.downloadanime.common.Binterface.IEnvBuilder;
import fan.zhunter.downloadanime.controller.DefinedController;

import java.util.List;
import java.util.Map;

public abstract class Aapplication {
    /**
     * 1. start的作用为启动Builder【初始化Config对象， 创建driver对象】
     * 2. 调用Controller
     * 3. 调用Builer关闭对象，清理缓存
     * 4. 模拟参数-》思考为了完成任务需要的参数
     * 5. 播放链接，是视频播放页还是视频列表页，使用什么类型的Driver，视频的名字,如果是视频播放列表，下载哪几集
     * */
    public void start(String url,String aname,Map<String, String> cookie,Class cz ,boolean isList,List<Integer> list){
        IEnvBuilder builder = new EnvBuilder().setUrl(url).setAname(aname).setDriverClass(cz, cookie).isList(isList).list(list).builder();
        IController control = new DefinedController();
        control.control();
        builder.close();
    }
}
