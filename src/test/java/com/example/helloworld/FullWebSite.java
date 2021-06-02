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
        String site = "http://www.mutou888.com";
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
            if (!href.startsWith("http") && !href.startsWith("/")) {
                href = baseUri + "/" + href;
            }
            if (href.startsWith("/")) {
                href = site + href;
            }
            String linkContent = HttpUtil.get(href);
            String path = saveDir + File.separator + href.replace(site, "");
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
            if (!src.startsWith("http") && !src.startsWith("/")) {
                src = baseUri + "/" + src;
            }
            if (src.startsWith("/")) {
                src = site + src;
            }
            String content = HttpUtil.get(src);
            String path = saveDir + File.separator + src.replace(site, "");
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
            if (!src.startsWith("http") && !src.startsWith("/")) {
                src = baseUri + "/" + src;
            }
            if (src.startsWith("/")) {
                src = site + src;
            }
            String path = saveDir + File.separator + src.replace(site, "");
            HttpUtil.downloadFile(src, path);
        }
        FileUtil.writeBytes(html.getBytes(), saveDir+File.separator+"index.html");
        System.out.println(html);
    }

    public void saveResource(String site, String baseUri, String url, String saveDir) {
        if (!url.startsWith("http") && !url.startsWith("/")) {
            url = baseUri + "/" + url;
        }
        if (url.startsWith("/")) {
            url = site + url;
        }
        String linkContent = HttpUtil.get(url);
        String path = saveDir + File.separator + url.replace(site, "");
        FileUtil.mkParentDirs(path);
        FileUtil.writeBytes(linkContent.getBytes(), path);
    }
}
