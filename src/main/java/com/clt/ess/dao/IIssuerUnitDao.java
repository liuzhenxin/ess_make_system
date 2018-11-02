package com.clt.ess.dao;

import com.clt.ess.entity.IssuerUnit;


import java.util.List;

public interface IIssuerUnitDao {

    /**
     * 根据单位ID 在独立单位配置参数表查询可用的证书授权单位值
     * @param unitId 单位ID
     * @return 证书授权单位参数 value
     */
    String findIssuerUnitValueByUnitId(String unitId);

    IssuerUnit findIssuerUnitById(String unitId);
}
