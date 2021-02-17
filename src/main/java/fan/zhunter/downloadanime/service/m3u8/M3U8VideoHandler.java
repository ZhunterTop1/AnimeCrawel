package fan.zhunter.downloadanime.service.m3u8;

import fan.zhunter.downloadanime.service.builder.AVideoHandler;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 1. 要求下载的文件都是，按照 seq.ts的方式命名
 * */
public class M3U8VideoHandler extends AVideoHandler{
    public M3U8VideoHandler(File in) {
        super(in);
    }

    @Override
    public void merge(File in) throws Exception {
        merge(in.getPath(), in.getPath());
    }
    public void merge(String inPath, String outPath) throws Exception {
        File file = new File(inPath);
        File[] files = file.listFiles();
        Arrays.sort(files,(o1,o2) -> {
            String o1Name = o1.getName();
            String o2Name = o2.getName();
            Pattern compile = Pattern.compile("[0-9]+");
            Matcher matcher = compile.matcher(o1Name);
            Matcher matcher1 = compile.matcher(o2Name);
            String o1Num = null;
            String o2Num = null;
            while (matcher.find()) {
                o1Num = matcher.group();
            }
            while (matcher1.find()) {
                o2Num = matcher1.group();
            }
            return (int) (Long.parseLong(o1Num) - Long.parseLong(o2Num));
        });
        Vector<FileInputStream> v = new Vector<FileInputStream>();
        int total = -1;
        File tmp = null;
        for (File one : files) {
            v.add(new FileInputStream(one));
            tmp = one;
            total++;
        }
        if (tmp.getName().endsWith(total + ".ts")){
            System.out.println(inPath + ": true Download");
        }else {
            System.out.println(inPath + ": error");
            throw new Exception("maybe the ts doesn't all download!!!");
        }
        Enumeration<FileInputStream> en = v.elements();
        SequenceInputStream sis = new SequenceInputStream(en);
        FileOutputStream fos = new FileOutputStream(outPath + "\\" + "comp.mp4");

        byte[] buf = new byte[1024];
        int len = 0;
        while((len=sis.read(buf))!=-1)
        {
            fos.write(buf,0,len);
        }
        fos.close();
        sis.close();
    }
}
