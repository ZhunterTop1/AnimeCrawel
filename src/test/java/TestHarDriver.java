import fan.zhunter.downloadanime.common.FormatChromeDriver;
import org.openqa.selenium.WebDriver;

public class TestHarDriver {
    public static void main(String[] args) {
        FormatChromeDriver formatChromeDriver = new FormatChromeDriver();
        WebDriver logDriver = formatChromeDriver.getLogDriver();
        logDriver.get("https://www.bilibili.com/bangumi/play/ss36198/?spm_id_from=333.6.b_696e7465726e6174696f6e616c486561646572.37");
        logDriver.close();
    }
}
