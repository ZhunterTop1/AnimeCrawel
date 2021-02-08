package fan.zhunter.downloadanime.common;

import fan.zhunter.downloadanime.common.Binterface.IEnvBuilder;
import fan.zhunter.downloadanime.common.Binterface.IDriver;
import fan.zhunter.downloadanime.util.ThreadLocalUtil;

import java.util.List;
import java.util.Map;

public class EnvBuilder implements IEnvBuilder {
    public Env env;

    public EnvBuilder(){
        this.env = new Env();
    }

    public IDriver getDriver(Class name, Map<String, String> cookie) {
        String className = name.getName();
        IDriver driver = null;
        switch (className){
            case "org.openqa.selenium.chrome.ChromeDriver":
               driver = new FormatChromeDriver();
        }
        if(cookie != null) {
            driver.getDriver();
            driver.setCookie(cookie, env.getUrl());
        }
        return driver;
    }

    public IEnvBuilder setUrl(String url) {
        env.setUrl(url);
        return this;
    }

    @Override
    public IEnvBuilder setAname(String aname) {
        env.setAname(aname);
        return this;
    }
    public IEnvBuilder setDriverClass(Class cz){
        return setDriverClass(cz, null);
    }


    @Override
    public IEnvBuilder setDriverClass(Class cz, Map<String, String> cookie) {
        env.setDriver(getDriver(cz, cookie));
        return this;
    }

    @Override
    public IEnvBuilder isList(boolean isList) {
        env.isList(isList);
        return this;
    }

    @Override
    public IEnvBuilder list(List<Integer> list) {
        env.setList(list);
        return this;
    }

    @Override
    public IEnvBuilder builder() {
        //将环境设置到ThreadLocal中
        ThreadLocalUtil.setEnv(env);
        return this;
    }

    @Override
    public void close() {
        env.getDriver().closeDriver();
    }

}
