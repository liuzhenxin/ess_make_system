package com.clt.ess.entity;

import java.util.Date;

public class SystemLog {
    private String sysLogId;
    private String powerName;
    private String logDetail;
    private Date logTime;
    private String userId;
    private String depId;
    private String unitId;
    private String safeHash;
    private int groupNum;


    public String getSysLogId() {
        return sysLogId;
    }

    public void setSysLogId(String sysLogId) {
        this.sysLogId = sysLogId;
    }

    public String getPowerName() {
        return powerName;
    }

    public void setPowerName(String powerName) {
        this.powerName = powerName;
    }

    public String getLogDetail() {
        return logDetail;
    }

    public void setLogDetail(String logDetail) {
        this.logDetail = logDetail;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDepId() {
        return depId;
    }

    public void setDepId(String depId) {
        this.depId = depId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getSafeHash() {
        return safeHash;
    }

    public void setSafeHash(String safeHash) {
        this.safeHash = safeHash;
    }

    public int getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(int groupNum) {
        this.groupNum = groupNum;
    }

    @Override
    public String toString() {
        return "SystemLog{" +
                "sysLogId='" + sysLogId + '\'' +
                ", powerName='" + powerName + '\'' +
                ", logDetail='" + logDetail + '\'' +
                ", logTime=" + logTime +
                ", userId='" + userId + '\'' +
                ", depId='" + depId + '\'' +
                ", unitId='" + unitId + '\'' +
                ", safeHash='" + safeHash + '\'' +
                ", groupNum=" + groupNum +
                '}';
    }
}
