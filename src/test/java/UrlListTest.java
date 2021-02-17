import fan.zhunter.downloadanime.common.driver.FormatChromeDriver;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 1.     display: block;
         height: 24px;
         width: 126px;
 * 2. 打印所有的height
 * 3. 正确的逻辑是：ul兄弟节点存在文字说明
 * 4. 找寻兄弟节点的css语法
 * 5. 使用lxml解析文件
 * 6. 通过特征来判断ul的方式彻底破产
 * 7. title包含，尝试
 * 8. bilibili的url是通过事件驱动的，播放链接无法直接捕捉
 * 9. 解决方法：点击按钮
 * 10. 这个筛选法复杂
 * 11. 直接使用配置法
 * 12. 出现bug
 * */
public class UrlListTest {
    private final String css2 = "#app > div.media-tab-wrp > div.media-tab-content > div > div.media-tab-detail-l-wrp > div > div:nth-child(1) > div > div:nth-child(2) > div.sl-ep-list > ul";
    @Test
    public void getList(){
        FormatChromeDriver formatChromeDriver = new FormatChromeDriver();
        WebDriver driver = formatChromeDriver.getDriver();
        String url = "https://www.bilibili.com/bangumi/media/md28231808/?spm_id_from=666.25.b_6d656469615f6d6f64756c65.2";
        driver.get(url);
        //TODO 使用css的语法获取所有的li标签，查看昨天所阅读书籍
        //结果为获取的链接不明
        try {
            driver.findElements(By.cssSelector("ttt"));
        }catch (Exception e){};
        List<WebElement> uls = driver.findElements(By.cssSelector(css2));
        String before = null;
        for (WebElement ul :
                uls) {
            List<WebElement> li = ul.findElements(By.cssSelector("li"));
            Set<String> windowHandles = driver.getWindowHandles();
            String rowHandler = new ArrayList<>(windowHandles).get(0);
            try {
                for (WebElement one : li){
                    one.click();
                    ArrayList<String> strings = new ArrayList<>(driver.getWindowHandles());
                    strings.remove(rowHandler);
                    String nowHandler = strings.get(0);
                    driver.switchTo().window(nowHandler);
                    System.out.println(driver.getCurrentUrl());
                    driver.switchTo().window(nowHandler).close();
                    driver.switchTo().window(rowHandler);
                }
            }catch (Exception e){}
//            String outerHTML = ul.getAttribute("outerHTML");
//            if(outerHTML != null && outerHTML.contains("title")){
//                try {
//                    //TODO 获取兄弟元素
////                    driver.findElements(By.cssSelector("[]"))
//                    WebElement li = ul.findElement(By.cssSelector("li"));
//                    String height = li.getCssValue("height");
////                    li.click();
//                    int he = getSize(height);
////                    if(he < 20 || he > 100) continue;
//                    System.out.println(height + ":" + outerHTML);
//                }catch (Exception e){}
//
//            }
        }
        System.out.println();
        formatChromeDriver.closeDriver();
    }
    public int getSize(String px){
        if(px == null || !px.endsWith("px")){
            return -1;
        }else{
            String px1 = px.split("px")[0];
            return Integer.valueOf(px1);
        }
    }
}
