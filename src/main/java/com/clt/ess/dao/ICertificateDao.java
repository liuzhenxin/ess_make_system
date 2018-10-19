package com.clt.ess.dao;

import com.clt.ess.entity.Certificate;

import java.util.List;

public interface ICertificateDao {

    int addCertificate(Certificate certificate);
    int updateCertificate(Certificate certificate);
    int deleteCertificate(Certificate certificate);
    Certificate selectCertificate(Certificate certificate);
    List<Certificate> selectCertificateList(Certificate certificate);

    List<Certificate> findCertificate(Certificate certificate);
}
