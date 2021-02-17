package fan.zhunter.downloadanime.service.m4s;

import fan.zhunter.downloadanime.service.builder.IDownLoadHandler;
import fan.zhunter.downloadanime.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * 1. 存在国际标准判断mp4是视频还是音频
 * 2. 在MP4文件里面搜68 64 6C 72(hdlr的十六进制数)，从下图可以看出该视频包含视频流和音频流：
 * video/mp4
 * video/mp4
 * video : 76696465  vide
 * audio : 736F756E  soun
 * */
public class M4sHandler implements IDownLoadHandler {
    @Override
    public boolean typeCheck(String msg) {
        return Utils.isNotEmpty(msg) && msg.contains(".m4s");
    }
    public static String getType(String fileName) throws IOException {
        String hex = getHex(getFileBytesData(fileName),528 *2);
        if(hex.contains("76696465")){
            return "video";
        }else if(hex.contains("736F756E")){
            return "audio";
        }
        File file = new File(fileName);
        String type = Files.probeContentType(file.toPath());
        return type;
    }
    /**
     * 获取16进制表示的魔数
     *
     * @param data              字节数组形式的文件数据
     * @param magicNumberLength 魔数长度
     * @return
     */
    public static String getHex(byte[] data, int magicNumberLength) {
        //提取文件的魔数
        StringBuilder magicNumber = new StringBuilder();
        //一个字节对应魔数的两位
        int magicNumberByteLength = magicNumberLength / 2;
        for (int i = 0; i < magicNumberByteLength; i++) {
            magicNumber.append(Integer.toHexString(data[i] >> 4 & 0xF));
            magicNumber.append(Integer.toHexString(data[i] & 0xF));
        }
        return magicNumber.toString().toUpperCase();
    }
    /**
     * 读取文件字节数据
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static byte[] getFileBytesData(String filePath) throws IOException {
        InputStream fs = new FileInputStream(filePath);
        byte[] b = new byte[fs.available()];
        fs.read(b);
        return b;
    }
}
