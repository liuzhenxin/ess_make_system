package com.clt.ess.dao;

import com.clt.ess.entity.Seal;
import com.clt.ess.entity.SealType;

import java.util.List;

public interface ISealDao {
    int addSeal(Seal seal);
    int updateSeal(Seal seal);
    int delSeal(Seal seal);
    List<Seal> findSealList(Seal seal);
    List<SealType> findSealType(SealType sealType);

    List<Seal> findSealListByIdNums(List<String> personIdNums);

    Seal findSealById(String sealId);

    int findSealCountByUnitId(String unitId);
}
