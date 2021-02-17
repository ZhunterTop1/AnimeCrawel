import fan.zhunter.downloadanime.common.driver.FormatChromeDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import java.io.*;

/**
 * chromedriver
 * dev-tool
 */
public class AnimeDownload_M3U8 {
    public static void main(String[] args) throws IOException {
        FormatChromeDriver formatChromeDriver = new FormatChromeDriver();
        WebDriver driver = formatChromeDriver.getLogDriver();
        driver.get("https://www.1905.com/vod/play/86388.shtml?fr=vodhome_jsxftj_0");
        //TODO 获取m3u8链接
        //获取全部的console信息
        LogEntries logEntries =driver.manage().logs().get(LogType.BROWSER);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(LogEntry entry : logEntries) {
            //依次打印出console信息
            System.out.println("chrome.console===="+" "+entry.getMessage());
        }
        // 下面是 根据业务需求编写的 获取信息
    	/*Thread.sleep(1000);
    	List<LogEntry>list=driver.manage().logs().get(LogType.BROWSER).getAll();
    	String content=list.get(list.size()-1).getMessage();//根据需求获取最后一次信息
    	String tempStr=content.substring(content.indexOf("\"") + 1, content.lastIndexOf("\""));
    	System.out.println(tempStr);//本次需要获取的某列信息*/
//        LogEntries logs = driver.manage().logs().get(LogType.PERFORMANCE);
//        for (Iterator<LogEntry> it = logs.iterator(); it.hasNext();) {
//            LogEntry entry = it.next();
//            try {
//                JsonObject json =  new JsonParser().parse(entry.getMessage()).getAsJsonObject();
//
//                JsonObject message = json.getAsJsonObject("message");
//                String method = message.get("method").getAsString();
//                int mark = 0;
//                //如果是响应
//                if (method != null && "Network.responseReceived".equals(method)) {
//                    JsonObject params = message.getAsJsonObject("params");
//                    JsonObject response = params.getAsJsonObject("response");
//                    String messageUrl = response.get("url").getAsString();
//                    System.out.println(messageUrl);
////                    if (params.toString().contains(".m3u8") && messageUrl.contains("url=")) {
////                        System.out.println(params);
////                    }
//                }
//            }catch (Exception e){}
        }
//    public static void downLoadM3U8(String filePath, String rowUrl) {
//        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));
//            String val = null;
//            rowUrl = rowUrl.split("index")[0];
//            while ((val = reader.readLine()) != null){
//                if (val.endsWith(".m3u8")){
//                    InputStream in=new URL(rowUrl + val).openConnection().getInputStream(); //创建连接、输入copy流
////                                String out = nameMap.get(Integer.parseInt(key.trim()));
//                    root = (rowUrl + val).split("index")[0];
//                    FileOutputStream f = new FileOutputStream(filePath);//创建文百件度输出流
//                    byte [] bb=new byte[1024]; //接收缓存
//                    int len;
//                    while( (len=in.read(bb))>0){ //接收
//                        f.write(bb, 0, len); //写入问文件
//                    }
//                    f.close();
//                    in.close();
//                    break;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    public static void down(String newpath, String key) throws IOException {
//        File file1 = new File(newpath);
//
//        FileInputStream inputStream = new FileInputStream(file1);
//        InputStreamReader reader = new InputStreamReader(inputStream);
//        BufferedReader bufferedReader = new BufferedReader(reader);
//        String info;
//        long start = System.currentTimeMillis();
//        while ((info =bufferedReader.readLine()) != null){
//            if (info.endsWith(".ts")){
//                System.out.println("download:" + info);
//                URLConnection conn = new URL(root + info).openConnection();
////                        .getInputStream(); //创建连接、输入copy流
//                conn.setRequestProperty("accept", "*/*");
//                conn.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8");
//                conn.setRequestProperty("Cache-Control","max-age=0");
//                conn.setRequestProperty("connection", "Keep-Alive");
////                conn.setRequestProperty("Cookie",cookie);
//                //conn.setRequestProperty("Host","www.zjtax.gov.cn");
//                conn.setRequestProperty("user-agent",
//                        "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
//                // 发送POST请求必须设置如下两行
//                conn.setDoOutput(true);
//                conn.setDoInput(true);
//                InputStream in = conn.getInputStream();
//                FileOutputStream f = new FileOutputStream("C:\\Users\\28124\\Desktop\\测试自动化\\自动化项目\\file4\\" + info);//创建文百件度输出流
//
//                byte [] bb=new byte[1024]; //接收缓存
//                int len;
//                while( (len=in.read(bb))>0){ //接收
//                    f.write(bb, 0, len); //写入问文件
//                }
//                f.close();
//                in.close();
//            }
//        }
//        long end = System.currentTimeMillis();
//        System.out.println((end - start)/1000);
//
//
//    }
//    public static void merge(String Filename, String inPath, String outPath) throws IOException {
//        File file = new File(inPath);
//        File[] files = file.listFiles();
////        Arrays.sort(files,new Comparator<File>() {
////            @Override
////            public int compare(File o1,File o2) {
////                return getName(o1.getName()) - getName(o2.getName());
////            }
////        });
//        Vector<FileInputStream> v = new Vector<FileInputStream>();
//        for (File one : files) {
//            v.add(new FileInputStream(one));
//        }
//        Enumeration<FileInputStream> en = v.elements();
//        SequenceInputStream sis = new SequenceInputStream(en);
////        SequenceInputStream sis2 = new SequenceInputStream(sis, sis);
//        FileOutputStream fos = new FileOutputStream(outPath + "\\" + Filename);
//
//        byte[] buf = new byte[1024];
//
//        int len = 0;
//
//        while((len=sis.read(buf))!=-1)
//        {
//            fos.write(buf,0,len);
//        }
//        fos.close();
//        sis.close();
//        for (File one : files) {
//            one.delete();
//        }
//    }
//    public static int getName(String str){
//        String[] split = str.split("[a-zA-Z]+");
//        String s = split[split.length - 1].split("\\.")[0];
//        int name = Integer.parseInt(s);
//        return name;
//    }
}