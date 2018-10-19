package com.clt.ess.entity;

public class IndependentUnitConfig {
    private String independentUnitId;
    private int num;
    private String value;

    public String getIndependentUnitId() {
        return independentUnitId;
    }

    public void setIndependentUnitId(String independentUnitId) {
        this.independentUnitId = independentUnitId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "IndependentUnitConfig{" +
                "independentUnitId='" + independentUnitId + '\'' +
                ", num=" + num +
                ", value='" + value + '\'' +
                '}';
    }
}
