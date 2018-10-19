package com.clt.ess.service.impl;


import com.clt.ess.dao.IUnitRoleAndPowerRelationDao;
import com.clt.ess.entity.UnitRoleAndPowerRelation;
import com.clt.ess.service.IRoleAndPowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleAndPowerServiceImpl implements IRoleAndPowerService {
    @Autowired
    private IUnitRoleAndPowerRelationDao unitRoleAndPowerRelationDao;
    @Override
    public UnitRoleAndPowerRelation queryByRoleIdAndPowerIdAndTopUnitId(String roleId, String powerId, String unitId) {

        UnitRoleAndPowerRelation unitRoleAndPowerRelation = new UnitRoleAndPowerRelation();
        unitRoleAndPowerRelation.setRoleId(roleId);
        unitRoleAndPowerRelation.setPowerId(powerId);
        unitRoleAndPowerRelation.setTopUnitId(unitId);

        return unitRoleAndPowerRelationDao.findUnitRoleAndPowerRelation(unitRoleAndPowerRelation);
    }
}
