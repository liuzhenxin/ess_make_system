package com.clt.ess.utils;

import com.multica.crypt.MuticaCryptException;

import java.util.regex.Pattern;

import static com.clt.ess.utils.uuidUtil.getUUID;
import static com.multica.crypt.MuticaCrypt.*;

public class StringUtils {

    /**推荐，速度最快
     * 判断是否为整数
     * @param str 传入的字符串
     * @return 是整数返回true,否则返回false
     */

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 获取一个对称加密的随机密码
     * @return
     * @throws MuticaCryptException
     */
    public static String getEncryptPwd() {
        String uuid = getUUID();
        String pwd = uuid.substring(8,16);
        System.out.println(pwd);
        byte[] b = new byte[0];
        try {
            b = ESSEncryptData(pwd.getBytes(),null,"esspwd".getBytes());
        } catch (MuticaCryptException e) {
            e.printStackTrace();
        }
        return ESSGetBase64Encode(b);
    }
    /**
     * 解密本系统生成的密码
     * @param pwd
     * @return
     * @throws MuticaCryptException
     */
    public static String getDecryptPwd(String pwd) {
        byte[] pwdByte = new byte[0];
        try {
            pwdByte = ESSDecryptData(ESSGetBase64Decode(pwd),"esspwd".getBytes());
        } catch (MuticaCryptException e) {
            e.printStackTrace();
        }
        return new String(pwdByte);
    }


    public static void main(String[] args) throws MuticaCryptException {

        String pwd = getEncryptPwd();
        System.out.println(pwd);
        byte[] a = ESSDecryptData(ESSGetBase64Decode(pwd),"esspwd".getBytes());
        System.out.println(new String(a));

    }

}
