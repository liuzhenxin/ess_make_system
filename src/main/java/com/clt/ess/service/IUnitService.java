package com.clt.ess.service;

import com.clt.ess.bean.ZTree;
import com.clt.ess.entity.IssuerUnit;
import com.clt.ess.entity.Unit;

import java.util.List;

public interface IUnitService {
    List<ZTree> queryUnitMenu(String unitId);
    Unit findUnitById(String unitId);
    Unit findTopUnitByChildUnitId(String unitId);
    Unit findTopUnit(String unitId);

    List<IssuerUnit> findIssuerUnitByUnitId(String unitId);

    Unit queryCompanyUnitByUserParentUnitId(String parentUnitId);

    String getUnitNameChain(String unitId);
}
