package com.kitisgang.lpz67.dto;
public class fileContentResponse {
    private String name;
    private String content;
    private long size;
    private long lastModified;

    public fileContentResponse(String name, String content, long size, long lastModified) {
        this.name = name;
        this.content = content;
        this.size = size;
        this.lastModified = lastModified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
}