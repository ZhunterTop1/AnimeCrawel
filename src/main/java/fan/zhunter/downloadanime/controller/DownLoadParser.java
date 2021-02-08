package fan.zhunter.downloadanime.controller;
/**
 * 1. 需求：下线多个文件
 * 2. 传参为：k->(k->V)
 * 3. 下载文件名 k\K, 下载地址V
 * 4. 多线程下载问题：
 *      1. 可能出现多个线程阻塞->kill重来
 *      2. 文件大小不一，结束时间不一，使用计时器记录，改线程多久没有get data
 *      3. 短时间的大量连接引起的反爬机制
 * */
public class DownLoadParser {
}
