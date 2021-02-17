package fan.zhunter.downloadanime.util;

import fan.zhunter.downloadanime.common.config.Env;

public class ThreadLocalUtil {
    private static ThreadLocal<Env> envLocal = new ThreadLocal<>();
    public static void setEnv(Env env){
        envLocal.set(env);
    }
    public static Env getEnv(){
        return envLocal.get();
    }
}
