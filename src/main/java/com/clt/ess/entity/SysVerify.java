package com.clt.ess.entity;

public class SysVerify {

	// 独立单位Id，有一级单位写一级单位id，没有则写起本身的单位Id
	private String independentId;
	
	private String unitName;
	
	private String serverIp;
	
	// uk公章数量
	private Integer ukSealJurSize;
	
	// 移动公章数量
	private Integer mobileSealJurSize;
	
	// uk手签数量
	private Integer ukHwJurSize;
	
	// 移动手签数量
	private Integer mobileHwJurSize;
	//授权编码编码
	private String jurProductCode;
	
	private String dueTime;
	
	private String signValue;
	
	private String verifyResult;
	
	
	
	public String getIndependentId() {
		return independentId;
	}
	public void setIndependentId(String independentId) {
		this.independentId = independentId;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public Integer getUkSealJurSize() {
		return ukSealJurSize;
	}
	public void setUkSealJurSize(Integer ukSealJurSize) {
		this.ukSealJurSize = ukSealJurSize;
	}
	public Integer getMobileSealJurSize() {
		return mobileSealJurSize;
	}
	public void setMobileSealJurSize(Integer mobileSealJurSize) {
		this.mobileSealJurSize = mobileSealJurSize;
	}
	public Integer getUkHwJurSize() {
		return ukHwJurSize;
	}
	public void setUkHwJurSize(Integer ukHwJurSize) {
		this.ukHwJurSize = ukHwJurSize;
	}
	public Integer getMobileHwJurSize() {
		return mobileHwJurSize;
	}
	public void setMobileHwJurSize(Integer mobileHwJurSize) {
		this.mobileHwJurSize = mobileHwJurSize;
	}
	public String getJurProductCode() {
		return jurProductCode;
	}
	public void setJurProductCode(String jurProductCode) {
		this.jurProductCode = jurProductCode;
	}
	public String getDueTime() {
		return dueTime;
	}
	public void setDueTime(String dueTime) {
		this.dueTime = dueTime;
	}
	public String getSignValue() {
		return signValue;
	}
	public void setSignValue(String signValue) {
		this.signValue = signValue;
	}
	public String getVerifyResult() {
		return verifyResult;
	}
	public void setVerifyResult(String verifyResult) {
		this.verifyResult = verifyResult;
	}

	@Override
	public String toString() {
		return "SysVerify{" +
				"independentId='" + independentId + '\'' +
				", unitName='" + unitName + '\'' +
				", serverIp='" + serverIp + '\'' +
				", ukSealJurSize=" + ukSealJurSize +
				", mobileSealJurSize=" + mobileSealJurSize +
				", ukHwJurSize=" + ukHwJurSize +
				", mobileHwJurSize=" + mobileHwJurSize +
				", jurProductCode='" + jurProductCode + '\'' +
				", dueTime='" + dueTime + '\'' +
				", signValue='" + signValue + '\'' +
				", verifyResult='" + verifyResult + '\'' +
				'}';
	}
}
