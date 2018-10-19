package com.clt.ess.web;

import com.clt.ess.base.Constant;
import com.clt.ess.bean.ResultMessageBeen;
import com.clt.ess.entity.*;
import com.clt.ess.service.*;
import com.clt.ess.utils.FastJsonUtil;
import com.clt.ess.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import static com.clt.ess.utils.PowerUtil.getLoginUser;
import static com.clt.ess.utils.dateUtil.getDate;
import static com.clt.ess.utils.uuidUtil.getEssUUID;
import static com.clt.ess.utils.uuidUtil.getUUID;
import static com.multica.crypt.MuticaCrypt.ESSGetBase64Encode;

@Controller
@RequestMapping("/make")
public class MakeController {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;

    @Autowired
    private ISealService sealService;
    @Autowired
    private IUnitService unitService;
    @Autowired
    private ICertificateService certificateService;
    @Autowired
    private IMessageService messageService;
    @Autowired
    private ILogService logService;
    @Autowired
    private IFileTypeService fileTypeService;
    @Autowired
    private ISealImgService sealImgService;
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
     *访问印章制作页面
     * @param unitId 单位ID
     */
    @RequestMapping(value="/list.html", method = RequestMethod.GET)
    public String seal_make(Model model, String unitId) {

        SealApply sealApply = new SealApply();
        sealApply.setUnitId(unitId);
        sealApply.setApplyState(Constant.REVIEW_THROUGH);
        List<SealApply> sealApplyList =  sealService.findSealApply(sealApply);

        sealApply.setApplyState(Constant.MAKE_NO_COMPLETION);
        List<SealApply> sealApplyList_1 =  sealService.findSealApply(sealApply);
        sealApplyList.addAll(sealApplyList_1);

        model.addAttribute("unit",  unitService.findUnitById(unitId));
        model.addAttribute("sealApplyList",  sealApplyList);

        return "make/make_list";

    }
    /**
     *访问印章制作
     * @param sealApplyId 申请信息ID
     */
    @RequestMapping(value="/make_detail.html", method = RequestMethod.GET)
    public ModelAndView seal_make_detail(String sealApplyId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        //根据申请信息ID获取详细信息
        SealApply sealApply = sealService.findSealApplyById(sealApplyId);
        if(sealApply==null){
            modelAndView.setViewName("error");
        }
        //查询当前单位对应的顶级单位支持的授权类型
        List<FileType> fileTypeList = fileTypeService.findFileTypeListByTop(sealApply.getUnitId());
        modelAndView.addObject("fileTypeList",  fileTypeList);
        modelAndView.addObject("sealApply",sealApply);
        //查询当前单位对应的顶级单位支持的证书颁发机构
        Unit unit = unitService.findTopUnit(sealApply.getUnitId());
        if(!"".equals(sealApply.getCerIssuer())){
            //获取证书颁发机构
            List<IssuerUnit> issuerUnitList = unitService.findIssuerUnitByUnitId(unit.getUnitId());
            modelAndView.addObject("issuerUnitList", issuerUnitList);
        }
        //判断申请信息类别
        switch(sealApply.getApplyType()){
            //申请新印章
            case Constant.APPLYTYPE_NEW:
                modelAndView.setViewName("make/make_detail");
                break;
            //注册UK
            case Constant.APPLYTYPE_REGISTER_UK:
                modelAndView.setViewName("make/make_detail_uk");
                break;
            //印章重做
            case Constant.APPLYTYPE_REPEAT:
                modelAndView.setViewName("make/make_detail_repeat");
                break;
            //印章授权延期
            case Constant.APPLYTYPE_DELAY_AUTH:
                modelAndView.setViewName("make/make_detail_delay");
                break;
            //证书授权延期
            case Constant.APPLYTYPE_DELAY_CER:
                modelAndView.setViewName("make/make_detail_delay_cer");
                break;
            default:
                break;
        }
        return modelAndView;

    }
    /**
     *访问印章制作
     * @param sealApplyId
     */
    @RequestMapping(value="/make_new_do.html", method = RequestMethod.POST)
    @ResponseBody
    public String make_new_do(String sealApplyId,String cerIssuer,String cardType,Certificate c, MultipartFile gifImg,
                               MultipartFile jpgImg) throws IOException {
        ResultMessageBeen messageBeen = new ResultMessageBeen();
        messageBeen.setMessage("success");
        messageBeen.setBody(null);
        //查找当前操作的申请信息
        SealApply sealApply = sealService.findSealApplyById(sealApplyId);
        //UK类型
        sealApply.setCardType(cardType);
        //根据上传的图像生成印章图片
        SealImg sealImg = new SealImg();
        sealImg.setSealImgId(getEssUUID(sealApply.getUnitId()));
        if(jpgImg!=null|| gifImg!=null){
            //如果提供了jpg图片，将jpg图片写入缩略图
            if (jpgImg!=null) {
                sealImg.setSealImgJpg(jpgImg.getBytes());
                sealImg.setSealThumbnailImgBase64(ESSGetBase64Encode(jpgImg.getBytes()));
            }
            if (gifImg!=null) {
                sealImg.setSealImgGifBase64(ESSGetBase64Encode(gifImg.getBytes()));
            }
            //如果只提供了gif图片，将gif图片写入缩略图
            if(gifImg!=null &&jpgImg==null){
                sealImg.setSealThumbnailImgBase64(ESSGetBase64Encode(gifImg.getBytes()));
            }
        }else{
            messageBeen.setMessage("error");
        }
        //获取印章图片ID
        String sealImgId = sealImgService.addSealImg(sealImg);
        //添加证书ID
        sealApply.setSealImgId(sealImgId);
//        //判断当前是否有印章余额 结果为3 时 允许添加
//        if(sealService.isAddSeal(sealApply.getUnitId())!=3){
//            messageBeen.setMessage("error");
//        }

        User user =  getLoginUser(session);
        //状态修改为制作完成
        sealApply.setApplyState(Constant.MAKE_COMPLETION);
        //设置制作人和制作时间
        sealApply.setMakeUserId((user.getUserId()));
        sealApply.setMakeTime(getDate());
        //设置证书颁发单位
        sealApply.setCerIssuer(cerIssuer);
        //证书相关的处理
        Certificate certificate =sealApply.getCertificate();
        certificate.setCountry(c.getCountry());
        certificate.setProvince(c.getProvince());
        certificate.setCity(c.getCity());
        certificate.setCertUnit(c.getCertUnit());
        certificate.setCertDepartment(c.getCertDepartment());
        certificate.setCerName(c.getCerName());
        //设置证书有效期生成和到期时间
        certificate.setStartTime(c.getStartTime());
        certificate.setEndTime(c.getEndTime());
        //设置证书的颁发者单位
        certificate.setIssuerUnitId(sealApply.getCerIssuer());
        //生成证书 证书未生成的时候
        if(sealApply.getCertificate().getFileState()==Constant.FILE_STATE_NULL){
            //证书未生成
            if(sealApply.getIsUK()==1){
                //Uk印章 证书不在此时生成。服务端操作完成后在写入UK时。由客户端提供UK的私钥生成。
                //这里只更改下证书的状态即可
                certificate.setFileState(Constant.FILE_STATE_CER);
            }else {
                //软证书印章 服务器生成证书
                certificate.setCerPsw(StringUtils.getEncryptPwd());
                certificate.setFileState(Constant.FILE_STATE_CERANDPFX);
                //生成证书
                Map<String, String> cerAndPfxMap =  certificateService.createCerFileAndPfx(certificate);

                certificate.setPfxBase64(cerAndPfxMap.get("pfxBase64"));
                certificate.setCerBase64(cerAndPfxMap.get("cerBase64"));
            }
        }
        //根据已有信息生成印章对象
        Seal newSeal = new Seal();
        //印章Id
        newSeal.setSealId(sealApply.getSealId());
        //印章名称
        newSeal.setSealName(sealApply.getSealName());
        //印章图像
        newSeal.setSealImgId(sealApply.getSealImgId());
        //印章证书
        newSeal.setCerId(sealApply.getCertificateId());
        //制作时间
        newSeal.setInputTime(sealApply.getMakeTime());
        //制作人
        newSeal.setInputUserId(sealApply.getMakeUserId());
        //印章单位
        newSeal.setUnitId(sealApply.getUnitId());
        //印章类型
        newSeal.setSealTypeId(sealApply.getSealTypeId());
        //手签身份证号
        newSeal.setSealHwIdNum(sealApply.getSealHwUserIdNum());
        //印章授权
        newSeal.setFileTypeNum(sealApply.getFileTypeNum());
        //印章授权时间
        newSeal.setSealStartTime(sealApply.getSealStartTime());
        newSeal.setSealEndTime(sealApply.getSealEndTime());
        //UK上的授权时间和信息
        newSeal.setAuthorizationTime(sealApply.getAuthorizationTime());
        newSeal.setAuthorizationInfo(sealApply.getAuthorizationInfo());
        //keyId
        newSeal.setKeyId(sealApply.getKeyId());
        //是否UK印章
        newSeal.setIsUK(sealApply.getIsUK());
        //印章状态有效
        newSeal.setSealState(Constant.SEAL_STATE_VALID);
        newSeal.setCardType(sealApply.getCardType());
        if("success".equals(messageBeen.getMessage())){
            try {
                sealService.addSeal(newSeal);
                certificateService.updateCertificate(certificate);
                sealService.updateSealApply(sealApply);
                messageBeen.setBody(newSeal);
                logService.addSystemLog("制作了"+newSeal.getSealName(),"印章制作",
                        newSeal.getUnitId(),user.getUserId(),"");
            }catch (Exception e){
                messageBeen.setMessage("error");
            }
        }

        return new String(FastJsonUtil.toJSONString(messageBeen).getBytes("utf8"),"iso8859-1");
    }

    /**
     *注册UK制作
     * @param sealApplyId
     */
    @RequestMapping(value="/make_uk_do.html", method = RequestMethod.POST)
    @ResponseBody
    public String make_uk_do(String sealApplyId,MultipartFile gifImg,MultipartFile jpgImg) throws IOException {
        ResultMessageBeen messageBeen = new ResultMessageBeen();
        messageBeen.setMessage("success");
        messageBeen.setBody(null);

        //查找当前操作的申请信息
        SealApply sealApply = sealService.findSealApplyById(sealApplyId);
        //根据上传的图像生成印章图片
        SealImg sealImg = new SealImg();
        sealImg.setSealImgId(getEssUUID(sealApply.getUnitId()));
        if(jpgImg!=null|| gifImg!=null){
            //如果提供了jpg图片，将jpg图片写入缩略图
            if (jpgImg!=null) {
                sealImg.setSealImgJpg(jpgImg.getBytes());
                sealImg.setSealThumbnailImgBase64(ESSGetBase64Encode(jpgImg.getBytes()));
            }
            if (gifImg!=null) {
                sealImg.setSealImgGifBase64(ESSGetBase64Encode(gifImg.getBytes()));
            }
            //如果只提供了gif图片，将gif图片写入缩略图
            if(gifImg!=null &&jpgImg==null){
                sealImg.setSealThumbnailImgBase64(ESSGetBase64Encode(gifImg.getBytes()));
            }
        }else{
            messageBeen.setMessage("error");
        }
        //获取印章图片ID
        String sealImgId = sealImgService.addSealImg(sealImg);
        //添加图片ID
        sealApply.setSealImgId(sealImgId);
//        //判断当前是否有印章余额 结果为3 时 允许添加
//        if(sealService.isAddSeal(sealApply.getUnitId())!=3){
//            messageBeen.setMessage("error");
//        }
        User user =  getLoginUser(session);
        //状态修改为制作完成
        sealApply.setApplyState(Constant.MAKE_COMPLETION);
        //设置制作人和制作时间
        sealApply.setMakeUserId((user.getUserId()));
        sealApply.setMakeTime(getDate());

        //根据已有信息生成印章对象
        Seal newSeal = new Seal();
        //印章Id
        newSeal.setSealId(sealApply.getSealId());
        //印章名称
        newSeal.setSealName(sealApply.getSealName());
        //印章图像
        newSeal.setSealImgId(sealApply.getSealImgId());
        //印章证书
        newSeal.setCerId(sealApply.getCertificateId());
        //制作时间
        newSeal.setInputTime(sealApply.getMakeTime());
        //制作人
        newSeal.setInputUserId(sealApply.getMakeUserId());
        //印章单位
        newSeal.setUnitId(sealApply.getUnitId());
        //印章类型
        newSeal.setSealTypeId(sealApply.getSealTypeId());
        //手签身份证号
        newSeal.setSealHwIdNum(sealApply.getSealHwUserIdNum());
        //印章授权
        newSeal.setFileTypeNum(sealApply.getFileTypeNum());
        //印章授权时间
        newSeal.setSealStartTime(sealApply.getSealStartTime());
        newSeal.setSealEndTime(sealApply.getSealEndTime());
        //UK上的授权时间和信息
        newSeal.setAuthorizationTime(sealApply.getAuthorizationTime());
        newSeal.setAuthorizationInfo(sealApply.getAuthorizationInfo());
        //keyId
        newSeal.setKeyId(sealApply.getKeyId());
        //是否UK印章
        newSeal.setIsUK(sealApply.getIsUK());
        //印章状态无效
        newSeal.setSealState(Constant.SEAL_STATE_INVALID);
        newSeal.setCardType(sealApply.getCardType());

        if("success".equals(messageBeen.getMessage())){
            try {
                sealService.addSeal(newSeal);
                sealService.updateSealApply(sealApply);
                messageBeen.setBody(newSeal);

                //向消息中心发消息
                Message message_new  = new Message();
                message_new.setMessageNo(getEssUUID(newSeal.getUnitId()));
                message_new.setSender(user.getUserId());
                message_new.setReceiver(sealApply.getApplyUserId());
                message_new.setSendTime(getDate());
                message_new.setMessageType(Constant.Message_Type_register);
                message_new.setMessageTitle("注册成功,请确认！");
                message_new.setMessageContent(sealApply.getSealName()+"UK注册已审核制作完成，请确认！");
                message_new.setApplyInfoId(sealApply.getSealApplyId());
                message_new.setReadState(2);
                message_new.setState(1);
                messageService.addMessage(message_new);

                logService.addSystemLog("制作了"+newSeal.getSealName(),"印章注册",
                        newSeal.getUnitId(),user.getUserId(),"");
            }catch (Exception e){
                messageBeen.setMessage("error");
            }
        }
        return new String(FastJsonUtil.toJSONString(messageBeen).getBytes("utf8"),"iso8859-1");
    }


    /**
     *重做
     * @param sealApplyId
     */
    @RequestMapping(value="/make_repeat_do.html", method = RequestMethod.POST)
    @ResponseBody
    public String make_repeat_do(String sealApplyId,String cerIssuer,String cardType,Certificate c, MultipartFile gifImg,
                              MultipartFile jpgImg) throws IOException {
        ResultMessageBeen messageBeen = new ResultMessageBeen();
        messageBeen.setMessage("success");
        messageBeen.setBody(null);

        //查找当前操作的申请信息
        SealApply sealApply = sealService.findSealApplyById(sealApplyId);
        sealApply.setCardType(cardType);
        //根据上传的图像生成印章图片
        SealImg sealImg = new SealImg();
        sealImg.setSealImgId(getEssUUID(sealApply.getUnitId()));
        if(jpgImg!=null|| gifImg!=null){
            //如果提供了jpg图片，将jpg图片写入缩略图
            if (jpgImg!=null) {
                sealImg.setSealImgJpg(jpgImg.getBytes());
                sealImg.setSealThumbnailImgBase64(ESSGetBase64Encode(jpgImg.getBytes()));
            }
            if (gifImg!=null) {
                sealImg.setSealImgGifBase64(ESSGetBase64Encode(gifImg.getBytes()));
            }
            //如果只提供了gif图片，将gif图片写入缩略图
            if(gifImg!=null &&jpgImg==null){
                sealImg.setSealThumbnailImgBase64(ESSGetBase64Encode(gifImg.getBytes()));
            }
        }else{
            messageBeen.setMessage("error");
        }
        //获取印章图片ID
        String sealImgId = sealImgService.addSealImg(sealImg);
        //添加证书ID
        sealApply.setSealImgId(sealImgId);
//        //判断当前是否有印章余额 结果为3 时 允许添加
//        if(sealService.isAddSeal(sealApply.getUnitId())!=3){
//            messageBeen.setMessage("error");
//        }
        User user =  getLoginUser(session);
        //状态修改为制作完成
        sealApply.setApplyState(Constant.MAKE_COMPLETION);
        //设置制作人和制作时间
        sealApply.setMakeUserId((user.getUserId()));
        sealApply.setMakeTime(getDate());
        //设置证书颁发单位
        sealApply.setCerIssuer(cerIssuer);
        //证书相关的处理
        Certificate certificate =sealApply.getCertificate();
        certificate.setCountry(c.getCountry());
        certificate.setProvince(c.getProvince());
        certificate.setCity(c.getCity());
        certificate.setCertUnit(c.getCertUnit());
        certificate.setCertDepartment(c.getCertDepartment());
        certificate.setCerName(c.getCerName());
        //设置证书有效期生成和到期时间
        certificate.setStartTime(c.getStartTime());
        certificate.setEndTime(c.getEndTime());
        //设置证书的颁发者单位
        certificate.setIssuerUnitId(sealApply.getCerIssuer());
        //生成证书 证书未生成的时候
        if(sealApply.getCertificate().getFileState()==Constant.FILE_STATE_NULL){
            //证书未生成
            if(sealApply.getIsUK()==1){
                //Uk印章 证书不在此时生成。服务端操作完成后在写入UK时。由客户端提供UK的私钥生成。
                //这里只更改下证书的状态即可
                certificate.setFileState(Constant.FILE_STATE_CER);
            }else {
                //软证书印章 服务器生成证书
                certificate.setCerPsw(StringUtils.getEncryptPwd());
                certificate.setFileState(Constant.FILE_STATE_CERANDPFX);
                //生成证书
                Map<String, String> cerAndPfxMap =  certificateService.createCerFileAndPfx(certificate);

                certificate.setPfxBase64(cerAndPfxMap.get("pfxBase64"));
                certificate.setCerBase64(cerAndPfxMap.get("cerBase64"));
            }
        }

        //根据已有信息生成印章对象
        Seal newSeal = new Seal();
        //印章Id
        newSeal.setSealId(sealApply.getSealId());
        //印章名称
        newSeal.setSealName(sealApply.getSealName());
        //印章图像
        newSeal.setSealImgId(sealApply.getSealImgId());
        //印章证书
        newSeal.setCerId(sealApply.getCertificateId());
        //制作时间
        newSeal.setInputTime(sealApply.getMakeTime());
        //制作人
        newSeal.setInputUserId(sealApply.getMakeUserId());
        //印章单位
        newSeal.setUnitId(sealApply.getUnitId());
        //印章类型
        newSeal.setSealTypeId(sealApply.getSealTypeId());
        //手签身份证号
        newSeal.setSealHwIdNum(sealApply.getSealHwUserIdNum());
        //印章授权
        newSeal.setFileTypeNum(sealApply.getFileTypeNum());
        //印章授权时间
        newSeal.setSealStartTime(sealApply.getSealStartTime());
        newSeal.setSealEndTime(sealApply.getSealEndTime());
        //UK上的授权时间和信息
        newSeal.setAuthorizationTime(sealApply.getAuthorizationTime());
        newSeal.setAuthorizationInfo(sealApply.getAuthorizationInfo());
        //keyId
        newSeal.setKeyId(sealApply.getKeyId());
        //是否UK印章
        newSeal.setIsUK(sealApply.getIsUK());
        //印章状态有效
        newSeal.setSealState(Constant.SEAL_STATE_VALID);
        newSeal.setCardType(sealApply.getCardType());
        //没有异常并且旧印章不为null
        if("success".equals(messageBeen.getMessage()) && sealService.findSealById(sealApply.getSealId())!=null){
            try {

                //删除原来的印章
                Seal seal = new Seal();
                seal.setSealId(sealApply.getSealId());
                sealService.delSeal(seal);
                //添加印章ID相同的新印章
                sealService.addSeal(newSeal);
                certificateService.updateCertificate(certificate);
                sealService.updateSealApply(sealApply);
                messageBeen.setBody(newSeal);

                //发送消息通知申请人确认重做印章
                //向消息中心发消息
                Message message_new_1  = new Message();
                message_new_1.setMessageNo(getEssUUID(newSeal.getUnitId()));
                message_new_1.setSender(((User) session.getAttribute("user")).getUserId());
                message_new_1.setReceiver(sealApply.getApplyUserId());
                message_new_1.setSendTime(getDate());
                message_new_1.setMessageType("3");
                message_new_1.setMessageTitle("重做成功");
                message_new_1.setMessageContent("您申请"+sealApply.getSealName()+"重做的印章已审核制作完成！");
                message_new_1.setApplyInfoId(sealApply.getSealApplyId());
                message_new_1.setReadState(2);
                message_new_1.setState(1);
                messageService.addMessage(message_new_1);


                logService.addSystemLog("制作了"+newSeal.getSealName(),"印章制作",
                        newSeal.getUnitId(),user.getUserId(),"");
            }catch (Exception e){
                messageBeen.setMessage("error");
            }
        }
        return new String(FastJsonUtil.toJSONString(messageBeen).getBytes("utf8"),"iso8859-1");
    }


    /**
     *延期
     * @param sealApplyId
     */
    @RequestMapping(value="/make_delay_do.html", method = RequestMethod.POST)
    @ResponseBody
    public String make_delay_do(String sealApplyId) throws IOException {
        ResultMessageBeen messageBeen = new ResultMessageBeen();
        messageBeen.setMessage("success");
        messageBeen.setBody(null);

        SealApply sealApply = sealService.findSealApplyById(sealApplyId);
        if(sealApply!=null){
            //状态修改为制作完成
            sealApply.setApplyState(Constant.MAKE_COMPLETION);
            //设置制作人和制作时间
            sealApply.setMakeUserId(((User) session.getAttribute("user")).getUserId());
            sealApply.setMakeTime(getDate());
            //更新申请信息数据

        }else{
            messageBeen.setMessage("error");
        }

        if("success".equals(messageBeen.getMessage())){
            try {
                sealService.updateSealApply(sealApply);
                //向消息中心发消息
                Message message_new_2  = new Message();
                message_new_2.setMessageNo(getEssUUID(sealApply.getUnitId()));
                message_new_2.setSender(((User) session.getAttribute("user")).getUserId());
                message_new_2.setReceiver(sealApply.getApplyUserId());
                message_new_2.setSendTime(getDate());
                message_new_2.setMessageType(Constant.Message_Type_auDelay);
                message_new_2.setMessageTitle("授权延期确认");
                message_new_2.setMessageContent("您申请"+sealApply.getSealName()+"的授权延期已审核制作完成,请您确认生效！");
                message_new_2.setApplyInfoId(sealApply.getSealApplyId());
                message_new_2.setReadState(2);
                message_new_2.setState(1);
                messageService.addMessage(message_new_2);

                User user =  getLoginUser(session);
                logService.addSystemLog("制作了"+sealApply.getSealName(),"印章制作",
                        sealApply.getUnitId(),user.getUserId(),"");

            }catch (Exception e){
                messageBeen.setMessage("error");
            }
        }
        return new String(FastJsonUtil.toJSONString(messageBeen).getBytes("utf8"),"iso8859-1");
    }

    /**
     *制作驳回
     * @param sealApplyId 申请信息ID
     */
    @RequestMapping(value="/make_reject.html", method = RequestMethod.GET)
    @ResponseBody
    public String seal_make_reject(Model model, String sealApplyId, String message) throws UnsupportedEncodingException {

        ResultMessageBeen messageBeen = new ResultMessageBeen();
        messageBeen.setMessage("success");
        messageBeen.setBody(null);

        SealApply sealApply = sealService.findSealApplyById(sealApplyId);

        if(sealApply!=null){
            //向消息中心添加数据
            Message message_new  = new Message();
            message_new.setMessageNo(Constant.JIANGSU_CODE+getUUID());
            message_new.setSender(((User) session.getAttribute("user")).getUserId());
            message_new.setReceiver(sealApply.getReviewUserId());
            message_new.setSendTime(getDate());
            message_new.setMessageType("驳回信息");
            message_new.setMessageTitle("申请"+sealApply.getSealName()+"被驳回");
            message_new.setMessageContent(message);
            message_new.setApplyInfoId(sealApply.getSealApplyId());
            message_new.setReadState(2);
            message_new.setState(1);
            boolean result = messageService.addMessage(message_new);

            //信息状态修改为审核人驳回
            sealApply.setApplyState(Constant.MAKE_NO_THROUGH);
            sealService.updateSealApply(sealApply);
            //添加日志
            User user =  (User) session.getAttribute("user");
            logService.addSystemLog("驳回了"+sealApply.getSealName()+"的制作申请","印章申请驳回",
                    sealApply.getUnitId(),user.getUserId(),"");
        }else{
            messageBeen.setMessage("error");
        }


        return new String(FastJsonUtil.toJSONString(messageBeen).getBytes("utf8"),"iso8859-1");
    }



    /**
     *更新印章keyID
     * @param seal 印章
     */
    @RequestMapping(value="/updateSealForKeyId.html", method = RequestMethod.POST)
    @ResponseBody
    public String updateSealForKeyId(Seal seal) {
        ResultMessageBeen messageBeen = new ResultMessageBeen();
        messageBeen.setMessage("success");
        messageBeen.setBody(null);

        boolean a = sealService.updateSeal(seal);
        if(!a){
            messageBeen.setMessage("error");
        }
        return FastJsonUtil.toJSONString(messageBeen);
    }

    /**
     *更新印章ID
     * @param sealApplyId 申请信息ID
     */
    @RequestMapping(value="/sealApplyFail.html", method = RequestMethod.POST)
    @ResponseBody
    public String sealApplyFail(String sealApplyId) {
        ResultMessageBeen messageBeen = new ResultMessageBeen();
        messageBeen.setMessage("success");
        messageBeen.setBody(null);

        SealApply sealApply = sealService.findSealApplyById(sealApplyId);
        //删除印章
        Seal seal = new Seal();
        seal.setSealId(sealApply.getSealId());
        sealService.delSeal(seal);

        sealApply.setApplyState(Constant.MAKE_NO_COMPLETION);

        sealService.updateSealApply(sealApply);

        Certificate certificate = new Certificate();
        certificate.setCertificateId(sealApply.getCertificateId());
        certificate.setFileState(Constant.FILE_STATE_NULL);
        certificateService.updateCertificate(certificate);

        return FastJsonUtil.toJSONString(messageBeen);
    }
    /**
     *访问证书延期页面
     * @param certificateId
     */
    @RequestMapping(value="/cer_delay.html", method = RequestMethod.GET)
    public String cer_delay(String certificateId) {

        //根据证书id查找到所属印章的信息
        Seal seal= new Seal();
        seal.setCerId(certificateId);
        List<Seal> sealList = sealService.findSealList(seal);

        if(sealList.size()>=1){
            seal = sealList.get(0);
        }

        //新建申请信息
        //根据印章延期申请制作新的申请信息
        SealApply sealApply = new SealApply();
        //设置新申请id
        sealApply.setSealApplyId(getEssUUID(seal.getUnitId()));
        //设置申请的印章Id
        sealApply.setSealId(seal.getSealId());
        //设置申请的类别 证书延期
        sealApply.setApplyType(Constant.APPLYTYPE_DELAY_CER);

        //设置延期起始时间 ()
        sealApply.setSealStartTime(seal.getSealStartTime());
        //设置延期到期时间 ()
        sealApply.setSealEndTime(seal.getSealEndTime());

        sealApply.setUnitId(seal.getUnitId());
        sealApply.setSealTypeId(seal.getSealTypeId());
        sealApply.setSealImgId(seal.getSealImgId());
        sealApply.setSealName(seal.getSealName());


        Certificate new_c = seal.getCertificate();
        new_c.setCertificateId(getEssUUID(seal.getUnitId()));
        new_c.setFileState(Constant.FILE_STATE_NULL);
        new_c.setCerBase64("");
        new_c.setPfxBase64("");

        new_c.setApplyTime(getDate());

        String cId = certificateService.addCertificate(new_c);

        sealApply.setCertificateId(cId);


        sealApply.setIsUK(seal.getIsUK());
        sealApply.setKeyId(seal.getKeyId());
        sealApply.setSealHwUserIdNum(seal.getSealHwIdNum());
        sealApply.setFileTypeNum(seal.getFileTypeNum());
        sealApply.setAuthorizationInfo(seal.getAuthorizationInfo());
        sealApply.setAuthorizationTime(seal.getAuthorizationTime());


        //设置申请人信息
        //设置申请人和申请时间
        sealApply.setApplyUserId(((User) session.getAttribute("user")).getUserId());
        sealApply.setApplyTime(getDate());
        //设置审核人信息
        //设置审核时间
        sealApply.setReviewUserId(((User) session.getAttribute("user")).getUserId());
        sealApply.setReviewTime(getDate());


        sealApply.setApplyState(Constant.CER_DELAY_APPLY);
        //向数据库添加申请信息
        sealService.addSealApply(sealApply);

        User user =  (User) session.getAttribute("user");
        logService.addSystemLog("申请延期证书"+sealApply.getSealName(),"证书延期",
                sealApply.getUnitId(),user.getUserId(),"");

        return "redirect:/make/make_detail.html?sealApplyId="+sealApply.getSealApplyId();
    }


    /**
     *延期
     * @param sealApplyId 申请信息ID
     */
    @RequestMapping(value="/cer_delay_do.html", method = RequestMethod.POST)
    @ResponseBody
    public String cer_delay_do(String cardType,String sealApplyId,String startTime,String endTime,String cerIssuer) throws IOException {
        ResultMessageBeen messageBeen = new ResultMessageBeen();
        messageBeen.setMessage("success");
        messageBeen.setBody(null);
        SealApply sealApply = null;
        Certificate c =null;
        Seal seal =new Seal();;


        //查出完整信息
        sealApply = sealService.findSealApplyById(sealApplyId);
        sealApply.setCardType(cardType);
        //状态修改为制作完成
        sealApply.setApplyState(Constant.MAKE_COMPLETION);
        //设置制作人和制作时间
        sealApply.setMakeUserId(((User) session.getAttribute("user")).getUserId());
        sealApply.setMakeTime(getDate());
        //新的授权单位
        sealApply.setCerIssuer(cerIssuer);

        c = sealApply.getCertificate();
        c.setStartTime(startTime);
        c.setEndTime(endTime);
        //原有颁发单位
        c.setIssuerUnitId(cerIssuer);

        seal.setUnitId(sealApply.getUnitId());

        if(sealApply.getIsUK()==1){
            //是UK
            c.setFileState(Constant.FILE_STATE_CER);
        }else{
            //不是UK
            c.setFileState(Constant.FILE_STATE_CERANDPFX);

            Map<String, String> cerAndPfxMap =  certificateService.createCerFileAndPfx(c);

            c.setPfxBase64(cerAndPfxMap.get("pfxBase64"));
            c.setCerBase64(cerAndPfxMap.get("cerBase64"));
            //此处将seal的证书Id修改
            seal.setSealId(sealApply.getSealId());
            seal.setCerId(c.getCertificateId());

        }
//        try{
//
//        }catch (Exception e){
//            messageBeen.setMessage("error");
//        }

        if("success".equals(messageBeen.getMessage())){
            certificateService.updateCertificate(c);
            sealService.updateSeal(seal);
            sealService.updateSealApply(sealApply);
            messageBeen.setBody(sealService.findSealById(sealApply.getSealId()));
            User user =  getLoginUser(session);
            logService.addSystemLog("延期了"+sealApply.getSealName(),"印章的证书",
                    sealApply.getUnitId(),user.getUserId(),"");
        }
        return new String(FastJsonUtil.toJSONString(messageBeen).getBytes("utf8"),"iso8859-1");
    }


    /**
     *更新印章证书ID
     * @param sealApplyId 申请信息ID
     */
    @RequestMapping(value="/changeCerId.html", method = RequestMethod.POST)
    @ResponseBody
    public String changeCerId(String sealApplyId,String certificateId) {
        ResultMessageBeen messageBeen = new ResultMessageBeen();
        messageBeen.setMessage("success");
        messageBeen.setBody(null);

        SealApply sealApply = sealService.findSealApplyById(sealApplyId);
        //删除印章
        Seal seal = new Seal();
        seal.setSealId(sealApply.getSealId());
        seal.setCerId(certificateId);

        sealService.updateSeal(seal);

        return FastJsonUtil.toJSONString(messageBeen);
    }

}
