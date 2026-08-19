package com.kodat.urlvideomod.services;

import com.kodat.urlvideomod.entity.DownloadFile;
import com.kodat.urlvideomod.entity.YtDlpFileInfo;
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
public class DownloadFileService implements IDownloadFileService {

    private final IYtDlpDownloader downloader;

    private final Map<UUID, DownloadFile> downloads =
            new ConcurrentHashMap<>();

    public DownloadFileService(IYtDlpDownloader downloader) {
        this.downloader = downloader;
    }

    @Override
    public UUID startDownload(String url, TypeOfDownload type) {

        // Zatím vytvoříme soubor bez názvu z YouTube
        DownloadFile file = new DownloadFile(
                url,
                "download_" + UUID.randomUUID(),
                type
        );

        downloads.put(file.getId(), file);

        CompletableFuture.runAsync(() -> {

            try {

                file.setStatus(FileStatus.DOWNLOADING);

                // Získáme informace o videu
                YtDlpFileInfo info = downloader.getFileInfo(url);

                file.setFileName(info.getTitle());

                // download začne až zde
                downloader.download(file);

            } catch (Exception e) {

                file.setStatus(FileStatus.FAILED);

                System.out.println(
                        "Download failed: " + e.getMessage()
                );
            }

        });

        return file.getId();
    }

    @Override
    public DownloadFile getDownload(UUID id) {
        return downloads.get(id);
    }

    public FileStatus getDownloadStatus(UUID id) {

        DownloadFile file = downloads.get(id);

        if (file == null) {
            return FileStatus.NOT_FOUND;
        }

        return file.getStatus();
    }

    public DownloadFile getDownloadInfo(UUID id) {

        return downloads.get(id);
    }
}