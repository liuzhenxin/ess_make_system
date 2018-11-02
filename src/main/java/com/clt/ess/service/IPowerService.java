package com.clt.ess.service;

import com.clt.ess.entity.Unit;

import javax.servlet.http.HttpSession;

public interface IPowerService {
    /**
     * 获取当前登录用户是否拥有buttonCode的使用权限
     * @param session 当前session
     * @param buttonCode 权限代码
     * @return 是否拥有权限
     */
    boolean getPowerForButton(HttpSession session,String buttonCode);

    /**
     * 检查当前登录用户是否拥有访问该单位的全向
     * @param session 当前session
     * @param unitById 要访问单位ID
     * @param topUnitLevel 顶级单位的层级
     * @return 是否拥有权限
     */
    boolean checkIsHaveThisRangePower(HttpSession session, Unit unitById, int topUnitLevel);
}
