package com.clt.ess.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class SaveFileUtil {

    /**
     * 将图片保存到硬盘
     * @return 文件路径
     */
    public static String saveImage(String filePath,String fileNmae,MultipartFile image) {


        //磁盘文件路径
        File dest = new File(filePath + "/" + fileNmae );
        try {
            image.transferTo(dest); //保存文件
            return "";
        } catch (IOException e) {
            return "";
        }
    }
}
