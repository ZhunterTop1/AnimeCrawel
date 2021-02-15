import fan.zhunter.downloadanime.controller.DownLoadParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class DownParserTest {
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
     * 13. 将实现的线程注册在Observer列表中
     * 14. 存在没次提醒或会重复操作值，解决办法，每次将之删除
     * 15. 存在多次下载文件依旧无法下载完成的情况，设定限制次数，暂定最多3次
     * 16. 需要发布者记录，成功的url，，，不需要，只有当失败是才会重新记录已经失败的次数
     * 17. 失败时，记录次数，CompletableFuture就可以完成这一系的操作
     * */
    public static void main(String[] args) {
        ThreadPoolExecutor pool = DownLoadParser.pool;
        List<String> urls = new ArrayList<>();//存在没有运行完成的
        List<String> next = new ArrayList<>();
        Map<String, Future<Boolean>> map = new HashMap<>();
        CountDownLatch barrier = new CountDownLatch(urls.size());
        for (String url : urls) {
            CompletableFuture.supplyAsync(new DownLoadParser.Single(url, new HashMap<>(), ""), pool)
                    .whenComplete((result,e)->{
                if(!result){
                    next.add(url);
                }
            });
        }
    }
}
