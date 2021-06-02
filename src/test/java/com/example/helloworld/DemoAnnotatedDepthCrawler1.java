package com.example.helloworld;


import cn.edu.hfut.dmic.webcollector.fetcher.Visitor.AfterParse;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.rocks.BreadthCrawler;

public class DemoAnnotatedDepthCrawler1 extends BreadthCrawler {
    public DemoAnnotatedDepthCrawler1(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);

        for (int i = 1; i <= 5; ++i) {
            this.addSeed((new CrawlDatum("http://news.hfut.edu.cn/list-1-" + i + ".html")).meta("depth", 1));
        }

        this.addRegex("http://news.hfut.edu.cn/show-.*html");
        this.addRegex("-.*\\.(jpg|png|gif).*");
        this.addRegex("-.*#.*");
    }

    public void visit(Page page, CrawlDatums next) {
        System.out.println("visiting:" + page.url() + "\tdepth=" + page.meta("depth"));
    }

    @AfterParse
    public void afterParse(Page page, CrawlDatums next) {
        int depth = 1;

        try {
            depth = page.metaAsInt("depth");
        } catch (Exception var5) {
        }

        ++depth;
        next.meta("depth", depth);
    }

    public static void main(String[] args) throws Exception {
        DemoAnnotatedDepthCrawler1 crawler = new DemoAnnotatedDepthCrawler1("crawl", true);
//        crawler.executor(".*", HtmlExtractor.class, new ExtractorParams("fsOutput", fsOutput));
        crawler.getConf().setTopN(5);
        crawler.start(3);
    }
}
