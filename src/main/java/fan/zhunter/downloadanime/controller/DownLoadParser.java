package fan.zhunter.downloadanime.controller;

import fan.zhunter.downloadanime.common.Env;
import fan.zhunter.downloadanime.util.ThreadLocalUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 1. 需求：下线多个文件
 * 2. 传参为：k->(k->V)
 * 3. 下载文件名 k\K, 下载地址V
 * 4. 多线程下载问题：
 *      1. 可能出现多个线程阻塞->kill重来
 *      2. 文件大小不一，结束时间不一，使用计时器记录，改线程多久没有get data
 *      3. 短时间的大量连接引起的反爬机制
 *      4. 下载就是IO，考虑java的NIO，学习其知识点
 * 5. 可以使用线程池进行操作
 * 6. 优化：线程池每个线程都要监听等待connect&response；这些时间片调系统调用函数没有必要，可以将他们注册到Selector中
 *      1. 如果出现某个连接出现数据，才开始调用这些读函数的系统函数
 *      2. 使用直接内存作为channel的buffer，减少读函数从内核态copy数据到用户态的过程，只需从网卡中读取数据到网卡中
 * 7. http连接如何切换到Socket来使用NIO
 * 8. 自定义URL处理类显然吃力不讨好，在网上搜索
 * 9. Netty框架实现了Http
 * 10. 最终技术定位：连接池+fileNIO
 * 11. 原因：有的url无法ping上，只能使用URL来处理
 * 12. 最终方案：URL下载文件，请求链接使用连接池，出现数据使用NIO，copy到直接内存中，之后直接写文件
 * //map的k,v用=连接， 每一项使用；连接
 * */
public class DownLoadParser {
    final private static int coreSize = Runtime.getRuntime().availableProcessors();
    final public static ThreadPoolExecutor pool;
    static {
        pool = new ThreadPoolExecutor(coreSize * 2,coreSize * 4,
                2,TimeUnit.MINUTES, new LinkedBlockingDeque<>());
    }
    public static class Single implements Supplier<Boolean>, Function<Boolean, Boolean> {
        URLConnection conn;
        Path out;
        boolean mark = false;
        public Single(String url, Map<String, String> cookie, String outPath){
            this.conn = DownLoadParser.getConnection(url,cookie);
            try {
                this.out = Paths.get(outPath);
                Files.createDirectories(out.getParent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Boolean get() {
            if(conn == null){
                return false;
            }
            try(InputStream stream = conn.getInputStream()) {
                DownLoadParser.NioFileWrite(stream, out);
            }catch (Exception e){
                return false;
            }
            mark = true;
            return true;
        }

        @Override
        public Boolean apply(Boolean aBoolean) {
            if(!mark){
                return get();
            }else{
                return true;
            }
        }
    }

    /**
     * 将stream创建下放，使逻辑足够丝滑
     * */
    public static URLConnection getConnection(String url, Map<String, String> cookie) {
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8");
            conn.setRequestProperty("Cache-Control","max-age=0");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Referer","https://www.bilibili.com/bangumi/play/ep374610/?spm_id_from=333.851.b_7265706f7274466972737431.1");
            if(cookie != null && cookie.size() > 1) conn.setRequestProperty("Cookie", parseCookie(cookie));
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            return conn;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void NioFileWrite(InputStream in, Path target) throws IOException {
        Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
    }
    public static String parseCookie(Map<String, String> cookie){
        StringBuilder re = new StringBuilder();
        cookie.forEach((k,v)->{
            re.append(k);
            re.append("=");
            re.append(v);
            re.append(";");
        });
        return re.toString();
    }
    /**
     * 1. 客户端使用时可以存在精巧的设计
     * 2. 下载Map为 name-><type, url>
     * 3. 当type完整处理后，这个链接的后序Handler就可以处理了
     * 4. 典型的CutDownLatch
     * 5. 还可以优化，判断一个线程是否正确下载的一般方法为，不停循环，将正确下载的url排除
     * 6. 可以借鉴selector的思路，如果返回结果就注册到类似CHANEL的组件中
     * 7. 每次只需循环已经返回结果的Task
     * 8. 发现jdk8已经自己实现了这个功能：CompletableFuture
     * 9. 使用CompletableFuture完成操作
     * 10. 实现逻辑未完成的url，有且只运行一次
     * 11. 模拟，这是典型的观察者模式
     * 12. 睡觉，明天再实现
     * 13. 入参思考， Cookie从env中获取，url从爬取的连接中获取，输出目录默认是当前项目的文件夹，父目录是总的 输入名称//map的k//子url的名称
     *      1. 进入的参数存在 url-><子url， 正式下载文件名
     *      2. 如果存在ul: url->name
     *      3. 1，2可以结合，替换url
     *      4. 如果不存在ul, 为 原始url->原始name
     *      5.     private String url;
                 private String aname;
                 private IDriver driver;
                 private List<Integer> list;
                 private boolean isList;
                 private Map<String, String> cookie;
            6. 将名称合并的任务下放给download
        7. 进行测试
        8. 使用m3u8最佳，实现m3u8实现类
        9. 播放链接可能不在html文件中
     * */
    public static void main(String[] args) {
        Env env = ThreadLocalUtil.getEnv();
        boolean islist = false;
        Map<String, Map<String, String>> downLoadUrls = new HashMap<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("1.ts", "https://m3u8i.vodfile.m1905.com/202102141059/83dc8758a8bb5af0339ac5012a7a3e41/movie/2019/02/18/m20190218P0L0WE6BO6QBAZ7W/90BD33D90325E9DDF1A6F4335.000.ts");
        map.put("2.ts", "https://m3u8i.vodfile.m1905.com/202102141059/83dc8758a8bb5af0339ac5012a7a3e41/movie/2019/02/18/m20190218P0L0WE6BO6QBAZ7W/90BD33D90325E9DDF1A6F4335.001.ts");
        downLoadUrls.put("./1", map);
        ThreadPoolExecutor pool = DownLoadParser.pool;
        downLoadUrls.forEach((k, v)->{
            v.forEach((k1, v1)->{
                String outpath = null;
                if(islist){
                    outpath = env.getAname() + "//" + k + "//" + k1;
                }else {
                    outpath = k + "//" + k1;
                }
                Single single = new Single(v1,null,outpath);
                CompletableFuture.supplyAsync(single, pool).thenApplyAsync(single, pool)
                        .thenApplyAsync(single, pool)
                        .thenApplyAsync(single, pool).whenComplete((r, e)->{
                    if(r){
                        System.out.println(v1 + "is Complete");
                    }
                });
            });
        });

    }
}
