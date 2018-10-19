package com.clt.ess.dao;

import com.clt.ess.entity.ErrorLog;

public interface IErrorLogDao {
    /**
     * 添加错误日志
     */
    int addErrorLog(ErrorLog errorLog);
}
