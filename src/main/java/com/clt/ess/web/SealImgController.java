package com.clt.ess.web;

import com.clt.ess.dao.IIssuerUnitDao;
import com.clt.ess.entity.IssuerUnit;
import com.clt.ess.entity.SealImg;
import com.clt.ess.service.ISealImgService;
import com.multica.crypt.MuticaCryptException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static com.clt.ess.utils.CertUtils.signCertByIssuerUnit;
import static com.clt.ess.utils.uuidUtil.getUUID;
import static com.multica.crypt.MuticaCrypt.ESSGetBase64Decode;
import static com.multica.crypt.MuticaCrypt.ESSGetBase64Encode;

@Controller
@RequestMapping("/img")
public class SealImgController {
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;

    @Autowired
    private ISealImgService sealImgService;
    @Autowired
    private IIssuerUnitDao iIssuerUnitDao;

    /**
     * 每次拦截到请求会先访问此函数
     * @param request http请求
     * @param response http回应
     */
    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
        this.session = request.getSession();

    }

    /**
     * 通过url请求返回图像的字节流
     */
    @RequestMapping(value="/download.do", method = RequestMethod.GET)
    public void img_download(String imgId,String type) throws MuticaCryptException, IOException {
        //根据获取到ID取到对应印章图片对象
        SealImg sealImg = sealImgService.findSealImgById(imgId);
        byte[] imgByte = null;
        //根据类型取到对应字符节数据
        if(sealImg!=null){
            if("jpg".equals(type)||"JPG".equals(type)){
                imgByte = (byte[]) sealImg.getSealImgJpg();
            } else if("clt".equals(type)||"CLT".equals(type)){
                imgByte = (byte[]) sealImg.getSealImgClt();
            }else if("gif".equals(type)||"GIF".equals(type)){
                imgByte = ESSGetBase64Decode(sealImg.getSealImgGifBase64());
            }
        }

        if(imgByte == null){
            //设置MIME类型
            response.setContentType("application/json");
            ServletOutputStream outputStream=response.getOutputStream();
            outputStream.write(("ESSNOO").getBytes());
            outputStream.close();
        }else{
            //设置MIME类型
            response.setContentType("application/octet-stream");
            //设置头信息,设置文件下载时的默认文件名，同时解决中文名乱码问题  返回文件名称为图像ID加上后缀格式
            response.addHeader("Content-disposition", "attachment;filename="+new String((imgId+"."+type).getBytes(), "ISO-8859-1"));
            ServletOutputStream outputStream=response.getOutputStream();
            outputStream.write(imgByte);
            outputStream.close();
        }
    }

    @RequestMapping(value="/upload.html", method = RequestMethod.GET)
    public String upload(HttpServletRequest request) {
        return "upload";
    }
    /**
     *采用spring提供的上传文件的方法
     */
    @RequestMapping(value="/upload.do", method = RequestMethod.POST)
    @ResponseBody
    public String  img_upload(@RequestParam("files") MultipartFile[] files,String imgId) throws IllegalStateException, IOException {
        String result = "";
        try {
            //判断file数组不能为空并且长度大于0
            if (files != null && files.length > 0) {
                SealImg sealImg = new SealImg();
                //循环获取file数组中得文件
                for (int i = 0; i < files.length; i++) {
                    MultipartFile file = files[i];
                    String fileName = file.getOriginalFilename();
                    String fileType = fileName.split("\\.")[1];

                    switch(fileType) {
                        case "jpg":
                            sealImg.setSealImgJpg(file.getBytes());
                            sealImg.setSealThumbnailImgBase64(ESSGetBase64Encode(file.getBytes()));
                            break;
                        case "clt":
                            sealImg.setSealImgClt(file.getBytes());
                            break;
                        case "gif":
                            sealImg.setSealImgGifBase64(ESSGetBase64Encode(file.getBytes()));
                            sealImg.setSealThumbnailImgBase64(ESSGetBase64Encode(file.getBytes()));
                            break;
                    }
                }
                if(imgId==null){
                    //不带有imgId参数
                    imgId = getUUID();
                    sealImg.setSealImgId(imgId);
                    imgId = sealImgService.addSealImg(sealImg);
                    result = "ESSYES"+imgId+"ESSEND";
                }else{
                    //带有imgId参数，更新数据
                    sealImg.setSealImgId(imgId);
                    boolean b = sealImgService.updateSealImg(sealImg);
                    if(b){
                        result = "ESSYES"+imgId+"ESSEND";
                    }else{
                        result = "ESSNOO";
                    }
                }
            }else{
                result = "ESSNOO";
            }
        }catch (Exception e){
            result = "ESSNOO";
        }
        //返回字符
        return result;
    }



}
