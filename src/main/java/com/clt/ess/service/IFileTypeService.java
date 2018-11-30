package com.clt.ess.service;

import com.clt.ess.entity.FileType;

import java.util.List;

public interface IFileTypeService {


    FileType findFileTypeById(String fileTypeId);

    List<FileType> findFileTypeListByUnitId(String UnitId);

    List<FileType> findFileTypeListByTop(String unitId);

    List<FileType> GetProductInfoFromAuthNumber(String sAuth);

    int GetAuthNumberFromProductInfo(String sProducts);

    /**
     * 根据单位ID查询注册UK时授权文档类型是根据平台还是UK
     * @param unitId 单位ID
     * @return
     */
    String findFileTypeConfigByTop(String unitId);
}
