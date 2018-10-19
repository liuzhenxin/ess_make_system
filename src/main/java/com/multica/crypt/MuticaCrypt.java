package com.multica.crypt;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
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

import com.sun.jmx.snmp.*;
import com.multica.jmj.*;

@SuppressWarnings("unused")
public abstract class MuticaCrypt {
	private static final int ENCBUFSIZE = 1024 * 7;
	private static final int DECBUFSIZE = 1024 * 7;
	private static final int DESPWDLEN = 24;
	private static final int SM4PWDLEN = 16;
	private static final int HASHBUFSIZE = 1024 * 1024;
	private static final int ENCDATA = 0;
	private static final int ENCFILE = 1;
	private static final int SIGNDATARESULT = 2;
	private static final int SIGNFILERESULT = 3;
	private static final long DEFAULTFILEBUFSIZE=1024*1024;
	private static final String SIGNDATAHEAD = "ESSSIGNDATA:";
	private static final String SIGNFILEHEAD = "ESSSIGNFILE:";
	private static String ADDITIONMSG = "";
	private static String SIGNTIME	= "";
	private static String SIGNEDFILENAME = "";
	private static byte[] ENCCERT = null;
	private static byte[] SIGNCERT = null;
	
	public static int iErrMsg = 0;
	public static String m_sBase64SignCert = "";
	
	

	// 根据相应的算�?从加密机申请对应长度的对称密�?
	private static byte[] GenerateRandomNumber() throws MuticaCryptException
	{
		try {
			return JMJ_Func.GenerateRandomNumber();
		} catch (JMJ_Exception e) {
			throw(new MuticaCryptException(e.getMessage()));
		}
	}
	
	private static String GetConfVal(String sKey) throws IOException
	{
		MuticaConfig mg = new MuticaConfig();
		return mg.GetKeyVal(sKey);
	}

	// �?个连续字节转换为整型
	public static int ByteToInt(byte[] bytes) {
		int test;
		int a1 = (bytes[3] & 0xFF) << 24;
		int a2 = (bytes[2] & 0xFF) << 16;
		int a3 = (bytes[1] & 0xFF) << 8;
		int a4 = (bytes[0] & 0xFF);
		test = a1 | a2 | a3 | a4;
		return test;
	}

	// 将整数转换为4个连续字�?
	public static byte[] IntToBytes(int i) {
		int test = i;
		byte[] b = new byte[4];
		b[0] = (byte) (test & 0xFF);
		b[1] = (byte) ((test & 0x0000FF00) >> 8);
		b[2] = (byte) ((test & 0x00FF0000) >> 16);
		b[3] = (byte) ((test & 0xFF000000) >> 24);
		return b;

	}

	// 将长整型转换�?个字�?
	private static long ByteToLong(byte[] bytes) {
		long lRet = 0;
		long l1 = (bytes[7] & 0xFF) << (64 - 8);
		long l2 = (bytes[6] & 0xFF) << (64 - 8 * 2);
		long l3 = (bytes[5] & 0xFF) << (64 - 8 * 3);
		long l4 = (bytes[4] & 0xFF) << (64 - 8 * 4);
		long l5 = (bytes[3] & 0xFF) << (64 - 8 * 5);
		long l6 = (bytes[2] & 0xFF) << (64 - 8 * 6);
		long l7 = (bytes[1] & 0xFF) << (64 - 8 * 7);
		long l8 = (bytes[0] & 0xFF) << (64 - 8 * 8);
		lRet = l1 | l2 | l3 | l4 | l5 | l6 | l7 | l8;
		return lRet;
	}

	// 将连�?个字�?转换为长整型
	private static byte[] LongToBytes(long l) {
		long test = l;
		byte[] b = new byte[8];
		b[0] = (byte) (test & 0xFF);
		b[1] = (byte) ((test & 0x0000FF00) >> 8);
		b[2] = (byte) ((test & 0x00FF0000) >> 16);
		b[3] = (byte) ((test & 0xFF000000) >> 24);
		b[4] = (byte) ((test & 0xFF000000) >> 32);
		b[5] = (byte) ((test & 0xFF000000) >> 40);
		b[6] = (byte) ((test & 0xFF000000) >> 48);
		b[7] = (byte) ((test & 0xFF000000) >> 56);
		return b;
	}
	
	public static String	ESSGetAdditionMsg()
	{
		return ADDITIONMSG;
	}
	
	public static String	ESSGetSignTime()
	{
		return SIGNTIME;
	}

	public static String	ESSGetSignedFileName()
	{
		return SIGNEDFILENAME;
	}
	// 
	/**
	 * 从X509证书中获得公�?暂不支持SM2证书)
	 * @param bCert 传入证书内容
	 * @throws MuticaCryptException
	 * @return PublicKey
	 */
	public static PublicKey getPublicKeyFromCert(byte[] bCert) throws MuticaCryptException {
		InputStream inStream = new ByteArrayInputStream(bCert);
		CertificateFactory cf = null;
		PublicKey key = null;
		try {
			cf = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate) cf
					.generateCertificate(inStream);
			key = cert.getPublicKey();
			inStream.close();
			return key;
		} catch (CertificateException e1) {
			throw(new MuticaCryptException(e1.getMessage()));
		} catch (IOException e2) {
			throw(new MuticaCryptException(e2.getMessage()));
		}
	}

	// 
	/**
	 * 计算传入数据的哈希�?
	 * @param bMsg 被计算HASH的数�?
	 * @throws MuticaCryptException
	 * @return byte[]
	 */
	public static byte[] ESSGetDigest(byte[] bMsg) throws MuticaCryptException{
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
			throw(new MuticaCryptException(e.getMessage()));
		}
	}

	// 
	/**
	 * 计算文件的哈希�?
	 * @param sFile 被计算HASH的文件路�?
	 * @throws MuticaCryptException
	 * @return byte[]
	 */
	public static byte[] ESSGetFileDigest(String sFile) throws MuticaCryptException{
		try
		{
			FileInputStream fis = new FileInputStream(sFile);
			MessageDigest md = MessageDigest.getInstance("SHA1");
			byte[] bMsg = new byte[HASHBUFSIZE];
			int i = 1;
			do {
				i = fis.read(bMsg);
				if (i > 0)
					md.update(bMsg, 0, i);
			} while (i > 0);
			fis.close();
			return md.digest();
		}catch(GeneralSecurityException e1)
		{
			throw(new MuticaCryptException(e1.getMessage()));
		}catch(IOException e2)
		{
			throw(new MuticaCryptException(e2.getMessage()));
		}
	}	
	
	
	/*****
	 * 设置加密机包中的pfx文件的信息
	 * @param sPfx
	 * @param sCert
	 * @param sPwd
	 */
	public static void SetJMJPfxInfo(String sPfx,String sCert,String sPwd)
	{
		JMJ_Func.SetPFxInfo(sPfx, sCert, sPwd);
	}
	
	
	/**
	 * 计算文件的哈希�?
	 * @param fis 被计算HASH的文件FileInputStream
	 * @throws MuticaCryptException
	 * @return byte[]
	 */
	public static byte[] ESSGetFileDigest(FileInputStream fis) throws MuticaCryptException{
		try
		{
			MessageDigest md = MessageDigest.getInstance("SHA1");
			byte[] bMsg = new byte[HASHBUFSIZE];
			int i = 1;
			do {
				i = fis.read(bMsg);
				if (i > 0)
					md.update(bMsg, 0, i);
			} while (i > 0);
			return md.digest();
		}catch(GeneralSecurityException e1)
		{
			throw(new MuticaCryptException(e1.getMessage()));
		}catch(IOException e2)
		{
			throw(new MuticaCryptException(e2.getMessage()));
		}
	}

	/**
	 * 将传入数据BASE64编码
	 * @param bMsg �?��编码的数�?
	 * @throws MuticaCryptException
	 * @return String
	 */
	public static String ESSGetBase64Encode(byte[] bMsg) {
		BASE64Encoder ben = new BASE64Encoder();
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		String sBase64File = ben.encode(bMsg);
		Matcher m = p.matcher(sBase64File);
		sBase64File = m.replaceAll("");
		return sBase64File;

	}

	/**
	 * 将传入数据BASE64解码
	 * @param sEncMsg �?��解码码的数据
	 * @throws MuticaCryptException
	 * @return byte[]
	 */
	public static byte[] ESSGetBase64Decode(String sEncMsg) throws MuticaCryptException {
		try
		{
			BASE64Decoder bdr = new BASE64Decoder();
			return bdr.decodeBuffer(sEncMsg);
		}catch(IOException e)
		{
			throw(new MuticaCryptException(e.getMessage()));
		}
	}

	// 
	/**
	 * 对称加密文件 生成新的密文文件
	 * @param sFilePath 		被加密的文件路径
	 * @param sEncFilePath		生成的密文文件的路径
	 * @param bCert				密文接收人的数字证书（不指定接收人，此参数为null�?
	 * @param bPwd				对称加密密钥(bCert 不等�?null时，此参数可以为null)
	 * @param blOverwrite		是否覆盖已有密文文件
	 * @throws MuticaCryptException
	 * @return boolean
	 */
	public static boolean ESSEncryptFile(String sFilePath, String sEncFilePath,
			byte[] bCert, byte[] bPwd, boolean blOverwrite) throws MuticaCryptException{
		
		try
		{
			String stemp = GetConfVal("FILEENCDECMAXBUFFER");
			if(stemp == null)
				stemp = Long.toString(DEFAULTFILEBUFSIZE);
	
			long lMaxBufSize = DEFAULTFILEBUFSIZE;
			try
			{
				lMaxBufSize = Long.parseLong(stemp);
			}catch(NumberFormatException e)
			{
				lMaxBufSize = DEFAULTFILEBUFSIZE;
			}
			// 加密结果�?数据类型1 +加密环境 �?字节�?+ 明文文件名称长度�?字节�?+ 明文文件名称 + 明文长度�?字节�?
			// 密文长度�?字节�?密文内容+加密算法�?字节�?是否数字信封�?字节�?证书长度�?字节�?+ 证书内容 + 加密密钥长度�?字节�?+
			// 加密密钥内容
			if (bCert == null && bPwd == null) {
				iErrMsg = 0x9005;
				return false;
			}
			File f1 = new File(sFilePath);
			String sFileName = f1.getName();
			FileInputStream fis = new FileInputStream(f1);
			File f2 = new File(sEncFilePath);
			if (f2.exists()) {
				if (!blOverwrite) {
					iErrMsg = 0x9002;
					fis.close();
					return false;
				} else {
					if (!f2.delete()) {
						iErrMsg = 0x9003;
						fis.close();
						return false;
					}
				}
			}
			FileOutputStream fos = new FileOutputStream(f2);
			File f = new File(sFilePath);
			
			long lFileLen = f.length();
			
			if (lFileLen == 0) {
				iErrMsg = 0x9004;
				fos.close();
				fis.close();
				return false;
			}
			int iAlgPwdLen = 0;
			iAlgPwdLen = DESPWDLEN;
			// 重新组织加密密码,若密码为空，则从密码机申请密�?
			// 密码不为空，则用0凑足�?��的密码长�?
			byte[] bPwdUse = null;
			if (bPwd == null)
				bPwdUse = GenerateRandomNumber();
			else {
				bPwdUse = new byte[iAlgPwdLen];
				for (int i = 0; i < iAlgPwdLen; i++)
					bPwdUse[i] = 0;
				if (bPwd.length < iAlgPwdLen)
					System.arraycopy(bPwd, 0, bPwdUse, 0, bPwd.length);
				else
					System.arraycopy(bPwd, 0, bPwdUse, 0, iAlgPwdLen);
			}
			byte[] bEncPwd = null;
			// 用证书加密密�?
			if (bCert != null) {
				PublicKey pk = getPublicKeyFromCert(bCert);
				if (pk == null) {
					iErrMsg = 0x9007;
					fis.close();
					fos.close();
					return false;
				}
				Cipher cipher = null;
				try {
					cipher = JMJ_Func.GetCipher(0);
				} catch (JMJ_Exception e) {
					fis.close();
					fos.close();
					throw(new MuticaCryptException(e.getMessage()));
				}
				cipher.init(Cipher.ENCRYPT_MODE, pk);
				bEncPwd = cipher.doFinal(bPwdUse);
			}
			// 用密码加密明�?
			SecretKey deskey = null;
			Cipher c1 = null;
			deskey = new SecretKeySpec(bPwdUse, "DESede");
			c1 = Cipher.getInstance("DESede/ECB/NoPadding");
	
			FileChannel fc = fis.getChannel();
			FileChannel fcout = fos.getChannel();
			boolean bFinal = false;
			byte[] bData = new byte[ENCBUFSIZE];
			byte[] benc = null;
			long lMax = lMaxBufSize - (lMaxBufSize % ENCBUFSIZE);
			if (lMax < ENCBUFSIZE)
				lMax = ENCBUFSIZE;
			long ltemp = 0;
			long lSize = lMax;
			fcout.write(ByteBuffer
					.wrap(new byte[1 + 1 + 4 + sFileName.length() + 8 + 8]));
			ltemp = 0;
			do {
				int iOffset = 0;
				if (lFileLen - ltemp < lMax) {
					lSize = lFileLen - ltemp;
					bFinal = true;
				}
				ByteBuffer a = fc.map(FileChannel.MapMode.READ_ONLY, ltemp, lSize);
				ltemp += lSize;
				while (a.hasRemaining()) {
					bData[iOffset++] = a.get();
					if (iOffset == ENCBUFSIZE) {
						c1.init(Cipher.ENCRYPT_MODE, deskey);
						benc = c1.doFinal(bData);
						fcout.write(ByteBuffer.wrap(benc));
						iOffset = 0;
					}
				}
				if (bFinal) {
					iOffset = iOffset + (8 - iOffset % 8);
					for (int i = iOffset; i < ENCBUFSIZE; i++)
						bData[i] = 0;
					// 加密�?�� �?���?
					c1.init(Cipher.ENCRYPT_MODE, deskey);
					benc = c1.doFinal(bData, 0, iOffset);
					fcout.write(ByteBuffer.wrap(benc));
					break;
				}
			} while (true);
			fc.close();
			int itemp3 = 0;
			// 加密算法
			bData[itemp3++] = 1;
			
			if (bCert != null) {
				bData[itemp3++] = 1; // 是否数字信封
				byte[] bLen = IntToBytes(bCert.length);
				System.arraycopy(bLen, 0, bData, itemp3, bLen.length);
				itemp3 += bLen.length; // 证书长度
				System.arraycopy(bCert, 0, bData, itemp3, bCert.length);
				itemp3 += bCert.length; // 证书内容
	
				bLen = IntToBytes(bEncPwd.length);
				System.arraycopy(bLen, 0, bData, itemp3, bLen.length);
				itemp3 += bLen.length; // 密钥长度
				System.arraycopy(bEncPwd, 0, bData, itemp3, bEncPwd.length);
				itemp3 += bEncPwd.length; // 密钥内容
			} else {
				bData[itemp3++] = 0;
			}
			fcout.write(ByteBuffer.wrap(bData, 0, itemp3));
			//返回文件�?���?
			fcout.position(0);
			int iOffset = 0;
			bData[iOffset++] = ENCFILE;
			bData[iOffset++] = 1; // 指定加密环境�?java
			// 源文件名称长�?
			byte[] bName = IntToBytes(sFileName.length());
			System.arraycopy(bName, 0, bData, iOffset, 4);
			iOffset += 4;
			// 源文件名�?
			System.arraycopy(sFileName.getBytes(), 0, bData, iOffset, sFileName
					.length());
			iOffset += sFileName.length();
			byte[] bLen = LongToBytes(lFileLen);
			System.arraycopy(bLen, 0, bData, iOffset, 8); // 被加密明文长�?
			iOffset += 8;
			bLen = LongToBytes(lFileLen + (8 - lFileLen % 8));
			System.arraycopy(bLen, 0, bData, iOffset, 8); // 密文长度
			iOffset += 8;
			fcout.write(ByteBuffer.wrap(bData, 0, iOffset));
			fcout.close();
			fis.close();
			fos.close();
			return true;
		}catch(GeneralSecurityException e1)
		{
			throw(new MuticaCryptException(e1.getMessage()));
		}catch(IOException e2)
		{
			throw(new MuticaCryptException(e2.getMessage()));
		} 
		
	}
	
	

	// 
	/**
	 * 对数�?进行加密
	 * @param bData		�?��加密的数�?
	 * @param bCert		密文接收人的数字证书（不指定接收人，此参数为null�?
	 * @param bPwd		对称加密密钥(bCert 不等�?null时，此参数可以为null)
	 * @throws MuticaCryptException
	 * @return byte[]	密文数据
	 */
	public static byte[] ESSEncryptData(byte[] bData, byte[] bCert, byte[] bPwd) throws MuticaCryptException{
		try
		{
			if (bPwd == null && bCert == null) {
				iErrMsg = 0x9005;
				return null;
			}
			int iAlgPwdLen = DESPWDLEN;
			byte[] bPwdUse = null;
			if (bPwd == null) {
				bPwdUse = GenerateRandomNumber();
			} else {
				bPwdUse = new byte[iAlgPwdLen];
				for (int i = 0; i < iAlgPwdLen; i++)
					bPwdUse[i] = 0;
				if (bPwd.length < iAlgPwdLen)
					System.arraycopy(bPwd, 0, bPwdUse, 0, bPwd.length);
				else
					System.arraycopy(bPwd, 0, bPwdUse, 0, iAlgPwdLen);
			}
			
			byte[] bEncPwd = null;
			// 用证书加密密�?
			if (bCert != null) {
				PublicKey pk = getPublicKeyFromCert(bCert);
				if (pk == null) {
					iErrMsg = 0x9007;
					return null;
				}
				Cipher cipher = JMJ_Func.GetCipher(0);
				cipher.init(Cipher.ENCRYPT_MODE, pk);
				bEncPwd = cipher.doFinal(bPwdUse);
			}
			// 用密码加密明�?
			SecretKey deskey = null;
			Cipher c1 = null;
			deskey = new SecretKeySpec(bPwdUse, "DESede");
			c1 = Cipher.getInstance("DESede/ECB/NoPadding");
	
			byte[] bEncData = new byte[bData.length
					+ (bData.length / ENCBUFSIZE + 1) * 8];
			byte[] btemp = null;
			int itemp = 0;
			int itemp2 = 0;
			int iEncIt = 0;
			do {
				if (bData.length - itemp > ENCBUFSIZE)
					iEncIt = ENCBUFSIZE;
				else
					iEncIt = bData.length - itemp;
				c1.init(Cipher.ENCRYPT_MODE, deskey);
	
				if (iEncIt % 8 == 0) {
					btemp = c1.doFinal(bData, itemp, iEncIt);
				} else {
					int k = iEncIt % 8;
					byte[] b = new byte[iEncIt + 8 - k];
					for (int i = 0; i < b.length; i++)
						b[i] = 0;
					System.arraycopy(bData, itemp, b, 0, iEncIt);
					btemp = c1.doFinal(b);
				}
				System.arraycopy(btemp, 0, bEncData, itemp2, btemp.length);
				itemp = itemp + iEncIt;
				itemp2 += btemp.length;
			} while (itemp < bData.length);
			// 组织�?��密文
			// 加密结果：数据类�? + 加密环境�?字节�?+ 明文长度 (8字节�?
			// 密文长度�?字节�?密文内容+加密算法�?字节�?是否数字信封�?字节�?证书长度�?字节�?+ 证书内容 + 加密密钥长度�?字节�?+
			// 加密密钥内容
			int iLen = 0;
			if (bCert == null)
				iLen = 1 + 1 + 8 + 8 + itemp2 + 1 + 1;
			else
				iLen = 1 + 1 + 8 + 8 + itemp2 + 1 + 1 + 4 + bCert.length + 4 + bEncPwd.length;
			int iOffset = 0;
			byte[] bRet = new byte[iLen];
			//数据类型 
			bRet[iOffset++] = ENCDATA;
			// 加密环境
			bRet[iOffset++] = 1;
			// 明文长度
			byte[] bLen = IntToBytes(bData.length);
			System.arraycopy(bLen, 0, bRet, iOffset, 4);
			iOffset += 4;
			bRet[iOffset++] = 0;
			bRet[iOffset++] = 0;
			bRet[iOffset++] = 0;
			bRet[iOffset++] = 0;
			// 密文长度
			bLen = IntToBytes(itemp2);
			System.arraycopy(bLen, 0, bRet, iOffset, 4);
			iOffset += 4;
			bRet[iOffset++] = 0;
			bRet[iOffset++] = 0;
			bRet[iOffset++] = 0;
			bRet[iOffset++] = 0;
			// 密文内容
			System.arraycopy(bEncData, 0, bRet, iOffset, itemp2);
			iOffset += itemp2;
			// 加密算法
			bRet[iOffset] = 1;
			iOffset++;
			// 是否有数字信�?
			if (bCert != null)
				bRet[iOffset] = 1;
			else
				bRet[iOffset] = 0;
			iOffset++;
			if (bCert != null) {
				// 证书长度
				bLen = IntToBytes(bCert.length);
				System.arraycopy(bLen, 0, bRet, iOffset, bLen.length);
				iOffset += bLen.length;
				// 证书内容
				System.arraycopy(bCert, 0, bRet, iOffset, bCert.length);
				iOffset += bCert.length;
				// 加密后的密钥长度
				bLen = IntToBytes(bEncPwd.length);
				System.arraycopy(bLen, 0, bRet, iOffset, bLen.length);
				iOffset += bLen.length;
				// 加密后的密钥内容
				System.arraycopy(bEncPwd, 0, bRet, iOffset, bEncPwd.length);
				iOffset += bEncPwd.length;
			}
			return bRet;
		}catch(GeneralSecurityException e1)
		{
			throw(new MuticaCryptException(e1.getMessage()));
		} catch (JMJ_Exception e2) {
			throw(new MuticaCryptException(e2.getMessage()));
		}
	}

	/**
	 * 解密密文文件
	 * @param sEncFilePath		�?��解密的密文文件路�?
	 * @param sFilePath			要生成的明文文件的路�?
	 * @param bPwd				对称加密密钥（若密文文件指定了接收人，此参数可以为null�?
	 * @param blOverwrite		是否覆盖已存在的明文文件
	 * @throws MuticaCryptException
	 * @return boolean
	 */
	public static boolean ESSDecryptFile(String sEncFilePath, String sFilePath,
			byte[] bPwd, boolean blOverwrite) throws MuticaCryptException {
		ENCCERT = null;
		try
		{
		
			String stemp = GetConfVal("FILEENCDECMAXBUFFER");
			long lMaxBufSize = DEFAULTFILEBUFSIZE;
			try
			{
				lMaxBufSize = Long.parseLong(stemp);
			}catch(NumberFormatException e)
			{
				lMaxBufSize = DEFAULTFILEBUFSIZE;
			}
			
			FileInputStream fEncFile = new FileInputStream(sEncFilePath);
			File f = new File(sFilePath);
			if (f.exists()) {
				if (!blOverwrite) {
					fEncFile.close();
					iErrMsg = 0x9002;
					return false;
				}
			}
			FileOutputStream fos = new FileOutputStream(f);
			byte[] bEncData = new byte[DECBUFSIZE];
	
			if (fEncFile.read(bEncData, 0,1+ 1 + 4) != (1+1+4)) {
				fEncFile.close();
				iErrMsg = 0x9009;
				fos.close();
				return false;
			}
			// 分解密文
			// 加密结果：数据类�?1 +  加密环境 �?字节�?+ 明文文件名称长度�?字节�?+ 明文文件名称 + 明文长度�?字节�?
			// 密文长度�?字节�?密文内容+加密算法�?字节�?是否数字信封�?字节�?证书长度�?字节�?+ 证书内容 + 加密密钥长度�?字节�?+
			// 加密密钥内容
			byte[] bLen = new byte[4];
			byte[] bLen2 = new byte[8];
			
			//密文文件中的文件�?
			String sFileName = "";
			//数据类型
			if(bEncData[0] != ENCFILE)
			{
				fEncFile.close();
				iErrMsg = 0x900B;
				fos.close();
				return false;
			}
			// 加密环境
			byte bEncEnv = bEncData[1];
			// 明文文件名称长度
			System.arraycopy(bEncData, 2, bLen, 0, 4);
			int iFileNameLen = ByteToInt(bLen);
			byte[] bFileName = new byte[iFileNameLen];
			// 明文文件名称
			fEncFile.read(bFileName);
			sFileName = new String(bFileName);
			
		//	System.out.println(sFileName);
			// 明文长度
			fEncFile.read(bLen2);
			long lPlainLen = ByteToLong(bLen2);
			// 密文长度
			fEncFile.read(bLen2);
			long lEncLen = ByteToLong(bLen2);
			// 加密算法
			byte bAlg = 1;
			// 是否数字信封
			byte bEnvelop = 0;
			// 证书 长度
			int iCertLen = 0;
			// 证书内容
			byte[] bCert = null;
			// 加密密码长度
			int iEncPwdLen = 0;
			// 加密密码内容
			byte[] bEncPwd = null;
			byte[] bPwdUse = new byte[24];
			for (int i = 0; i < 24; i++)
				bPwdUse[i] = 0;
			// 跳过密文
			if (fEncFile.skip(lEncLen) != lEncLen)
			{
				iErrMsg = 0x9009;
				fEncFile.close();
				fos.close();
				return false;
			}
	
			// 加密算法
			fEncFile.read(bEncData, 0, 1);
			bAlg = bEncData[0];
	
			// 是否数字信封
			fEncFile.read(bEncData, 0, 1);
			bEnvelop = bEncData[0];
	
			if (bEnvelop == 1) {
				// 证书长度
				fEncFile.read(bLen, 0, 4);
				iCertLen = ByteToInt(bLen);
				// System.out.println("证书长度:" + iCertLen);
				// 证书内容
				bCert = new byte[iCertLen];
				fEncFile.read(bCert, 0, iCertLen);
				
				ENCCERT = new byte[iCertLen];
				System.arraycopy(bCert, 0, ENCCERT, 0, iCertLen);
				
				// 密钥长度
				fEncFile.read(bLen, 0, 4);
				iEncPwdLen = ByteToInt(bLen);
				bEncPwd = new byte[iEncPwdLen];
				fEncFile.read(bEncPwd, 0, iEncPwdLen);
				// 若是Vc生成，则反序
				if (bEncEnv == 0) {
					byte[] btemp = new byte[iEncPwdLen];
					for (int i = 0; i < iEncPwdLen; i++)
						btemp[i] = bEncPwd[iEncPwdLen - i - 1];
					System.arraycopy(btemp, 0, bEncPwd, 0, iEncPwdLen);
				}
			}
			if (bEnvelop == 1) {
				// 解密出真正的加密 密钥
				PrivateKey pk = JMJ_Func.GetPrivateKeyByCert(bCert);
				Cipher cipher = null;
				cipher = JMJ_Func.GetCipher(0);
				cipher.init(Cipher.DECRYPT_MODE, pk);
				bPwdUse = cipher.doFinal(bEncPwd);
		//		System.out.println("DECFILE PWD:" + ESSGetBase64Encode(bPwdUse));
			} else {
				int itemp = DESPWDLEN;
				if (bAlg == 2)
					itemp = SM4PWDLEN;
				if (bPwd != null) {
					if (bPwd.length > itemp)
						System.arraycopy(bPwd, 0, bPwdUse, 0, itemp);
					else
						System.arraycopy(bPwd, 0, bPwdUse, 0, bPwd.length);
				} else
				{
					iErrMsg = 0x9006;
					fEncFile.close();
					fos.close();
					return false;
				}
			}
			SecretKey deskey = null;
			Cipher c1 = null;
			if (bAlg == 1) {
				deskey = new SecretKeySpec(bPwdUse, "DESede");
				c1 = Cipher.getInstance("DESede/ECB/NoPadding");
			} else {
				deskey = new SecretKeySpec(bPwdUse, "SM4");
				c1 = Cipher.getInstance("SM4/ECB/NoPadding");
			}
	
			FileChannel fcin = fEncFile.getChannel();
			FileChannel fcout = fos.getChannel();
			long lMax = lMaxBufSize - (lMaxBufSize % DECBUFSIZE);
			if (lMax < DECBUFSIZE)
				lMax = DECBUFSIZE;
			long ltemp = 0;
			long lSize = 0;
			boolean bFinal = false;
			long ltemp2 = 0;
			do {
				if (lEncLen - ltemp > lMax)
					lSize = lMax;
				else {
					lSize = lEncLen - ltemp;
					bFinal = true;
				}
				ByteBuffer a = fcin.map(FileChannel.MapMode.READ_ONLY,
						ltemp + 8 + 8 + iFileNameLen + 4 + 1 + 1, lSize);
				ltemp = ltemp + lSize;
				int iOffset = 0;
				byte[] bPlain = null;
				while (a.hasRemaining()) {
					bEncData[iOffset++] = a.get();
					if (iOffset == DECBUFSIZE) {
						c1.init(Cipher.DECRYPT_MODE, deskey);
						bPlain = c1.doFinal(bEncData);
						fcout.write(ByteBuffer.wrap(bPlain));
						iOffset = 0;
						ltemp2 = ltemp2 + bPlain.length;
					}
				}
	
				if (bFinal) {
					if ((iOffset % 8) != 0) {
						fcin.close();
						fcout.close();
						System.out.println("length err");
						fEncFile.close();
						fos.close();
						return false;
					}
					c1.init(Cipher.DECRYPT_MODE, deskey);
					bPlain = c1.doFinal(bEncData, 0, iOffset);
					fcout.write(ByteBuffer.wrap(bPlain, 0,
							(int) (lPlainLen - ltemp2)));
					break;
				}
			} while (true);
			fcin.close();
			fcout.close();
			
			fEncFile.close();
			fos.close();
			return true;
		}catch(GeneralSecurityException e1)
		{
			throw(new MuticaCryptException(e1.getMessage()));
		}catch(IOException e2)
		{
			throw(new MuticaCryptException(e2.getMessage()));
		} catch (JMJ_Exception e3) {
			throw(new MuticaCryptException(e3.getMessage()));
		}

	}

	/**
	 * 解密密文数据
	 * @param bEncData			�?��解密的密文数�?
	 * @param bPwd				对称加密密钥（若密文指定了接收人，此参数可以为null�?
	 * @throws MuticaCryptException
	 * @return byte[]
	 */
	public static byte[] ESSDecryptData(byte[] bEncData, byte[] bPwd) throws MuticaCryptException {
		ENCCERT = null;
		// 分解密文
		// 加密结果�?数据类型 + 加密环境 �?字节�?+ 明文长度�?字节�?
		// 密文长度�?字节�?密文内容+加密算法�?字节�?是否数字信封�?字节�?证书长度�?字节�?+ 证书内容 + 加密密钥长度�?字节�?+
		// 加密密钥内容
		try
		{
			byte[] bLen = new byte[4];
			byte[] bLen2 = new byte[8];
			int iOffset = 0;
			if(bEncData[iOffset++] != ENCDATA)
			{
				iErrMsg = 0x900B;
				return null;
			}
			byte bEncEnv = bEncData[iOffset++];
			// 明文长度
			System.arraycopy(bEncData, iOffset, bLen2, 0, 8);
			iOffset += 8;
			long lPlainLen = ByteToLong(bLen2);
			// 密文长度
			System.arraycopy(bEncData, iOffset, bLen2, 0, 8);
			long lEncLen = ByteToLong(bLen2);
			iOffset = iOffset + 8 + (int) lEncLen;
			// System.out.println("密文长度:" + lEncLen);
			// 加密算法
			byte bAlg = bEncData[iOffset++];
			// System.out.println("加密算法�? + bAlg);
			// 是否存在数字信封
			byte bEnvelop = bEncData[iOffset++];
			// System.out.println("数字信封�? + bEnvelop);
			int iCertLen = 0;
			byte[] bCert = null;
			int iEncPwdLen = 0;
			byte[] bEncPwd = null;
	
			byte[] bPwdUse = new byte[24];
			for (int i = 0; i < 24; i++)
				bPwdUse[i] = 0;
			if (bEnvelop == 1) {
				// 证书长度
				System.arraycopy(bEncData, iOffset, bLen, 0, 4);
				iCertLen = ByteToInt(bLen);
				iOffset += 4;
				// 证书内容
				bCert = new byte[iCertLen];
				System.arraycopy(bEncData, iOffset, bCert, 0, iCertLen);
				iOffset += iCertLen;
				
				ENCCERT = new byte[iCertLen];
				System.arraycopy(bCert, 0, ENCCERT, 0, iCertLen);
				// 加密的密码长�?
				System.arraycopy(bEncData, iOffset, bLen, 0, 4);
				iEncPwdLen = ByteToInt(bLen);
				iOffset += 4;
				// 加密密钥内容
				bEncPwd = new byte[iEncPwdLen];
				System.arraycopy(bEncData, iOffset, bEncPwd, 0, iEncPwdLen);
				iOffset += iEncPwdLen;
	
				if (bEncEnv == 0) {
					byte[] btemp = new byte[iEncPwdLen];
					for (int i = 0; i < iEncPwdLen; i++)
						btemp[i] = bEncPwd[iEncPwdLen - i - 1];
					System.arraycopy(btemp, 0, bEncPwd, 0, iEncPwdLen);
				}
				// 解密出真正的加密 密钥
				//KeyPairGenerator kpg = null;
				//KeyPair kp = null;
				Cipher cipher = null;
				/*
				if (bAlg == 1) {
					kpg = KeyPairGenerator.getInstance("RSA", "SwxaJCE");
					kpg.initialize(1 << 16);
					kp = kpg.genKeyPair();
					cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "SwxaJCE");
				} else {
					kpg = KeyPairGenerator.getInstance("SM2", "SwxaJCE");
					kpg.initialize(25 << 16);
					kp = kpg.genKeyPair();
					cipher = Cipher.getInstance("SM2", "SwxaJCE");
				}*/
				PrivateKey pk = JMJ_Func.GetPrivateKeyByCert(bCert);
				cipher = JMJ_Func.GetCipher(0);
				cipher.init(Cipher.DECRYPT_MODE, pk);
				bPwdUse = cipher.doFinal(bEncPwd);
				
			//	System.out.println("DEC PWD IS:" + ESSGetBase64Encode(bPwdUse));
				
			} else {
				int itemp = DESPWDLEN;
				if (bAlg == 2)
					itemp = SM4PWDLEN;
				if (bPwd != null) {
					if (bPwd.length > itemp)
						System.arraycopy(bPwd, 0, bPwdUse, 0, itemp);
					else
						System.arraycopy(bPwd, 0, bPwdUse, 0, bPwd.length);
				} else
				{
					iErrMsg = 0x9006;
					return null;
				}
			}
			SecretKey deskey = null;
			Cipher c1 = null;
			/*
			if (bAlg == 1) {
				deskey = new SecretKeySpec(bPwdUse, "DESede");
				c1 = Cipher.getInstance("DESede/ECB/NOPADDING", "SwxaJCE");
			} else {
				deskey = new SecretKeySpec(bPwdUse, "SM4");
				c1 = Cipher.getInstance("SM4/ECB/NOPADDING", "SwxaJCE");
			}
			*/
			deskey = new SecretKeySpec(bPwdUse, "DESede");
			c1 = Cipher.getInstance("DESede/ECB/NoPadding");		
			byte[] bPlain = new byte[bEncData.length];
			int itemp = 0;
			int itemp2 = 0;
			int iDecIt = 0;
			do {
				byte[] btemp = null;
				if (lEncLen - itemp > DECBUFSIZE)
					iDecIt = DECBUFSIZE;
				else
					iDecIt = (int) lEncLen - itemp;
				c1.init(Cipher.DECRYPT_MODE, deskey);
				btemp = c1.doFinal(bEncData,1 + 1 + 8 + 8 + itemp, iDecIt);
				System.arraycopy(btemp, 0, bPlain, itemp2, btemp.length);
				itemp2 = itemp2 + btemp.length;
				itemp = itemp + iDecIt;
			} while (itemp < lEncLen);
	
			if (lPlainLen > itemp2)
				return null;
			byte[] bRet = new byte[(int) lPlainLen];
			System.arraycopy(bPlain, 0, bRet, 0, (int) lPlainLen);
			return bRet;
		}catch(GeneralSecurityException e)
		{
			throw(new MuticaCryptException(e.getMessage()));
		} catch (JMJ_Exception e) {
			throw(new MuticaCryptException(e.getMessage()));
		}
	}
	
	
	/**
	 * 对文件签名进行验�?
	 * @param sEncFile			�?��验证完整性的文件路径
	 * @param sFile				要生成的被签名文件的路径
	 * @param blCover			是否覆盖已经存在�?sFile 路径上的同名文件
	 * @throws MuticaCryptException
	 * @return byte[]			返回文件的签名�?
	 */
	public static byte[] ESSVerifyFile_Attach(String sEncFile,String sFile,boolean blCover) throws MuticaCryptException
	{
		ADDITIONMSG = "";
		SIGNTIME = "";
		SIGNEDFILENAME = "";
		//签名文件结构�?  签名文件�?原始文件长度�?字节�?原始文件内容+附加信息长度�?字节) +  附加信息内容 + 文件签名值长�?+ 文件签名�?+ 附近信息签名值长�?+ 附加信息签名�?
		File f = new File(sFile);
		if(f.exists())
		{
			if(blCover == false)
			{
				iErrMsg = 0x9002;
				return null;
			}
		}
		boolean blIn = false;
		boolean blOut = false;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(sEncFile);
			blIn = true;
			byte[] b = new byte[SIGNFILEHEAD.length()];
			fis.read(b);
			String s = new String(b);
			if(!s.equals(SIGNFILEHEAD))
			{
				iErrMsg = 0x900F;
				return null;
			}
			byte[] bLen = new byte[8];
			fis.read(bLen);
			long lFileLen = ByteToLong(bLen);
			
			fos = new FileOutputStream(f);
			blOut = true;
			byte[] bRead = new byte[HASHBUFSIZE];
			
			int ix = (int)lFileLen;
			while(true)
			{
				int iRead = -1;
				if(ix > HASHBUFSIZE)
				{
					iRead = fis.read(bRead);
					if(iRead > 0)
						fos.write(bRead);
				}
				else
				{
					byte[] b1 = new byte[ix];
					iRead = fis.read(b1);
					if(iRead > 0)
						fos.write(b1);
					break;
				}
				if(iRead > 0)
					ix = ix - iRead;
			}
			fos.close();
			blOut = false;
			bLen = new byte[4];
			int iRead = fis.read(bLen);
			int iLen = ByteToInt(bLen);		//附加信息长度
			if(iLen > 0)
			{
				byte[] b2 = new byte[iLen];
				if(fis.read(b2) != iLen)
				{
					f.deleteOnExit();
					fis.close();
					blIn = false;
					iErrMsg = 0x900F;
					return null;
				}
				ADDITIONMSG = new String(b2);	//附加信息	
			}
			iRead = fis.read(bLen);					
			int iFileSignLen = ByteToInt(bLen);	//文件签名值长�?
			byte[] bFileSign = new byte[iFileSignLen];
			fis.read(bFileSign);				//文件签名�?
			fis.read(bLen);						//附加签名值长�?
			int iAddSignLen = ByteToInt(bLen);	
			byte[] bAddSign = null;
			if(iAddSignLen > 0)
			{
				bAddSign = new byte[iAddSignLen];
				fis.read(bAddSign);
			}
			fis.close();
			blIn = false;
			
			//验证文件签名
			bFileSign = ESSGetBase64Decode(new String(bFileSign));
			boolean blRet = ESSVerifyFile(sFile,bFileSign);
			if(!blRet)
				return null;
			
			//验附加信息签�?
			if(bAddSign != null)
			{
				bAddSign = ESSGetBase64Decode(new String(bAddSign));
				blRet = ESSVerifyData(ADDITIONMSG.getBytes(),bAddSign);
				if(!blRet)
					return null;
			}
			return bFileSign;
		} catch (FileNotFoundException e) {
			throw new MuticaCryptException(e.getMessage());
		} catch (IOException e) {
			throw new MuticaCryptException(e.getMessage());
		} finally
		{
			if(blIn)
			{
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(blOut)
			{
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
	}

	// 
	/**
	 * 对文件签名进行验�?
	 * @param sFile				�?��验证完整性的文件路径
	 * @param bSignVal			对文件的签名�?
	 * @throws MuticaCryptException
	 * @return boolean
	 */
	public static boolean ESSVerifyFile(String sFile, byte[] bSignVal) throws MuticaCryptException{
		SIGNTIME = "";
		SIGNEDFILENAME = "";
		SIGNCERT = null;
		// 数据类型1 + 签名环境�?字节�?+ 签名证书长度�?字节�?+ 证书内容（最�?4*1024字节�?+�?ASH值长度（４字节） +
		// HASH值内容（�?�� MAXHASHLEN字节�?+ 签名值长度（4字节�?+ 签名值内容（�?�� MAXSIGNVALLEN
		// 字节�?hash算法�?字节�?签名算法�?字节�? 文件�?长度�?字节 �?+ 文件�?+ 签名时间长度(4字节)+签名时间
		// hash算法 1：SHA1 2：SM算法
		// 签名算法 1:RSA 2:SM 算法
		// 分解签名�?
		try
		{
			int len = 0;
			byte[] bLen = new byte[4];
			int lOffset = 0;
			
			if(bSignVal[lOffset++] != SIGNFILERESULT )
			{
				iErrMsg = 0x900B;
				return false;
			}
			byte bSignEnv = bSignVal[lOffset++]; // 签名环境
			
			System.arraycopy(bSignVal, lOffset, bLen, 0, 4); // 证书长度
			lOffset += 4;
			len = ByteToInt(bLen);
	
			byte[] bCert = new byte[len];
			System.arraycopy(bSignVal, lOffset, bCert, 0, len); // 证书内容
			lOffset += len;
			
			SIGNCERT = new byte[len];
			System.arraycopy(bCert, 0, SIGNCERT, 0, len);
			
			m_sBase64SignCert = ESSGetBase64Encode(bCert);
	
			if (bSignEnv == 0) {
				System.arraycopy(bSignVal, lOffset, bLen, 0, 4); // Hash长度
				lOffset += 4;
				len = ByteToInt(bLen);
	
				byte[] bHash = new byte[len];
				System.arraycopy(bSignVal, lOffset, bHash, 0, len); // hash内容
				lOffset += len;		
			}
	
			System.arraycopy(bSignVal, lOffset, bLen, 0, 4); // 签名值长�?
			lOffset += 4;
			len = ByteToInt(bLen);
	
			byte[] bSign = new byte[len]; // 签名�?
			System.arraycopy(bSignVal, lOffset, bSign, 0, len);
			if (bSignEnv == 0) {
				// java签名值和VC签名值需要反�?
				byte[] btemp = new byte[len];
				for (int i = 0; i < len; i++)
					btemp[i] = bSign[len - i - 1];
				System.arraycopy(btemp, 0, bSign, 0, len);
			}
			lOffset += len;
			byte bHashAlg = bSignVal[lOffset];
			lOffset += 1;
			byte bSignAlg = bSignVal[lOffset];
			lOffset++;
			
			
			System.arraycopy(bSignVal, lOffset, bLen, 0, 4); // 文件名称长度
			int iFileNameLen = ByteToInt(bLen);
			lOffset += 4;
			byte[] bFileName = new byte[iFileNameLen];
			System.arraycopy(bSignVal, lOffset, bFileName, 0, iFileNameLen); // 文件�?	
			lOffset += iFileNameLen;
			String sFileName = new String(bFileName);
			SIGNEDFILENAME = sFileName;
			
			System.arraycopy(bSignVal, lOffset, bLen, 0, 4); // 签名时间长度
			int iTimeLen = ByteToInt(bLen);		
			lOffset += 4;
			
			byte[] bTime = new byte[iTimeLen];
			System.arraycopy(bSignVal, lOffset, bTime, 0, iTimeLen); // 签名时间
			lOffset += iTimeLen;
			
			SIGNTIME = new String(bTime);
			
	//		System.out.println(new String(bTime));
			// ////////////////////////////////////////////////////////////////////////////
			// 从证书中提取公钥
			boolean blSM = false;
			if (bHashAlg == 2)
				blSM = true;
			PublicKey puk = getPublicKeyFromCert(bCert);
			if (puk == null) {
				iErrMsg = 0x9007;
				return false;
			}
			Signature signatue = JMJ_Func.InitSignature();
			signatue.initVerify(puk);
			FileInputStream fis = null;
	
			byte[] data = new byte[HASHBUFSIZE];
			fis = new FileInputStream(sFile);
			int n = 0;
			do {
				n = fis.read(data);
				if (n > 0)
					signatue.update(data, 0, n);
			} while (n != -1);
			
			if(sFileName.indexOf("EMPTYFILENAME") == -1)
				signatue.update(bTime);
			fis.close();
			
			if (signatue.verify(bSign))
				return true;
			else
			{
				iErrMsg = 0x900A;
				return false;
			}
		}catch(GeneralSecurityException e)
		{
			throw(new MuticaCryptException(e.getMessage()));
		}catch(IOException e)
		{
			throw(new MuticaCryptException(e.getMessage()));
		} catch (JMJ_Exception e) 
		{
			throw(new MuticaCryptException(e.getMessage()));
		}
	}

	// 
	/**
	 * 验证数据签名的有效�?
	 * @param bSignData				�?��验证完整性的数据
	 * @throws MuticaCryptException
	 * @return byte[]				返回被签名数据的内容	
	 */
	
	public static byte[] ESSVerifyData_Attach(String signData) throws MuticaCryptException
	{
		ADDITIONMSG = "";
		SIGNTIME = "";
		byte[] bSignData = ESSGetBase64Decode(signData);
		//标记�?+ 被签名数据长度（DWORD�?+ 被签名数据内�?+ 签名值长�?+ 签名值内�?+ 附加信息长度 + 附加信息内容 + 附加签名值长�?+ 附加签名值内�?
		byte[] b = new byte[SIGNDATAHEAD.length()];
		System.arraycopy(bSignData, 0, b, 0, b.length);
		String stemp = new String(b);
		if(!stemp.equals(SIGNDATAHEAD))
		{
			iErrMsg = 0x900B;
			return null;
		}
		int iOffset = SIGNDATAHEAD.length();
		
		byte[] bLen = new byte[4];
		System.arraycopy(bSignData, iOffset, bLen, 0, 4);
		iOffset += 4;
		int iLen = ByteToInt(bLen);		//被签名数据长�?
		byte[] bData = new byte[iLen];
		System.arraycopy(bSignData, iOffset, bData, 0, iLen); //被签名数�?
		iOffset += iLen;
		
		System.arraycopy(bSignData, iOffset, bLen, 0, 4);		//签名值长�?
		iOffset += 4;
		iLen = ByteToInt(bLen);
		byte[] bDataSign = new byte[iLen];
		System.arraycopy(bSignData, iOffset, bDataSign, 0, iLen);	//签名�?
		iOffset += iLen;
		//签名值需要BASE64解码
		bDataSign = ESSGetBase64Decode(new String(bDataSign));		
		//验证签名�?
		if(ESSVerifyData(bData,bDataSign) == false)
		{
			return null;
		}
		if(iOffset == bSignData.length)
			return bData;
		
		System.arraycopy(bSignData, iOffset, bLen, 0, 4);	//附加信息长度
		iOffset += 4;
		iLen = ByteToInt(bLen);
		if(iLen != 0)
		{
			//附加信息
			byte[] bAdd = new byte[iLen];
			System.arraycopy(bSignData, iOffset, bAdd, 0, iLen);
			iOffset += iLen;
			ADDITIONMSG = new String(bAdd);
			//附加信息签名值长�?
			System.arraycopy(bSignData, iOffset, bLen, 0, 4);	
			iOffset += 4;
			iLen = ByteToInt(bLen);
			byte[] bAddSign = new byte[iLen];
			System.arraycopy(bSignData, iOffset, bAddSign, 0, iLen);
			
			//签名值需�?4解码
			bAddSign = ESSGetBase64Decode(new String(bAddSign));
			
			if(ESSVerifyData(bAdd,bAddSign) == false)
				return null;
		}
		return bData;
	}
	

	// 
	/**
	 * 验证数据签名的有效�?
	 * @param data				�?��验证完整性的数据
	 * @param bSignVal			对数据的签名�?
	 * @throws MuticaCryptException
	 * @return boolean
	 */
	public static boolean ESSVerifyData(byte[] data, byte[] bSignVal) throws MuticaCryptException{
		SIGNTIME = "";
		SIGNCERT = null;
		// 数据类型 1 + 签名环境+签名证书长度�?字节�?+ 证书内容（最�?4*1024字节�?+�?ASH值长度（４字节） + HASH值内容（�?��
		// MAXHASHLEN字节�?+ 签名值长度（4字节�?+ 签名值内容（�?�� MAXSIGNVALLEN
		// 字节�?hash算法�?字节�?签名算法�?字节�? 签名时间长度(4字节)+签名时间
		// hash算法 1：SHA1 2：SM算法
		// 签名算法 1:RSA 2:SM 算法
		try
		{
			// 分解签名�?
			int lOffset = 0;
			if(bSignVal[lOffset++] != SIGNDATARESULT)
			{
				iErrMsg = 0x900B;
				return false;
			}
			byte bSignEnv = bSignVal[lOffset++]; // 签名环境
			int len = 0;
			byte[] bLen = new byte[4];
			System.arraycopy(bSignVal, lOffset, bLen, 0, 4); // 签名证书长度
			lOffset += 4;
			len = ByteToInt(bLen);
	
			byte[] bCert = new byte[len];
			System.arraycopy(bSignVal, lOffset, bCert, 0, len); // 签名证书
			lOffset += len;
			
			SIGNCERT = new byte[len];
			System.arraycopy(bCert, 0, SIGNCERT, 0, len);
			
			
			
			m_sBase64SignCert = ESSGetBase64Encode(bCert);
	
			if (bSignEnv == 0) // hash长度和内�?
			{
				System.arraycopy(bSignVal, lOffset, bLen, 0, 4);
				lOffset += 4;
				len = ByteToInt(bLen);
	
				byte[] bHash = new byte[len];
				System.arraycopy(bSignVal, lOffset, bHash, 0, len);
				lOffset += len;
			}
			System.arraycopy(bSignVal, lOffset, bLen, 0, 4); // 签名值长�?
			lOffset += 4;
			len = ByteToInt(bLen);
	
			byte[] bSign = new byte[len];
			System.arraycopy(bSignVal, lOffset, bSign, 0, len); // 签名值内�?
			lOffset += len;
	
			
			byte bHashAlg = bSignVal[lOffset]; // 哈希算法
			lOffset += 1;
			byte bSignAlg = bSignVal[lOffset]; // 签名算法
			lOffset += 1;
			
			//签名时间 长度
			System.arraycopy(bSignVal, lOffset, bLen, 0, 4);
			len = ByteToInt(bLen);
			lOffset += 4;
			//签名时间
			byte[] bTime = new byte[len];
			System.arraycopy(bSignVal, lOffset, bTime, 0, len);
			lOffset += len;
			
			SIGNTIME = new String(bTime);
			
	//		System.out.println(new String(bTime));
			
	
		
			// ////////////////////////////////////////////////////////////////////////////
			// 从证书中提取公钥
			boolean blSM = false;
			if (bSignAlg == 2)
				blSM = true;
			PublicKey puk = getPublicKeyFromCert(bCert);
			if (puk == null) {
				iErrMsg = 0x9007;
				return false;
			}
			Signature signatue = JMJ_Func.InitSignature();
			signatue.initVerify(puk);
	
			int iDataLen = data.length;
			int iOffset = 0;
			do {
				int itemp = iDataLen - iOffset;
				if (itemp > HASHBUFSIZE)
					itemp = HASHBUFSIZE;
	
				signatue.update(data, iOffset, itemp);
				iOffset += itemp;
			} while (iOffset < iDataLen);
	
			signatue.update(bTime);
			
			// signatue.update(data);
			byte[] bSignConvert = new byte[bSign.length];
	
			
			if (bSignEnv == 0) {
				// java签名值和VC签名值需要反�?
				for (int i = 0; i < bSign.length; i++)
					bSignConvert[i] = bSign[bSign.length - i - 1];
			} else
				System.arraycopy(bSign, 0, bSignConvert, 0, bSign.length);
	
			if (signatue.verify(bSignConvert))
				return true;
			else
			{
				iErrMsg = 0x900A;
				return false;
			}
		}catch(GeneralSecurityException e)
		{
			throw(new MuticaCryptException(e.getMessage()));
		} catch (JMJ_Exception e) {
			throw(new MuticaCryptException(e.getMessage()));
		}
	}
	

	public static boolean ESSVerifyData(byte[] data, byte[] bSignVal,String strProvider) throws MuticaCryptException{
		SIGNTIME = "";
		SIGNCERT = null;
		try
		{
			// 分解签名�?
			int lOffset = 0;
			if(bSignVal[lOffset++] != SIGNDATARESULT)
			{
				iErrMsg = 0x900B;
				return false;
			}
			byte bSignEnv = bSignVal[lOffset++]; // 签名环境
			int len = 0;
			byte[] bLen = new byte[4];
			System.arraycopy(bSignVal, lOffset, bLen, 0, 4); // 签名证书长度
			lOffset += 4;
			len = ByteToInt(bLen);
	
			byte[] bCert = new byte[len];
			System.arraycopy(bSignVal, lOffset, bCert, 0, len); // 签名证书
			lOffset += len;
			
			SIGNCERT = new byte[len];
			System.arraycopy(bCert, 0, SIGNCERT, 0, len);
			
			
			
			m_sBase64SignCert = ESSGetBase64Encode(bCert);
	
			if (bSignEnv == 0) // hash长度和内�?
			{
				System.arraycopy(bSignVal, lOffset, bLen, 0, 4);
				lOffset += 4;
				len = ByteToInt(bLen);
	
				byte[] bHash = new byte[len];
				System.arraycopy(bSignVal, lOffset, bHash, 0, len);
				lOffset += len;
			}
			System.arraycopy(bSignVal, lOffset, bLen, 0, 4); // 签名值长�?
			lOffset += 4;
			len = ByteToInt(bLen);
	
			byte[] bSign = new byte[len];
			System.arraycopy(bSignVal, lOffset, bSign, 0, len); // 签名值内�?
			lOffset += len;
	
			
			byte bHashAlg = bSignVal[lOffset]; // 哈希算法
			lOffset += 1;
			byte bSignAlg = bSignVal[lOffset]; // 签名算法
			lOffset += 1;
			
			//签名时间 长度
			System.arraycopy(bSignVal, lOffset, bLen, 0, 4);
			len = ByteToInt(bLen);
			lOffset += 4;
			//签名时间
			byte[] bTime = new byte[len];
			System.arraycopy(bSignVal, lOffset, bTime, 0, len);
			lOffset += len;
			
			SIGNTIME = new String(bTime);
			
	//		System.out.println(new String(bTime));
			
	
		
			// ////////////////////////////////////////////////////////////////////////////
			// 从证书中提取公钥
			boolean blSM = false;
			if (bSignAlg == 2)
				blSM = true;
			PublicKey puk = getPublicKeyFromCert(bCert);
			if (puk == null) {
				iErrMsg = 0x9007;
				return false;
			}
			Signature signatue = JMJ_Func.InitSignature(strProvider);
			signatue.initVerify(puk);
	
			int iDataLen = data.length;
			int iOffset = 0;
			do {
				int itemp = iDataLen - iOffset;
				if (itemp > HASHBUFSIZE)
					itemp = HASHBUFSIZE;
	
				signatue.update(data, iOffset, itemp);
				iOffset += itemp;
			} while (iOffset < iDataLen);
	
			signatue.update(bTime);
			
			// signatue.update(data);
			byte[] bSignConvert = new byte[bSign.length];
	
			
			if (bSignEnv == 0) {
				// java签名值和VC签名值需要反�?
				for (int i = 0; i < bSign.length; i++)
					bSignConvert[i] = bSign[bSign.length - i - 1];
			} else
				System.arraycopy(bSign, 0, bSignConvert, 0, bSign.length);
	
			if (signatue.verify(bSignConvert))
				return true;
			else
			{
				iErrMsg = 0x900A;
				return false;
			}
		}catch(GeneralSecurityException e)
		{
			throw(new MuticaCryptException(e.getMessage()));
		} catch (JMJ_Exception e) {
			throw(new MuticaCryptException(e.getMessage()));
		}
	}
	
	
	// 
	/**
	 * 对文件签�?得到签名文件
	 * @param sFilePath			被签名文件的路径
	 * @param sSignedFile		要生成的签名文件路径
	 * @param bCert				签名人数字证�?
	 * @param sCurTime			签名时间，格式随�?
	 * @param sAddInfo			�?��被签名的附件信息
	 * @param blCover			当指定的签名文件已经存在时，是否覆盖
	 * @throws MuticaCryptException
	 * @return byte[]			签名�?
	 */
	
	public static byte[] ESSSignFile_Attach(String sFilePath,String sSignedFile,byte[] bCert,String sCurTime,String sAddInfo,boolean blCover) throws MuticaCryptException{
		File f = new File(sSignedFile);
		if(f.exists())
		{
			if(blCover == false)
			{
				iErrMsg = 0x9002;
				return null;
			}
		}
	//	long lFileLen = f.length();				
		byte[] bFileRet = ESSSignFile(sFilePath,bCert,sCurTime);
		byte[] bDataRet = null;
		if(sAddInfo != null)
		{
			if(sAddInfo.length() > 0)
				bDataRet = ESSSignData(sAddInfo.getBytes(),bCert,sCurTime);
		}
		String sFileSign = ESSGetBase64Encode(bFileRet);	
		String sDataSign = "";
		if(bDataRet != null)
			sDataSign = ESSGetBase64Encode(bDataRet);
		
		FileInputStream fis = null;
		FileOutputStream fos = null;
		boolean blInOpen = false;
		boolean blOutOpen = false;
		try {
			f.createNewFile();
			//签名文件结构�?  签名文件�?原始文件长度�?字节�?原始文件内容+附加信息长度�?字节) +  附加信息内容 + 文件签名值长�?+ 文件签名�?+ 附近信息签名值长�?+ 附加信息签名�?
			String sHead  = "ESSSIGNFILE:";
			fos = new FileOutputStream(f);
			blOutOpen = true;
			
			fis = new FileInputStream(sFilePath);
			long lFileLen = fis.available();
			fos.write(sHead.getBytes());
			byte[] b = LongToBytes(lFileLen);
			fos.write(b);
			
			
			
			b = new byte[(int)DEFAULTFILEBUFSIZE];
			blInOpen = true;
			int iRead = 0;
			do
			{
				iRead = fis.read(b);
				if(iRead == (int)DEFAULTFILEBUFSIZE)
				{
					fos.write(b);
				}else 
				{
					if(iRead > 0)
					{
						byte[] b2 = new byte[iRead];
						System.arraycopy(b, 0, b2, 0, iRead);
						fos.write(b2);
					}
				}
			}while(iRead > 0);
			
			fis.close();
			blInOpen = false;
			
			
			//附加信息
			int iDataLen = 0;
			if(bDataRet != null)
				iDataLen = sAddInfo.length();
			b = IntToBytes(iDataLen);
			fos.write(b);						//附加信息长度
			
			if(bDataRet != null)
			{
				b = sAddInfo.getBytes();		//附加信息
				fos.write(b);
			}
			
			//文件签名�?
			int iLen = sFileSign.length();
			fos.write(IntToBytes(iLen));
			fos.write(sFileSign.getBytes());
			
			//附加信息签名�?
			if(bDataRet != null)
				iLen = sDataSign.length();
			else
				iLen = 0;
			fos.write(IntToBytes(iLen));
			
			if(bDataRet != null)
				fos.write(sDataSign.getBytes());
			fos.close();
			
			blOutOpen = false;
			return bFileRet;
			
		} catch (IOException e) {	
			try
			{
				if(fos != null)
				{
					if(blOutOpen)
						fos.close();
				}
				if(fis != null)
				{
					if(blInOpen)
						fis.close();				
				}
			}catch(IOException e2)
			{
				throw(new MuticaCryptException(e2.getMessage()));
			}
			if(f.exists())
				f.deleteOnExit();
			throw(new MuticaCryptException(e.getMessage()));
		}
	}

	// 
	/**
	 * 对文件签�?得到签名�?
	 * @param sFilePath			被签名文件的路径
	 * @param bCert				签名人数字证�?
	 * @param sCurTime			签名时间，格式随�?
	 * @throws MuticaCryptException
	 * @return byte[]			签名�?
	 */
	public static byte[] ESSSignFile(String sFilePath, byte[] bCert,String sCurTime) throws MuticaCryptException {
		try
		{
			if (bCert == null || sFilePath == null || sCurTime == null) {
				iErrMsg = 0x9006;
				return null;
			}
			if(sFilePath.length() ==0 || sCurTime.length() == 0)
			{
				iErrMsg = 0x9006;
				return null;			
			}
			File f = new File(sFilePath);
			String sFileName = f.getName();
			int iFileNameLen = sFileName.length();
	
			FileInputStream fis = new FileInputStream(sFilePath);
			Signature signatue = JMJ_Func.InitSignature();
			signatue.initSign(JMJ_Func.GetPrivateKeyByCert(bCert));
			/*
			KeyPair kp = null;
			KeyPairGenerator kpg = null;
			
			
			if (blSM) {
				signatue = Signature.getInstance("SM3WithSM2", "SwxaJCE");
				kpg = KeyPairGenerator.getInstance("SM2", "SwxaJCE");
				kpg.initialize(25 << 16);
			} else {
				signatue = Signature.getInstance("SHA1WithRSA", "SwxaJCE");
				kpg = KeyPairGenerator.getInstance("RSA", "SwxaJCE");
				kpg.initialize(1 << 16);
			}
			
			kp = kpg.genKeyPair();
			signatue.initSign(kp.getPrivate());
			*/
			// 数据类型1 + 签名环境+签名证书长度�?字节�?+ 证书内容（最�?4*1024字节�?+�?签名值长度（4字节�?+ 签名值内容（�?��
			// MAXSIGNVALLEN 字节�?hash算法�?字节�?签名算法�?字节�? 文件名长度（４字节） +�?��件名 + 签名时间长度(4字节)+签名时间
			byte[] bRet = null;
			byte[] bSignVal = null;
			byte[] btemp = new byte[HASHBUFSIZE];
			//对文件内�?签名
			do {
				int i = fis.read(btemp);
				if (i <= 0)
					break;
				signatue.update(btemp, 0, i);
			} while (true);
			//对时间签�?
			signatue.update(sCurTime.getBytes());
			bRet = signatue.sign();
			int iSignValLen =1 + 1 + 4 + bCert.length + 4 + bRet.length + 1 + 1 + 4
					+ iFileNameLen + 4 + sCurTime.length();
			bSignVal = new byte[iSignValLen];
			int iOffset = 0;
			bSignVal[iOffset++] = SIGNFILERESULT;
			bSignVal[iOffset++] = 1;
			
			byte[] bLen = IntToBytes(bCert.length);
			System.arraycopy(bLen, 0, bSignVal, iOffset, bLen.length);
			iOffset += 4;
			System.arraycopy(bCert, 0, bSignVal, iOffset, bCert.length);
			iOffset += bCert.length;
			bLen = IntToBytes(bRet.length);
			System.arraycopy(bLen, 0, bSignVal, iOffset, bLen.length);
			iOffset += 4;
			System.arraycopy(bRet, 0, bSignVal, iOffset, bRet.length);
			iOffset += bRet.length;
			/*
			if (false) {
				bSignVal[iOffset++] = 2; // HASH算法
				bSignVal[iOffset++] = 2; // 签名算法
			} else {
				bSignVal[iOffset++] = 1; // HASH算法
				bSignVal[iOffset++] = 1; // 签名算法
			}
			*/
			bSignVal[iOffset++] = 1; // HASH算法
			bSignVal[iOffset++] = 1; // 签名算法
			
			
			btemp = IntToBytes(iFileNameLen);
			System.arraycopy(btemp, 0, bSignVal, iOffset, 4);
			iOffset += 4; // 文件名长�?
			btemp = sFileName.getBytes();
			System.arraycopy(btemp, 0, bSignVal, iOffset, btemp.length); // 文件名\
			iOffset += iFileNameLen;
			//时间的长�?
			btemp = IntToBytes(sCurTime.length());
			System.arraycopy(btemp, 0, bSignVal, iOffset, 4);
			iOffset += 4;
			//时间
			System.arraycopy(sCurTime.getBytes(), 0, bSignVal, iOffset, sCurTime.length());
			fis.close();
			return bSignVal;
		}catch(GeneralSecurityException e)
		{
			throw(new MuticaCryptException(e.getMessage()));
		}catch(IOException e)
		{
			throw(new MuticaCryptException(e.getMessage()));
		} catch (JMJ_Exception e) 
		{
			throw(new MuticaCryptException(e.getMessage()));
		}
	}
	
	
	/**
	 * 对数据进行签名，并返回签名数据（返回的是128字节的标准签名�?�?
	 * @param bData				被签名的数据				
	 * @param bCert				签名人的数字证书
	 * @return byte[]			签名�?
	 */
	public static byte[] ESSSignData_Dettach(byte[] bData,byte[] bCert) throws MuticaCryptException {
		try
		{
			if(bData == null)
			{
				iErrMsg = 0x9006;
				return null;
			}
			Signature signatue = JMJ_Func.InitSignature();
			signatue.initSign(JMJ_Func.GetPrivateKeyByCert(bCert));
			byte[] bRet = null;
			byte[] bSignVal = null;
			int iDataLen = bData.length;
			int iOffset = 0;
			//对明文数据进行签�?
			do {
				int itemp = iDataLen - iOffset;
				if (itemp > HASHBUFSIZE)
					itemp = HASHBUFSIZE;
				signatue.update(bData, iOffset, itemp);
				iOffset += itemp;
			} while (iOffset < iDataLen);
			bRet = signatue.sign();
			return bRet;
		}catch(GeneralSecurityException e)
		{
			throw(new MuticaCryptException(e.getMessage()));
		} catch (JMJ_Exception e) 
		{
			throw(new MuticaCryptException(e.getMessage()));
		}
	}
	
	
	
	/**
	 * 对数据签�?得到签名数据
	 * @param bData				被签名文件的数据
	 * @param bCert				签名人数字证�?
	 * @param sCurTime			签名时间，格式随�?
	 * @param sAddInfoPara			�?��被签名的附加数据
	 * @throws MuticaCryptException
	 * @return byte[]			BASE64编码的签名数�?
	 */

	public static String ESSSignData_Attach(byte[] bData, byte[] bCert,String sCurTime,String sAddInfoPara) throws MuticaCryptException
	{
		byte[] bAddInfo = null;
		if(sAddInfoPara != null)
		{
			if(sAddInfoPara.length() > 0)
				bAddInfo = sAddInfoPara.getBytes();
		}
		byte[] bDataSign = ESSSignData(bData,bCert,sCurTime);
		byte[] bAddInfoSign = null;
		if(bAddInfo != null)
			bAddInfoSign = ESSSignData(bAddInfo,bCert,sCurTime);
		//标记�?+ 被签名数据长度（DWORD�?+ 被签名数据内�?+ 签名值长�?+ 签名值内�?+ 附加信息长度 + 附加信息内容 + 附加签名值长�?+ 附加签名值内�?
		String sHead = SIGNDATAHEAD;
		byte[] bHead = sHead.getBytes();
		int iLen = bHead.length;
		
		String sDataSign = ESSGetBase64Encode(bDataSign);
		String sAddInfoSign = "";
		if(bAddInfoSign != null)
			sAddInfoSign = ESSGetBase64Encode(bAddInfoSign);
		
		iLen = iLen + 4 + bData.length + 4 + sDataSign.length();
		
		if(bAddInfo != null)
			iLen = iLen + 4 + bAddInfo.length + 4 + sAddInfoSign.length();
		
		byte[] b = new byte[iLen];
		
		int iOffset = 0;
		System.arraycopy(bHead, 0, b, 0, bHead.length);
		iOffset  = iOffset + bHead.length;
		
		byte[] bLen = IntToBytes((int)bData.length);
		System.arraycopy(bLen, 0, b, iOffset, bLen.length);
		iOffset += bLen.length;
		System.arraycopy(bData, 0, b, iOffset, bData.length);
		iOffset += bData.length;
		
		bLen = IntToBytes(sDataSign.length());
		System.arraycopy(bLen, 0, b, iOffset, bLen.length);
		iOffset += bLen.length;
		System.arraycopy(sDataSign.getBytes(), 0, b, iOffset, sDataSign.length());
		iOffset += sDataSign.length();
		
		if(bAddInfo != null)
		{
			bLen = IntToBytes(bAddInfo.length);							// 附加数据的长�?
			System.arraycopy(bLen, 0, b, iOffset, bLen.length);	
			iOffset += bLen.length;
			System.arraycopy(bAddInfo, 0, b, iOffset, bAddInfo.length);	//附加数据
			iOffset += bAddInfo.length;	
			
			
		//	System.out.println(sAddInfoSign.length());
			bLen = IntToBytes(sAddInfoSign.length());						//附加签名数据的长�?
			System.arraycopy(bLen, 0, b, iOffset, bLen.length);
			iOffset += bLen.length;
			System.arraycopy(sAddInfoSign.getBytes(), 0, b, iOffset, sAddInfoSign.length());
			iOffset += sAddInfoSign.length();			
		}
		if(b != null)
			return ESSGetBase64Encode(b);
		else
			return null;
	}
	
	/**
	 * 对数据签�?得到签名�?
	 * @param bData				被签名文件的数据
	 * @param bCert				签名人数字证�?
	 * @param sCurTime			签名时间，格式随�?
	 * @throws MuticaCryptException
	 * @return byte[]			签名�?
	 */
	public static byte[] ESSSignData(byte[] bData, byte[] bCert,String sCurTime) throws MuticaCryptException{
		try
		{
			if(bData == null || bCert == null || sCurTime == null)
			{
				iErrMsg = 0x9006;
				return null;
			}
			
			if(sCurTime.length() == 0)
			{
				iErrMsg = 0x9006;
				return null;		
			}
			Signature signatue = JMJ_Func.InitSignature();
			signatue.initSign(JMJ_Func.GetPrivateKeyByCert(bCert));
			
			// 数据类型1+签名环境+签名证书长度�?字节�?+ 证书内容（最�?4*1024字节�?+�?签名值长度（4字节�?+ 签名值内容（�?��
			// MAXSIGNVALLEN 字节�?hash算法�?字节�?签名算法�?字节�? 签名时间长度 �?字节�?+ 签名时间 
			byte[] bRet = null;
			byte[] bSignVal = null;
	
			int iDataLen = bData.length;
			int iOffset = 0;
			
			//对明文数据进行签�?
			do {
				int itemp = iDataLen - iOffset;
				if (itemp > HASHBUFSIZE)
					itemp = HASHBUFSIZE;
	
				signatue.update(bData, iOffset, itemp);
				iOffset += itemp;
			} while (iOffset < iDataLen);
			//对签名时�?签名
			signatue.update(sCurTime.getBytes());
			bRet = signatue.sign();
			int iSignValLen = 1 + 1 + 4 + bCert.length + 4 + bRet.length + 1 + 1 + 4 + sCurTime.length();
			bSignVal = new byte[iSignValLen];
			iOffset = 0;
			bSignVal[iOffset++] = SIGNDATARESULT;
			bSignVal[iOffset++] = 1;
			byte[] bLen = IntToBytes(bCert.length);
			System.arraycopy(bLen, 0, bSignVal, iOffset, bLen.length);
			iOffset += 4;
			System.arraycopy(bCert, 0, bSignVal, iOffset, bCert.length);
			iOffset += bCert.length;
			bLen = IntToBytes(bRet.length);
			System.arraycopy(bLen, 0, bSignVal, iOffset, bLen.length);
			iOffset += 4;
			System.arraycopy(bRet, 0, bSignVal, iOffset, bRet.length);
			iOffset += bRet.length;
			/*
			if (false) {
				bSignVal[iOffset++] = 2; // HASH算法
				bSignVal[iOffset++] = 2; // 签名算法
			} else {
				bSignVal[iOffset++] = 1; // HASH算法
				bSignVal[iOffset++] = 1; // 签名算法
			}*/
			bSignVal[iOffset++] = 1; // HASH算法
			bSignVal[iOffset++] = 1; // 签名算法
			byte[] btemp = IntToBytes((int)sCurTime.length());
			System.arraycopy(btemp, 0, bSignVal, iOffset, btemp.length);
			iOffset += btemp.length;
			System.arraycopy(sCurTime.getBytes(), 0, bSignVal, iOffset, sCurTime.length());
			return bSignVal;
		}catch(GeneralSecurityException e)
		{
			throw(new MuticaCryptException(e.getMessage()));
		} catch (JMJ_Exception e) 
		{
			throw(new MuticaCryptException(e.getMessage()));
		}
	}


	//sFileType:  JKS  PKCS12 JCEKS BKS
	/**
	 * 从文件类型的KeyStore中获得签名私钥，限定keystore中仅存有�?��私钥
	 * @param sFile				KeyStore文件的路�?
	 * @param sPwd				KeyStore的保护密�?
	 * @param sFileType			KeyStore文件类型，可取�?：JKS、PKCS12、JCEKS、BKS
	 * @throws MuticaCryptException
	 * @return PrivateKey			
	 */
	public static PrivateKey ESSGetPrivateKeyFromFile(String sFile,String sPwd,String sFileType) throws  MuticaCryptException  
	{
		try
		{
			String sProv = "";
			sFileType = sFileType.toLowerCase();
			if(sFileType.indexOf("pkcs") != -1)
				sProv = "SunJSSE";
			if(sFileType.indexOf("jks") != -1)
				sProv = "SUN";
			if(sFileType.indexOf("jceks") != -1)
				sProv = "SunJCE";
			if(sFileType.indexOf("bks") != -1)
				sProv = "BouncyCastleProvider";
			FileInputStream fis;
			fis = new FileInputStream(sFile);
			char[] nPassword = null;
			nPassword = sPwd.toCharArray();
			KeyStore inputKeyStore = KeyStore.getInstance(sFileType,sProv);
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
			throw(new MuticaCryptException(e.getMessage()));
		}catch(GeneralSecurityException e)
		{
			throw(new MuticaCryptException(e.getMessage()));
		}
	}

	/**
	 * 从文件类型的KeyStore中获得证书信息，限定keystore中仅存有�?��证书
	 * @param sFile				KeyStore文件的路�?
	 * @param sPwd				KeyStore的保护密�?
	 * @param sFileType			KeyStore文件类型，可取�?：JKS、PKCS12、JCEKS、BKS
	 * @throws MuticaCryptException
	 * @return byte[] 			编码的证书内�?		
	 */
	public static byte[] ESSGetCertFromFile(String sFile,String sPwd,String sFileType) throws MuticaCryptException
	{
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
			X509Certificate xc = null;
			if (inputKeyStore.isKeyEntry(keyAlias))
			{
				Certificate[] certChain = inputKeyStore.getCertificateChain(keyAlias);
				xc = (X509Certificate) certChain[0];
			}
			fis.close();
			return xc.getEncoded();
		}catch(IOException e)
		{
			throw(new MuticaCryptException(e.getMessage()));
		}catch(GeneralSecurityException e)
		{
			throw(new MuticaCryptException(e.getMessage()));
		}
	}

	
	public static String ESSGetErrorMsg(int lErrCode)
	{
		String sRet = "";
		if(lErrCode == -1)
			return ESSGetErrorMsg(iErrMsg);
		
		switch(lErrCode)
		{
		case 0x9001:
			sRet = "not support SM Algorithm";
			break;
		case 0x9002:
			sRet = "the same file exist";
			break;
		case 0x9003:
			sRet = "could not delete the old file";
			break;
		case 0x9004:
			sRet = "there has no data";
			break;
		case 0x9005:
			sRet = "the wrong parameter";		//0x9005 �?0x9006 含义相同，手误，无需矫正
			break;
		case 0x9006:
			sRet = "the wrong parameter";
			break;
		case 0x9007:
			sRet = "get pulbic key from cert fail";
			break;
		case 0x9008:
			sRet = "not enough memory";
			break;
		case 0x9009:
			sRet = "secret file 's format error";
			break;
		case 0x900A:
			sRet = "electronic signature invalid";
			break;
		case 0x900B:
			sRet = "data type error";
			break;
		case 0x900C:
			sRet = "lack secret key";
			break;
		case 0x900D:
			sRet = "could not find configuration file";
			break;
		case 0x900E:
			sRet = "could not find JAVA_HOME environment variable";
			break;
		case 0x900F:
			sRet = "signed file 's format error";
			break;
		default:
			sRet = "";
		}
		return sRet;
	}
	/**
	 * 获得加密数据或文件中的加密证书内�?
	 * @return byte[] 			加密证书内容			
	 */
	public static byte[] ESSGetEncryptCert()
	{
		return ENCCERT;
	}
	/**
	 * 获得签名数据或文件中的签名人证书内容
	 * @return byte[] 			证书内容			
	 */	
	public static byte[] ESSGetSignCert()
	{
		return SIGNCERT;
	}
	
	private static String bytesToHexString(byte[] src){  
	    StringBuilder stringBuilder = new StringBuilder("");  
	    if (src == null || src.length <= 0) {  
	        return null;  
	    }  
	    for (int i = 0; i < src.length; i++) {  
	        int v = src[i] & 0xFF;  
	        String hv = Integer.toHexString(v);  
	        if (hv.length() < 2) {  
	            stringBuilder.append(0);  
	        }  
	        stringBuilder.append(hv);  
	    }  
	    return stringBuilder.toString();  
	}  
	
	
	private static String BigIntToString(BigInteger bit)
	{
		BigInteger b = new BigInteger("0");
		BigInteger b2 = bit;
		String sRet = null;
		int i = 0;
		while(true)
		{
			b2 = b2.shiftRight(1);
			i++;
			if(b2.compareTo(b) == 0)
				break;
		}
		//i 中保存了数字的bit位数
		System.out.println(i);
		if(i < Integer.SIZE)
			sRet = Integer.toHexString(bit.intValue());
		else if(i < Long.SIZE)
			sRet = Long.toHexString(bit.longValue());
		else
			sRet = null;
		
		return sRet;
	}
	/**
	 * 获取证书中包含的信息
	 * @param	bCert			�?��解析的证书内容（非BASE64编码�?
	 * @param	iType			�?��获取的证书项索引�?：证书版本；2：证书序列号;3：证书签名算法；4：证书颁发�?信息�?：有效期（起始）
	 * 							6：有效期终止�?：用户信�?8：公钥信息；9：扩展项信息
	 * @param	sAt				�?��获取的扩展项的名�?
	 * @return String 			证书项的�?		
	 */	
	public static String ESSGetCertItemInfo(byte[] bCert,int iType,String sAt)
	{
		try {
			InputStream sbs = new ByteArrayInputStream(bCert); 
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate)cf.generateCertificate(sbs);
			String sRet = null;
			byte[] btemp = null;
			Date da = null;
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			switch(iType)
			{
			case 1:
				sRet = Integer.toString(cert.getVersion());
				sRet = "V" + sRet;
				break;
			case 2:
				sRet = BigIntToString(cert.getSerialNumber());
				break;
			case 3:
				sRet = cert.getSigAlgName();
				break;
			case 4:
				sRet = cert.getIssuerDN().toString();
				break;
			case 5:
				da =  cert.getNotBefore();
				sRet = format.format(da);
				break;
			case 6:
				da = cert.getNotAfter();
				sRet = format.format(da);
				break;
			case 7:
				sRet = cert.getSubjectDN().toString();
				break;
			case 8:
				btemp = cert.getPublicKey().getEncoded();
				sRet = bytesToHexString(btemp);
				break;
			case 9:
				sRet = new String(cert.getExtensionValue(sAt));
				break;
			default:
				sRet = null;
				
			}
			 return sRet;
			 
		} catch (CertificateException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @param sO			单位名称
	 * @param sOU			部门名称
	 * @param sDN			印章名称或个人姓�?
	 * @param lDateLen		有效期（天，从当前时间计算到期时间）
	 * @param sPwd			新证书的使用密钥
	 * @param sIssuerNo		从加密机获取颁发者私钥句柄的凭证
	 * @param sNewPfxPath	生成的PFX证书的保存路�?
	 * @return				返回新生成的cer证书
	 * @throws MuticaCryptException 
	 */
	public static byte[] CreatePfxFile(String sO,String sOU,String sDN,long lDateLen,String sPwd,String sIssuerNo,String sNewPfxPath) throws MuticaCryptException
	{
		/*
		 *  先生成一份自签名证书，然后对自签名证书的公钥证书使用颁发者证书签�?
		 * */
		try {	  
			PrivateKey issuer_PrivateKey = JMJ_Func.GetPrivateKeyByCert(sIssuerNo.getBytes());
			X500Name bIssuer=JMJ_Func.GetIssuerInfo(sIssuerNo);
	        if(bIssuer == null)
	        	throw(new MuticaCryptException("could not get issuer info"));       
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(1024);
			KeyPair keyPair = kpg.generateKeyPair();
			PublicKey pubKeyNew = keyPair.getPublic();
			PrivateKey priKeyNew = keyPair.getPrivate();
			X509CertInfo info = new X509CertInfo();
			Date from = new Date(new Date().getTime()-86400000l);
			// 往前推一天
			long time = from.getTime();
			long agoTime = time-86400000l;
			Date to = new Date(agoTime + lDateLen * 86400000l);
			CertificateValidity interval = new CertificateValidity(from, to);	
			BigInteger sn = new BigInteger(64, new SecureRandom());
			String sCertOwner = JMJ_Func.GetDefaultCertOwnerInfo("");
			sCertOwner = sCertOwner.replace("ccn", sDN);
			sCertOwner = sCertOwner.replace("oou", sOU);
			sCertOwner = sCertOwner.replace("oox", sO);
			X500Name owner = new X500Name(sCertOwner);

			info.set(X509CertInfo.VALIDITY, interval);
			info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(sn));
			info.set(X509CertInfo.SUBJECT, new CertificateSubjectName(owner));
			info.set(X509CertInfo.ISSUER, new CertificateIssuerName(bIssuer));
			info.set(X509CertInfo.KEY, new CertificateX509Key(keyPair.getPublic()));
			info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
			AlgorithmId algo = new AlgorithmId(AlgorithmId.md5WithRSAEncryption_oid);
			info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algo));

			X509CertImpl cert = new X509CertImpl(info);
			cert.sign(issuer_PrivateKey, "MD5WithRSA");
			
	        KeyStore store = KeyStore.getInstance("PKCS12");  
	        //System.out.println(sNewPfxPath);
	        store.load(null, null);  			
	        store.setKeyEntry("esspfx", keyPair.getPrivate(), sPwd.toCharArray(), new Certificate[] { cert });  
	        FileOutputStream fos =new FileOutputStream(sNewPfxPath);  
	        
	        
	        store.store(fos, sPwd.toCharArray());         
	        fos.close();
	        return cert.getEncoded();
	        		
		} catch (NoSuchAlgorithmException e1) {
			throw(new MuticaCryptException(e1.getMessage()));
		} catch (NoSuchProviderException e2) {
			throw(new MuticaCryptException(e2.getMessage()));
		} catch (IOException e3) {
			throw(new MuticaCryptException(e3.getMessage()));
		} catch (CertificateException e4) {
			throw(new MuticaCryptException(e4.getMessage()));
		} catch (InvalidKeyException e5) {
			throw(new MuticaCryptException(e5.getMessage()));
		} catch (SignatureException e6) {
			throw(new MuticaCryptException(e6.getMessage()));
		} catch (JMJ_Exception e7) {
			throw(new MuticaCryptException(e7.getMessage()));
		} catch (KeyStoreException e8) {
			throw(new MuticaCryptException(e8.getMessage()));
		}
	}
	private static void WriteFile(byte[] bData,String sFile) throws IOException
	{
		File f = new File(sFile);
		if(f.exists())
			f.delete();
		FileOutputStream fos = new FileOutputStream(f);
		fos.write(bData);
		fos.close();
	}
	
	public static void main(String[] args) throws MuticaCryptException {
		
		File f = new File("D:\\uploadImg\\pfx\\8521ea20-cd2f-48bf-a782-930b6ba5029d.cer");
		FileInputStream fis;
		try {
			fis = new FileInputStream(f);
			byte[] b = new byte[(int)f.length()];
			fis.read(b);
			fis.close();
			b = MuticaCrypt.ESSGetDigest(b);
			String s = MuticaCrypt.ESSGetBase64Encode(b);
			System.out.println(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		
		
//		byte[] bData = new byte[100];		//被加密数�?
//		for(int i = 0;i<100;i++)
//			bData[i] = (byte)i;
//		
//		String sFile = "d:\\temp\\test.rar";			//被加密或签名的原始文�?
//		String sEncFile = "d:\\temp\\test.rar.enc";		//密文文件
//		String sSignedFile = "d:\\temp\\test.rar.sig";	//签名文件
//		
//		//用于制作数字信封或签名的证书（BASE64编码�?
//		String sCert = "MIIEZzCCA9CgAwIBAgIBATANBgkqhkiG9w0BAQUFADCBszENMAsGA1UEBh4ETi1W/TEPMA0GA1UECB4GntGfmWxfMQ8wDQYDVQQHHgZUyFwUbugxGzAZBgNVBAoeEp7Rn5lsX3cBT+Fgb06nThpThTEbMBkGA1UECx4SntGfmWxfdwFP4WBvTqdOGlOFMRswGQYDVQQDHhKe0Z+ZbF93AU/hYG9Op04aU4UxKTAnBgkqhkiG9w0BCQEeGgBoAGwAagB4AGMAdABAAGcAbwB2AC4AYwBuMB4XDTA3MDIyMTAyMTQ0NVoXDTE3MDIxODAyMTQ0NVowfzENMAsGA1UEBh4ETi1W/TEPMA0GA1UECB4GntGfmWxfMQ8wDQYDVQQHHgZUyFwUbugxEzARBgNVBAoeCk/hYG9Op04aU4UxEzARBgNVBAseCk/hYG9Op04aU4UxDzANBgNVBAMeBltZACBzxTERMA8GCSqGSIb3DQEJAR4CACAwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAPjjNfbgHN0TxbORuGeVbKG4EoBdNJGfoYQQ+/L0NO5mTEHCs3z3FSQoBCI1GisCfoHW3CQi99Xm58Qb3/7+g1DSiRglappenLf/vIpnAvFATjdCB7AGfx45L40F55yKVGqzam2pKEd5GMLeQzSE6JCpMeGU8D6kuryRsdiO7ZGhAgMBAAGjggG8MIIBuDAkBgorBgEEAalDZAIFBBYWFElEMTMwMjAzMTk3NzAzMDYwNjE4MAkGA1UdEwQCMAAwHQYDVR0OBBYEFGbF0mJ356F3iSXnrPz4WClTl6i2MIHKBgNVHSMEgcIwgb+hgbmkgbYwgbMxDTALBgNVBAYeBE4tVv0xDzANBgNVBAgeBp7Rn5lsXzEPMA0GA1UEBx4GVMhcFG7oMRswGQYDVQQKHhKe0Z+ZbF93AU/hYG9Op04aU4UxGzAZBgNVBAseEp7Rn5lsX3cBT+Fgb06nThpThTEbMBkGA1UEAx4SntGfmWxfdwFP4WBvTqdOGlOFMSkwJwYJKoZIhvcNAQkBHhoAaABsAGoAeABjAHQAQABnAG8AdgAuAGMAboIBADALBgNVHQ8EBAMCA/gwgYsGA1UdJQSBgzCBgAYIKwYBBQUHAwEGCCsGAQUFBwMCBggrBgEFBQcDAwYIKwYBBQUHAwQGCCsGAQUFBwMIBgorBgEEAYI3AgEWBgorBgEEAYI3CgMDBgorBgEEAYI3CgMEBgorBgEEAYI3FAICBggrBgEFBQcDBQYIKwYBBQUHAwYGCCsGAQUFBwMHMA0GCSqGSIb3DQEBBQUAA4GBAG79V76ayK6oN9L3549pOcsLhjo3COQ90BdGKvp5djPUTokWmMLtuS71sPY1uuqKU1dLVNKeIURt2TgsHyVYrWK7QI7QajaC9F+dFgzxnw/OPm6COKYEtQgnpieaxZ1kJy/awMCwRLdZY+hqi/nWcvqYRvmYE+huqWjwHBOzbSBW";
//		
//		MuticaCrypt.CreatePfxFile("O", "OU", "DN", 365, "111111", "1","d:/temp/out.pfx");
		
		
		/*
		try
		{
			//把证书BASE64 解码
			byte[] bCert = MuticaCrypt.ESSGetBase64Decode(sCert);
			//加密数据(密码加密)
			byte[] bEncData = MuticaCrypt.ESSEncryptData(bData, null, bPwd);
			//解密数据（密码解密）
			byte[] btemp = MuticaCrypt.ESSDecryptData(bEncData, bPwd);
			
			if(btemp.length == bData.length)
			{
				boolean bl = false;
				for(int i=0;i<btemp.length;i++)
				{
					if(btemp[i] != bData[i])
						bl = true;
				}
				if(bl == false)
					System.out.println("解密后的数据与原始数据相�?");
				else
					System.out.println("解密后的数据与原始数据不�?");					
			}
			
			//加密数据（数字信封）
			bEncData = MuticaCrypt.ESSEncryptData(bData, bCert, null);
			//解密数据（数字信封）
			btemp = MuticaCrypt.ESSDecryptData(bEncData, null);
			
			if(btemp.length == bData.length)
			{
				boolean bl = false;
				for(int i=0;i<btemp.length;i++)
				{
					if(btemp[i] != bData[i])
						bl = true;
				}
				if(bl == false)
					System.out.println("解密后的数据与原始数据相�?");
				else
					System.out.println("解密后的数据与原始数据不�?");					
			}
		

			//文件加密(数字信封)
			MuticaCrypt.ESSEncryptFile(sFile, sEncFile, bCert, null, true);
			//文件加密（密码）
	//		MuticaCrypt.ESSEncryptFile(sFile, sEncFile, null, bPwd, true);
			//文件解密
			String stempfile = "d:\\temp\\temp.rar";
			MuticaCrypt.ESSDecryptFile(sEncFile, stempfile, null, true);
	//		MuticaCrypt.ESSDecryptFile(sEncFile, stempfile, bPwd, true);

			//数据签名
			byte[] bSig = MuticaCrypt.ESSSignData(bData, bCert, "2014-3-6 12:0:0");
			//验证数据签名
			if(MuticaCrypt.ESSVerifyData(bData, bSig) == true)
			{
				System.out.println("数据完整1");
				System.out.println(MuticaCrypt.ESSGetSignTime());
			}
			else
				System.out.println("数据不完�?");
			
			//数据签名(attach 方式)
			String sSig = MuticaCrypt.ESSSignData_Attach(bData, bCert, "2014-3-6 12:30:0", "fu  jia   xin   xi ");
			//数据验证(attach方式)
			btemp = MuticaCrypt.ESSVerifyData_Attach(sSig);
			if(btemp != null)
			{
				if(btemp.length == bData.length)
				{
					boolean bl = false;
					for(int i=0;i<btemp.length;i++)
					{
						if(btemp[i] != bData[i])
							bl = true;
					}
					if(bl == false)
					{
						System.out.println("提取出的被签名数据与原始数据相同3");
						System.out.println("签名时间�? + MuticaCrypt.ESSGetSignTime());
						System.out.println("附加信息�? + MuticaCrypt.ESSGetAdditionMsg());
					}
					else
						System.out.println("提取出的被签名数据与原始数据相同3");					
				}
			}
			
			//文件签名
			bSig = MuticaCrypt.ESSSignFile(sFile, bCert, "2014-3-6 13:00:0");
			//验证文件签名
			if(MuticaCrypt.ESSVerifyFile(sFile, bSig) == true)
			{	
				System.out.println("文件完整4");
				System.out.println("签名时间 �? + MuticaCrypt.ESSGetSignTime());
				System.out.println("原始文件名称�? + MuticaCrypt.ESSGetSignedFileName());
			}
			
			
			//文件签名（attach�?
			bSig = MuticaCrypt.ESSSignFile_Attach(sFile, sSignedFile, bCert, "2014-3-6 13:30", "fu jia xinxi 2", true);
			//验证签名文件(attach)
			if(MuticaCrypt.ESSVerifyFile_Attach(sSignedFile, "d:\\temp\\test2.rar", true) != null)
			{
				System.out.println("文件完整5");
				System.out.println("签名时间 �? + MuticaCrypt.ESSGetSignTime());
				System.out.println("原始文件名称�? + MuticaCrypt.ESSGetSignedFileName());				
			}else
			{
				System.out.println(MuticaCrypt.ESSGetErrorMsg(-1));
			}
		}catch(MuticaCryptException e)
		{
			System.out.println(e.getMessage());
		}
		
		
		*/
	}

}
