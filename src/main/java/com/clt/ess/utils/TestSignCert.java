package com.clt.ess.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;

import com.multica.jmj.JMJ_Exception;
import com.multica.jmj.JMJ_Func;

import sun.security.x509.AlgorithmId;
import sun.security.x509.CertificateAlgorithmId;
import sun.security.x509.CertificateIssuerName;
import sun.security.x509.CertificateSerialNumber;
import sun.security.x509.CertificateSubjectName;
import sun.security.x509.CertificateValidity;
import sun.security.x509.CertificateVersion;
import sun.security.x509.CertificateX509Key;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;

import static com.multica.crypt.MuticaCrypt.ESSGetBase64Decode;

public class TestSignCert {
	
	public static X500Name GetIssuerInfo(String rootCertPath)
	{
		File f = new File(rootCertPath);
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

	
	public static PrivateKey GetPrivateKey(String sPfxFile,String sPwd) throws JMJ_Exception
	{
		String sFile = sPfxFile;
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
	
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		File f = new File("d:/test.cer");
//		FileInputStream fis = new FileInputStream(f);
//		byte[] bCert = new byte[(int)f.length()];
		byte[] bCert = ESSGetBase64Decode("MIIDwTCCAyqgAwIBAgIBADANBgkqhkiG9w0BAQUFADCBtTENMAsGA1UEBh4ETi1W/TENMAsGA1UECB4EW4lfvTENMAsGA1UEBx4EgpxuVjElMCMGA1UECh4cgpxuVl4CTrpSm41EbpBUjHk+TxpP3ZacXEAAIDElMCMGA1UECx4cgpxuVl4CTrpSm41EbpBUjHk+TxpP3ZacXEAAIDElMCMGA1UEAx4cgpxuVl4CTrpSm41EbpBUjHk+TxpP3ZacXEAAIDERMA8GCSqGSIb3DQEJAR4CACAwHhcNMTcxMjA2MDEzNzAxWhcNMjcxMjA0MDEzNzAxWjCBtTENMAsGA1UEBh4ETi1W/TENMAsGA1UECB4EW4lfvTENMAsGA1UEBx4EgpxuVjElMCMGA1UECh4cgpxuVl4CTrpSm41EbpBUjHk+TxpP3ZacXEAAIDElMCMGA1UECx4cgpxuVl4CTrpSm41EbpBUjHk+TxpP3ZacXEAAIDElMCMGA1UEAx4cgpxuVl4CTrpSm41EbpBUjHk+TxpP3ZacXEAAIDERMA8GCSqGSIb3DQEJAR4CACAwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBANGfGJR9qo+4x0XS8P5qQSBMO736zuOAun/7C9BGzMTkXx737ic8vZy012PFiniTMf3JpcktuIMKIw25KlJoO2fLny20pv6kvl/mVwduHDsRw1BhrzgaRkKcoPhIDQIu0RLpdikccISMhLiY1GuVFX5SGTg538kJ2OPR/kjAWasJAgMBAAGjgd4wgdswJAYKKwYBBAGpQ2QCBQQWFhRJRDEzMDIwMzE5NzcwMzA2MDYxODAMBgNVHRMEBTADAQH/MAsGA1UdDwQEAwIBBjCBlwYDVR0lBIGPMIGMBggrBgEFBQcDAwYIKwYBBQUHAwEGCCsGAQUFBwMEBggrBgEFBQcDAgYIKwYBBQUHAwgGCisGAQQBgjcCARYGCisGAQQBgjcKAwEGCisGAQQBgjcKAwMGCisGAQQBgjcKAwQGCisGAQQBgjcUAgIGCCsGAQUFBwMFBggrBgEFBQcDBgYIKwYBBQUHAwcwDQYJKoZIhvcNAQEFBQADgYEALCNCtkrpZKUjm7vSwmSWq0oWEM6L0Lknu58G+PtaMJWeafADZrTToO3P8qNDq7t61Ai85hEwPX2pH6qAwHswgpO31Lz5Jq43JaN+FOAwufHmpAyjOtLsSRsTG9BL7SALGIlX7LkJBHuZMflbwJ4v0wHqoY0iTre3xkvishdL9ng=");
//		fis.read(bCert);
//		fis.close();
		
		X500Name bIssuer=TestSignCert.GetIssuerInfo("d:/temp/root.cer");
		//颁发者
		System.out.println(bIssuer.toString());
		
		InputStream sbs = new ByteArrayInputStream(bCert); 
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		X509Certificate cert = (X509Certificate)cf.generateCertificate(sbs);		
		
		X509CertInfo info = new X509CertInfo();
		Date from = new Date();
		Date to = new Date(from.getTime() + 365 * 86400000l);
		CertificateValidity interval = new CertificateValidity(from, to);	
		BigInteger sn = new BigInteger(64, new SecureRandom());
		String sCertOwner = "CN=ccn, OU=oou, O=o, L=l, S=s, C=cn";
//		sCertOwner = sCertOwner.replace("ccn", "���");
//		sCertOwner = sCertOwner.replace("oou", "ר���ල�����");
		System.out.println(sCertOwner);
		X500Name owner = new X500Name(sCertOwner);
		
		info.set(X509CertInfo.VALIDITY, interval);
		info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(sn));
		info.set(X509CertInfo.SUBJECT, new CertificateSubjectName(owner));
//		info.set(X509CertInfo.SUBJECT, owner);
//		info.set(X509CertInfo.ISSUER, bIssuer);
		info.set(X509CertInfo.ISSUER, new CertificateIssuerName(bIssuer));
		info.set(X509CertInfo.KEY, new CertificateX509Key(cert.getPublicKey()));
		info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
		AlgorithmId algo = new AlgorithmId(AlgorithmId.md5WithRSAEncryption_oid);
		info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algo));		
		
		
		
		PrivateKey issuer_PrivateKey = TestSignCert.GetPrivateKey("d:/temp/root.pfx", "111111");


		X509CertImpl certImpl = new X509CertImpl(info);
		certImpl.sign(issuer_PrivateKey, "MD5WithRSA");

		byte[] bOutCert = certImpl.getEncoded();
		
		File f = new File("d:/out.cer");
		FileOutputStream fos = new FileOutputStream(f);
		fos.write(bOutCert);
		fos.close();
		
	}

}
