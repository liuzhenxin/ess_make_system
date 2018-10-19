package com.clt.ess.entity;

public class ErrorLog {
    private int errorLogId;
    private String sysName;
    private String errorDetail;
    private String time;

    public int getErrorLogId() {
        return errorLogId;
    }

    public void setErrorLogId(int errorLogId) {
        this.errorLogId = errorLogId;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ErrorLog{" +
                "errorLogId=" + errorLogId +
                ", sysName='" + sysName + '\'' +
                ", errorDetail='" + errorDetail + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
