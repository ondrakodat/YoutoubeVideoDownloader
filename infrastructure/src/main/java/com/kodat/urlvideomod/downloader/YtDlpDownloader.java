    package com.kodat.urlvideomod.downloader;

    import com.kodat.urlvideomod.entity.YtDlpFileInfo;
    import com.kodat.urlvideomod.interfaces.IDownloader;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import java.io.IOException;
    import java.nio.charset.StandardCharsets;


    public class YtDlpDownloader implements IDownloader {
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
               Process process = processBuilder.start();
               processOutput = new String(process
                       .getInputStream()
                       .readAllBytes(), StandardCharsets.UTF_8);

               return objectMapper.readValue(processOutput, YtDlpFileInfo.class);
           } catch (IOException e) {
               throw new RuntimeException("Could not start external process yt-dlp" + e);
           }
        }
    }
