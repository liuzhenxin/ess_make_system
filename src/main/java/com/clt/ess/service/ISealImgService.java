package com.clt.ess.service;

import com.clt.ess.entity.SealApply;
import com.clt.ess.entity.SealImg;
import org.springframework.web.multipart.MultipartFile;

public interface ISealImgService {
    /**
     * 添加印章图片
     * @param sealImg
     * @return 返回添加数据的ID
     */
    String addSealImg(SealImg sealImg);

    /**
     *
     * @param imgId
     * @return
     */
    SealImg findSealImgById(String imgId);

    /**
     * 更新印章图片数据
     * @param sealImg
     * @return
     */
    boolean updateSealImg(SealImg sealImg);

    /**
     * 根据提供信息生成印章图片对象
     * @param sealApply
     * @param gifImg
     * @param jpgImg
     * @return
     */
    SealImg createSealImg(SealApply sealApply, MultipartFile gifImg, MultipartFile jpgImg);

    /**
     * 根据Id删除对象
     * @param seaImgId 印章图片ID
     * @return
     */
    boolean deleteSealImgById(String seaImgId);
}
