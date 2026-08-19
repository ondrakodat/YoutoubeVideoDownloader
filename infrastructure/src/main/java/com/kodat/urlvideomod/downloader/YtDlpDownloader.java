package com.kodat.urlvideomod.downloader;

import com.kodat.urlvideomod.entity.DownloadFile;
import com.kodat.urlvideomod.entity.YtDlpFileInfo;
import com.kodat.urlvideomod.enums.FileStatus;
import com.kodat.urlvideomod.enums.TypeOfDownload;
import com.kodat.urlvideomod.interfaces.IYtDlpDownloader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class YtDlpDownloader implements IYtDlpDownloader {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public YtDlpFileInfo getFileInfo(String url) {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "yt-dlp",
                "--dump-single-json",
                url
        );
        String processOutput = "";
        try{
            Process process = processBuilder.redirectErrorStream(true).start();
            processOutput = new String(process
                    .getInputStream()
                    .readAllBytes(), StandardCharsets.UTF_8);

            return objectMapper.readValue(processOutput, YtDlpFileInfo.class);
        } catch (IOException e) {
            throw new RuntimeException("Could not start external process yt-dlp" + e);
        }
    }

    @Override
    public DownloadFile download(DownloadFile file) {

        TypeOfDownload format = file.getFormat();
        String url = file.getUrl();

        log("DOWNLOAD", "Starting download: " + url);
        log("DOWNLOAD", "Format: " + format);
        log("DOWNLOAD", "File: " + file.getFileName());

        if (format == TypeOfDownload.LIST_OF_URLS) {
            log("DOWNLOAD", "Downloading list of URLs");
            downloadList();
            return null;
        }

        file.setStatus(FileStatus.DOWNLOADING);

        ProcessBuilder processBuilder;

        switch (format) {

            case MP3:
                processBuilder = new ProcessBuilder(
                        "yt-dlp",
                        "-x",
                        "--audio-format", "mp3",
                        "--newline",
                        "--progress",
                        "-o", file.getFilePath(),
                        url
                );
                break;

            case MP4:
                processBuilder = new ProcessBuilder(
                        "yt-dlp",
                        "-t", "mp4",
                        "--newline",
                        "--progress",
                        "-o", file.getFilePath(),
                        url
                );
                break;

            case MP4_SUBTITLES:
                processBuilder = new ProcessBuilder(
                        "yt-dlp",
                        "--write-subs",
                        "--sub-lang", "en",
                        "--embed-subs",
                        "--newline",
                        "--progress",
                        "-o", file.getFilePath(),
                        url
                );
                break;

            case MP4_SUBTITLES_AUTO:
                processBuilder = new ProcessBuilder(
                        "yt-dlp",
                        "--write-auto-subs",
                        "--newline",
                        "--progress",
                        "-o", file.getFilePath(),
                        url
                );
                break;

            default:
                throw new IllegalArgumentException(
                        "Nepodporovaný typ stahování: " + format
                );
        }

        try {

            Files.createDirectories(Path.of("downloads"));

            log("DOWNLOAD", "Starting yt-dlp process");

            Process process = processBuilder.redirectErrorStream(true).start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {

                String line;

                while ((line = reader.readLine()) != null) {

                    log("YTDLP", line);

                    updateProgress(file, line);
                }
            }

            int exitCode = process.waitFor();

            if (exitCode == 0) {

                file.setProgress(100);
                file.setStatus(FileStatus.COMPLETED);

                log(
                        "DOWNLOAD",
                        "Completed: " + file.getFilePath()
                );

            } else {

                file.setStatus(FileStatus.FAILED);

                log(
                        "DOWNLOAD",
                        "yt-dlp failed with exit code: " + exitCode
                );
            }

        } catch (IOException | InterruptedException e) {

            file.setStatus(FileStatus.FAILED);

            log(
                    "ERROR",
                    "Download failed: " + e.getMessage()
            );

            throw new RuntimeException(
                    "Process could not start",
                    e
            );
        }

        return file;
    }

    private void downloadList(){
        ProcessBuilder processBuilder = new ProcessBuilder(
                "yt-dlp",
                "-a",
                "urls.txt"

        );

        try {
            Process process = processBuilder.start();

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Videa z urls.txt byla stažena.");
            } else {
                System.out.println("yt-dlp skončilo chybou " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Process could not start", e);
        }

    }

    private void updateProgress(DownloadFile file, String line) {

        if (!line.startsWith("[download]")) {
            return;
        }

        String[] parts = line.trim().split("\\s+");

        if (parts.length < 4 || !parts[1].endsWith("%")) {
            return;
        }

        try {

            double progress = Double.parseDouble(
                    parts[1].replace("%", "")
            );

            String totalSizeText = parts[3];

            long totalBytes = parseSize(totalSizeText);

            long downloadedBytes =
                    (long) (totalBytes * (progress / 100.0));

            file.setProgress(progress);
            file.setTotalBytes(totalBytes);
            file.setDownloadedBytes(downloadedBytes);

            log(
                    "PROGRESS",
                    String.format(
                            "%.1f%% | %d / %d bytes",
                            progress,
                            downloadedBytes,
                            totalBytes
                    )
            );

        } catch (Exception e) {

            log(
                    "ERROR",
                    "Nepodařilo se parsovat progress: " + line
            );
        }
    }

    private long parseSize(String size) {

        size = size.toUpperCase();

        if (size.endsWith("KIB")) {
            double value = Double.parseDouble(
                    size.replace("KIB", "")
            );

            return (long) (value * 1024);
        }

        if (size.endsWith("MIB")) {
            double value = Double.parseDouble(
                    size.replace("MIB", "")
            );

            return (long) (value * 1024 * 1024);
        }

        if (size.endsWith("GIB")) {
            double value = Double.parseDouble(
                    size.replace("GIB", "")
            );

            return (long) (value * 1024 * 1024 * 1024);
        }

        return 0;
    }

    private void log(String type, String message) {
        System.out.printf(
                "[%s] [%s] %s%n",
                LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
                ),
                type,
                message
        );
    }
}