package com.multica.crypt;

import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SimpleDesede {
    // 定义加密算法
    private static final String Algorithm = "DESede";
    // 加密 src为源数据的字节数组
    public static byte[] encryptMode(byte[] src,String sPwd) {
    	int iLen = src.length;
    	if((iLen % 8) != 0)
    		iLen = (8 - src.length % 8) + iLen;
    	byte[] bSrc = new byte[iLen];
    	System.arraycopy(src, 0, bSrc, 0, src.length);
    	for(int i = src.length;i<iLen;i++)
    		bSrc[i] = 0;
        try {// 生成密钥
            SecretKey deskey = new SecretKeySpec(build3Deskey(sPwd), Algorithm);
            // 实例化cipher
            Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, deskey);      
            return cipher.doFinal(bSrc);
        } catch (Exception e) {
            return null;
        }
    }

    // 解密函数
    public static byte[] decryptMode(byte[] src,String sPwd) {
        SecretKey deskey;
        try {
            deskey = new SecretKeySpec(build3Deskey(sPwd),Algorithm);
            Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, deskey);
            byte[] b = cipher.doFinal(src);
            int iLen = b.length;
            for(int i = b.length -1;i>0;i--)
            {
            	if(b[i] == 0)
            		iLen--;
            	else
            		break;
            }
            byte[] c = new byte[iLen];
            System.arraycopy(b, 0, c, 0, iLen);
            return c;
        } catch (Exception e) {
            return null;
        }
    }

    // 根据字符串生成密钥24位的字节数组
    public static byte[] build3Deskey(String keyStr) throws Exception {
        byte[] key = new byte[24];
        byte[] temp = keyStr.getBytes();
        if (key.length > temp.length) {
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    }
    
    public static void main(String[] args) throws IOException  {
    	byte[] b = SimpleDesede.encryptMode("1234567812345678123456789ESS".getBytes(),"123456");
    	if(b == null)
    		System.out.println("no");
    	else
    	{
    		b = SimpleDesede.decryptMode(b, "123456");
    		String s = new String(b);
    		System.out.print(s);
    		System.out.print("haha");
    	}
    }
}
