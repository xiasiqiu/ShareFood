package com.xinyuan.xyorder.common.bean;

import java.io.Serializable;

/**
 * Created by f-x on 2017/11/3017:48
 */

public class UploadFileBean  implements Serializable{
    private static final long serialVersionUID = -1307106642339356192L;
    private String contentType;
    private long fileSize;
    private String originalUrl;
    private String thumbnailUrl;
    private String watermarkUrll;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getWatermarkUrll() {
        return watermarkUrll;
    }

    public void setWatermarkUrll(String watermarkUrll) {
        this.watermarkUrll = watermarkUrll;
    }

    @Override
    public String toString() {
        return "UploadFileBean{" +
                "contentType='" + contentType + '\'' +
                ", fileSize=" + fileSize +
                ", originalUrl='" + originalUrl + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", watermarkUrll='" + watermarkUrll + '\'' +
                '}';
    }
}
