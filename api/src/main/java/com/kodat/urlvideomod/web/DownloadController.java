package com.kodat.urlvideomod.web;

import com.kodat.urlvideomod.entity.DownloadFile;
import com.kodat.urlvideomod.enums.FileStatus;
import com.kodat.urlvideomod.enums.TypeOfDownload;
import com.kodat.urlvideomod.interfaces.IDownloadFileService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/download")
public class DownloadController {
    private final IDownloadFileService downloadFileService;

    public DownloadController(IDownloadFileService downloadFileService){
        this.downloadFileService = downloadFileService;
    }

    @PostMapping
    public UUID download(
            @RequestParam String url,
            @RequestParam TypeOfDownload type
    ){
        return downloadFileService.startDownload(url, type);
    }

    @GetMapping("/{id}")
    public DownloadFile checkDownloading(
            @PathVariable UUID id
    ){
        return downloadFileService.getDownload(id);
    }

    @GetMapping("/{id}/status")
    public FileStatus getFileStatus(
            @PathVariable UUID id
    ){
        return downloadFileService.getDownloadStatus(id);
    }
}
