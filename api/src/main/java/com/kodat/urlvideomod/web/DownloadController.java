package com.kodat.urlvideomod.web;

import com.kodat.urlvideomod.downloader.YtDlpDownloader;
import org.springframework.web.bind.annotation.*;

public class DownloadController {
    private final YtDlpDownloader downloader;
    public DownloadController(YtDlpDownloader downloader){
        this.downloader = downloader;
    }
}
