package com.clt.ess.service;

import com.clt.ess.entity.SysVerify;

public interface ISysVerifyService {
    SysVerify findSysVerifyById(String unitId);

    Integer updateVerifyResultByIndepdId(SysVerify sysVerify);
}
