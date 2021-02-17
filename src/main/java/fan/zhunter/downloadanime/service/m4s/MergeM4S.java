package fan.zhunter.downloadanime.service.m4s;

import java.io.*;

public class MergeM4S extends Thread{
    String cmd = null;
    String mergePath = null;
    String file = null;
    public MergeM4S(String ffmpegPath,String mergePath,String outPath,String name) {

        this.cmd = ffmpegPath + "\\ffmpeg -i " + mergePath + "\\video.mp4 -i " + mergePath + "\\audio.mp4 -c:v copy -c:a aac -strict experimental "
                + outPath + "\\" + name + ".mp4";
        this.mergePath = mergePath;
        this.file = outPath + "\\" + name + ".mp4";
        System.out.println(cmd);
    }
    public void run() {
//        while (! FileNoOPUtils.isNoMod(new File(mergePath)));//使用改变名称法
        long start = System.currentTimeMillis();
        while (true) {
            File file = new File(this.file);
            if (file.exists()) {
                file.delete();
            }
            String cmdStr = cmd;
            Runtime run = Runtime.getRuntime();
            try {
                Process process = run.exec(cmdStr);
                InputStream in = process.getErrorStream();
                InputStreamReader reader = new InputStreamReader(in);
                BufferedReader br = new BufferedReader(reader);
                StringBuffer sb = new StringBuffer();
                String message;
                while(true) {
                    if ((message = br.readLine()) == null) {
                        break;
                    }
                    if (System.currentTimeMillis() - start > 10*60*1000) {
                        start = System.currentTimeMillis();
                        new Exception("execute time too long！！！");
                    }
                }
                File file1 = new File(this.file);
                File file2 = new File(mergePath + "\\video.mp4");
                if (file1.length() > file2.length()) {
                    break;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("----------");
            }
        }
        File[] files = new File(mergePath).listFiles();
        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }
        new File(mergePath).delete();

    }
}