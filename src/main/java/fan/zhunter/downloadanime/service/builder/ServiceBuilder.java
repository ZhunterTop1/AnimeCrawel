package fan.zhunter.downloadanime.service.builder;

import fan.zhunter.downloadanime.common.Binterface.IDownUrlParse;
import fan.zhunter.downloadanime.common.Binterface.IListParse;
import fan.zhunter.downloadanime.common.Binterface.IServiceBuilder;
import fan.zhunter.downloadanime.util.Utils;

public class ServiceBuilder implements IServiceBuilder {
    private String domain;
    @Override
    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public IListParse buildListParse() {
        switch (domain){
            case "bilibili.com":
                return new BiliBiliPlayList();
        }
        return null;
    }

    @Override
    public IDownUrlParse buildPlayServide() {
        switch (domain){
            case "bilibili.com":
//                return new BiliBiliDownLoad();
                NormalDownLoad load = new NormalDownLoad();
                load.setTop(2);
                return load;
        }
        return null;
    }
}
