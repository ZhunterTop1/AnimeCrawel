package fan.zhunter.downloadanime.service.flv;

import java.io.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

public class MergeFLV extends Thread{
    String outPath = null;
    String fileName = null;
    String inPath = null;
    public MergeFLV(String filenName,String inPath,String outPath) {
        this.fileName = filenName;
        this.inPath = inPath;
        this.outPath = outPath;
    }

    @Override
    public void run() {
        try {
            merge(fileName, inPath, outPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void merge(String filenName,String inPath,String outPath) throws IOException {
//        while (! FileNoOPUtils.isNoMod(new File(inPath)));
        while (true) {
            File file = new File(inPath);
            File[] files = file.listFiles();
            Arrays.sort(files);
            Vector<FileInputStream> v = new Vector<FileInputStream>();
            int total = -1;
            File tmp = null;
            for (File one : files) {
                System.out.println(one.getName());
                v.add(new FileInputStream(one));
                tmp = one;
                total++;
            }
            if (tmp.getName().endsWith(total + ".flv")){
                System.out.println(inPath + ": true Download");
            }else {
                System.out.println(inPath + ": error");
                return;
            }
            Enumeration<FileInputStream> en = v.elements();
            SequenceInputStream sis = new SequenceInputStream(en);
            FileOutputStream fos = new FileOutputStream(outPath + "\\" + filenName + ".flv");

            byte[] buf = new byte[1024];

            int len = 0;

            while((len=sis.read(buf))!=-1)
            {
                fos.write(buf,0,len);
            }
            fos.close();
            sis.close();
            for (int i = 0; i < files.length; i++){
                files[i].delete();
            }
            file.delete();
            break;
        }
        System.out.println(inPath + "成功操作！！！");
    }
}
