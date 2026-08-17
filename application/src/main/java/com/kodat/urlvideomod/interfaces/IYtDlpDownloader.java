package com.kodat.urlvideomod.interfaces;

import com.kodat.urlvideomod.entity.DownloadFile;
import com.kodat.urlvideomod.entity.YtDlpFileInfo;
import com.kodat.urlvideomod.enums.TypeOfDownload;

public interface IDownloader {
    YtDlpFileInfo getFileInfo(String url);
    DownloadFile download (String url, TypeOfDownload format);
}
