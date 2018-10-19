package com.clt.ess.dao;


import com.clt.ess.entity.SealApply;

import java.util.List;

public interface ISealApplyDao {
    int addSealApply(SealApply sealApply);

    List<SealApply> findSealApply(SealApply sealApply);

    int delSealApply(SealApply sealApply);

    int updateSealApply(SealApply sealApply);

    SealApply findSealApplyById(String sealApplyId);

//    int updateSealApply(SealApply sealApply);
//    int deleteSealApply(SealApply sealApply);
//    SealApply selectSealApply(SealApply sealApply);
//    List<SealApply> selectSealApplyList(SealApply sealApply);
}
