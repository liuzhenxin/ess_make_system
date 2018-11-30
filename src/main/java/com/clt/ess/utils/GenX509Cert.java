package com.clt.ess.utils;



import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.clt.ess.base.Constant;
import com.clt.ess.entity.IssuerUnit;
import sun.misc.BASE64Encoder;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.*;

import static com.clt.ess.utils.dateUtil.strToDate;
import static com.clt.ess.utils.uuidUtil.getUUID;


/**
 * 首先生成CA的根证书，然后有CA的根证书签署生成ScriptX的证书
 *
 * @author Administrator
 *
 */
public class GenX509Cert {


    public static Map<String, String> CreatePfxFile() {
        String sDN = "www.cltess.cn";
        String sOU = "技术部";
        String sO = "测试证书";
        String sL = "技术部";
        String sS = "测试证书";
        String sC = "技术部";

        //起始时间
        Date dateStart = strToDate("2018-09-27");
        Date dateEnd  = strToDate("2019-09-27");
        String sPwd = "123456";

        /*
         *  先生成一份自签名证书，然后对自签名证书的公钥证书使用颁发者证书签�?
         * */
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            //获得密钥对
            KeyPair keyPair = kpg.generateKeyPair();
            //证书有效期
            CertificateValidity interval = new CertificateValidity(dateStart, dateEnd);

            BigInteger sn = new BigInteger(64, new SecureRandom());
            //使用人信息
            String sCertOwner =GetDefaultCertOwnerInfo();
            sCertOwner = sCertOwner.replace("ccn", sDN);
            sCertOwner = sCertOwner.replace("oou", sOU);
            sCertOwner = sCertOwner.replace("oox", sO);
            sCertOwner = sCertOwner.replace("ool", sL);
            sCertOwner = sCertOwner.replace("ooS", sS);
            sCertOwner = sCertOwner.replace("ooC", sC);

            X500Name owner = new X500Name(sCertOwner);
            //证书信息对象
            X509CertInfo info = new X509CertInfo();
            //有效期
            info.set(X509CertInfo.VALIDITY, interval);
            //
            info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(sn));
            //证书所有人
            info.set(X509CertInfo.SUBJECT, new CertificateSubjectName(owner));
            //证书颁发者
            info.set(X509CertInfo.ISSUER, new CertificateIssuerName(owner));
            //公钥
            info.set(X509CertInfo.KEY, new CertificateX509Key(keyPair.getPublic()));
            //版本
            info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
            //算法
            AlgorithmId algo = new AlgorithmId(AlgorithmId.md5WithRSAEncryption_oid);
            info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algo));

            X509CertImpl cert = new X509CertImpl(info);
            cert.sign(keyPair.getPrivate(), "SHA1withRSA");

            KeyStore store = KeyStore.getInstance("PKCS12");
            //System.out.println(sNewPfxPath);
            store.load(null, null);
            store.setKeyEntry("esspfx", keyPair.getPrivate(), sPwd.toCharArray(), new java.security.cert.Certificate[] { cert });

            //生成pfx证书
            String pfxPath = "D:\\temp\\server.pfx";
            FileOutputStream fos =new FileOutputStream(pfxPath);
            store.store(fos, sPwd.toCharArray());
            fos.close();

            //生成cer证书
            BASE64Encoder encoder = new BASE64Encoder();
            String cerBase64 = encoder.encode(cert.getEncoded());
//            FileWriter fw = new FileWriter("D:\\temp\\server.cer");
//            fw.write(cerBase64);
//            fw.close();
            try {
                // 根据绝对路径初始化文件
                File localFile = new File("D:\\temp\\server.cer");
                if (!localFile.exists()) {
                    localFile.createNewFile();
                }
                // 输出流
                OutputStream os = new FileOutputStream(localFile);
                os.write(cert.getEncoded());
                os.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            String pfxBase64 = Base64Utils.encodeBase64File(pfxPath);

            Map<String, String> CerAndPfxMap =  new HashMap<String, String>();
            CerAndPfxMap.put("pfxBase64", pfxBase64);
            CerAndPfxMap.put("cerBase64", cerBase64);

            return CerAndPfxMap;
        } catch (Exception e1) {
            System.out.println(e1.toString());
        }
        return  null;
    }

    private static String GetDefaultCertOwnerInfo()
    {
        return "CN = ccn,OU = oou,O = oox,L = ool,S = ooS,C = ooC";
    }

    public static void main(String[] args){
//        GenX509Cert g = new GenX509Cert();
        CreatePfxFile();

    }
}