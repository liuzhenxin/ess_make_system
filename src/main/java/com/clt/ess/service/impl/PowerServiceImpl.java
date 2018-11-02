package com.clt.ess.service.impl;

import com.clt.ess.base.Constant;
import com.clt.ess.entity.Unit;
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
    /**
     * 获取当前登录用户是否拥有buttonCode的使用权限
     * @param session 当前session
     * @param buttonCode 权限代码
     * @return 是否拥有权限
     */
    @Override
    public boolean getPowerForButton(HttpSession session, String buttonCode) {

        return PowerUtil.checkUserIsHavaThisPower(session,buttonCode,roleAndPowerService,
                unitService,Constant.topUnitLevel,Constant.companyLevel);
    }
    /**
     * 检查当前登录用户是否拥有访问该单位的全向
     * @param session 当前session
     * @param unit 要访问单位
     * @param topUnitLevel 顶级单位的层级
     * @return 是否拥有权限
     */
    @Override
    public boolean checkIsHaveThisRangePower(HttpSession session, Unit unit, int topUnitLevel) {
        return PowerUtil.checkUserIsHaveThisRangePower(session,unit,Constant.topUnitLevel);
    }
}
