package com.clt.ess.entity;


import java.util.List;

public class Unit {
    private String unitId;
    private String unitName;
    private int level;
    private String parentUnitId;
    private String inputUserId;
    private int state;
    private List<Unit> menus; //子菜单列表

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getParentUnitId() {
        return parentUnitId;
    }

    public void setParentUnitId(String parentUnitId) {
        this.parentUnitId = parentUnitId;
    }

    public String getInputUserId() {
        return inputUserId;
    }

    public void setInputUserId(String inputUserId) {
        this.inputUserId = inputUserId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<Unit> getMenus() {
        return menus;
    }

    public void setMenus(List<Unit> menus) {
        this.menus = menus;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "unitId='" + unitId + '\'' +
                ", unitName='" + unitName + '\'' +
                ", level=" + level +
                ", parentUnitId='" + parentUnitId + '\'' +
                ", inputUserId='" + inputUserId + '\'' +
                ", state=" + state +
                ", menus=" + menus +
                '}';
    }
}
