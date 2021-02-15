import lombok.SneakyThrows;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.DeflateDecompressingEntity;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.codecs.DefaultHttpRequestWriterFactory;
import org.apache.http.impl.nio.codecs.DefaultHttpResponseParserFactory;
import org.apache.http.impl.nio.conn.ManagedNHttpClientConnectionFactory;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.conn.ManagedNHttpClientConnection;
import org.apache.http.nio.conn.NHttpConnectionFactory;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.nio.util.HeapByteBufferAllocator;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.Future;


/**
 * 1. HttpAsyncClien返回数据乱码
 */
public class HttpAsyncDownloadUtils {


    private static final Logger LOGGER = LoggerFactory.getLogger(HttpAsyncDownloadUtils.class);
    public static CloseableHttpAsyncClient httpclient = null;


    /**
     * 获取 ttpAsyncClient
     *
     * @return CloseableHttpAsyncClient
     * @throws KeyStoreException        KeyStoreException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws IOReactorException       IOReactorException
     */
    public static synchronized CloseableHttpAsyncClient getHttpAsyncClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException, IOReactorException {
        if (httpclient == null) {


            // HTTPConnection工厂 ：配置请求/解析响应
            NHttpConnectionFactory<ManagedNHttpClientConnection> connFactory = new ManagedNHttpClientConnectionFactory(
                    DefaultHttpRequestWriterFactory.INSTANCE, DefaultHttpResponseParserFactory.INSTANCE, HeapByteBufferAllocator.INSTANCE);


            //ssl 连接设置 无须证书也能访问 https
            //使用 loadTrustMaterial() 方法实现一个信任策略，信任所有证书
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();

            // 为支持的协议方案创建自定义连接套接字工厂的注册表。
            Registry<SchemeIOSessionStrategy> sessionStrategyRegistry = RegistryBuilder.<SchemeIOSessionStrategy>create()
                    .register("http", NoopIOSessionStrategy.INSTANCE)
                    .register("https", new SSLIOSessionStrategy(sslContext, NoopHostnameVerifier.INSTANCE))
                    .build();

            //DNS解析器
            DnsResolver dnsResolver = SystemDefaultDnsResolver.INSTANCE;

            // Create I/O reactor configuration
            IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                    .setIoThreadCount(Runtime.getRuntime().availableProcessors())
                    .setConnectTimeout(30000)
                    .setSoTimeout(30000)
                    .build();

            // 创建一个定制的I/O reactort
            ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);

            // 使用自定义配置创建连接管理器。
            PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(
                    ioReactor, connFactory, sessionStrategyRegistry, dnsResolver);

            //创建连接配置
            ConnectionConfig connectionConfig = ConnectionConfig.custom()
                    .setMalformedInputAction(CodingErrorAction.IGNORE)
                    .setUnmappableInputAction(CodingErrorAction.IGNORE)
                    .setCharset(Consts.UTF_8)
                    .build();
            // 将连接管理器配置为默认使用或针对特定主机使用连接配置。
            connManager.setDefaultConnectionConfig(connectionConfig);

            // 配置永久连接的最大总数或每个路由限制
            // 可以保留在池中或由连接管理器租用。
            //每个路由的默认最大连接，每个路由实际最大连接为默认为DefaultMaxPreRoute控制，而MaxTotal是控制整个池子最大数
            connManager.setMaxTotal(100);
            connManager.setDefaultMaxPerRoute(10);


            // 创建全局请求配置
            RequestConfig defaultRequestConfig = RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.DEFAULT)
                    .setSocketTimeout(5 * 1000)
                    .setConnectTimeout(5 * 1000)
                    .setExpectContinueEnabled(true)
                    .build();

            // Create an HttpClientUtils with the given custom dependencies and configuration.
            httpclient = HttpAsyncClients.custom()
                    .setConnectionManager(connManager)
                    .setDefaultRequestConfig(defaultRequestConfig)
                    .build();

            //jvm 停止或重启时，关闭连接池释放连接资源(跟数据库连接池类似)
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        httpclient.close();
                    } catch (IOException e) {
                        LOGGER.error("HttpClient close exception", e);
                    }
                }
            });

        }
        return httpclient;
    }

    public static HttpResponse doGet(HttpGet httpget) {
        CloseableHttpAsyncClient httpAsyncClient = null;

        try {
            LOGGER.info("Executing request {}", httpget.getRequestLine());
            httpAsyncClient = getHttpAsyncClient();
            httpAsyncClient.start();


            // Pass local context as a parameter
            Future<HttpResponse> future = httpAsyncClient.execute(httpget, new FutureCallback<HttpResponse>() {

                @SneakyThrows
                @Override
                public void completed(final HttpResponse response2) {
                    LOGGER.info("{} -> {} completed", httpget.getRequestLine(), response2.getStatusLine());
                }

                @Override
                public void failed(final Exception ex) {
                    LOGGER.error("{}->", httpget.getRequestLine(), ex);
                }

                @Override
                public void cancelled() {
                    LOGGER.error("{} cancelled", httpget.getRequestLine());
                }

            });
            //等待响应结果
            return future.get();
        } catch (Exception e) {
            LOGGER.error("GET execute exception ", e);
        } finally {
            if (httpAsyncClient != null) {
                //直接关闭socket,会导致长连接不能复用,所以返回Future,调用放调用get方法，将一直处于阻塞状态
                try {
                    httpAsyncClient.close();
                } catch (IOException e) {
                    LOGGER.error("GET execute httpAsyncClient close exception ", e);
                }
            }

        }
        return null;
    }

    public static final int cache = 10 * 1024;
    public static void main(String[] args) throws Exception {
        //content-encoding: gzip
        String url = "https://upos-sz-mirrorcoso1.bilivideo.com/upgcxcode/84/35/281433584/281433584-1-30112.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1612947150&gen=playurl&os=coso1bv&oi=2029251989&trid=388a987baed949108ccba249dfd4c2b2p&platform=pc&upsig=5e27ab60db6c0d2dddc8daf1c73e083d&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,platform&mid=195588405&orderid=0,3&agrr=1&logo=80000000";
        final HttpGet httpget = new HttpGet(url);
        httpget.addHeader("Refer", "https://www.bilibili.com");
        httpget.addHeader("accept", "*/*");
        httpget.addHeader("Accept-Language","zh-CN,zh;q=0.8");
        httpget.addHeader("Cache-Control","max-age=0");
        httpget.addHeader("connection", "Keep-Alive");
        httpget.addHeader("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        httpget.addHeader("Cookie", "finger=158939783; _uuid=6A6526E6-34BD-3E26-713F-0A7720D7AC8582774infoc; buvid3=6A43A7B7-BA04-49F9-B035-CE6CD1F4CDB4185002infoc; fingerprint=691bb311e76f14d0c0d180085f16a546; buvid_fp=6A43A7B7-BA04-49F9-B035-CE6CD1F4CDB4185002infoc; buvid_fp_plain=2DE773AD-7542-4785-9FEC-312A1B05B48218552infoc; CURRENT_FNVAL=80; blackside_state=1; SESSDATA=54f82a73%2C1628149578%2C41012%2A21; bili_jct=13130a4ce4bbe458b9540eef6b366e1a; DedeUserID=195588405; DedeUserID__ckMd5=0441c9aa7209ac5b; sid=alsnafg4");
        SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(), NoopHostnameVerifier.INSTANCE);
        CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(scsf).build();
        CloseableHttpResponse response = client.execute(httpget);
//        HttpResponse response = doGet(httpget);
        if (response == null) {
            return;
        }
        LOGGER.info("Response: {}", response.getStatusLine());
        int status = response.getStatusLine().getStatusCode();
        if (status >= 200 && status < 300) {
            HttpEntity entity = response.getEntity();
            if (entity.getContentEncoding() != null) {
                if ("gzip".equalsIgnoreCase(entity.getContentEncoding().getValue())) {
                    entity = new GzipDecompressingEntity(entity);
                } else if ("deflate".equalsIgnoreCase(entity.getContentEncoding().getValue())) {
                    entity = new DeflateDecompressingEntity(entity);
                }
            }
//            byte[] byteData = EntityUtils.toByteArray(response.getEntity());
            String path = "./1.mp4";
            FileOutputStream fileout = new FileOutputStream(path);
            InputStream is = entity.getContent();
            byte[] buffer = new byte[cache];
            int ch = 0;
            while ((ch = is.read(buffer)) != -1) {
                fileout.write(buffer, 0, ch);
            }
            is.close();
            fileout.flush();
            fileout.close();
//            String res = entity != null ? EntityUtils.toString(entity, StandardCharsets.UTF_8) : null;
//            LOGGER.info("result->{}", res);
        } else {
            throw new ClientProtocolException("Unexpected response status: " + status + "->" + response.getStatusLine().getReasonPhrase());
        }

    }

}