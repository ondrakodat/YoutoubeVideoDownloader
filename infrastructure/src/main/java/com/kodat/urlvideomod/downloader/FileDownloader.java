package com.kodat.urlvideomod.downloader;

import com.kodat.urlvideomod.entity.DownloadFile;
import com.kodat.urlvideomod.interfaces.IFileDownloader;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.concurrent.CompletableFuture;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Files;


public class FileDownloader implements IFileDownloader {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    @Override
    public CompletableFuture<DownloadFile> download(DownloadFile file) {
        return CompletableFuture.supplyAsync(() -> {

            try{
                URI uri = URI.create(file.getUrl());
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(uri)
                        .GET()
                        .build();
                HttpResponse<byte[]> response = httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofByteArray()
                );

                Path path = Path.of("downloads", file.getFileName());

                Files.write(path, response.body());

                file.setFilePath(path.toAbsolutePath().toString());

                return file;
            }catch(IOException | InterruptedException e){

                throw new RuntimeException("Download Failed", e);
            }
            }
        );
    }
}
