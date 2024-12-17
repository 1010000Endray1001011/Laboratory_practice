package com.kitisgang.lpz67.dto;


public class fileInfoResponse {
    private String name;
    private boolean isDirectory;
    private long size;
    private long lastModified;

    public fileInfoResponse(String name, boolean isDirectory, long size, long lastModified) {
        this.name = name;
        this.isDirectory = isDirectory;
        this.size = size;
        this.lastModified = lastModified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
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


