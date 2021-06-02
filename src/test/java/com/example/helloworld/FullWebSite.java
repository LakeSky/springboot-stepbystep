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

/**
 * Created by gang on 2021/6/2.
 */
public class FullWebSite {

    @Test
    public void display() throws Exception {
        String saveDir = "F:\\try\\scraw";
        String site = "https://www.rescuespa.com/";
        if (site.endsWith("/")) {
            site = site.substring(0, site.length() - 1);
        }
        String siteDir = site.replace("http://", "");
        siteDir = siteDir.replace("https://", "");
        saveDir = saveDir + File.separator + siteDir;
        Document doc = Jsoup.connect(site).get();
        String html = doc.html();
        String baseUri = doc.baseUri();
        System.out.println(baseUri);
        Elements links = doc.select("link");
        for (Element element : links) {
            String href = element.attr("href");
            if (StrUtil.isEmpty(href)) {
                continue;
            }
            href = fixUrl(href, site, baseUri);
            String linkContent = HttpUtil.get(href);
            String navPath = urlToPath(href, site);
            if (StrUtil.isEmpty(navPath)) {
                continue;
            }
            String path = saveDir + File.separator + navPath;
            System.out.println(path);
            FileUtil.mkParentDirs(path);
            FileUtil.writeBytes(linkContent.getBytes(), path);
            System.out.println(element.attr("href"));
        }

        Elements scripts = doc.select("script");
        for (Element element : scripts) {
            String src = element.attr("src");
            if (StrUtil.isEmpty(src)) {
                continue;
            }
            src = fixUrl(src, site, baseUri);
            String content = HttpUtil.get(src);
            String navPath = urlToPath(src, site);
            if (StrUtil.isEmpty(navPath)) {
                continue;
            }
            String path = saveDir + File.separator + navPath;
            FileUtil.mkParentDirs(path);
            FileUtil.writeBytes(content.getBytes(), path);
            System.out.println(element.attr("src"));
        }

        Elements images = doc.select("img");
        for (Element element : images) {
            String src = element.attr("src");
            if (StrUtil.isEmpty(src)) {
                continue;
            }
            src = fixUrl(src, site, baseUri);
            String navPath = urlToPath(src, site);
            if (StrUtil.isEmpty(navPath)) {
                continue;
            }
            String path = saveDir + File.separator + navPath;
            HttpUtil.downloadFile(src, path);
        }
        FileUtil.writeBytes(html.getBytes(), saveDir+File.separator+"index.html");
        System.out.println(html);
    }

    public String urlToPath(String url, String site) {
        url = url.replace(site, "");
        if (url.startsWith("/")) {
            url = url.substring(0, url.length() - 1);
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
        System.out.println("path:" + url);
        return url;
    }

    public static String fixUrl(String url, String site, String baseUri) {
        System.out.println("fixUrl:" + url);
        if (!url.startsWith("http") && !url.startsWith("/")) {
            url = baseUri + "/" + url;
        }
        if (url.startsWith("/")) {
            url = site + url;
        }
        return url;
    }
}
