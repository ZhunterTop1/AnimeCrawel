package fan.zhunter.downloadanime.common.Binterface;

import fan.zhunter.downloadanime.common.Env;

import java.util.List;
import java.util.Map;

public interface IEnvBuilder {
    Env env = null;

    IEnvBuilder setUrl(String url);

    IEnvBuilder setAname(String aname);

    IEnvBuilder setDriverClass(Class cz, Map<String, String> cookie);

    IEnvBuilder setDriverClass(Class cz);

    IEnvBuilder isList(boolean isList);

    IEnvBuilder list(List<Integer> list);

    IEnvBuilder builder();

    void close();
}
