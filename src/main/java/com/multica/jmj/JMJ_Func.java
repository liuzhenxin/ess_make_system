package com.multica.jmj;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.util.Enumeration;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import sun.security.x509.CertificateIssuerName;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;
public class JMJ_Func {
	//CA PFX证书
	private static String pfxPath = null;
	//CA 公钥证书
	private static String certPath = null;
	//pfx 证书的密码
	private static String pfxPwd = null;
	//默认的颁发给信息
	private static String dnVal = "CN = ccn,OU = oou,O = oox,L = 成都,S = 四川,C = 中国";
	//设置pfx信息
	public static void SetPFxInfo(String sPfxPath,String sCertPath,String sPfxPwd)
	{
		pfxPath = sPfxPath;
		certPath = sCertPath;
		pfxPwd = sPfxPwd;
		if(pfxPath ==  null)
			pfxPath = "D:/uploadImg/rootPfx/rootPfx.pfx";
		if(certPath == null)
			certPath = "D:/uploadImg/rootPfx/rootCer.cer";
		if(pfxPwd == null)
			pfxPwd = "11111111";
	}
	public static String GetDefaultCertOwnerInfo(String sInfo)
	{
		return dnVal;
	}
	//从加密机获取3DES对称加密密钥
	public static byte[] GenerateRandomNumber() throws JMJ_Exception{
		try
		{
			KeyGenerator kg = KeyGenerator.getInstance("DESede");
			kg.init(168);
			Key key = kg.generateKey();
			if (key == null)
				return null;
			else
				return key.getEncoded();
		}catch(GeneralSecurityException e1)
		{
			throw(new JMJ_Exception(e1.getMessage()));
		}
	}
	
	//获取某种类型的Chipher
	//0: RSA/ECB/PKCS1Padding   
	public static Cipher GetCipher(int iCipherType) throws JMJ_Exception 
	{
		try
		{
			String sTransformation = null;
			if(iCipherType == 0)
				sTransformation = "RSA/ECB/PKCS1Padding";
			if(sTransformation == null)
				return null;
			else
			{
				Cipher c = Cipher.getInstance(sTransformation);
				return c;	
			}
		}catch(NoSuchAlgorithmException e1)
		{
			throw(new JMJ_Exception(e1.getMessage()));
		}catch(GeneralSecurityException e2)
		{
			throw(new JMJ_Exception(e2.getMessage()));
		}
	}
	
	//根据指定条件返回对应的颁发�?信息
	public static X500Name GetIssuerInfo(String sInfo)
	{
		File f = new File(certPath);
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
	
	//根据传入的证书，获取加密机上对应的私�?
	public static PrivateKey GetPrivateKeyByCert(byte[] bCert) throws JMJ_Exception
	{
		//PFX证书路径
		String sFile = pfxPath;
		//PFX证书的密�?
		String sPwd = pfxPwd;
		String sFileType = "PKCS12";
		try
		{
			FileInputStream fis;
			fis = new FileInputStream(sFile);
			char[] nPassword = null;
			nPassword = sPwd.toCharArray();
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
	
	//初始化Signature对象
	public static Signature InitSignature() throws JMJ_Exception 
	{
		try
		{
			Signature signatue = null;
			signatue = Signature.getInstance("MD5withRSA");
			return signatue;
		}catch(NoSuchAlgorithmException e1)
		{
			throw(new JMJ_Exception(e1.getMessage()));
		}
	}
	
	
	//初始化Signature对象
	public static Signature InitSignature(String sAlg) throws JMJ_Exception 
	{
		try
		{
			Signature signatue = null;
			signatue = Signature.getInstance(sAlg);
			return signatue;
		}catch(NoSuchAlgorithmException e1)
		{
			throw(new JMJ_Exception(e1.getMessage()));
		}
	}
	
}
