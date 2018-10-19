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

    /**
     * 获取颁发者cer证书信息
     * @param issuerUnit
     * @return
     */
    public static X500Name GetIssuerInfo(IssuerUnit issuerUnit)
    {
        File f = new File(issuerUnit.getIssuerUnitRoot());
        long len = f.length();
        byte[] bIssuerPfx = new byte[(int) len];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            fis.read(bIssuerPfx);
            fis.close();
            X509CertImpl cimpl=new X509CertImpl(bIssuerPfx);
            X509CertInfo cinfol=(X509CertInfo)cimpl.get(X509CertImpl.NAME+"."+X509CertImpl.INFO);
            X500Name bIssuer=(X500Name)cinfol.get(X509CertInfo.SUBJECT+"."+CertificateIssuerName.DN_NAME);
            return bIssuer;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从cer证书base64 中读取信息
     * @param cerBase64
     * @return
     */
    public static Map<String,String> showCertInfo(String cerBase64) {
        Map<String,String> cerInfoMap = new HashMap<>();
        try {
            //读取证书文件
            byte[] cerByte = ESSGetBase64Decode(cerBase64);

            InputStream inStream = byte2Input(cerByte);

            //创建X509工厂类
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            //创建证书对象
            X509Certificate oCert = (X509Certificate)cf.generateCertificate(inStream);
            inStream.close();
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            String info = null;
            //获得证书版本
            cerInfoMap.put("version",String.valueOf(oCert.getVersion()));
            //获得证书序列号
            cerInfoMap.put("sn",oCert.getSerialNumber().toString(16));
            //获得证书有效期
            cerInfoMap.put("startTime",dateformat.format(oCert.getNotBefore()));
            //获得证书失效日期
            cerInfoMap.put("endTime",dateformat.format(oCert.getNotAfter()));
            //获得证书主体信息
            cerInfoMap.put("owner",oCert.getSubjectDN().getName());
//            System.out.println("证书扩展域:" + oCert.getSubjectDN().getName());
            String[] a = oCert.getSubjectDN().getName().split(", ");
            for(String a12:a){
                System.out.println(a12);
            }
            //获得证书颁发者信息
            cerInfoMap.put("issuer",oCert.getIssuerDN().getName());
//            System.out.println("证书扩展域:" + oCert.getIssuerDN().getName());
            //获得证书签名算法名称
            cerInfoMap.put("algorithm",oCert.getSigAlgName());

//            byte[] byt = oCert.getExtensionValue("1.2.86.11.7.9");
//            String strExt = new String(byt);
//            System.out.println("证书扩展域:" + strExt);
//            byt = oCert.getExtensionValue("1.2.86.11.7.1.8");
//            String strExt2 = new String(byt);
//            System.out.println("证书扩展域2:" + strExt2);
        }
        catch (Exception e) {
            System.out.println("解析证书出错！");
        }

        return cerInfoMap;
    }//end showCertInfo

    private static String GetDefaultCertOwnerInfo()
    {
        return "CN = ccn,OU = oou,O = oox,L = ool,S = ooS,C = 中国";
    }

    /**
     * @param sS			所在省
     * @param sL 			所在市
     * @param sO			单位名称
     * @param sOU			部门（单位）名称
     * @param sDN			印章名称或个人姓名
     * @param dateStart     有效期起始
     * @param dateEnd		有效期到期
     * @param sPwd			新证书的使用密钥   6--8  字符  数字 组合
     * @param sNewPfxPath	生成的PFX证书的保存路径
     * @param sNewCerPath	生成的CER证书的保存路径
     * @param algorithm     签名算法
     * @return				返回新生成的cer证书
    //	 * @throws MuticaCryptException
     */
    public static  Map<String, String> CreatePfxFile(String sS, String sL, String sO, String sOU, String sDN, Date dateEnd, Date dateStart,
                                                     String sPwd, String sNewPfxPath, String sNewCerPath, String algorithm, IssuerUnit issuerUnit)
    {
        /*
         *  先生成一份自签名证书，然后对自签名证书的公钥证书使用颁发者证书签名
         * */
        try {
            //获取颁发者证书私钥
            PrivateKey issuer_PrivateKey = GetPrivateKey(issuerUnit);
            //根证书的基本信息
            X500Name bIssuer=GetIssuerInfo(issuerUnit);
            if(bIssuer == null){
                return null;
            }
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            //获得密钥对
            KeyPair keyPair = kpg.generateKeyPair();
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
            //证书信息对象
            X509CertInfo info = new X509CertInfo();
            //有效期
            info.set(X509CertInfo.VALIDITY, interval);
            //
            info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(sn));
            //证书所有人
            info.set(X509CertInfo.SUBJECT, new CertificateSubjectName(owner));
            //证书颁发者
            info.set(X509CertInfo.ISSUER, new CertificateIssuerName(bIssuer));
            //公钥
            info.set(X509CertInfo.KEY, new CertificateX509Key(keyPair.getPublic()));
            //版本
            info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
            //算法
            AlgorithmId algo = new AlgorithmId(AlgorithmId.md5WithRSAEncryption_oid);
            info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algo));

            X509CertImpl cert = new X509CertImpl(info);
            cert.sign(issuer_PrivateKey, algorithm);

            KeyStore store = KeyStore.getInstance("PKCS12");
            //System.out.println(sNewPfxPath);
            store.load(null, null);
            store.setKeyEntry("esspfx", keyPair.getPrivate(), sPwd.toCharArray(), new Certificate[] { cert });

            //生成pfx证书
            FileOutputStream fos =new FileOutputStream(sNewPfxPath);
            store.store(fos, sPwd.toCharArray());
            fos.close();

            //生成cer证书
            BASE64Encoder encoder = new BASE64Encoder();
            String cerBase64 = encoder.encode(cert.getEncoded());

            FileWriter fw = new FileWriter(sNewCerPath);
            fw.write(cerBase64);
            fw.close();

            String pfxBase64 = Base64Utils.encodeBase64File(sNewPfxPath);
//            String pfxBase64 = Base64Utils.encodeBase64File(sNewPfxPath);
            Map<String, String> CerAndPfxMap =  new HashMap<String, String>();

            CerAndPfxMap.put("pfxBase64", pfxBase64);

            CerAndPfxMap.put("cerBase64", cerBase64);
            return CerAndPfxMap;


        } catch (Exception e1) {
            System.out.println(e1);
        }
        return  null;
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
//            //返回新证书路径
//            File f = new File("d:/out.cer");
//            FileOutputStream fos = new FileOutputStream(f);
//            fos.write(bOutCert);
//            fos.close();

        }catch (Exception e){
            System.out.print(e);
            return null;
        }
        return bOutCert;

    }


    public static void main(String[] args) throws Exception {

//        IssuerUnit issuerUnit = new IssuerUnit();
//        issuerUnit.setIssuerUnitPfx("d:/temp/root.pfx");
//        issuerUnit.setIssuerUnitRoot("d:/temp/root.cer");
//        issuerUnit.setPfxPwd("111111");
//
//
//        byte[] cerByte = ESSGetBase64Decode("MIIDwTCCAyqgAwIBAgIBADANBgkqhkiG9w0BAQUFADCBtTENMAsGA1UEBh4ETi1W/TENMAsGA1UECB4EW4lfvTENMAsGA1UEBx4EgpxuVjElMCMGA1UECh4cgpxuVl4CTrpSm41EbpBUjHk+TxpP3ZacXEAAIDElMCMGA1UECx4cgpxuVl4CTrpSm41EbpBUjHk+TxpP3ZacXEAAIDElMCMGA1UEAx4cgpxuVl4CTrpSm41EbpBUjHk+TxpP3ZacXEAAIDERMA8GCSqGSIb3DQEJAR4CACAwHhcNMTcxMjA2MDEzNzAxWhcNMjcxMjA0MDEzNzAxWjCBtTENMAsGA1UEBh4ETi1W/TENMAsGA1UECB4EW4lfvTENMAsGA1UEBx4EgpxuVjElMCMGA1UECh4cgpxuVl4CTrpSm41EbpBUjHk+TxpP3ZacXEAAIDElMCMGA1UECx4cgpxuVl4CTrpSm41EbpBUjHk+TxpP3ZacXEAAIDElMCMGA1UEAx4cgpxuVl4CTrpSm41EbpBUjHk+TxpP3ZacXEAAIDERMA8GCSqGSIb3DQEJAR4CACAwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBANGfGJR9qo+4x0XS8P5qQSBMO736zuOAun/7C9BGzMTkXx737ic8vZy012PFiniTMf3JpcktuIMKIw25KlJoO2fLny20pv6kvl/mVwduHDsRw1BhrzgaRkKcoPhIDQIu0RLpdikccISMhLiY1GuVFX5SGTg538kJ2OPR/kjAWasJAgMBAAGjgd4wgdswJAYKKwYBBAGpQ2QCBQQWFhRJRDEzMDIwMzE5NzcwMzA2MDYxODAMBgNVHRMEBTADAQH/MAsGA1UdDwQEAwIBBjCBlwYDVR0lBIGPMIGMBggrBgEFBQcDAwYIKwYBBQUHAwEGCCsGAQUFBwMEBggrBgEFBQcDAgYIKwYBBQUHAwgGCisGAQQBgjcCARYGCisGAQQBgjcKAwEGCisGAQQBgjcKAwMGCisGAQQBgjcKAwQGCisGAQQBgjcUAgIGCCsGAQUFBwMFBggrBgEFBQcDBgYIKwYBBQUHAwcwDQYJKoZIhvcNAQEFBQADgYEALCNCtkrpZKUjm7vSwmSWq0oWEM6L0Lknu58G+PtaMJWeafADZrTToO3P8qNDq7t61Ai85hEwPX2pH6qAwHswgpO31Lz5Jq43JaN+FOAwufHmpAyjOtLsSRsTG9BL7SALGIlX7LkJBHuZMflbwJ4v0wHqoY0iTre3xkvishdL9ng=");
//        cerByte = getfileBytes("d:/temp/root.cer");
//        cerByte = signCertByIssuerUnit(cerByte,issuerUnit);
//        File f = new File("d:/out.cer");
//        FileOutputStream fos = new FileOutputStream(f);
//        fos.write(cerByte);
//        fos.close();
    }


}
