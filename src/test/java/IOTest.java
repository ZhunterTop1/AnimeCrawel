import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 1. 要寻找多socket链接下，快速下载文件的方法
 * 2. 学习IO知识
 * 3. 测试类
 * 4. NIO是使用Channel块传输&&直接内存缓存&&selector三者的IO处理模型
 * 5. 情报：将NIO结合到大量文件下载的模型中
 * 6. NIO的非阻塞Channel是基于socket流的，NIO满足要求
 * 7. NIO是基于单线程，IO复用的处理模式，多个文件下载的不同的socket，google查看是否可以将多个Socket注册到一个Selector中
 * 8. 根据demo，可以确定Selector可以注册多个Selector
 * 9. 传统的download的java工具类是net.URL，接下来只需找寻URL->Socket and ByteChannel的方法
 * 10. 图式demo的处理结构模型，从中汲取灵感
 * 11. 将Thread-URL当成client，将数据流放入channel中，channel注册到selector中
 * 12. 优点在于将数据放入到直接内存中，减少用户态和核心态的切换；将输入写到磁盘中比写到内存中耗时，将这个重量级操作交给单线程的selector来操作；
 * 13. 细节思考：
 *      1. 这些URL线程如何管理？
 *      2. 如果线程还是放到线程池中，Thread block时，还不是需要消耗cpu时间吗？
 * 14. 看相关的技术论文发现，demo使用的NIO并不完美
 * 15. 多路复用IO的特质在于，将所有事件注册在1个Channel中
 * 16. 查看更多的demo，学习IO的准确使用
 * 17. NIO is a selector that polls multiple channels. The channel registers itself in the selector, but if its data is ready,
 *      it will notify the selector, which is "ready to notify me."
 * 18. 我寻找到了目标，使用netty完成这个多线的下载任务开发
 * 19. 使用1天时间详细阅读netty的各种实现，原理，源码；重开一个项目，名为nettyLearn
 * */
public class IOTest {
    public static void main(String[] args) throws Exception {
//        new FileInputStream()
        URL url = new URL("");
        int[] a=null;

    }

}
