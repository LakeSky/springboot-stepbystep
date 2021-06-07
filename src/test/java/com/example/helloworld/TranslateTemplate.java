package com.example.helloworld;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;

/**
 * Created by gang on 2021/6/3.
 */
public class TranslateTemplate {
    public void trans() throws Exception {
        //想着是国外的模板MIT协议的，翻译过来相当于改造一下，再发出去，
        //https://www.jianshu.com/p/e0c2dc1ce636 这个是有人用js实现的替换
        Document doc = Jsoup.parse(new File(""), "utf-8");

    }
}
