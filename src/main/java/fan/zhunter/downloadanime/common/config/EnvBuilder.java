package fan.zhunter.downloadanime.common.config;

import fan.zhunter.downloadanime.common.Binterface.IEnvBuilder;
import fan.zhunter.downloadanime.common.Binterface.IDriver;
import fan.zhunter.downloadanime.common.driver.FormatChromeDriver;
import fan.zhunter.downloadanime.util.ThreadLocalUtil;

import java.util.List;
import java.util.Map;

public class EnvBuilder implements IEnvBuilder {
    public Env env;
    private boolean useLog;

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
            if(useLog){
                driver.getLogDriver();
                driver.setLogCookie(cookie, env.getUrl());
            }
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
    @Override
    public IEnvBuilder setDriverClass(Class cz){
        env.setDriver(getDriver(cz, env.getCookie()));
        return this;
    }
    @Override
    public IEnvBuilder setCookie(Map<String, String> cookie){
        env.setCookie(cookie);
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
    public IEnvBuilder setUseLog(boolean useLog) {
        this.useLog = useLog;
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
