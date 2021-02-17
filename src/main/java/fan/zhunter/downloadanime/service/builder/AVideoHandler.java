package fan.zhunter.downloadanime.service.builder;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * 1. 这应该是个线程
 * 2. 在该文件countdownLatch会唤醒这个线程
 * */
public abstract class AVideoHandler implements IVideoHandler, Callable<Boolean> {
    private File in = null;
    public AVideoHandler(File in){
        this.in = in;
    }
    @Override
    public Boolean call() throws Exception {
        try {
            rename(in);
            return true;
        }catch (Exception e){}
        return false;
    }

    @Override
    public void rename(File in) throws Exception {
        merge(in);
        String absolutePath = in.getAbsolutePath();
        String originFile = in.getAbsolutePath() + "//comp.mp4";
        File ori = new File(originFile);
        ori.renameTo(new File(absolutePath + ".mp4"));
        deleteRecursion(in);
        in.delete();
    }
    public void deleteRecursion(File file){
        if(file.isFile()){
            file.delete();
            return;
        }
        if(file.listFiles().length == 0){
            file.delete();
            return;
        }
        for(File child : file.listFiles()){
            deleteRecursion(child);
        }
    }
}
