package com.clt.ess.service.impl;

import com.clt.ess.base.Constant;
import com.clt.ess.dao.IFileTypeDao;
import com.clt.ess.dao.IIndependentUnitConfigDao;
import com.clt.ess.dao.ISysVerifyDao;
import com.clt.ess.entity.FileType;
import com.clt.ess.entity.IndependentUnitConfig;
import com.clt.ess.entity.SysVerify;
import com.clt.ess.entity.Unit;
import com.clt.ess.service.IFileTypeService;
import com.clt.ess.service.IUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileTypeService implements IFileTypeService {
    @Autowired
    private IFileTypeDao fileTypeDao;
    @Autowired
    private IIndependentUnitConfigDao independentUnitConfigDao;
    @Autowired
    private ISysVerifyDao sysVerifyDao;
    @Autowired
    private IUnitService unitService;

    @Override
    public FileType findFileTypeById(String fileTypeId) {
        return fileTypeDao.findFileTypeById(fileTypeId);
    }

    @Override
    public List<FileType> findFileTypeListByUnitId(String topUnitId) {

        return fileTypeDao.findFileTypeListByUnitId(topUnitId);
    }

    /**
     * 根据单位ID查询授权文档类型
     * @param unitId 单位ID
     * @return 授权文档类型集合
     */
    @Override
    public List<FileType> findFileTypeListByTop(String unitId) {
        //根据unitId 查询授权代码
//        SysVerify sysVerify = sysVerifyDao.findSysVerifyById(unitId);
//        if(sysVerify ==null){
//            //当前单位没有记录 查找一级单位
//            Unit topUnit =unitService.findTopUnit(unitId);
//            sysVerify = sysVerifyDao.findSysVerifyById(topUnit.getUnitId());
//        }
//
//        String sAuth_1 = sysVerify.getJurProductCode();

        //暂时自定义
        String sAuth = "31";

        return GetProductInfoFromAuthNumber(sAuth);
    }
    /**
     * 根据授权代码获取授权具体内容
     * @param sAuth 授权代码
     * @return 授权具体内容
     */
    @Override
    public  List<FileType> GetProductInfoFromAuthNumber(String sAuth){
        int iAuth = Integer.parseInt(sAuth);
        List<FileType> retList = new ArrayList<>();
        String sRet = "";
        if((iAuth & 1) != 0){
            sRet = sRet + "OFFICE 全文批注(office annotation)@" ;
            FileType fileType = new FileType();
            fileType.setFileTypeValue("office annotation");
            fileType.setFileTypeName("OFFICE 全文批注");
            retList.add(fileType);
        }
        if((iAuth & 2) != 0){
            sRet = sRet + "网页批注(web annotation)@" ;
            FileType fileType = new FileType();
            fileType.setFileTypeValue("web annotation");
            fileType.setFileTypeName("网页批注");
            retList.add(fileType);
        }
        if((iAuth & 4) != 0){
            sRet = sRet + "网页签章(ESSWebSign)@" ;
            FileType fileType = new FileType();
            fileType.setFileTypeValue("ESSWebSign");
            fileType.setFileTypeName("网页签章");
            retList.add(fileType);
        }
        if((iAuth & 8) != 0){
            sRet = sRet + "WORD签章(ESSWordSign)@" ;
            FileType fileType = new FileType();
            fileType.setFileTypeValue("ESSWordSign");
            fileType.setFileTypeName("WORD签章");
            retList.add(fileType);
        }
        if((iAuth & 16) != 0){
            sRet = sRet + "EXCEL签章(ESSExcelSign)@" ;
            FileType fileType = new FileType();
            fileType.setFileTypeValue("ESSExcelSign");
            fileType.setFileTypeName("EXCEL签章");
            retList.add(fileType);
        }
        if((iAuth & 32) != 0){
            sRet = sRet + "PDF签章(ESSPdfSign)@" ;
            FileType fileType = new FileType();
            fileType.setFileTypeValue("ESSPdfSign");
            fileType.setFileTypeName("PDF签章");
            retList.add(fileType);
        }
        if((iAuth & 64) != 0){
            sRet = sRet + "中间件(ESSMidWare)@" ;
            FileType fileType = new FileType();
            fileType.setFileTypeValue("ESSMidWare");
            fileType.setFileTypeName("中间件");
            retList.add(fileType);
        }
        return retList;
    }

    /**
     * 根据授权内容转换层授权代码
     * @param sProducts 授权内容
     * @return 授权内容
     */
    @Override
    public int GetAuthNumberFromProductInfo(String sProducts){
        int iAuth = 0;
        String s[] = sProducts.split("@");
        for(int i=0;i<s.length;i++){
            if(s[i].indexOf("office") != -1 && s[i].indexOf("annotation") != -1 )
            {
                iAuth = iAuth | 1;
            }else if(s[i].indexOf("web") != -1 && s[i].indexOf("annotation") != -1 ){
                iAuth = iAuth | 2;
            }else if(s[i].indexOf("ESSWebSign") != -1){
                iAuth = iAuth | 4;
            }else if(s[i].indexOf("ESSWordSign") != -1){
                iAuth = iAuth | 8;
            }else if(s[i].indexOf("ESSExcelSign") != -1){
                iAuth = iAuth | 16;
            }else if(s[i].indexOf("ESSPdfSign") != -1){
                iAuth = iAuth | 32;
            }else if(s[i].indexOf("ESSMidWare") != -1){
                iAuth = iAuth | 64;
            }else{
            }
        }
        return iAuth;
    }
    /**
     * 查询 UK注册时到期时间和授权文档类型是以系统为准还是以UK为准
     * @param unitId 单位ID
     * @return 1根据平台 2根据UK
     */
    @Override
    public String findFileTypeConfigByTop(String unitId) {
        IndependentUnitConfig independentUnitConfig = new IndependentUnitConfig();
        independentUnitConfig.setIndependentUnitId(unitId);
        //查询类型 ：UK注册时到期时间和授权文档类型是以系统为准还是以Uk为准
        independentUnitConfig.setNum(Constant.INDEPENDENTUNITCONFIG_NUM_4);
        //1根据平台 2根据UK
        return independentUnitConfigDao.findIndependentUnitConfig(independentUnitConfig);
    }
}
