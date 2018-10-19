package com.clt.ess.web;

import com.clt.ess.dao.ISealImgDao;
import com.clt.ess.entity.SealImg;
import com.clt.ess.utils.CertUtils;
import com.clt.ess.utils.CertificateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.util.Enumeration;

public class main {

    public static void main(String[] args) {

       if(true){
           System.out.print("1233");
       }



    }
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



}
