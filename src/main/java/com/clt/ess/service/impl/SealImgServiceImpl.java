package com.clt.ess.service.impl;


import com.clt.ess.base.Constant;
import com.clt.ess.dao.ISealImgDao;
import com.clt.ess.entity.SealApply;
import com.clt.ess.entity.SealImg;
import com.clt.ess.service.ISealImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.clt.ess.utils.uuidUtil.getEssUUID;
import static com.clt.ess.utils.uuidUtil.getUUID;
import static com.multica.crypt.MuticaCrypt.ESSGetBase64Encode;

@Service
public class SealImgServiceImpl implements ISealImgService {

    @Autowired
    private ISealImgDao sealImgDao;
    @Override
    public String addSealImg(SealImg sealImg) {
        if(sealImg.getSealImgId()==null&&"".equals(sealImg.getSealImgId())){
            sealImg.setSealImgId(getUUID());
        }
        //执行add动作，获取返回结果
        int result = sealImgDao.addSealImg(sealImg);
        if(result!=0){
            return sealImg.getSealImgId();
        }else{
            return null;
        }
    }

    @Override
    public SealImg findSealImgById(String imgId) {
        return sealImgDao.findSealImgById(imgId);
    }

    @Override
    public boolean updateSealImg(SealImg sealImg) {
        int result = sealImgDao.updateSealImg(sealImg);
        if(result==1){
            return true;
        }
        return false;
    }

    @Override
    public SealImg createSealImg(SealApply sealApply, MultipartFile gifImg, MultipartFile jpgImg) {
        SealImg sealImg = new SealImg();
        //判断申请信息类别
        try {
            switch(sealApply.getApplyType()) {
                //申请新印章
                case Constant.APPLYTYPE_NEW:
                    if (!jpgImg.isEmpty()) {
                        sealImg.setSealImgJpg(jpgImg.getBytes());
                    }
                    if (!gifImg.isEmpty()) {
                        sealImg.setSealImgGifBase64(ESSGetBase64Encode(gifImg.getBytes()));
                        sealImg.setSealThumbnailImgBase64(ESSGetBase64Encode(gifImg.getBytes()));
                    }
                    sealImg.setSealImgId(getEssUUID(sealApply.getUnitId()));
                    break;
                //注册UK
                case Constant.APPLYTYPE_REGISTER_UK:
                    SealImg oldSealImg = findSealImgById(sealApply.getSealImgId());
                    //查找出上传的图片信息
                    sealImg = oldSealImg;
                    //设置新的带有区号的ID
                    sealImg.setSealImgId(getEssUUID(sealApply.getUnitId()));
                    //如果有更新则更新数据
                    if (!jpgImg.isEmpty()) {
                        sealImg.setSealImgJpg(jpgImg.getBytes());
                    }
                    if (!gifImg.isEmpty()) {
                        sealImg.setSealImgGifBase64(ESSGetBase64Encode(gifImg.getBytes()));
                        sealImg.setSealThumbnailImgBase64(ESSGetBase64Encode(gifImg.getBytes()));
                    }
                    //删除原有图片信息
                    deleteSealImgById(oldSealImg.getSealImgId());
                    break;
                //申请重做
                case Constant.APPLYTYPE_REPEAT:

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sealImg;
    }

    @Override
    public boolean deleteSealImgById(String seaImgId) {
        return sealImgDao.deleteSealImgById(seaImgId);
    }
}
