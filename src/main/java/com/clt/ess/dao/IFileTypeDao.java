package com.clt.ess.dao;

import com.clt.ess.entity.FileType;

import java.util.List;

public interface IFileTypeDao {


    List<FileType> findFileTypeList(FileType fileType);

    FileType findFileTypeById(String fileTypeId);

    List<FileType> findFileTypeListByUnitId(String topUnitId);

    /**
     * 根据单位ID查询注册UK时授权文档类型是根据平台还是UK
     * @param unitId 单位ID
     * @return config 参数
     */
    String findFileTypeConfigByTop(String unitId);
}
