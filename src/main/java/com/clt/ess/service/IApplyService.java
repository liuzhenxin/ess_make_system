package com.clt.ess.service;

import com.clt.ess.entity.Certificate;
import com.clt.ess.entity.SealApply;
import com.clt.ess.entity.SealImg;
import com.multica.crypt.MuticaCryptException;

import javax.servlet.http.HttpSession;

public interface IApplyService {
    boolean addSealApply(SealApply sealApply, Certificate c, HttpSession session);
    //添加新印章申请
    boolean addSealApplyNew(SealApply sealApply , Certificate c, HttpSession session) throws MuticaCryptException;
    //添加注册UK申请
    boolean addSealApplyRegister(SealApply sealApply , Certificate c, HttpSession session);
    //添加印章重做申请
    boolean addSealApplyRepeat(SealApply sealApply , Certificate c, HttpSession session);
    //添加印章延期申请
    boolean addSealApplyDelay(SealApply sealApply , Certificate c, HttpSession session);
}
