package com.kodat.urlvideomod.services;


import com.kodat.urlvideomod.entity.DownloadFile;
import com.kodat.urlvideomod.enums.TypeOfDownload;
import com.kodat.urlvideomod.interfaces.IDownloadFileService;
import com.kodat.urlvideomod.interfaces.IYtDlpDownloader;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class DownloadFileService implements IDownloadFileService{
    private final IYtDlpDownloader downloader;
    public DownloadFileService(IYtDlpDownloader downloader) {
        this.downloader = downloader;
    }

    @Override
    public CompletableFuture<DownloadFile> downloadFile(String url, TypeOfDownload format) {
        return CompletableFuture.supplyAsync(() -> downloader.download(url, format));
    }
}

