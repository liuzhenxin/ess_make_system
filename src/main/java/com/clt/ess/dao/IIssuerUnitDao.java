package com.clt.ess.dao;

import com.clt.ess.entity.IssuerUnit;


import java.util.List;

public interface IIssuerUnitDao {
    String findIssuerUnitValueByUnitId(String unitId);

    IssuerUnit findIssuerUnitById(String unitId);
}
