package com.clt.ess.dao;

import com.clt.ess.entity.IndependentUnitConfig;

public interface IIndependentUnitConfigDao {

    /**
     * 根据单位ID查询注册UK时授权文档类型是根据平台还是UK
     * @param independentUnitConfig 对象
     * @return config 参数
     */
    String findIndependentUnitConfig(IndependentUnitConfig independentUnitConfig);
}
