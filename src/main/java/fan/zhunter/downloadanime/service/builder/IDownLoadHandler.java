package fan.zhunter.downloadanime.service.builder;
/***
 * 1. 每种视频类型都有自己的处理模式，
 *      1. mp4不需要处理
 *      2. m4s需要判断视频哪个是 video，哪个是 audio
 *      3. flv需要确定视频的顺序
 *      4. m3u8需要获取所有的ts文件&&确定顺序
 *      。。。
 * 2. 它们没有统一的处理标准，这个接口只是一个标志，方便实现多态，最后进行强转
 * 3. 需要定义什么是有用的视频，什么是无用的视频链接
 * */
public interface IDownLoadHandler {
    boolean typeCheck(String msg);
}
