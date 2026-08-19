package com.kodat.urlvideomod.interfaces;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import com.kodat.urlvideomod.entity.DownloadFile;
import com.kodat.urlvideomod.enums.FileStatus;
import com.kodat.urlvideomod.enums.TypeOfDownload;

public interface IDownloadFileService {
//    CompletableFuture<DownloadFile> downloadFile(String url, TypeOfDownload format);
public DownloadFile getDownload(UUID id);
    public FileStatus getDownloadStatus(UUID id);
    UUID startDownload(String url, TypeOfDownload type);
    public DownloadFile getDownloadInfo(UUID id);

}
