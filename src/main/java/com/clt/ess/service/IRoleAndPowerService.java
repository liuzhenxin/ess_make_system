package com.clt.ess.service;

import com.clt.ess.entity.UnitRoleAndPowerRelation;

public interface IRoleAndPowerService {

    UnitRoleAndPowerRelation queryByRoleIdAndPowerIdAndTopUnitId(String roleId, String powerId, String unitId);
}
