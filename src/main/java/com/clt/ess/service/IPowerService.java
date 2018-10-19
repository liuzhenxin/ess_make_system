package com.clt.ess.service;

import javax.servlet.http.HttpSession;

public interface IPowerService {
    /**
     * 获取当前登录用户是否拥有buttonCode的使用权限
     * @param session
     * @param buttonCode
     * @return
     */
    boolean getPowerForButton(HttpSession session,String buttonCode);


}
