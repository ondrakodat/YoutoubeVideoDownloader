package com.kodat.urlvideomod.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
public class YtDlpFileInfo {
    private String id;
    private String title;
    private int duration;
    @JsonProperty("webpage_url")
    private String webPageUrl;
    private String thumbnail;
    private String uploader;

    public YtDlpFileInfo(){}

    public YtDlpFileInfo(String id, String title, int duration, String webPageUrl, String thumbnail, String uploader) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.webPageUrl = webPageUrl;
        this.thumbnail = thumbnail;
        this.uploader = uploader;
    }
}
