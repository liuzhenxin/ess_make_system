package com.clt.ess.dao;


import com.clt.ess.entity.Unit;

import java.util.List;

public interface IUnitDao {

    Unit findUnitByUnitId(String unitId);


    List<Unit> findUnitByParentUnitId(String parentUnitId);
}
