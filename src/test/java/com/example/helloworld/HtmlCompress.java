package com.example.helloworld;

import cn.hutool.core.io.FileUtil;
import com.googlecode.htmlcompressor.compressor.HtmlCompressor;
import org.junit.Test;

/**
 * Created by gang on 2021/5/31.
 */
public class HtmlCompress {
    @Test
    public void display() {
        System.out.println("helloworld");
    }

    @Test
    public void compress() {
        String baseDir = "F:\\audit\\";
        String src = baseDir + "tagcloud.html";
        String dest = baseDir + "index.html";
        String htmlStr = FileUtil.readString(src, "utf-8");
        System.out.println(htmlStr);
        String compressStr = htmlCompress(htmlStr);
        System.out.println(compressStr);
        FileUtil.writeBytes(compressStr.getBytes(), dest);
    }

    private static String htmlCompress(String text) {
        HtmlCompressor compressor = new HtmlCompressor();
        compressor.setEnabled(true);
        compressor.setCompressCss(true);
        compressor.setYuiJsPreserveAllSemiColons(true);
        compressor.setYuiJsLineBreak(1);
        compressor.setPreserveLineBreaks(false);
        compressor.setRemoveIntertagSpaces(true);
        compressor.setRemoveComments(true);
        compressor.setRemoveMultiSpaces(true);
        compressor.setCompressJavaScript(true);
        return compressor.compress(text);
    }
}
