package com.clt.ess.service.impl;



import com.clt.ess.base.Constant;
import com.clt.ess.dao.ISystemLogDao;
import com.clt.ess.entity.SystemLog;
import com.clt.ess.service.ILogService;
import com.clt.ess.service.IUnitService;
import com.clt.ess.utils.dateUtil;
import com.multica.crypt.MuticaCrypt;
import com.multica.crypt.MuticaCryptException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.Date;

import static com.clt.ess.utils.uuidUtil.getUUID;

/**
 * 操作日志处理
 */
@Service
public class LogServiceImpl implements ILogService {

    @Autowired
    private IUnitService unitService;
    @Autowired
    private ISystemLogDao systemLogDao;
    @Override
    public boolean addSystemLog(String logDetail,String powerName,String unitId,String userId,String depId) {

        SystemLog systemLog = new SystemLog();
        //日志ID
        systemLog.setSysLogId(Constant.JIANGSU_CODE+getUUID());

        Timestamp timeStamp = new Timestamp(new Date().getTime());
        //日志时间
        systemLog.setLogTime(timeStamp);

        systemLog.setPowerName(powerName);

        systemLog.setUnitId(unitId);

        systemLog.setUserId(userId);

        systemLog.setDepId(depId);

        systemLog.setLogDetail(""+unitService.getUnitNameChain(unitId)+logDetail);

        systemLog.setGroupNum(1);
        //安全hash
        systemLog.setSafeHash(getSafeHash(systemLog));
        int result =systemLogDao.addSystemLog(systemLog);
        if(result!=0){
            return true;
        }
        return false;
    }

    @Override
    public boolean addLogAddSealApplyNew(String logDetail, String unitId, String userId, String depId) {
        SystemLog systemLog = createSystemLog("申请制作新印章",unitId,userId,depId);

        systemLog.setLogDetail(""+unitService.getUnitNameChain(unitId)+logDetail);

        int result =systemLogDao.addSystemLog(systemLog);
        if(result!=0){
            return true;
        }
        return false;
    }

    @Override
    public boolean addLogRegisterSealApplyNew(String logDetail, String unitId, String userId, String depId) {
        SystemLog systemLog = createSystemLog("注册UK印章",unitId,userId,depId);

        systemLog.setLogDetail(""+unitService.getUnitNameChain(unitId)+logDetail);

        int result =systemLogDao.addSystemLog(systemLog);
        if(result!=0){
            return true;
        }
        return false;

    }

    @Override
    public boolean addLogReMakeSealApplyNew(String logDetail, String unitId, String userId, String depId) {
        SystemLog systemLog = createSystemLog("重做UK",unitId,userId,depId);

        systemLog.setLogDetail(""+unitService.getUnitNameChain(unitId)+logDetail);

        int result =systemLogDao.addSystemLog(systemLog);
        if(result!=0){
            return true;
        }
        return false;
    }

    @Override
    public boolean addLogReviewSealApplyNew(String logDetail, String unitId, String userId, String depId) {
        SystemLog systemLog = createSystemLog("审核印章申请",unitId,userId,depId);

        systemLog.setLogDetail(""+unitService.getUnitNameChain(unitId)+logDetail);

        int result =systemLogDao.addSystemLog(systemLog);
        if(result!=0){
            return true;
        }
        return false;
    }

    @Override
    public boolean addLogRejectSealApplyNew(String logDetail, String unitId, String userId, String depId) {
        SystemLog systemLog = createSystemLog("审核时驳回印章申请",unitId,userId,depId);

        systemLog.setLogDetail(""+unitService.getUnitNameChain(unitId)+logDetail);

        int result =systemLogDao.addSystemLog(systemLog);
        if(result!=0){
            return true;
        }
        return false;
    }

    @Override
    public boolean addLogMakeSealApplyNew(String logDetail, String unitId, String userId, String depId) {
        SystemLog systemLog = createSystemLog("制作印章",unitId,userId,depId);

        systemLog.setLogDetail(""+unitService.getUnitNameChain(unitId)+logDetail);

        int result =systemLogDao.addSystemLog(systemLog);
        if(result!=0){
            return true;
        }
        return false;
    }

    @Override
    public boolean addLogMakeRejectSealApplyNew(String logDetail, String unitId, String userId, String depId) {
        SystemLog systemLog = createSystemLog("制作时驳回印章申请",unitId,userId,depId);

        systemLog.setLogDetail(""+unitService.getUnitNameChain(unitId)+logDetail);

        int result =systemLogDao.addSystemLog(systemLog);
        if(result!=0){
            return true;
        }
        return false;
    }




    private SystemLog createSystemLog(String powerName, String unitId, String userId, String depId) {
        SystemLog systemLog = new SystemLog();
        //日志ID
        systemLog.setSysLogId(Constant.JIANGSU_CODE+getUUID());

        Timestamp timeStamp = new Timestamp(new Date().getTime());
        //日志时间
        systemLog.setLogTime(timeStamp);

        systemLog.setPowerName(powerName);

        systemLog.setUnitId(unitId);

        systemLog.setUserId(userId);

        systemLog.setDepId(depId);

        systemLog.setGroupNum(1);
        //安全hash
        systemLog.setSafeHash(getSafeHash(systemLog));

        return systemLog;
    }

    /**
     * 生成日志的安全hash
     * @param sysLog
     * @return
     */
    public String getSafeHash(SystemLog sysLog) {

//        SystemLog log = new SystemLog();

        StringBuilder safeBuilder = new StringBuilder();

        // 拼接待计算hash的数据  用户ID
        safeBuilder.append(sysLog.getUserId()+"@ESS@");
        // 拼接待计算hash的数据  日志ID
        safeBuilder.append(sysLog.getSysLogId()+"@ESS@");

        // 拼接待计算hash的数据 用户ID
        safeBuilder.append(sysLog.getUserId()+"@ESS@");
        // 拼接待计算hash的数据 组ID
        safeBuilder.append(sysLog.getGroupNum()+"@ESS@");

        // 拼接待计算hash的数据 日志内容
        safeBuilder.append(sysLog.getLogDetail()+"@ESS@");


        // 拼接待计算hash的数据 单位ID
        safeBuilder.append(sysLog.getUnitId()+"@ESS@");

        // 拼接待计算hash的数据 部门ID
        safeBuilder.append(sysLog.getDepId()+"@ESS@");

        // 拼接待计算hash的数据 日志时间
        safeBuilder.append(sysLog.getLogTime());
        // 设置hash
//        // 获取当天开始向前推一个月中的时间最大的日志数据
//        SystemLog sysLog_month = getMaxTimeSysLogInPrevYear();
//
////        if(sysLog_month == null){
////            return EssResultUtil.getNoResult();
////        }
//        // 拼接待计算hash的数据 上一天日志的hash
//        safeBuilder.append(sysLog_month.getSafeHash());

        String safeHash = "";

        try {

            byte[] essGetDigest = MuticaCrypt.ESSGetDigest(safeBuilder.toString().getBytes());

            safeHash = MuticaCrypt.ESSGetBase64Encode(essGetDigest);

        } catch (MuticaCryptException e) {

            e.printStackTrace();
        }

        sysLog.setSafeHash(safeHash);

        return safeHash;
    }


    /**
     *获取当前时间一个月以前的日志
     */
    public SystemLog getMaxTimeSysLogInPrevYear(){


        //获取一年前的时间
        Timestamp timeStamp = new Timestamp(dateUtil.getYearDate().getTime());

        SystemLog systemLog = new SystemLog();
        systemLog  = systemLogDao.findSystemLogByTimeLimit(timeStamp);
        if(systemLog!= null){
            return systemLog;
        }
        return null;
    }

    /**
     * 根绝输入的信息返回日志内容
     * @param session
     */
    public String getLogDetilString(HttpSession session){
        return "";

    }
}
