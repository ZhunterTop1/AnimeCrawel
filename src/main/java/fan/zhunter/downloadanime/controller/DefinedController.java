package fan.zhunter.downloadanime.controller;

import fan.zhunter.downloadanime.common.Binterface.IController;
import fan.zhunter.downloadanime.common.DownLoadRequest;
import fan.zhunter.downloadanime.common.VideoType;
import fan.zhunter.downloadanime.service.DefinedPlayService;
import fan.zhunter.downloadanime.util.UrlUtil;
import fan.zhunter.downloadanime.util.Utils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import static fan.zhunter.downloadanime.common.VideoType.*;

/**
 * 1. 思考Controller的实现
 * 2. Controller需要实现的功能
 * 3. 因为是爬虫框架，流程为：根据链接获取网页-》获取网页中的有效url->根据有效有效url下载视频->后处理
 * 4. 模拟具体的执行流程
 * 5. 判断是否是下载一个列表的，如果是下载列表的，初始链接是一系列链接，思考成多次执行6[service]
 * 6. 根据视频播放链接url，获取其中的downUlr，可能多个，可能一个，使用Collection[service]
 * 7. 将downUrl下放给合适的下载视频类【util】
 * 8. 将下载好的视频下放给合适的后处理平台
 * 9. 细节模拟
 * 10.下放给download平台需要相应的Cookie
 * 11.Cookie的获取需要之前Env的支持
 * 12. Cookie可以是模拟登陆来的，也可以是直接设置的
 * 13. 从简单的入手假设之前是通过设置Cookie得来的，Cookie的数据结构设为Map
 * 14. 先修改之前的类，再完成Controller
 * 15. 修改完成，实现Controller的总体布局
 * 16. ifElse这是一种状态转换，可以使用状态模式，将判断不同播放链接的类型下放给Service
 * 17. Controller 本来就是为控制选择类而存在，但是又存在状态的设计模式
 * 18. 思考他们的选择性
 * 19. 阅读状态选择模式，理解状态设计模式的
 * 20. Controller实现的不同种的service类的切换，状态设计模式控制同一类可以相互转换的类的操作
 * 21. 学习状态设计模式的细节
 * 22. state使用Context容器化State子类，外界产生行为时，state就会设置转化
 * 23. 这里是播放列表 vs 视频播放页
 * 24. 这里IService使用责任链的方式，逻辑为筛法
 * 25. 明晰为播放类型的Service
 * 26. 为了解析相应的ul列表，可以为每个网站配置相应的ul
 * 27. 比如bilibili播放文件类型为m4s
 * 28. 挑战最高难度直接解析html文件
 * 29. 实验点击法
 * 30. 最终技术选型：手动适配法
 * 31. 模拟适配法
 * 32. 需要解析不同网站的列表解析类,视频解析类
 * 33. 先处理列表解析类：返回值:<视频名：视频链接>Map,传参为Cookie, Url，playList
 * 34. 再处理视频解析类：返回值:<视频名:<视频类型【audio or video】，种类链接>>，传入参数cookie，urls
 * 35. 这里涉及到类选择，组合类的问题：使用抽象工厂类
 * 36. download的类型固定，只存在几种
 * 37. 包括m4s, mp4, ts[m3u8], flv, gif,只需要配置相应的工具类下载就ok
 * 38. EndHandler 是和 下载的格式相关，一个网站可能存在多种格式， 可以考虑放在env中
 * 39. 判定数据类型，并选择相应的handler类
 * 40. 使用面向对象的思想，将不同的数据类型格式编程枚举类
 * 41. 处理多线程下载文件问题，交给DownLoad
 * */
public class DefinedController implements IController {
    @Override
    public void control() {
        DefinedPlayService service = new DefinedPlayService();
        service.service();
        Set<DownLoadRequest> downLoadUrls = service.getDownUrl();
        System.out.println(downLoadUrls);
        if(Utils.isEmpty(downLoadUrls)){
            System.err.println("downLoadurl don`t get, Task Failure!!!");
            return;
        }
//        VideoType type = getType(downLoadUrls);
//        DownLoadParser loader = getLoader(type);
//        IHandler handler = getHandler(type);
//        loader.downLoad(downLoadUrls);
//        handler.handler();
    }

    private VideoType getType(Map<String, Map<String, String>> downLoadUrls) {
        String key = new ArrayList<>(downLoadUrls.keySet()).get(0);
        String url = new ArrayList<>(downLoadUrls.get(key).keySet()).get(0);
        String type = UrlUtil.getType(url);
        switch (VideoType.valueOf(type)){
            case flv:
                return flv;
            case m4s:
                return m4s;
            case ts:
                return ts;
            case mp4:
                return mp4;
        }
        return null;
    }
}
