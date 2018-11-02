package com.clt.ess.base;

public class Constant {
    //系统一级单位 区号
    public static final String JIANGSU_CODE = "025";

    public static final int FILETYPE = 10;
    public static final int STATE_YES = 1;
    public static final int STATE_NO = 0;

    /**
     *  申请审核制作信息状态
     */
    //提交申请
    public static final int SUBMIT_APPLICATION = 1;
    //审核通过
    public static final int REVIEW_THROUGH = 2;
    //审核人驳回
    public static final int REVIEW_NO_THROUGH = 3;
    //制作人驳回
    public static final int MAKE_NO_THROUGH = 4;
    //审核未通过
    public static final int MAKE_REJECT = 5;
    //制作完成
    public static final int MAKE_COMPLETION = 7;
    //制作失败
    public static final int MAKE_NO_COMPLETION = 8;
    //制作失败
    public static final int CER_DELAY_APPLY = 8;


    //申请审核制作信息类别
    //申请新印章
    public static final int APPLYTYPE_NEW = 1;
    //注册UK
    public static final int APPLYTYPE_REGISTER_UK = 2;
    //授权延期
    public static final int APPLYTYPE_DELAY_AUTH = 3;
    //证书延期
    public static final int APPLYTYPE_DELAY_CER = 4;
    //印章重做
    public static final int APPLYTYPE_REPEAT = 5;

    /**
     * 证书状态
     */
    //证书未生成
    public static final int FILE_STATE_NULL = 1;
    //存在CER证书
    public static final int FILE_STATE_CER = 2;
    //存在PFX证书
    public static final int FILE_STATE_PFX = 3;
    //存在CER和PFX证书
    public static final int FILE_STATE_CERANDPFX = 4;


    /**
     * 印章状态
     */
    //有效
    public static final int SEAL_STATE_VALID = 1;
    //无效
    public static final int SEAL_STATE_INVALID = 2;
    //暂停
    public static final int SEAL_STATE_PAUSE = 3;

    /**
     * 消息类别  驳回 1  注册 2  授权延期 3  证书延期4
     */


    /**
     * 文件根路径
     */
//    public static final String ISSUERUNIT_ROOT_PATH = "D:/temp/issuerUnit/";
//    public static final String ROOT_PATH = "D:/temp/";
    //linux
//    public static final String PFX_TEMP_PATH = "/usr/esstempfile/temp.pfx";
    //windows
    public static final String PFX_TEMP_PATH = "d:/temp.pfx";
//    public static final String CER_NAME = "clt.cer";
//    public static final String PFX_NAME = "clt.pfx";

    /**
     * 证书信息
     */
    //证书加密算法
    public static final String CER_ALGORITHM = "SHA1withRSA";
    public static final String CER_CLASS = "签章证书";
    public static final String CER_VERSION = "V3";

    //顶级单位层级,多个单位(公司)使用同一套系统时,该值需要修改为0
    public static final int topUnitLevel=1;
    //每个公司的层级 一级单位的层级
    public static final int  companyLevel=1;

    /**
     * 附件临时地址
     */
    public static final String ATTACHMENT_PATH = "d:/temp/attachment/";


    /**
     * 解析pfx 证书的临时地址
     */
    public static final String PFX_FILE_PATH = "d:/temp/pfxTemp/";

    /**
     * 消息中心 消息类别
     */
    //驳回
    public static final String Message_Type_reject = "1";
    //注册
    public static final String Message_Type_register = "2";
    //重做
    public static final String Message_Type_reMake = "3";
    //授权延期
    public static final String Message_Type_auDelay = "4";
    //证书延期
    public static final String Message_Type_cerDelay = "5";
    /**
     * 独立单位配置num
     */
    //代表 颁发者单位
    public static final int  INDEPENDENTUNITCONFIG_NUM_3=3;
    //代表 UK注册时到期时间和授权文档类型是以系统为准还是以Uk为准
    public static final int  INDEPENDENTUNITCONFIG_NUM_4=4;
}
