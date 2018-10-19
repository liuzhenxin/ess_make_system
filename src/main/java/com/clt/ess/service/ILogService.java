package com.clt.ess.service;

public interface ILogService {
    boolean addSystemLog(String logDetail, String powerName, String unitId, String userId, String depId);

    /**
     * 添加 操作日志  申请新印章
     * @param logDetail
     * @param unitId
     * @param userId
     * @param depId
     * @return
     */
    boolean addLogAddSealApplyNew(String logDetail, String unitId, String userId, String depId);

    /**
     * 添加操作日志 注册UK
     * @param logDetail
     * @param unitId
     * @param userId
     * @param depId
     * @return
     */
    boolean addLogRegisterSealApplyNew(String logDetail, String unitId, String userId, String depId);
    /**
     * 添加操作日志 重做印章
     * @param logDetail
     * @param unitId
     * @param userId
     * @param depId
     * @return
     */
    boolean addLogReMakeSealApplyNew(String logDetail, String unitId, String userId, String depId);

    /**
     * 添加操作日志 审核通过印章制作申请
     * @param logDetail
     * @param unitId
     * @param userId
     * @param depId
     * @return
     */
    boolean addLogReviewSealApplyNew(String logDetail, String unitId, String userId, String depId);

    /**
     * 添加操作日志 审核驳回印章制作申请
     * @param logDetail
     * @param unitId
     * @param userId
     * @param depId
     * @return
     */
    boolean addLogRejectSealApplyNew(String logDetail, String unitId, String userId, String depId);

    /**
     * 添加操作日志 制作印章制作申请
     * @param logDetail
     * @param unitId
     * @param userId
     * @param depId
     * @return
     */
    boolean addLogMakeSealApplyNew(String logDetail, String unitId, String userId, String depId);
    /**
     * 添加操作日志 制作驳回印章制作申请
     * @param logDetail
     * @param unitId
     * @param userId
     * @param depId
     * @return
     */
    boolean addLogMakeRejectSealApplyNew(String logDetail, String unitId, String userId, String depId);



}
