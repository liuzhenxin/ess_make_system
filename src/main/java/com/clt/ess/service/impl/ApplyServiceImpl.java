package com.clt.ess.service.impl;

import com.clt.ess.base.Constant;
import com.clt.ess.entity.Certificate;
import com.clt.ess.entity.SealApply;
import com.clt.ess.entity.SealImg;
import com.clt.ess.entity.User;
import com.clt.ess.service.*;
import com.clt.ess.utils.PowerUtil;
import com.clt.ess.utils.ReadPfx;
import com.clt.ess.utils.StringUtils;
import com.multica.crypt.MuticaCryptException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

import static com.clt.ess.utils.Base64Utils.encodeBase64File;
import static com.clt.ess.utils.ReadPfx.getOwnerString;
import static com.clt.ess.utils.dateUtil.*;
import static com.clt.ess.utils.uuidUtil.getEssUUID;

@Service
public class ApplyServiceImpl implements IApplyService {

    @Autowired
    ISealService sealService;
    @Autowired
    ICertificateService certificateService;
    @Autowired
    ISealImgService sealImgService;
    @Autowired
    IUnitService unitService;
    @Autowired
    ILogService logService;

    @Override
    public boolean addSealApply(SealApply sealApply, Certificate c, HttpSession session){

        boolean result = false;
        //生成申请信息ID
        sealApply.setSealApplyId(getEssUUID(sealApply.getUnitId()));
        //申请信息状态
        sealApply.setApplyState(Constant.SUBMIT_APPLICATION);
        //设置申请人和申请时间
        sealApply.setApplyUserId(PowerUtil.getLoginUser(session).getUserId());
        sealApply.setApplyTime(getDate());

        User user = PowerUtil.getLoginUser(session);
        String logDetail = null;
        //判断申请信息类别
        switch(sealApply.getApplyType()) {
            //申请新印章
            case Constant.APPLYTYPE_NEW:
                result = addSealApplyNew(sealApply, c, session);
//                logService.addSystemLog("申请制作新印章"+sealApply.getSealName(),"申请新印章",
//                        sealApply.getUnitId(),user.getUserId(),"");
                logDetail = "向"+unitService.getUnitNameChain(sealApply.getUnitId())+"申请制作新印章："+sealApply.getSealName();
                logService.addLogAddSealApplyNew(logDetail, sealApply.getUnitId(),user.getUserId(),"");
                break;
            //注册UK
            case Constant.APPLYTYPE_REGISTER_UK:
                result= addSealApplyRegister(sealApply, c,session);
//                logService.addSystemLog("申请注册已有UK印章"+sealApply.getSealName(),"注册UK印章",
//                        sealApply.getUnitId(),user.getUserId(),"");
                logDetail = "向"+unitService.getUnitNameChain(sealApply.getUnitId())+"申请注册已有UK："+sealApply.getSealName();
                logService.addLogRegisterSealApplyNew(logDetail, sealApply.getUnitId(),user.getUserId(),"");
                break;
            //申请重做
            case Constant.APPLYTYPE_REPEAT:
                result = addSealApplyRepeat(sealApply, c, session);
//                logService.addSystemLog("申请重做印章"+sealApply.getSealName(),"申请重做印章",
//                        sealApply.getUnitId(),user.getUserId(),"");
                logDetail = "向"+unitService.getUnitNameChain(sealApply.getUnitId())+"申请重新制作印章："+sealApply.getSealName();
                logService.addLogReMakeSealApplyNew(logDetail, sealApply.getUnitId(),user.getUserId(),"");
                break;
        }
        return result;
    }

    @Override
    public boolean addSealApplyNew(SealApply sealApply, Certificate c, HttpSession session) {
        //为申请的印章生成id
        sealApply.setSealId(getEssUUID(sealApply.getUnitId()));
        //创建证书
        //生成证书信息，返回证书ID

        c.setCertificateId(getEssUUID(sealApply.getUnitId()));

        if(c.getFileState() == Constant.FILE_STATE_PFX){
            Map<String,String> cerInfo = ReadPfx.GetCertInfoFromPfxBase64(c.getCerBase64(),c.getCerPsw());


            String version = null;
            if (cerInfo != null) {
                version = cerInfo.get("version");
            }else{
                //抛出解析证书失败异常！或者错误日志

            }
            String sn = cerInfo.get("sn");
            String startTime = cerInfo.get("startTime");
            String endTime = cerInfo.get("endTime");
            String owner = cerInfo.get("owner");
            String issuer = cerInfo.get("issuer");
            String algorithm = cerInfo.get("algorithm");
            String[] a = owner.split(", ");

            Map<String,String> ownerInfo = new HashMap<>();
            ownerInfo.put(a[a.length-1].split("=")[0],a[a.length-1].split("=")[1]);
            ownerInfo.put(a[a.length-2].split("=")[0],a[a.length-2].split("=")[1]);
            ownerInfo.put(a[a.length-3].split("=")[0],a[a.length-3].split("=")[1]);
            ownerInfo.put(a[a.length-4].split("=")[0],a[a.length-4].split("=")[1]);
            ownerInfo.put(a[a.length-5].split("=")[0],a[a.length-5].split("=")[1]);
            ownerInfo.put(a[a.length-6].split("=")[0],a[a.length-6].split("=")[1]);

            //a[0] CN  证书名称
            String cerName = ownerInfo.get("CN");
            c.setCerName((cerName.substring(1, cerName.length()-1)).replace(" ",""));
            //a[1] OU  部门名称
            String cerDe = ownerInfo.get("OU");
            c.setCertDepartment((cerDe.substring(1, cerDe.length()-1)).replace(" ",""));
            //a[2] O   单位名称
            String certUnit = ownerInfo.get("O");
            c.setCertUnit((certUnit.substring(1, certUnit.length()-1)).replace(" ",""));
            //a[3] L   市
            c.setCity(ownerInfo.get("L"));
            //a[4] ST  省
            c.setProvince(ownerInfo.get("ST"));
            //a[5] C   中国
            c.setCountry(ownerInfo.get("C"));


            c.setStartTime(startTime);
            c.setEndTime(endTime);
            c.setApplyTime(startTime);
            c.setCerName(owner);
            c.setIssuer(issuer);
            c.setAlgorithm(algorithm);
            c.setCerClass(Constant.CER_CLASS);
            c.setCertificateVersion(version);
            c.setFileState(Constant.FILE_STATE_PFX);
            c.setPfxBase64(c.getPfxBase64());
            //设置证书信息：证书密码
            c.setCerPsw(c.getCerPsw());
        }else{
            c.setCertificateVersion(Constant.CER_VERSION);
            c.setAlgorithm(Constant.CER_ALGORITHM);
            c.setCerClass(Constant.CER_CLASS);
            //设置证书状态未生成
            c.setFileState(Constant.FILE_STATE_NULL);
            //证书申请时间
            c.setApplyTime(getStringDateShort());
        }
        //获得证书ID
        String certificateId  = certificateService.addCertificate(c);
        //申请信息中添加证书
        sealApply.setCertificateId(certificateId);
        sealService.addSealApply(sealApply);
        return true;
    }

    @Override
    public boolean addSealApplyRegister(SealApply sealApply, Certificate c, HttpSession session) {
        //为申请的印章生成id
        sealApply.setSealId(getEssUUID(sealApply.getUnitId()));
        //UK印章
        sealApply.setIsUK(1);
        //对提供的证书解析
        //提供证书，解析
        //生成证书，返回证书ID
        c.setCertificateId(getEssUUID(sealApply.getUnitId()));

        String pfxBase64 =null;
        try {
            pfxBase64 = encodeBase64File("d:/temp/root.cer");
            pfxBase64 = c.getPfxBase64();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String,String> cerInfo = ReadPfx.showCertInfo(pfxBase64);
        String version = cerInfo.get("version");
        String sn = cerInfo.get("sn");
        String startTime = cerInfo.get("startTime");
        String endTime = cerInfo.get("endTime");
        String owner = cerInfo.get("owner");
        String issuer = cerInfo.get("issuer");
        String algorithm = cerInfo.get("algorithm");
        String[] a = owner.split(", ");

        Map<String,String> ownerInfo = new HashMap<>();
        ownerInfo.put(a[a.length-1].split("=")[0],a[a.length-1].split("=")[1]);
        ownerInfo.put(a[a.length-2].split("=")[0],a[a.length-2].split("=")[1]);
        ownerInfo.put(a[a.length-3].split("=")[0],a[a.length-3].split("=")[1]);
        ownerInfo.put(a[a.length-4].split("=")[0],a[a.length-4].split("=")[1]);
        ownerInfo.put(a[a.length-5].split("=")[0],a[a.length-5].split("=")[1]);
        ownerInfo.put(a[a.length-6].split("=")[0],a[a.length-6].split("=")[1]);

        //a[0] CN  证书名称
        String cerName = ownerInfo.get("OU");
        c.setCerName((cerName.substring(1, cerName.length()-1)).replace(" ",""));
        //a[1] OU  部门名称
        String cerDe = ownerInfo.get("OU");
        c.setCertDepartment((cerDe.substring(1, cerDe.length()-1)).replace(" ",""));
        //a[2] O   单位名称
        String certUnit = ownerInfo.get("O");
        c.setCertUnit((certUnit.substring(1, certUnit.length()-1)).replace(" ",""));
        //a[3] L   市
        c.setCity(ownerInfo.get("L"));
        //a[4] ST  省
        c.setProvince(ownerInfo.get("ST"));
        //a[5] C   中国
        c.setCountry(ownerInfo.get("C"));

        c.setStartTime(startTime);
        c.setEndTime(endTime);
        c.setApplyTime(startTime);

        c.setIssuer(issuer);

        c.setCerBase64(c.getCerBase64());

        c.setAlgorithm(algorithm);
        c.setCerClass(Constant.CER_CLASS);
        c.setCertificateVersion(version);
        c.setFileState(Constant.FILE_STATE_CER);

        //获得证书ID
        String certificateId  = certificateService.addCertificate(c);

        String[] bb = c.getIssuer().split(",");
        int s = bb.length-4;
        sealApply.setCerIssuer(bb[s]);

        sealApply.setCertificateId(certificateId);

        sealService.addSealApply(sealApply);
        return true;
    }

    @Override
    public boolean addSealApplyRepeat(SealApply sealApply, Certificate c, HttpSession session) {
        //创建证书
        //生成证书信息，返回证书ID
        c.setCertificateId(getEssUUID(sealApply.getUnitId()));

        if(c.getFileState() == Constant.FILE_STATE_PFX){
            Map<String,String> cerInfo = ReadPfx.GetCertInfoFromPfxBase64(c.getCerBase64(),c.getCerPsw());
            String version = cerInfo.get("version");
            String sn = cerInfo.get("sn");
            String startTime = cerInfo.get("startTime");
            String endTime = cerInfo.get("endTime");
            String owner = cerInfo.get("owner");
            String issuer = cerInfo.get("issuer");
            String algorithm = cerInfo.get("algorithm");
            String[] a = owner.split(", ");

            Map<String,String> ownerInfo = new HashMap<>();
            ownerInfo.put(a[a.length-1].split("=")[0],a[a.length-1].split("=")[1]);
            ownerInfo.put(a[a.length-2].split("=")[0],a[a.length-2].split("=")[1]);
            ownerInfo.put(a[a.length-3].split("=")[0],a[a.length-3].split("=")[1]);
            ownerInfo.put(a[a.length-4].split("=")[0],a[a.length-4].split("=")[1]);
            ownerInfo.put(a[a.length-5].split("=")[0],a[a.length-5].split("=")[1]);
            ownerInfo.put(a[a.length-6].split("=")[0],a[a.length-6].split("=")[1]);

            //a[0] CN  证书名称
            String cerName = ownerInfo.get("OU");
            c.setCerName((cerName.substring(1, cerName.length()-1)).replace(" ",""));
            //a[1] OU  部门名称
            String cerDe = ownerInfo.get("OU");
            c.setCertDepartment((cerDe.substring(1, cerDe.length()-1)).replace(" ",""));
            //a[2] O   单位名称
            String certUnit = ownerInfo.get("O");
            c.setCertUnit((certUnit.substring(1, certUnit.length()-1)).replace(" ",""));
            //a[3] L   市
            c.setCity(ownerInfo.get("L"));
            //a[4] ST  省
            c.setProvince(ownerInfo.get("ST"));
            //a[5] C   中国
            c.setCountry(ownerInfo.get("C"));


            c.setStartTime(startTime);
            c.setEndTime(endTime);
            c.setApplyTime(startTime);
            c.setCerName(owner);
            c.setIssuer(issuer);
            c.setAlgorithm(algorithm);
            c.setCerClass(Constant.CER_CLASS);
            c.setCertificateVersion(version);
            c.setFileState(Constant.FILE_STATE_PFX);
            c.setPfxBase64(c.getPfxBase64());
            //设置证书信息：证书密码
            c.setCerPsw(c.getCerPsw());
        }else{
            c.setCertificateVersion(Constant.CER_VERSION);
            c.setAlgorithm(Constant.CER_ALGORITHM);
            c.setCerClass(Constant.CER_CLASS);
            //设置证书状态未生成
            c.setFileState(Constant.FILE_STATE_NULL);
            //证书申请时间
            c.setApplyTime(getStringDateShort());
        }
        //获得证书ID
        String certificateId  = certificateService.addCertificate(c);
        //申请信息中添加证书
        sealApply.setCertificateId(certificateId);
        sealService.addSealApply(sealApply);
        return true;
    }

    @Override
    public boolean addSealApplyDelay(SealApply sealApply, Certificate c, HttpSession session) {

        return false;
    }
}
