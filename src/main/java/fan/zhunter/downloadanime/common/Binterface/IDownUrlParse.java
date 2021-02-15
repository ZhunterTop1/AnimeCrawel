package fan.zhunter.downloadanime.common.Binterface;

import fan.zhunter.downloadanime.common.DownLoadRequest;

import java.util.Map;
import java.util.Set;

public interface IDownUrlParse {
    Set<DownLoadRequest> getDownLoadUrl(Map<String, String> urls);
}
