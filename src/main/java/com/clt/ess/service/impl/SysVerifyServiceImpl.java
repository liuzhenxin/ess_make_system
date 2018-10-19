package com.clt.ess.service.impl;

import com.clt.ess.dao.ISysVerifyDao;
import com.clt.ess.entity.SysVerify;
import com.clt.ess.service.ISysVerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysVerifyServiceImpl implements ISysVerifyService {

    @Autowired
    ISysVerifyDao sysVerifyDao;
    @Override
    public SysVerify findSysVerifyById(String independentId) {
        return sysVerifyDao.findSysVerifyById(independentId);
    }

    @Override
    public Integer updateVerifyResultByIndepdId(SysVerify sysVerify) {
        return sysVerifyDao.updateVerifyResultByIndepdId(sysVerify);
    }
}
