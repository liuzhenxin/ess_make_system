package com.clt.ess.entity;

public class Certificate {
    //证书id
    private String certificateId;
    //证书名称
    private String cerName;
    //签名算法
    private String algorithm;
    //证书种类
    private String cerClass;
    //颁发者
    private String issuer;
    //颁发者
    private String issuerUnitId;
    //证书所有人
    private String country;
    //证书版本
    private String certificateVersion;
    //省
    private String province;
    //市
    private String city;
    //单位
    private String certUnit;
    //部门
    private String certDepartment;
    //Cer证书base64
    private String cerBase64;
    //pfx证书base64
    private String pfxBase64;
    //有效期开始时间
    private String startTime;
    //有效期结束时间
    private String endTime;
    //证书申请时间
    private String ApplyTime;
    //证书密钥
    private String cerPsw;
    //证书文件状态 0 证书未生成 1 cer  2 pfx  3  都有
    private int fileState;
    //状态 0无效1停用
    private int state;

    private IssuerUnit issuerUnit;

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public String getCerName() {
        return cerName;
    }

    public void setCerName(String cerName) {
        this.cerName = cerName;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getCerClass() {
        return cerClass;
    }

    public void setCerClass(String cerClass) {
        this.cerClass = cerClass;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getIssuerUnitId() {
        return issuerUnitId;
    }

    public void setIssuerUnitId(String issuerUnitId) {
        this.issuerUnitId = issuerUnitId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCertificateVersion() {
        return certificateVersion;
    }

    public void setCertificateVersion(String certificateVersion) {
        this.certificateVersion = certificateVersion;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCertUnit() {
        return certUnit;
    }

    public void setCertUnit(String certUnit) {
        this.certUnit = certUnit;
    }

    public String getCertDepartment() {
        return certDepartment;
    }

    public void setCertDepartment(String certDepartment) {
        this.certDepartment = certDepartment;
    }

    public String getCerBase64() {
        return cerBase64;
    }

    public void setCerBase64(String cerBase64) {
        this.cerBase64 = cerBase64;
    }

    public String getPfxBase64() {
        return pfxBase64;
    }

    public void setPfxBase64(String pfxBase64) {
        this.pfxBase64 = pfxBase64;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getApplyTime() {
        return ApplyTime;
    }

    public void setApplyTime(String applyTime) {
        ApplyTime = applyTime;
    }

    public String getCerPsw() {
        return cerPsw;
    }

    public void setCerPsw(String cerPsw) {
        this.cerPsw = cerPsw;
    }

    public int getFileState() {
        return fileState;
    }

    public void setFileState(int fileState) {
        this.fileState = fileState;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public IssuerUnit getIssuerUnit() {
        return issuerUnit;
    }

    public void setIssuerUnit(IssuerUnit issuerUnit) {
        this.issuerUnit = issuerUnit;
    }

    @Override
    public String toString() {
        return "Certificate{" +
                "certificateId='" + certificateId + '\'' +
                ", cerName='" + cerName + '\'' +
                ", algorithm='" + algorithm + '\'' +
                ", cerClass='" + cerClass + '\'' +
                ", issuer='" + issuer + '\'' +
                ", issuerUnitId='" + issuerUnitId + '\'' +
                ", country='" + country + '\'' +
                ", certificateVersion='" + certificateVersion + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", certUnit='" + certUnit + '\'' +
                ", certDepartment='" + certDepartment + '\'' +
                ", cerBase64='" + cerBase64 + '\'' +
                ", pfxBase64='" + pfxBase64 + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", ApplyTime='" + ApplyTime + '\'' +
                ", cerPsw='" + cerPsw + '\'' +
                ", fileState=" + fileState +
                ", state=" + state +
                ", issuerUnit=" + issuerUnit +
                '}';
    }
}
