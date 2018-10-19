package com.clt.ess.entity;

public class User {
    //用户id   ---- 区号+uuid
    private String userId;
    //所属部门id  如果是管理员角色可能没有部门id
    private String depId;
    //所属单位id
    private String unitId;
    //管理范围 1-6
    private int powerRange;
    //角色id
    private String roleId;
    //录入人用户id
    private String inputUserId;
    //用户层级
    private int level;
    //人员id
    private String personId;
    //是否已激活 1已激活 0未激活
    private int isActive;
    //用户状态 1有效 0无效
    private int state;

    private Person person;
    //单位
    private Unit unit;

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

    public int getPowerRange() {
        return powerRange;
    }

    public void setPowerRange(int powerRange) {
        this.powerRange = powerRange;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getInputUserId() {
        return inputUserId;
    }

    public void setInputUserId(String inputUserId) {
        this.inputUserId = inputUserId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", depId='" + depId + '\'' +
                ", unitId='" + unitId + '\'' +
                ", powerRange=" + powerRange +
                ", roleId='" + roleId + '\'' +
                ", inputUserId='" + inputUserId + '\'' +
                ", level=" + level +
                ", personId='" + personId + '\'' +
                ", isActive=" + isActive +
                ", state=" + state +
                ", person=" + person +
                ", unit=" + unit +
                '}';
    }
}
