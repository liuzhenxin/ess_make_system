package com.clt.ess.entity;


/**
 * 申请审核制作信息
 */
public class SealApply {
    //申请信息ID
    private String sealApplyId;
    //申请信息类别 0 申请新印章 1注册已有UK 2印章授权延期，3印章证书延期 4印章重做
    private int applyType;
    //印章ID
    private String sealId;
    //印章名称
    private String sealName;
    //图片Id
    private String sealImgId;
    //证书Id
    private String certificateId;
    //单位ID
    private String unitId;
    //印章类型Id
    private String sealTypeId;
    //是否UK印章 0否 1是
    private int isUK;
    //证书颁发机构
    private String cerIssuer;
    //产品使用权
    private int fileTypeNum;
    //keyId
    private String keyId;
    //是否将图片写入UK 0 否 1 是
    private int isImageImport;
    //是否将授权信息写入UK 0 否 1 是
    private int isAuthorizedImport;
    //印章开始时间
    private String sealStartTime;
    //印章到期时间
    private String sealEndTime;
    //UK的到期时间
    private String authorizationTime;
    //UK的授权信息
    private String authorizationInfo;
    //印章掌控人
    private String controlUserId;
    //手签对应的身份证号
    private String sealHwUserIdNum;
    //申请人
    private String applyUserId;
    //申请时间
    private String applyTime;
    //审核人
    private String reviewUserId;
    //审核时间
    private String reviewTime;
    //制作人
    private String makeUserId;
    //制作时间
    private String makeTime;
    //申请信息状态 0 提交申请，1审核通过，2审核未通过，3证书制作中，4证书申请失败，5UK待制作，6制作完成，7UK预申请
    private int applyState;

    //UK类型
    private String cardType;
    //附件名字
    private String attachment;

    private SealImg sealImg;

    private Certificate certificate;

    private Unit unit;

    private User applyUser;

    private User reviewUser;

    private User makeUser;

    public String getSealApplyId() {
        return sealApplyId;
    }

    public void setSealApplyId(String sealApplyId) {
        this.sealApplyId = sealApplyId;
    }

    public int getApplyType() {
        return applyType;
    }

    public void setApplyType(int applyType) {
        this.applyType = applyType;
    }

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

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
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

    public int getIsUK() {
        return isUK;
    }

    public void setIsUK(int isUK) {
        this.isUK = isUK;
    }

    public String getCerIssuer() {
        return cerIssuer;
    }

    public void setCerIssuer(String cerIssuer) {
        this.cerIssuer = cerIssuer;
    }

    public int getFileTypeNum() {
        return fileTypeNum;
    }

    public void setFileTypeNum(int fileTypeNum) {
        this.fileTypeNum = fileTypeNum;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public int getIsImageImport() {
        return isImageImport;
    }

    public void setIsImageImport(int isImageImport) {
        this.isImageImport = isImageImport;
    }

    public int getIsAuthorizedImport() {
        return isAuthorizedImport;
    }

    public void setIsAuthorizedImport(int isAuthorizedImport) {
        this.isAuthorizedImport = isAuthorizedImport;
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

    public String getControlUserId() {
        return controlUserId;
    }

    public void setControlUserId(String controlUserId) {
        this.controlUserId = controlUserId;
    }

    public String getSealHwUserIdNum() {
        return sealHwUserIdNum;
    }

    public void setSealHwUserIdNum(String sealHwUserIdNum) {
        this.sealHwUserIdNum = sealHwUserIdNum;
    }

    public String getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(String applyUserId) {
        this.applyUserId = applyUserId;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getReviewUserId() {
        return reviewUserId;
    }

    public void setReviewUserId(String reviewUserId) {
        this.reviewUserId = reviewUserId;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(String reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getMakeUserId() {
        return makeUserId;
    }

    public void setMakeUserId(String makeUserId) {
        this.makeUserId = makeUserId;
    }

    public String getMakeTime() {
        return makeTime;
    }

    public void setMakeTime(String makeTime) {
        this.makeTime = makeTime;
    }

    public int getApplyState() {
        return applyState;
    }

    public void setApplyState(int applyState) {
        this.applyState = applyState;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
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

    public User getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(User applyUser) {
        this.applyUser = applyUser;
    }

    public User getReviewUser() {
        return reviewUser;
    }

    public void setReviewUser(User reviewUser) {
        this.reviewUser = reviewUser;
    }

    public User getMakeUser() {
        return makeUser;
    }

    public void setMakeUser(User makeUser) {
        this.makeUser = makeUser;
    }

    @Override
    public String toString() {
        return "SealApply{" +
                "sealApplyId='" + sealApplyId + '\'' +
                ", applyType=" + applyType +
                ", sealId='" + sealId + '\'' +
                ", sealName='" + sealName + '\'' +
                ", sealImgId='" + sealImgId + '\'' +
                ", certificateId='" + certificateId + '\'' +
                ", unitId='" + unitId + '\'' +
                ", sealTypeId='" + sealTypeId + '\'' +
                ", isUK=" + isUK +
                ", cerIssuer='" + cerIssuer + '\'' +
                ", fileTypeNum=" + fileTypeNum +
                ", keyId='" + keyId + '\'' +
                ", isImageImport=" + isImageImport +
                ", isAuthorizedImport=" + isAuthorizedImport +
                ", sealStartTime='" + sealStartTime + '\'' +
                ", sealEndTime='" + sealEndTime + '\'' +
                ", authorizationTime='" + authorizationTime + '\'' +
                ", authorizationInfo='" + authorizationInfo + '\'' +
                ", controlUserId='" + controlUserId + '\'' +
                ", sealHwUserIdNum='" + sealHwUserIdNum + '\'' +
                ", applyUserId='" + applyUserId + '\'' +
                ", applyTime='" + applyTime + '\'' +
                ", reviewUserId='" + reviewUserId + '\'' +
                ", reviewTime='" + reviewTime + '\'' +
                ", makeUserId='" + makeUserId + '\'' +
                ", makeTime='" + makeTime + '\'' +
                ", applyState=" + applyState +
                ", attachment='" + attachment + '\'' +
                ", sealImg=" + sealImg +
                ", certificate=" + certificate +
                ", unit=" + unit +
                ", applyUser=" + applyUser +
                ", reviewUser=" + reviewUser +
                ", makeUser=" + makeUser +
                '}';
    }
}
