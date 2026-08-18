package com.kodat.urlvideomod.services;


import com.kodat.urlvideomod.entity.DownloadFile;
import com.kodat.urlvideomod.enums.FileStatus;
import com.kodat.urlvideomod.enums.TypeOfDownload;
import com.kodat.urlvideomod.interfaces.IDownloadFileService;
import com.kodat.urlvideomod.interfaces.IYtDlpDownloader;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DownloadFileService implements IDownloadFileService{
    private final IYtDlpDownloader downloader;
    private final Map<UUID, CompletableFuture<DownloadFile>> downloads = new ConcurrentHashMap<>();

    public DownloadFileService(IYtDlpDownloader downloader) {
        this.downloader = downloader;
    }

    @Override
    public CompletableFuture<DownloadFile> downloadFile(
            String url,
            TypeOfDownload format) {
        return CompletableFuture
                .supplyAsync(() -> downloader.download(url, format));
    }

    public UUID addDownload(CompletableFuture<DownloadFile> future){
        UUID id = UUID.randomUUID();
        downloads.put(id, future);
        return id;
    }

    public CompletableFuture<DownloadFile> getDownload(UUID id){
        return downloads.get(id);
    }

    public FileStatus getDownloadStatus(UUID id){
        CompletableFuture<DownloadFile> future = downloads.get(id);
        if(future == null)
            return FileStatus.NOT_FOUND;
        if(future.isCancelled())
            return FileStatus.CANCELED;
        if(future.isDone())
            return FileStatus.COMPLETED;
        if(future.isCompletedExceptionally())
            return FileStatus.FAILED;
        return null;
    }


}

