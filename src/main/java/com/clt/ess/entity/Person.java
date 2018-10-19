package com.clt.ess.entity;

public class Person {
    private String personId;
    private String personName;
    private String phone;
    private String idNum;
    private String personImgBase64;
    private int sex;
    private int state;

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getPersonImgBase64() {
        return personImgBase64;
    }

    public void setPersonImgBase64(String personImgBase64) {
        this.personImgBase64 = personImgBase64;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Person{" +
                "personId='" + personId + '\'' +
                ", personName='" + personName + '\'' +
                ", phone='" + phone + '\'' +
                ", idNum='" + idNum + '\'' +
                ", personImgBase64='" + personImgBase64 + '\'' +
                ", sex=" + sex +
                ", state=" + state +
                '}';
    }
}
