package com.kodat.urlvideomod.interfaces;

import com.kodat.urlvideomod.entity.DownloadFile;
import com.kodat.urlvideomod.entity.YtDlpFileInfo;
import com.kodat.urlvideomod.enums.TypeOfDownload;

import java.util.UUID;


public interface IYtDlpDownloader {
    YtDlpFileInfo getFileInfo(String url);
    DownloadFile download (DownloadFile file);
}
