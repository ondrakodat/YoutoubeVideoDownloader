package com.kodat.urlvideomod;

import com.kodat.urlvideomod.downloader.YtDlpDownloader;
import com.kodat.urlvideomod.entity.YtDlpFileInfo;

public class YtDlpDownloaderTest {
    public static void main() {
        YtDlpDownloader downloaderTest = new YtDlpDownloader();
        YtDlpFileInfo info = downloaderTest.getFileInfo("https://www.youtube.com/watch?v=RMlit1075Jc");


        System.out.println("ID: " + info.getId());
        System.out.println("Title: " + info.getTitle());
        System.out.println("Duration: " + info.getDuration());
        System.out.println("Uploader: " + info.getUploader());
        System.out.println("Thumbnail: " + info.getThumbnail());
        System.out.println("URL: " + info.getWebPageUrl());

    }
}
