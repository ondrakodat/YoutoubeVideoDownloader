package com.kodat.urlvideomod.entity;
import java.time.LocalDateTime;
import java.util.UUID;

import com.kodat.urlvideomod.enums.FileFormat;
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
    private FileFormat format;

    public DownloadFile(String url, String fileName, FileFormat format) {
        this.id = UUID.randomUUID();
        this.url = url;
        this.fileName = fileName;
        this.createdAt = LocalDateTime.now();
        this.status = FileStatus.WAITING;
        this.format = format;
        this.filePath = null;
    }
}
