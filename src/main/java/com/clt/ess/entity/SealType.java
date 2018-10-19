package com.clt.ess.entity;

public class SealType {
    private String sealTypeId;
    private String sealTypeName;
    private String sealTypeNum;
    private String topUnitId;

    public String getSealTypeId() {
        return sealTypeId;
    }

    public void setSealTypeId(String sealTypeId) {
        this.sealTypeId = sealTypeId;
    }

    public String getSealTypeName() {
        return sealTypeName;
    }

    public void setSealTypeName(String sealTypeName) {
        this.sealTypeName = sealTypeName;
    }

    public String getSealTypeNum() {
        return sealTypeNum;
    }

    public void setSealTypeNum(String sealTypeNum) {
        this.sealTypeNum = sealTypeNum;
    }

    public String getTopUnitId() {
        return topUnitId;
    }

    public void setTopUnitId(String topUnitId) {
        this.topUnitId = topUnitId;
    }

    @Override
    public String toString() {
        return "SealType{" +
                "sealTypeId='" + sealTypeId + '\'' +
                ", sealTypeName='" + sealTypeName + '\'' +
                ", sealTypeNum='" + sealTypeNum + '\'' +
                ", topUnitId='" + topUnitId + '\'' +
                '}';
    }
}
