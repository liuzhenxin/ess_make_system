package com.clt.ess.entity;

public class UnitRoleAndPowerRelation {
    private int roleAndPowerRelationId;
    private String topUnitId;
    private String roleId;
    private String powerId;


    public int getRoleAndPowerRelationId() {
        return roleAndPowerRelationId;
    }

    public void setRoleAndPowerRelationId(int roleAndPowerRelationId) {
        this.roleAndPowerRelationId = roleAndPowerRelationId;
    }

    public String getTopUnitId() {
        return topUnitId;
    }

    public void setTopUnitId(String topUnitId) {
        this.topUnitId = topUnitId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPowerId() {
        return powerId;
    }

    public void setPowerId(String powerId) {
        this.powerId = powerId;
    }

    @Override
    public String toString() {
        return "UnitRoleAndPowerRelation{" +
                "roleAndPowerRelationId=" + roleAndPowerRelationId +
                ", topUnitId='" + topUnitId + '\'' +
                ", roleId='" + roleId + '\'' +
                ", powerId='" + powerId + '\'' +
                '}';
    }
}

