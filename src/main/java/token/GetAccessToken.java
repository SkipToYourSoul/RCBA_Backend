package token;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Get token from web urls
 * You can config the web url in RC.conf, we have an url but it's private.
 * The class is fit for our url.
 * So maybe you can not use this class.
 *
 * @author liye
 */
public class GetAccessToken {
    private List<String> urls;
    private Set<String> finishedUrls;
    private int count;
    private String fileDir;
    private int max;

    public GetAccessToken() {
        this.urls = new ArrayList();
        this.finishedUrls = new HashSet();
        this.count = 0;
        this.fileDir = ".\\";
        this.max = 100;
    }

    public String accessUrl(String url) {
        StringBuffer sb = new StringBuffer();
        try {
            URL u = new URL(url);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setConnectTimeout(3000);
            huc.setReadTimeout(3000);

            BufferedReader br = null;

            br = new BufferedReader(new InputStreamReader(huc.getInputStream()));

            huc.connect();
            String tmp = null;
            while ((tmp = br.readLine()) != null) {
                sb.append(tmp);
            }
            huc.disconnect();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String content = sb.toString();
        int index = 0;
        while ((index = content.indexOf("href=/")) != -1) {
            content = content.substring(index + 6);
            String tmpUrl = content.substring(0, content.indexOf("/"));
            if (tmpUrl.startsWith("http")) {
                this.urls.add(tmpUrl);
            }
        }
        this.count += 1;
        return sb.toString();
    }

    public String getCharset(String s) {
        String contentType = s;
        String[] values = contentType.split(";");
        String charset = "";
        for (String value : values) {
            value = value.trim();
            if (value.toLowerCase().startsWith("charset=")) {
                charset = value.substring("charset=".length());
            }
        }
        return charset;
    }

    public void addUrl(String url) {
        this.urls.add(url);
    }

    public void setFileDir(String dir) {
        this.fileDir = dir;
    }

    public void setMax(int m) {
        this.max = m;
    }

    public void work() {
        while ((this.count < this.max) && (!this.urls.isEmpty())) {
            String url = (String) this.urls.get(0);
            if (this.finishedUrls.contains(url)) {
                this.urls.remove(0);
            } else {
                save(accessUrl(url));
                this.urls.remove(0);
                this.finishedUrls.add(url);
            }
        }
    }

    public void save(String s) {
        try {
            File f = new File(this.fileDir);
            if (!f.exists()) {
                f.mkdirs();
            }
            String[] str = s.split("},");
            FileWriter fw = new FileWriter(this.fileDir + String.valueOf(this.count) + ".txt", false);
            for (int i = 0; i < str.length; i++) {
                fw.append(str[i]);
                fw.append("\n");
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getAccessToken()
            throws IOException, ParseException {
        File file = new File(this.fileDir + String.valueOf(this.count) + ".txt");
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, "GBK");
        BufferedReader br = new BufferedReader(isr);

        String line = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date end = new Date();
        long endT = end.getTime();

        ArrayList<String> tempList = new ArrayList();
        while ((line = br.readLine()) != null) {
            String create_time = line.substring(line.indexOf("create_time") + 14, line.indexOf("create_time") + 32);
            String accesss_token = line.substring(line.indexOf("access_token") + 15, line.indexOf("access_token") + 47);
            Date start_time = sdf.parse(create_time);
            long startT = start_time.getTime();

            long mint = (endT - startT) / 1000L;
            int hor = (int) mint / 3600;
            int day = hor / 24;
            if (day < 30) {
                tempList.add(accesss_token);
            }
        }
        FileWriter fileWriter = null;
        BufferedWriter bw = null;
        try {
            fileWriter = new FileWriter("." + File.separator + "Tokens" + File.separator + "tokens.txt", false);
            bw = new BufferedWriter(fileWriter);
            for (String temp : tempList) {
                bw.append(temp);
                bw.newLine();
            }
            bw.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run()
            throws IOException, ParseException {
        System.out.println("Refresh Token!");
        setFileDir("." + File.separator + "Tokens" + File.separator);

        Properties props = new Properties();
        props.load(new FileInputStream(".//config/RC.conf"));
        String url = props.getProperty("tokenUrl");

        addUrl(url);
        work();
        getAccessToken();
    }

    public static void main(String[] args) {
        GetAccessToken wc = new GetAccessToken();
        try {
            wc.run();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("refresh token finished");
    }
}
