package local.eventMonitor.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class WebTool {
    private HttpClientBuilder mClientBuilder;

    public WebTool() {
        this.mClientBuilder = HttpClientBuilder.create();
    }

    public String getContent(String url, String charSet) {
        String lines = "";

        CloseableHttpClient client = this.mClientBuilder.build();
        HttpGet get = new HttpGet(url);
        get.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        get.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
        get.setHeader("Cache-Control", "max-age=0");
        get.setHeader("Connection", "keep-alive");
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.117 Safari/537.36");

        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(5000).build();
        get.setConfig(requestConfig);
        try {
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                InputStream instream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(instream, charSet));
                String line;
                while ((line = reader.readLine()) != null) {
                    lines = lines + "\r\n";
                    lines = lines + line;
                }
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
