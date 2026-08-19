package com.kodat.urlvideomod.entity;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

import com.kodat.urlvideomod.enums.TypeOfDownload;
import com.kodat.urlvideomod.enums.FileStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DownloadFile {
    private UUID id;
    private String url;
    private String fileName;
    private LocalDateTime createdAt;
    private FileStatus status;
    private String filePath;
    private TypeOfDownload format;
    private long totalBytes = 0;
    private long downloadedBytes = 0;
    private double progress = 0;

    public DownloadFile(String url, String fileName, TypeOfDownload format) {
        this.id = UUID.randomUUID();
        this.url = url;
        this.fileName = sanitizeFileName(fileName);
        this.createdAt = LocalDateTime.now();
        this.status = FileStatus.WAITING;
        this.format = format;

        String extension = switch (format) {
            case MP3 -> ".mp3";
            case MP4, MP4_SUBTITLES, MP4_SUBTITLES_AUTO -> ".mp4";
            default -> "";
        };

        this.filePath = Path.of(
                "downloads",
                this.fileName + extension
        ).toString();
    }

    private String sanitizeFileName(String fileName) {
        return fileName
                .replaceAll("[\\\\/:*?\"<>|]", "_")
                .trim();
    }
}
