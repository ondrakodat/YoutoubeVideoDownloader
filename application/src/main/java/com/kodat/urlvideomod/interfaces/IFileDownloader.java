package com.kodat.urlvideomod.interfaces;

import com.kodat.urlvideomod.entity.DownloadFile;

import java.util.concurrent.CompletableFuture;

public interface IFileDownloader {
    CompletableFuture<DownloadFile> download (DownloadFile file);
}
