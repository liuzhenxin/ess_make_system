package com.clt.ess.entity;

import java.sql.Blob;

public class SealImg {
    //印章图片id
    private String sealImgId;

    //jpg图片
    private Object sealImgJpg;
    //印章图片base64
    private String sealImgGifBase64;
    //Clt文件
    private Object sealImgClt;

    //印章图片hash
    private String sealImgHash;
    //缩略图base64
    private String sealThumbnailImgBase64;
    //图片上传方式 0UK读取，1用户上传
    private int seaIImageUploadType;


    public String getSealImgId() {
        return sealImgId;
    }

    public void setSealImgId(String sealImgId) {
        this.sealImgId = sealImgId;
    }

    public Object getSealImgJpg() {
        return sealImgJpg;
    }

    public void setSealImgJpg(Object sealImgJpg) {
        this.sealImgJpg = sealImgJpg;
    }

    public String getSealImgGifBase64() {
        return sealImgGifBase64;
    }

    public void setSealImgGifBase64(String sealImgGifBase64) {
        this.sealImgGifBase64 = sealImgGifBase64;
    }

    public Object getSealImgClt() {
        return sealImgClt;
    }

    public void setSealImgClt(Object sealImgClt) {
        this.sealImgClt = sealImgClt;
    }

    public String getSealImgHash() {
        return sealImgHash;
    }

    public void setSealImgHash(String sealImgHash) {
        this.sealImgHash = sealImgHash;
    }

    public String getSealThumbnailImgBase64() {
        return sealThumbnailImgBase64;
    }

    public void setSealThumbnailImgBase64(String sealThumbnailImgBase64) {
        this.sealThumbnailImgBase64 = sealThumbnailImgBase64;
    }

    public int getSeaIImageUploadType() {
        return seaIImageUploadType;
    }

    public void setSeaIImageUploadType(int seaIImageUploadType) {
        this.seaIImageUploadType = seaIImageUploadType;
    }

    @Override
    public String toString() {
        return "SealImg{" +
                "sealImgId='" + sealImgId + '\'' +
                ", sealImgJpg=" + sealImgJpg +
                ", sealImgGifBase64='" + sealImgGifBase64 + '\'' +
                ", sealImgClt=" + sealImgClt +
                ", sealImgHash='" + sealImgHash + '\'' +
                ", sealThumbnailImgBase64='" + sealThumbnailImgBase64 + '\'' +
                ", seaIImageUploadType=" + seaIImageUploadType +
                '}';
    }
}
