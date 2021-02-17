package fan.zhunter.downloadanime.service.majority;

import fan.zhunter.downloadanime.common.Binterface.IDownUrlParse;
import fan.zhunter.downloadanime.common.Binterface.IDriver;
import fan.zhunter.downloadanime.common.requets.DownLoadRequest;
import fan.zhunter.downloadanime.common.config.VideoType;
import fan.zhunter.downloadanime.util.HarUtil;
import fan.zhunter.downloadanime.util.ThreadLocalUtil;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.*;

import static fan.zhunter.downloadanime.util.UrlUtil.getRefer;
import static fan.zhunter.downloadanime.util.UrlUtil.getUrl;

/**
 * 1. 这是一个算法计算一个区间中的topK的值
 * 2. 操作前会存在时间的，这个util已经获取了时间了，可以对比
 * 3. 操作方法为栈法 && 将数据包装成 DownRequest
 * 4. TopK，这是一个优先队列
 * 5. 先确定所在操作的区间
 * 6. 入值为 time -> url，name
 *      long totalMilliSeconds = System.currentTimeMillis();
 * 7. 确定k值，填入Request中
 * 8. 以最复杂的Bilibili为例
 * 9. 模拟
 * */
public class NormalDownLoad implements IDownUrlParse {
    VideoType type = null;
    WebDriver driver = null;
    int k=Integer.MAX_VALUE;
    public void setTop(int k){
        this.k = k;
    }
    public NormalDownLoad(){}
    public NormalDownLoad(VideoType type){
        this.type = type;
    }
    @Override
    public Set<DownLoadRequest> getDownLoadUrl(Map<String, String> urls) {
        IDriver iDriver = ThreadLocalUtil.getEnv().getDriver();
        driver = iDriver.getLogDriver();
        Set<DownLoadRequest> re = null;
        //是使用3个map的目的就是让time和url和name可以对上
        //使用对象优化， time ->(raw_url, name, context)
        //context存储的字串存在筛选规则
        //可以将筛选的过程下放给HarUtil
        //使用对象过多，就存在过度设计的嫌疑
        HashMap<Long, String> mark = new HashMap<>();
        TreeMap<Long, Integer> index = new TreeMap<>();
        HashMap<Integer, PriorityQueue<Ele>> filter = new HashMap<>();
        int i = 0;
        for (String name : urls.keySet()){
            String url = urls.get(name);
            long now = System.currentTimeMillis();
            System.out.println(now);
            mark.put(now, name);
            index.put(now, ++i);
            filter.put(i, new PriorityQueue<Ele>(k,(o1,o2) -> {
                if(o2.getTime() - o2.getTime() < 0){
                    return -1;
                }else{
                    return 1;
                }
            }));
            driver.get(url);
        }
        index.put(Long.MAX_VALUE, -1);
        driver.quit();
        try {
            ArrayList<Long> longs = new ArrayList<>(index.keySet());
            Collections.sort(longs);
            Map<Long, String> harMap = HarUtil.getHarMap(index);
            for (Long id : harMap.keySet()) {
                String context = harMap.get(id);
                if(useful(context)){
                    long time = Long.parseLong(context.split("\t")[0]);
                    long nearTime = getNear(longs, time);
                    PriorityQueue<Ele> queue = filter.get(index.get(nearTime));
                    if (queue.size() == k){
                        if(queue.peek().getTime() > time){
                            queue.poll();
                        }
                    }
                    queue.add(new Ele(time, context));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //TODO 遍历
//        filter.forEach((k, v)->{
//            Iterator<Ele> iterator = v.iterator();
//            while (iterator.hasNext()){
//                String context = iterator.next().context;
//                System.out.println(k + "," +getUrl(context));
//            }
//        });
//        re = parserEleToRequest(index, filter, mark);
        return re;
    }

    private Set<DownLoadRequest> parserEleToRequest(HashMap<Long, Integer> index,HashMap<Integer, PriorityQueue<Ele>> filter,HashMap<Long, String> mark) {
        Set<DownLoadRequest> re = new HashSet<>();
        String aname = ThreadLocalUtil.getEnv().getAname();
//        String aname = "a";
        index.forEach((k, v)->{
            if(v == -1) return;
            PriorityQueue<Ele> eles = filter.get(v);
            String refer = null;
            ArrayList<String> urls = new ArrayList<>();
            while (!eles.isEmpty()){
                Ele poll = eles.poll();
                String url = getUrl(poll.context);
                urls.add(url);
                refer = getRefer(poll.context);
            }
            String name = mark.get(k);
            DownLoadRequest request = new DownLoadRequest();
            request.setPath("./" + aname + "/" + name + "/");
            request.setRefer(refer);
            request.setUrl(urls);
            re.add(request);
        });
        return re;
    }

    private long getNear(ArrayList<Long> longs,long time) {
        int n = longs.size();
        int l=0,r=n-1;
        while (l < r){
            int mid = l + (r-l+1)/2;
            if(longs.get(mid) > time){
                r = mid -1;
            }else{
                l = mid;
            }
        }
        return longs.get(l);
    }

    public boolean useful(String v){
        return v.contains(".m3u8") && v.contains("MEDIUM");
    }
    public class Ele{
        Long time;
        String context;

        public Ele(long time,String context) {
            this.time = time;
            this.context = context;
        }

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }
    }
}
