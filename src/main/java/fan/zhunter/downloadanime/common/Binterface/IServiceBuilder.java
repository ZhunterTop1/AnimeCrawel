package fan.zhunter.downloadanime.common.Binterface;

public interface IServiceBuilder {
    void setDomain(String domain);
    IListParse buildListParse();
    IDownUrlParse buildPlayServide();
}
