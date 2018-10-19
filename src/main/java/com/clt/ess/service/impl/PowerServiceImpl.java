package com.clt.ess.service.impl;

import com.clt.ess.base.Constant;
import com.clt.ess.service.IPowerService;
import com.clt.ess.service.IRoleAndPowerService;
import com.clt.ess.service.IUnitService;
import com.clt.ess.utils.PowerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class PowerServiceImpl implements IPowerService {

    @Autowired
    private IUnitService unitService;
    @Autowired
    private IRoleAndPowerService roleAndPowerService;

    @Override
    public boolean getPowerForButton(HttpSession session, String buttonCode) {

        return PowerUtil.checkUserIsHavaThisPower(session,buttonCode,roleAndPowerService,
                unitService,Constant.topUnitLevel,Constant.companyLevel);
    }
}
