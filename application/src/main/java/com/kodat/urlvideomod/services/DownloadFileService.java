package com.kodat.urlvideomod.services;

import com.kodat.urlvideomod.entity.DownloadFile;
import com.kodat.urlvideomod.enums.TypeOfDownload;
import com.kodat.urlvideomod.interfaces.IDownloadFileService;

import java.util.concurrent.CompletableFuture;


public class DownloadFileService implements IDownloadFileService{
    @Override
    public CompletableFuture<DownloadFile> DownloadFile(String url, TypeOfDownload format) {
        return null;
    }
}

