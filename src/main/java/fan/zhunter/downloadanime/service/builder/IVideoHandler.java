package fan.zhunter.downloadanime.service.builder;

import java.io.File;

/**
 * 1. 每种视频最终处理的方式不一样
 *      1. 对于mp4的视频，需要将其命名修改
 *      2. 对于ts的视频，使用IO流将视频merge，再修改名称
 *      3. 对于m4s视频，先辨别处哪个是video，哪个是audio，再使用ffmpeg合并视频，再重命名
 *      4. 对于flv的视频，其处理逻辑和ts视频的处理逻辑相同
 * 2. 对于这个接口
 *      1. 每个视频的下载逻辑都是 /动漫名/视频名/单个小名字
 *      2. 可以将之移动到 /动漫名/视频.mp4
 *      3. 如果不指定动漫名和集数，使用随机数充当名字
 * 3. 定义方法，将一个file改变成另一个file
 * */
public interface IVideoHandler {
    void rename(File in) throws Exception;
    void merge(File in) throws Exception;
}
