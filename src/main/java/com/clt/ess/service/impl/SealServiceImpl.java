package com.clt.ess.service.impl;



import com.clt.ess.dao.ISealApplyDao;
import com.clt.ess.dao.ISealDao;
import com.clt.ess.entity.*;
import com.clt.ess.service.ICertificateService;
import com.clt.ess.service.ISealService;
import com.clt.ess.entity.SysVerify;
import com.clt.ess.service.ISysVerifyService;
import com.clt.ess.service.IUnitService;
import com.clt.ess.utils.dateUtil;
import com.multica.crypt.VerifyServerAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SealServiceImpl implements ISealService {
    @Autowired
    private ISealDao sealDao;
    @Autowired
    private ISealApplyDao sealApplyDao;
    @Autowired
    private ICertificateService certificateService;
    @Autowired
    private ISysVerifyService sysVerifyService;
    @Autowired
    private IUnitService unitService;

    @Override
    public List<SealType> findSealType(SealType sealType) {


        return sealDao.findSealType(sealType);
    }

    @Override
    public boolean addSealApply(SealApply sealApply) {

        int result = sealApplyDao.addSealApply(sealApply);

        if(result == 1 ){
            return true;
        }
        return false;

    }

    @Override
    public List<SealApply> findSealApply(SealApply sealApply) {
        List<SealApply> sealApplies = sealApplyDao.findSealApply(sealApply);
        return sealApplies;
    }

    @Override
    public boolean delSealApply(SealApply sealApply) {
        int result = sealApplyDao.delSealApply(sealApply);
        if(result==1){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateSealApply(SealApply sealApply) {
        int result = sealApplyDao.updateSealApply(sealApply);
        if(result==1){
            return true;
        }
        return false;
    }

    @Override
    public List<Seal> findSealList(Seal seal) {
        return sealDao.findSealList(seal);
    }




    @Override
    public boolean addSeal(Seal newSeal) {

        //印章更新
        int result = sealDao.addSeal(newSeal);
        if(result==1){
            return true;
        }
        return false;
    }

    @Override
    public void delSeal(Seal seal) {
        int result = sealDao.delSeal(seal);
    }

    @Override
    public List<Seal> findSealListByIdNums(List<String> personIdNums) {
        return sealDao.findSealListByIdNums(personIdNums);
    }

    @Override
    public boolean updateSeal(Seal seal_up) {
        int result = sealDao.updateSeal(seal_up);
        if(result==1){
            return true;
        }
        return false;
    }

    @Override
    public boolean createSealApplyInfo(SealApply sealApply, SealImg sealImg, Certificate c) {
        return false;
    }

    @Override
    public SealApply findSealApplyById(String sealApplyId) {

        return sealApplyDao.findSealApplyById(sealApplyId);
    }

    @Override
    public Seal findSealById(String sealId) {
        return sealDao.findSealById(sealId);
    }

    @Override
    public int findSealCountByUnitId(String unitId) {
        return sealDao.findSealCountByUnitId(unitId);
    }


    @Override
    public int isAddSeal(String unitId) {
        // 判断当前配置的服务器是否有添加人员的剩余数量
        // 获取当前登录用户的单位Id
        // 独立单位Id,可能是当前登录人的单位id，也可能是一级单位id
        String independentId = "";
        // 先根据当前印章的单位id查询,若查询不到,则查询独立单位id（一级单位）,并根据独立单位id查
        SysVerify sysVerify = sysVerifyService.findSysVerifyById(unitId);
        // 先将其设置为当前登录人的所属单位Id
        independentId = unitId;
        if(sysVerify == null || sysVerify.getVerifyResult() == null){
            Unit unit = unitService.findUnitById(unitId);
            if(unit == null){
                return 1;
            }
            // 查询一级单位的Id
            Unit topUnit = unitService.findTopUnit(unitId);
            // 如果通过当前登录人没有查询到验证信息,则通过独立单位id查询
            independentId = topUnit.getUnitId();
            sysVerify = sysVerifyService.findSysVerifyById(topUnit.getUnitId());
        }
        if(sysVerify == null || sysVerify.getVerifyResult() == null){
            //无权限
			return 1;
        }
        int verifyStatus = VerifyServerAuthority.GetServerAuthorityVerifyStatus(dateUtil.getCurrentTimeToMinuteNoDelimiter(), sysVerify.getVerifyResult());
        if(verifyStatus == 1 || verifyStatus == 3){
			//验证失败
			return 2;
        }else if(verifyStatus == 2){
            int iSealMaxCount = sysVerify.getUkSealJurSize() + sysVerify.getMobileSealJurSize() + sysVerify.getUkHwJurSize() + sysVerify.getMobileHwJurSize();
            String verifyServerAuth = VerifyServerAuthority.VerifyServerAuth(sysVerify.getUnitName(), sysVerify.getServerIp(), iSealMaxCount, sysVerify.getJurProductCode(), sysVerify.getDueTime(), sysVerify.getSignValue());
            if(verifyServerAuth == null || "".equals(verifyServerAuth)){
                // 验证失败
                return 2;
            }else{
                // 验证成功,将返回值写入查询出的sysVerify的验证结果字段中
                sysVerify.setVerifyResult(verifyServerAuth);
                // 查询当前数据库中有效状态下的印章数量
                Integer sealSize = findSealCountByUnitId(independentId);
                if(sealSize == null){
                    //验证失败
                    return 2;
                }
                if(sealSize == sysVerify.getUkSealJurSize()){
                    //false
                };

                sysVerify.setIndependentId(independentId);

                // 执行更新操作
                Integer size = sysVerifyService.updateVerifyResultByIndepdId(sysVerify);
                return 3;
            }
        }
        return 0;
    }
}
