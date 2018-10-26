package com.clt.ess.service.impl;

import com.clt.ess.dao.IErrorLogDao;
import com.clt.ess.entity.ErrorLog;
import com.clt.ess.service.IErrorLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.clt.ess.utils.dateUtil.getDate;
@Service
public class ErrorLogServiceImpl implements IErrorLogService {


    @Autowired
    IErrorLogDao errorLogDao;
    @Override
    public boolean addErrorLog(String errorDetail) {
        ErrorLog errorLog = new ErrorLog();
        errorLog.setTime(getDate());
        errorLog.setSysName("制章平台");
        errorLog.setErrorDetail(errorDetail);

        int result = errorLogDao.addErrorLog(errorLog);

        if(result ==1){
            return true;
        }
        return false;
    }

}
