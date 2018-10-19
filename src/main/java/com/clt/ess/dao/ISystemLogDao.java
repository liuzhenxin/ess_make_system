package com.clt.ess.dao;


import com.clt.ess.entity.SystemLog;

import java.sql.Timestamp;

public interface ISystemLogDao {
    /**
     * 添加系统日志
     */
    int addSystemLog(SystemLog systemLog);

    /**
     * 根据时间期限查找日志记录
     * @param timeStamp
     * @return
     */
    SystemLog findSystemLogByTimeLimit(Timestamp timeStamp);
}
