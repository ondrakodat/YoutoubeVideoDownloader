package com.kodat.urlvideomod.interfaces;
import java.util.concurrent.CompletableFuture;
import com.kodat.urlvideomod.entity.DownloadFile;
import com.kodat.urlvideomod.enums.FileFormat;

public interface IDownloadFileService {
    CompletableFuture<DownloadFile> DownloadFile(String url, FileFormat format );
}
