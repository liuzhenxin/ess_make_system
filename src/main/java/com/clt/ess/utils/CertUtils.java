package com.clt.ess.utils;

import com.clt.ess.entity.IssuerUnit;
import com.multica.jmj.JMJ_Exception;
import sun.misc.BASE64Encoder;
import sun.security.x509.*;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static com.clt.ess.utils.FileUtil.byte2Input;
import static com.clt.ess.utils.FileUtil.getfileBytes;
import static com.multica.crypt.MuticaCrypt.ESSGetBase64Decode;

public class CertUtils {

    /**
     * 获取颁发者私钥
     * @param issuerUnit
     * @return
     * @throws JMJ_Exception
     */
    public static PrivateKey GetPrivateKey(IssuerUnit issuerUnit) throws JMJ_Exception
    {
        String sFile = issuerUnit.getIssuerUnitPfx();
        String sFileType = "PKCS12";
        try
        {
            FileInputStream fis;
            fis = new FileInputStream(sFile);
            char[] nPassword = null;
            nPassword = issuerUnit.getPfxPwd().toCharArray();
            KeyStore inputKeyStore = KeyStore.getInstance(sFileType);
            inputKeyStore.load(fis, nPassword);
            Enumeration<String> enuma = inputKeyStore.aliases();
            String keyAlias = null;
            keyAlias = (String) enuma.nextElement();
            Key key = null;
            if (inputKeyStore.isKeyEntry(keyAlias))
                key = inputKeyStore.getKey(keyAlias, nPassword);
            fis.close();
            PrivateKey pk = (PrivateKey)key;
            return pk;
        }catch(IOException e)
        {
            throw(new JMJ_Exception(e.getMessage()));
        }catch(GeneralSecurityException e)
        {
            throw(new JMJ_Exception(e.getMessage()));
        }
    }




    private static String GetDefaultCertOwnerInfo()
    {
        return "CN = ccn,OU = oou,O = oox,L = ool,S = ooS,C = 中国";
    }



    public static byte[] signCertByIssuerUnit(byte[] bCert,IssuerUnit issuerUnit,String sDN,String sOU,String sO,
                                              String sL,String sS,String algorithm,Date dateStart,Date dateEnd){
        byte[] bOutCert = null;
        try {
            X500Name bIssuer=TestSignCert.GetIssuerInfo(issuerUnit.getIssuerUnitRoot());

            InputStream sbs = byte2Input(bCert);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            //被签名证书
            X509Certificate cert = (X509Certificate)cf.generateCertificate(sbs);

            //生成一份行的证书信息
            X509CertInfo info = new X509CertInfo();
//            //起始时间
//            CertificateValidity interval = new CertificateValidity(cert.getNotBefore(), cert.getNotAfter());
//
//            BigInteger sn = new BigInteger(64, new SecureRandom());
//            X500Name owner = new X500Name(cert.getSubjectDN().getName());

            //证书有效期
            CertificateValidity interval = new CertificateValidity(dateStart, dateEnd);

            BigInteger sn = new BigInteger(64, new SecureRandom());
            //使用人信息
            String sCertOwner = GetDefaultCertOwnerInfo();
            sCertOwner = sCertOwner.replace("ccn", sDN);
            sCertOwner = sCertOwner.replace("oou", sOU);
            sCertOwner = sCertOwner.replace("oox", sO);
            sCertOwner = sCertOwner.replace("ool", sL);
            sCertOwner = sCertOwner.replace("ooS", sS);

            X500Name owner = new X500Name(sCertOwner);

            //证书信息
            info.set(X509CertInfo.VALIDITY, interval);
            info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(sn));

            info.set(X509CertInfo.SUBJECT, new CertificateSubjectName(owner));
            info.set(X509CertInfo.ISSUER, new CertificateIssuerName(bIssuer));
            info.set(X509CertInfo.KEY, new CertificateX509Key(cert.getPublicKey()));

            System.out.print(cert.getVersion());
            info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));

            //算法
            AlgorithmId algo = new AlgorithmId(AlgorithmId.md5WithRSAEncryption_oid);
            info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algo));

            PrivateKey issuer_PrivateKey = GetPrivateKey(issuerUnit);

            X509CertImpl certImpl = new X509CertImpl(info);
            certImpl.sign(issuer_PrivateKey, cert.getSigAlgName());
            System.out.print(certImpl.getVersion());
            bOutCert = certImpl.getEncoded();

        }catch (Exception e){
            System.out.print(e);
            return null;
        }
        return bOutCert;

    }


}
