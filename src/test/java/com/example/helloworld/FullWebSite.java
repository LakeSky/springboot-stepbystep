package com.example.helloworld;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by gang on 2021/6/2.
 */
public class FullWebSite {

    @Test
    public void display() throws Exception {
        String saveDir = "F:\\try\\scraw";
        String site = "https://www.rescuespa.com/";
        site = "http://www.mutou888.com/";
        savePage(site,saveDir);
    }

    //将网页保存到本地 String site = "https://www.rescuespa.com/";String saveDir = "F:\\try\\scraw";
    public static void savePage(String site, String saveDir) {
        if (site.endsWith("/")) {
            site = site.substring(0, site.length() - 1);
        }
        String siteDir = site.replace("http://", "");
        siteDir = siteDir.replace("https://", "");
        saveDir = saveDir + File.separator + siteDir;
        Document doc = null;
        try {
            doc = Jsoup.connect(site).get();
            String baseUri = doc.baseUri();
            System.out.println(baseUri);
            Elements links = doc.select("link");
            for (Element element : links) {
                String href = element.attr("href");
                if (StrUtil.isEmpty(href)) {
                    continue;
                }
                element.attr("href", urlToPath(href, baseUri));
                href = fixUrl(href, baseUri);
                String linkContent = HttpUtil.get(href);
                String navPath = urlToSavePath(href, baseUri);
                if (StrUtil.isEmpty(navPath)) {
                    continue;
                }
                String path = saveDir + File.separator + navPath;
                File pathFile = new File(path);
                if (pathFile.exists()) {
                    continue;
                }
                FileUtil.mkParentDirs(path);
                FileUtil.writeBytes(linkContent.getBytes(), path);
            }

            Elements scripts = doc.select("script");
            for (Element element : scripts) {
                String src = element.attr("src");
                element.attr("src", urlToPath(src, baseUri));
                if (StrUtil.isEmpty(src)) {
                    continue;
                }
                src = fixUrl(src, baseUri);
                String content = HttpUtil.get(src);
                String navPath = urlToSavePath(src, baseUri);
                if (StrUtil.isEmpty(navPath)) {
                    continue;
                }
                String path = saveDir + File.separator + navPath;
                File pathFile = new File(path);
                if (pathFile.exists()) {
                    continue;
                }
                FileUtil.mkParentDirs(path);
                FileUtil.writeBytes(content.getBytes(), path);
            }

            Elements images = doc.select("img");
            for (Element element : images) {
                String src = element.attr("src");
                element.attr("src", urlToPath(src, baseUri));
                if (StrUtil.isEmpty(src)) {
                    continue;
                }
                src = fixUrl(src, baseUri);
                String navPath = urlToSavePath(src, baseUri);
                if (StrUtil.isEmpty(navPath)) {
                    continue;
                }
                String path = saveDir + File.separator + navPath;
                File pathFile = new File(path);
                if (pathFile.exists()) {
                    continue;
                }
                HttpUtil.downloadFile(src, path);
            }
            String html = doc.html();
            FileUtil.writeBytes(html.getBytes(), saveDir + File.separator + "index.html");
            System.out.println(html);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String urlToSavePath(String url, String baseUri) {
        url = url.replace(baseUri, "");
        if (url.startsWith("/")) {
            url = url.substring(1);
        }
        //外站连接不处理
        if (url.startsWith("http")) {
            return null;
        }
        if (StrUtil.isEmpty(url)) {
            return url;
        }
        //如果url里面的内容不是以文件名结尾的，比如/css/dist/block-library/style.min.css?ver=a8fe96efb5165c1a8cbca34c4840851
        if (url.contains("?")) {
            int indexOf = url.indexOf("?");
            url = url.substring(0, indexOf);
        }
        //url以/结尾，这样的不处理wp-json/wp/v2/pages/
        if (url.endsWith("/")) {
            return null;
        }
        //有的根本就不是文件，而是一些路径，这些要过滤掉
        int lastSlash = url.lastIndexOf("/");
        String endPath = url.substring(lastSlash);
        if (!endPath.contains(".")) {
            return null;
        }
        System.out.println("path:" + url);
        return url;
    }

    public static String urlToPath(String url, String baseUri) {
        url = url.replace(baseUri, "");
        if (url.startsWith("/")) {
            url = url.substring(1);
        }
        //外站连接不处理
        if (url.startsWith("http")) {
            return "";
        }
        if (StrUtil.isEmpty(url)) {
            return url;
        }
        //如果url里面的内容不是以文件名结尾的，比如/css/dist/block-library/style.min.css?ver=a8fe96efb5165c1a8cbca34c4840851
        if (url.contains("?")) {
            int indexOf = url.indexOf("?");
            url = url.substring(0, indexOf);
        }
        System.out.println("path:" + url);
        return url;
    }

    public static String fixUrl(String url, String baseUri) {
        System.out.println("fixUrl:" + url);
        if (!url.startsWith("http") && !url.startsWith("/")) {
            url = baseUri + "/" + url;
        }
        if (url.startsWith("/")) {
            url = baseUri + "/" + url;
        }
        return url;
    }
}
