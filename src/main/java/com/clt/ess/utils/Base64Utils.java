package com.clt.ess.utils;


import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by clt_abc on 2017/5/31.
 */

public class Base64Utils {
    /**
     * 将传入数据BASE64编码
     * @param bMsg  ?  编码的数 ?
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
     * @param sEncMsg  ?  解码码的数据
     * @return byte[]
     */
    public static byte[] ESSGetBase64Decode(String sEncMsg) {
        byte[] date= null;
        try
        {
            BASE64Decoder bdr = new BASE64Decoder();
            date = bdr.decodeBuffer(sEncMsg);
            return date;
        }catch(IOException e)
        {
//            throw(new MuticaCryptException(e.getMessage()));
        }
        return date;
    }

    /**
     * 将传入多媒体文件转化为Base64
     * @param FileData  ?  解码码的数据
     * @return byte[]
     */
    public static String Base64MultipartFileToString(MultipartFile FileData) {
        String dataString = null ;
        try {
            BASE64Encoder encoder = new BASE64Encoder();
            // 通过base64来转化图片
            dataString = encoder.encode(FileData.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataString;
    }

    /**
     * <p>将文件转成base64 字符串</p>
     * @param path 文件路径
     * @return
     * @throws Exception
     */
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return new BASE64Encoder().encode(buffer);
    }
    /**
     * <p>将base64字符解码保存文件</p>
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */
    public static void decoderBase64File(String base64Code,String targetPath) throws Exception {
        byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
    }

}
