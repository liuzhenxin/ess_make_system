package com.clt.ess.service.impl;


import com.clt.ess.base.Constant;
import com.clt.ess.dao.ICertificateDao;
import com.clt.ess.dao.IIssuerUnitDao;
import com.clt.ess.entity.Certificate;
import com.clt.ess.entity.IssuerUnit;
import com.clt.ess.service.ICertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.clt.ess.utils.CertificateUtil.CreatePfxFile;

import static com.clt.ess.utils.StringUtils.getDecryptPwd;
import static com.clt.ess.utils.dateUtil.strToDate;

@Service
public class CertificateServiceImpl implements ICertificateService {

    @Autowired
    private ICertificateDao certificateDao;    @Autowired
    private IIssuerUnitDao issuerUnitDao;

    /**
     * 添加证书
     * @param certificate
     * @return 返回添加数据的主键ID
     */
    @Override
    public String addCertificate(Certificate certificate) {

        //证书ID
//        certificate.setCertificateId(getEssUUID(certificate.getUnit()));
        //设置证书基本信息
        //设置证书信息：算法
        certificate.setAlgorithm(Constant.CER_ALGORITHM);
        //设置证书信息：种类
        certificate.setCerClass(Constant.CER_CLASS);
        //设置证书信息：版本
        certificate.setCertificateVersion(Constant.CER_VERSION);
        //设置证书信息有效
        certificate.setState(Constant.STATE_YES);

        //执行add动作，获取返回结果
        int result = certificateDao.addCertificate(certificate);
        if(result == 1 ){
            return certificate.getCertificateId();
        }else{
            return null;
        }
    }
    /**
     * 根据证书颁发机构生成证书
     */
    @Override
    public  Map<String, String> createCerFileAndPfx(Certificate certificate) {
        try{
            IssuerUnit issuerUnit = issuerUnitDao.findIssuerUnitById(certificate.getIssuerUnitId());
            String algorithm = certificate.getAlgorithm();
            String sC = certificate.getCountry();
            String sS = certificate.getProvince();
            String sL = certificate.getCity();
            String sO = certificate.getCertUnit();
            //部门由单位代替（暂时）
            String sOU = certificate.getCertDepartment();
            String sDN = certificate.getCerName();
            //起始时间
            Date dateStart = strToDate(certificate.getStartTime());
            Date dateEnd  = strToDate(certificate.getEndTime());
            String sPwd = getDecryptPwd(certificate.getCerPsw());
            return  CreatePfxFile(sC,sS,sL,sO,sOU,sDN,dateEnd,dateStart,
                    sPwd,algorithm,issuerUnit);

        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<Certificate> findCertificate(Certificate certificate) {

        return certificateDao.findCertificate(certificate);
    }


    @Override
    public boolean updateCertificate(Certificate certificate) {
        int result = certificateDao.updateCertificate(certificate);
        if(result == 1 ){
            return true;
        }else{
            return false;
        }
    }


    @Override
    public String signCertificateWithIssuer(String cerIssuer, Certificate certificate_1) {
        return null;
    }

    public static void main(String[] args) throws IOException {

//        CertificateServiceImpl certificateService = new CertificateServiceImpl();

//        Certificate certificate = new Certificate();
//        certificate.setProvince("江苏省");
//        certificate.setCity("南京市");
//        certificate.setUnit("烟草局");
//        certificate.setDepartment("办公室");
//        certificate.setCerName("局章");
//        certificate.setCerPsw("123456");
//
//        certificate.setAlgorithm("MD5WithRSA");
//
//        certificate.setStarTime("2018-06-01");
//        certificate.setEndTime("2018-09-01");

//        certificateService.createCerFileAndPfx("",certificate,"025seal");



        //根据文件名获取证书存储路径
        String pfxPath = "d:/asdasdad/123.pfx";
        String certificateFilePath =pfxPath.substring(pfxPath.length()-7);
        System.out.print(certificateFilePath);
        certificateFilePath =pfxPath.substring(0,pfxPath.length()-7);
        System.out.print(certificateFilePath);
        //创建文件夹
//        createFile(certificateFilePath);
    }



}
