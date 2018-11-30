package com.clt.ess.utils;

import com.clt.ess.base.Constant;
import com.clt.ess.entity.IssuerUnit;
import com.multica.crypt.MuticaCryptException;
import sun.misc.BASE64Encoder;
import sun.security.x509.*;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static com.clt.ess.utils.FileUtil.byte2File;
import static com.clt.ess.utils.dateUtil.getDate;
import static com.clt.ess.utils.uuidUtil.getUUID;
import static com.multica.crypt.MuticaCrypt.ESSGetBase64Decode;

public class CertificateUtil {

    private static int HASHBUFSIZE = 1024*1024;

    public static byte[] ESSGetDigest(byte[] bMsg){
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            int iOffset = 0;
            do {
                int len = bMsg.length - iOffset;
                if (len > HASHBUFSIZE)
                    len = HASHBUFSIZE;
                md.update(bMsg, iOffset, len);
                iOffset += len;
            } while (iOffset < bMsg.length);
            return md.digest();
        }catch(GeneralSecurityException e)
        {
            return null;
        }
    }
    /***
     * 这个函数根据传入的证书，返回颁发者证书（也就是根证书）的私钥
     * 参数在这里没有任何意义
     * */
    private static PrivateKey GetPrivateKeyByCert(IssuerUnit issuerUnit)
    {
        //作为根证书的PFX证书路径
//        String sFile = IssuerUnitRootPath;
        //作为根证书的PFX证书的密码
        String sPwd = "111111";
        String sFileType = "PKCS12";
        try
        {
            FileInputStream fis;
            fis = new FileInputStream(issuerUnit.getIssuerUnitPfx());
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

        }catch(GeneralSecurityException e)
        {

        }
        return null;
    }


    /**
     * 这个函数返回根证书的基本信息
     * **/
    private static X500Name GetIssuerInfo(IssuerUnit issuerUnit)
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

    /*
     * 这个按说再是不用管，一时半会用不上
     * 生成自签名证书 ，应将其改造一下。
     * 第一句 x500String= 后面的内容应该改为传入
     * CN 是印章或人员的身份证号码
     * OU 是部门名称，如果是人员，填写其姓名
     * O 是单位名称，如果是人员，填写“ ”
     * L 是单位所在地（市级），如果是人员，填“ ”
     * S 是单位所在地（省级）
     * C 固定是 中国
     * **/
    public static void CreatePfxItSelf() throws NoSuchAlgorithmException, IOException, CertificateException, KeyStoreException, InvalidKeyException, NoSuchProviderException, SignatureException
    {
        String x500String = "CN = 乌江渡发电厂电子签章,OU = 乌江渡发电厂电子签章,O = 乌江渡发电厂,L = 遵义,S = 贵州,C = 中国";
        long lDateLen = 10*365;
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        KeyPair keyPair = kpg.generateKeyPair();
        X509CertInfo info = new X509CertInfo();
        Date from = new Date(new Date().getTime()-86400000l);
        long time = from.getTime();
        long agoTime = time-86400000l;
        Date to = new Date(agoTime + lDateLen * 86400000l);
        CertificateValidity interval = new CertificateValidity(from, to);
        BigInteger sn = new BigInteger(64, new SecureRandom());

        X500Name owner = new X500Name(x500String);

        X500Name bIssuer = new X500Name(x500String);
        info.set(X509CertInfo.VALIDITY, interval);
        info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(sn));
        info.set(X509CertInfo.SUBJECT, new CertificateSubjectName(owner));
        info.set(X509CertInfo.ISSUER, new CertificateIssuerName(bIssuer));
        info.set(X509CertInfo.KEY, new CertificateX509Key(keyPair.getPublic()));
        info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
        AlgorithmId algo = new AlgorithmId(AlgorithmId.md5WithRSAEncryption_oid);
        info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algo));

        X509CertImpl cert = new X509CertImpl(info);
        cert.sign(keyPair.getPrivate(), "MD5WithRSA");
        KeyStore store = KeyStore.getInstance("PKCS12");
        store.load(null, null);
        store.setKeyEntry("esspfx", keyPair.getPrivate(), "123456".toCharArray(), new Certificate[] { cert });
        FileOutputStream fos =new FileOutputStream("d:/temp/selfPfx.pfx");
        store.store(fos, "123456".toCharArray());
        fos.close();
        return;
    }


    private static String GetDefaultCertOwnerInfo()
    {
        return "CN = ccn,OU = oou,O = oox,L = ool,S = ooS,C = ooC";
    }



    /**
     * @param sC			所在国家
     * @param sS			所在省
     * @param sL 			所在市
     * @param sO			单位名称
     * @param sOU			部门（单位）名称
     * @param sDN			印章名称或个人姓名
     * @param dateStart     有效期起始
     * @param dateEnd		有效期到期
     * @param sPwd			新证书的使用密钥   6--8  字符  数字 组合
     * @param algorithm     签名算法
     * @return				返回新生成的cer证书
    //	 * @throws MuticaCryptException
     */
    public static  Map<String, String> CreatePfxFile(String sC,String sS,String sL,String sO,String sOU,String sDN,Date dateEnd,Date dateStart,
                                       String sPwd,String algorithm,IssuerUnit issuerUnit)
    {
        /*
         *  先生成一份自签名证书，然后对自签名证书的公钥证书使用颁发者证书签�?
         * */
        try {
            //获取颁发者证书私钥
            PrivateKey issuer_PrivateKey = GetPrivateKeyByCert(issuerUnit);
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
            String pfxPath =Constant.PFX_FILE_PATH+getUUID()+".pfx";
            FileOutputStream fos =new FileOutputStream(pfxPath);
            store.store(fos, sPwd.toCharArray());
            fos.close();
            //生成cer证书
            BASE64Encoder encoder = new BASE64Encoder();
            String cerBase64 = encoder.encode(cert.getEncoded());
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

//    public static void createFile(String filePath) {
//        File folder = new File(filePath);
//        //文件夹路径不存在
//        if (!folder.exists() && !folder.isDirectory()) {
//            System.out.println("文件夹路径不存在，创建路径:" + filePath);
//            folder.mkdirs();
//        } else {
//            System.out.println("文件夹路径存在:" + filePath);
//        }
//    }


    /**
     * 获取私钥别名等信息
     */
    public static String getPrivateKeyInfo(String privKeyFileString,String privKeyPswdString)
    {
//        String privKeyFileString = Conf_Info.PrivatePath;
//        String privKeyPswdString = "" + Conf_Info.password;
        String keyAlias = null;
        try
        {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            FileInputStream fileInputStream = new FileInputStream(privKeyFileString);
            char[] nPassword = null;
            if ((privKeyPswdString == null) || privKeyPswdString.trim().equals(""))
            {
                nPassword = null;
            } else
            {
                nPassword = privKeyPswdString.toCharArray();
            }
            keyStore.load(fileInputStream, nPassword);
            fileInputStream.close();
            System.out.println("keystore type=" + keyStore.getType());

            Enumeration<String> enumeration = keyStore.aliases();

            if (enumeration.hasMoreElements())
            {
                keyAlias = (String) enumeration.nextElement();
                System.out.println("alias=[" + keyAlias + "]");
            }
            System.out.println("is key entry=" + keyStore.isKeyEntry(keyAlias));
            PrivateKey prikey = (PrivateKey) keyStore.getKey(keyAlias, nPassword);
            Certificate cert = keyStore.getCertificate(keyAlias);
            PublicKey pubkey = cert.getPublicKey();
            System.out.println("cert class = " + cert.getClass().getName());
            System.out.println("cert = " + cert);
            System.out.println("public key = " + pubkey);
            System.out.println("private key = " + prikey);

        } catch (Exception e)
        {
            System.out.println(e);
        }
        return keyAlias;
    }


    public static void main(String[] args) throws Exception {

//        CreatePfxFile("1","1","1","1","1",new Date(),new Date(),"123456","D:\\temp\\demo.pfx",
//                "D:\\temp\\demo.cer","SHA1withRSA",
//                "D:\\temp\\root.pfx");

//        //获取颁发者证书私钥
//        PrivateKey issuer_PrivateKey = GetPrivateKeyByCert("D:\\temp\\root.pfx");
//        //读取证书文件
//        String base = "MIIDwTCCAyqgAwIBAgIBADANBgkqhkiG9w0BAQUFADCBtTENMAsGA1UEBh4ETi1W/TENMAsGA1UECB4EW4lfvTENMAsGA1UEBx4EgpxuVjElMCMGA1UECh4cgpxuVl4CTrpSm41EbpBUjHk+TxpP3ZacXEAAIDElMCMGA1UECx4cgpxuVl4CTrpSm41EbpBUjHk+TxpP3ZacXEAAIDElMCMGA1UEAx4cgpxuVl4CTrpSm41EbpBUjHk+TxpP3ZacXEAAIDERMA8GCSqGSIb3DQEJAR4CACAwHhcNMTcxMjA2MDEzNzAxWhcNMjcxMjA0MDEzNzAxWjCBtTENMAsGA1UEBh4ETi1W/TENMAsGA1UECB4EW4lfvTENMAsGA1UEBx4EgpxuVjElMCMGA1UECh4cgpxuVl4CTrpSm41EbpBUjHk+TxpP3ZacXEAAIDElMCMGA1UECx4cgpxuVl4CTrpSm41EbpBUjHk+TxpP3ZacXEAAIDElMCMGA1UEAx4cgpxuVl4CTrpSm41EbpBUjHk+TxpP3ZacXEAAIDERMA8GCSqGSIb3DQEJAR4CACAwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBANGfGJR9qo+4x0XS8P5qQSBMO736zuOAun/7C9BGzMTkXx737ic8vZy012PFiniTMf3JpcktuIMKIw25KlJoO2fLny20pv6kvl/mVwduHDsRw1BhrzgaRkKcoPhIDQIu0RLpdikccISMhLiY1GuVFX5SGTg538kJ2OPR/kjAWasJAgMBAAGjgd4wgdswJAYKKwYBBAGpQ2QCBQQWFhRJRDEzMDIwMzE5NzcwMzA2MDYxODAMBgNVHRMEBTADAQH/MAsGA1UdDwQEAwIBBjCBlwYDVR0lBIGPMIGMBggrBgEFBQcDAwYIKwYBBQUHAwEGCCsGAQUFBwMEBggrBgEFBQcDAgYIKwYBBQUHAwgGCisGAQQBgjcCARYGCisGAQQBgjcKAwEGCisGAQQBgjcKAwMGCisGAQQBgjcKAwQGCisGAQQBgjcUAgIGCCsGAQUFBwMFBggrBgEFBQcDBgYIKwYBBQUHAwcwDQYJKoZIhvcNAQEFBQADgYEALCNCtkrpZKUjm7vSwmSWq0oWEM6L0Lknu58G+PtaMJWeafADZrTToO3P8qNDq7t61Ai85hEwPX2pH6qAwHswgpO31Lz5Jq43JaN+FOAwufHmpAyjOtLsSRsTG9BL7SALGIlX7LkJBHuZMflbwJ4v0wHqoY0iTre3xkvishdL9ng=";
//        byte[] cerByte = ESSGetBase64Decode("MIIDwTCCAyqgAwIBAgIBADANBgkqhkiG9w0BAQUFADCBtTENMAsGA1UEBh4ETi1W/TENMAsGA1UECB4EW4lfvTENMAsGA1UEBx4EgpxuVjElMCMGA1UECh4cgpxuVl4CTrpSm41EbpBUjHk+TxpP3ZacXEAAIDElMCMGA1UECx4cgpxuVl4CTrpSm41EbpBUjHk+TxpP3ZacXEAAIDElMCMGA1UEAx4cgpxuVl4CTrpSm41EbpBUjHk+TxpP3ZacXEAAIDERMA8GCSqGSIb3DQEJAR4CACAwHhcNMTcxMjA2MDEzNzAxWhcNMjcxMjA0MDEzNzAxWjCBtTENMAsGA1UEBh4ETi1W/TENMAsGA1UECB4EW4lfvTENMAsGA1UEBx4EgpxuVjElMCMGA1UECh4cgpxuVl4CTrpSm41EbpBUjHk+TxpP3ZacXEAAIDElMCMGA1UECx4cgpxuVl4CTrpSm41EbpBUjHk+TxpP3ZacXEAAIDElMCMGA1UEAx4cgpxuVl4CTrpSm41EbpBUjHk+TxpP3ZacXEAAIDERMA8GCSqGSIb3DQEJAR4CACAwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBANGfGJR9qo+4x0XS8P5qQSBMO736zuOAun/7C9BGzMTkXx737ic8vZy012PFiniTMf3JpcktuIMKIw25KlJoO2fLny20pv6kvl/mVwduHDsRw1BhrzgaRkKcoPhIDQIu0RLpdikccISMhLiY1GuVFX5SGTg538kJ2OPR/kjAWasJAgMBAAGjgd4wgdswJAYKKwYBBAGpQ2QCBQQWFhRJRDEzMDIwMzE5NzcwMzA2MDYxODAMBgNVHRMEBTADAQH/MAsGA1UdDwQEAwIBBjCBlwYDVR0lBIGPMIGMBggrBgEFBQcDAwYIKwYBBQUHAwEGCCsGAQUFBwMEBggrBgEFBQcDAgYIKwYBBQUHAwgGCisGAQQBgjcCARYGCisGAQQBgjcKAwEGCisGAQQBgjcKAwMGCisGAQQBgjcKAwQGCisGAQQBgjcUAgIGCCsGAQUFBwMFBggrBgEFBQcDBgYIKwYBBQUHAwcwDQYJKoZIhvcNAQEFBQADgYEALCNCtkrpZKUjm7vSwmSWq0oWEM6L0Lknu58G+PtaMJWeafADZrTToO3P8qNDq7t61Ai85hEwPX2pH6qAwHswgpO31Lz5Jq43JaN+FOAwufHmpAyjOtLsSRsTG9BL7SALGIlX7LkJBHuZMflbwJ4v0wHqoY0iTre3xkvishdL9ng=");
//        singCertByPrivateKey(cerByte,issuer_PrivateKey);

        //2.执行签名
//        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(issuer_PrivateKey.getEncoded());
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
//
//        Signature signature = Signature.getInstance("MD5withRSA");
//        signature.initSign(issuer_PrivateKey);
//        signature.update(cerByte);
//        byte[] result = signature.sign();
//
//        //生成cer证书
//        BASE64Encoder encoder = new BASE64Encoder();
//        String cerBase64 = encoder.encode(result);
//
//        FileWriter fw = new FileWriter("D:\\temp\\demo1.cer");
//        fw.write(cerBase64);
//        fw.close();



    }

}
