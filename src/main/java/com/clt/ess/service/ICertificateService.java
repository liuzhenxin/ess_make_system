package com.clt.ess.service;

import com.clt.ess.entity.Certificate;

import java.util.List;
import java.util.Map;

public interface ICertificateService {
    /**
     * 添加证书
     * @param certificate
     * @return 返回添加数据的主键ID
     */
    String addCertificate(Certificate certificate);

    Map<String, String> createCerFileAndPfx(Certificate certificate);

    /**
     * 查找符合条件的证书
     * @param certificate
     * @return
     */
    List<Certificate> findCertificate(Certificate certificate);

    /**
     * 更新证书数据
     * @param certificate
     */
    boolean updateCertificate(Certificate certificate);

    /**
     * 对上传的cer证书进行签名
     * @param cerIssuer
     * @param certificate_1
     * @return
     */
    String signCertificateWithIssuer(String cerIssuer, Certificate certificate_1);
}
