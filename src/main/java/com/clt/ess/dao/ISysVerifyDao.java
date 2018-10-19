package com.clt.ess.dao;

import com.clt.ess.entity.SysVerify;

public interface ISysVerifyDao {
    SysVerify findSysVerifyById(String independentId);

    Integer updateVerifyResultByIndepdId(SysVerify sysVerify);
}
