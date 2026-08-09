    package com.kodat.urlvideomod.downloader;

    import com.kodat.urlvideomod.entity.DownloadFile;
    import com.kodat.urlvideomod.entity.YtDlpFileInfo;
    import com.kodat.urlvideomod.enums.TypeOfDownload;
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

        @Override
        public void download(String url, TypeOfDownload format) {
            if (format == TypeOfDownload.LIST_OF_URLS) {
                downloadList();
                return;
            }

            YtDlpFileInfo info = getFileInfo(url);

            DownloadFile file = new DownloadFile(
                    url,
                    info.getTitle(),
                    format
            );

            ProcessBuilder processBuilder = null;
            switch (format){
                case TypeOfDownload.MP3:
                    processBuilder = new ProcessBuilder(
                            "yt-dlp",
                            "-x",
                            "--audio-format", "mp3",
                            url
                    );
                    break;
                case TypeOfDownload.MP4:
                    processBuilder = new ProcessBuilder(
                            "yt-dlp",
                            "-f", "mp4",
                            url
                    );
                    break;
                case TypeOfDownload.MP4_SUBTITLES:
                    processBuilder = new ProcessBuilder(
                            "yt-dlp",
                            "--write-subs",
                            "--sub-lang", "en",
                            "--embed-subs",
                            url
                    );
                    break;
                case TypeOfDownload.MP4_SUBTITLES_AUTO:
                        processBuilder = new ProcessBuilder(
                                "yt-dlp",
                                "--write-auto-subs",
                                url
                        );
                    break;
                default:
                    throw new IllegalArgumentException(
                            "Nepodporovaný typ stahování: " + format
                    );
            }
            try{
                Process process = processBuilder.start();
                int exitCode = process.waitFor();
                if(exitCode == 0)
                System.out.println(file.getFileName() + " byl stažen do " + file.getFilePath());
                else
                System.out.println("yt-dlp skončilo chybou " + exitCode);
            }catch (IOException | InterruptedException e){
                throw new RuntimeException("Process could not start" + e);
            }
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

            return;
        }
        }

    }
