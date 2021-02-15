package fan.zhunter.downloadanime.common.Binterface;

import fan.zhunter.downloadanime.common.Env;

import java.util.List;
import java.util.Map;

public interface IEnvBuilder {
    Env env = null;

    IEnvBuilder setUrl(String url);

    IEnvBuilder setAname(String aname);

    IEnvBuilder setDriverClass(Class cz);

    IEnvBuilder setCookie(Map<String, String> cookie);

    IEnvBuilder isList(boolean isList);

    IEnvBuilder list(List<Integer> list);

    IEnvBuilder setUseLog(boolean useLog);

    IEnvBuilder builder();

    void close();
}
