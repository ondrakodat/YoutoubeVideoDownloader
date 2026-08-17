package com.kodat.urlvideomod.interfaces;
import java.util.concurrent.CompletableFuture;
import com.kodat.urlvideomod.entity.DownloadFile;
import com.kodat.urlvideomod.enums.TypeOfDownload;

public interface IDownloadFileService {
    CompletableFuture<DownloadFile> downloadFile(String url, TypeOfDownload format);
}
