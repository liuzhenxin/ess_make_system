package com.clt.ess.entity;

public class FileType {
    private String fileTypeValue;
    private String fileTypeName;

    public String getFileTypeValue() {
        return fileTypeValue;
    }

    public void setFileTypeValue(String fileTypeValue) {
        this.fileTypeValue = fileTypeValue;
    }

    public String getFileTypeName() {
        return fileTypeName;
    }

    public void setFileTypeName(String fileTypeName) {
        this.fileTypeName = fileTypeName;
    }

    @Override
    public String toString() {
        return "FileType{" +
                "fileTypeValue='" + fileTypeValue + '\'' +
                ", fileTypeName='" + fileTypeName + '\'' +
                '}';
    }
}
