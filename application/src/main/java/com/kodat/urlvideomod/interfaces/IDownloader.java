package com.kodat.urlvideomod.interfaces;

import com.kodat.urlvideomod.entity.YtDlpFileInfo;

public interface IDownloader {
    YtDlpFileInfo getFileInfo(String url);
}
