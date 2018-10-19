package com.clt.ess.entity;

public class Seal {
    //印章id
    private String sealId;
    //印章名称
    private String sealName;
    //印章图片Id
    private String sealImgId;
    //证书id
    private String cerId;
    //录入人Id
    private String inputUserId;
    //录入时间
    private String inputTime;
    //所属单位id
    private String unitId;
    //印章类型Id
    private String sealTypeId;
    //手签人身份证号
    private String sealHwIdNum;
    //产品使用权
    private int fileTypeNum;
    //授权到期时间
    private String sealStartTime;
    //授权到期时间
    private String sealEndTime;
    //UK上的授权信息（注册的UK）
    private String authorizationTime;
    //UK上的授权信息（注册的UK）
    private String authorizationInfo;
    //UK对应的唯一Id
    private String keyId;
    //是否为UK印章 0软证书印章 1UK印章
    private int isUK;
    //印章状态 1有效 2停用 3 暂停
    private int sealState;

    private String cardType;

    private SealImg sealImg;

    private Certificate certificate;

    private Unit unit;

    private User inputUser;


    public String getSealId() {
        return sealId;
    }

    public void setSealId(String sealId) {
        this.sealId = sealId;
    }

    public String getSealName() {
        return sealName;
    }

    public void setSealName(String sealName) {
        this.sealName = sealName;
    }

    public String getSealImgId() {
        return sealImgId;
    }

    public void setSealImgId(String sealImgId) {
        this.sealImgId = sealImgId;
    }

    public String getCerId() {
        return cerId;
    }

    public void setCerId(String cerId) {
        this.cerId = cerId;
    }

    public String getInputUserId() {
        return inputUserId;
    }

    public void setInputUserId(String inputUserId) {
        this.inputUserId = inputUserId;
    }

    public String getInputTime() {
        return inputTime;
    }

    public void setInputTime(String inputTime) {
        this.inputTime = inputTime;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getSealTypeId() {
        return sealTypeId;
    }

    public void setSealTypeId(String sealTypeId) {
        this.sealTypeId = sealTypeId;
    }

    public String getSealHwIdNum() {
        return sealHwIdNum;
    }

    public void setSealHwIdNum(String sealHwIdNum) {
        this.sealHwIdNum = sealHwIdNum;
    }

    public int getFileTypeNum() {
        return fileTypeNum;
    }

    public void setFileTypeNum(int fileTypeNum) {
        this.fileTypeNum = fileTypeNum;
    }

    public String getSealStartTime() {
        return sealStartTime;
    }

    public void setSealStartTime(String sealStartTime) {
        this.sealStartTime = sealStartTime;
    }

    public String getSealEndTime() {
        return sealEndTime;
    }

    public void setSealEndTime(String sealEndTime) {
        this.sealEndTime = sealEndTime;
    }

    public String getAuthorizationTime() {
        return authorizationTime;
    }

    public void setAuthorizationTime(String authorizationTime) {
        this.authorizationTime = authorizationTime;
    }

    public String getAuthorizationInfo() {
        return authorizationInfo;
    }

    public void setAuthorizationInfo(String authorizationInfo) {
        this.authorizationInfo = authorizationInfo;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public int getIsUK() {
        return isUK;
    }

    public void setIsUK(int isUK) {
        this.isUK = isUK;
    }

    public int getSealState() {
        return sealState;
    }

    public void setSealState(int sealState) {
        this.sealState = sealState;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public SealImg getSealImg() {
        return sealImg;
    }

    public void setSealImg(SealImg sealImg) {
        this.sealImg = sealImg;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public User getInputUser() {
        return inputUser;
    }

    public void setInputUser(User inputUser) {
        this.inputUser = inputUser;
    }

    @Override
    public String toString() {
        return "Seal{" +
                "sealId='" + sealId + '\'' +
                ", sealName='" + sealName + '\'' +
                ", sealImgId='" + sealImgId + '\'' +
                ", cerId='" + cerId + '\'' +
                ", inputUserId='" + inputUserId + '\'' +
                ", inputTime='" + inputTime + '\'' +
                ", unitId='" + unitId + '\'' +
                ", sealTypeId='" + sealTypeId + '\'' +
                ", sealHwIdNum='" + sealHwIdNum + '\'' +
                ", fileTypeNum=" + fileTypeNum +
                ", sealStartTime='" + sealStartTime + '\'' +
                ", sealEndTime='" + sealEndTime + '\'' +
                ", authorizationTime='" + authorizationTime + '\'' +
                ", authorizationInfo='" + authorizationInfo + '\'' +
                ", keyId='" + keyId + '\'' +
                ", isUK=" + isUK +
                ", sealState=" + sealState +
                ", sealImg=" + sealImg +
                ", certificate=" + certificate +
                ", unit=" + unit +
                ", inputUser=" + inputUser +
                '}';
    }
}
