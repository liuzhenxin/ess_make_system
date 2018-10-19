package com.clt.ess.service;

import com.clt.ess.entity.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public interface ISealService {

    /**
     * 根据条件查询印章类型
     * @param sealType
     * @return
     */

    List<SealType> findSealType(SealType sealType);

    /**
     * 添加申请信息
     * @param sealApply
     */
    boolean addSealApply(SealApply sealApply);

    /**
     * 查询符合条件的申请信息
     * @param sealApply
     * @return
     */
    List<SealApply> findSealApply(SealApply sealApply);


    boolean delSealApply(SealApply sealApply);

    boolean updateSealApply(SealApply sealApply);

    /**
     * 查询印章列表
     * @return
     */
    List<Seal> findSealList(Seal seal);

    /**
     * 添加印章
     * @param newSeal
     * @return
     */
    boolean addSeal(Seal newSeal);

    /**
     * 删除印章
     * @param seal
     */
    void delSeal(Seal seal);

    /**
     * 根据身份证号数组查询印章列表
     * @param personIdNums
     * @return
     */
    List<Seal> findSealListByIdNums(List<String> personIdNums);

    /**
     * 更新印章信息
     * @param seal_up
     */
    boolean updateSeal(Seal seal_up);

    /**
     * 添加申请信息
     * @param sealApply
     * @param sealImg
     * @param c
     * @return
     */
    boolean createSealApplyInfo(SealApply sealApply, SealImg sealImg, Certificate c);

    /**
     * 查询符合条件的申请信息
     * @param sealApplyId
     * @return
     */
    SealApply findSealApplyById(String sealApplyId);
    /**
     * 查询符合条件的印章信息
     * @param sealId
     * @return
     */
    Seal findSealById(String sealId);

    /**
     * 查询当前单位印章数量
     * @return
     */
    int findSealCountByUnitId(String unitId);

    int isAddSeal(String unitId);
}
