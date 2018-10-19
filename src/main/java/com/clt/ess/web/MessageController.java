package com.clt.ess.web;


import com.clt.ess.base.Constant;
import com.clt.ess.bean.ResultMessageBeen;
import com.clt.ess.dao.IUserDao;
import com.clt.ess.entity.*;
import com.clt.ess.service.ICertificateService;
import com.clt.ess.service.IMessageService;
import com.clt.ess.service.ISealService;
import com.clt.ess.utils.FastJsonUtil;
import com.clt.ess.utils.dateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/message")
public class MessageController {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;
    @Autowired
    private IUserDao userDao;
    @Autowired
    private IMessageService messageService;
    @Autowired
    private ISealService sealService;
    @Autowired
    private ICertificateService certificateService;
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
     *访问消息列表
     * @param
     */
    @RequestMapping(value="/message_list.html", method = RequestMethod.GET)
    public String seal_list(Model model) {

        Message message = new Message();
        message.setState(1);
        message.setReceiver(((User) session.getAttribute("user")).getUserId());

        List<Message> messageList = messageService.findMessage(message);
        model.addAttribute("messageList",  messageList);
        return "message/message_list";

    }

    /**
     *访问消息x详细内容
     * @param
     */
    @RequestMapping(value="/message_detail.html", method = RequestMethod.GET)
    public String message_detail(Model model,String messageNo) {

        Message message = new Message();
        message.setMessageNo(messageNo);
        message.setState(1);

        message = messageService.findMessageOnly(message);

        SealApply sealApply = sealService.findSealApplyById(message.getApplyInfoId());

        model.addAttribute("sealApply",  sealApply);
        model.addAttribute("message",  message);

        return "message/message_detail";

    }

    /**
     *驳回
     * @param
     */
    @RequestMapping(value="/messageMethodType_1.html", method = RequestMethod.GET)
    @ResponseBody
    public String messageMethodType_1(Model model,String messageNo) {


        Message message = new Message();

        String applyId = messageService.findMessage(message).get(0).getApplyInfoId();

        return applyId;

    }
    /**
     *驳回
     * @param
     */
    @RequestMapping(value="/reSealApply.html", method = RequestMethod.GET)
    public String reSealApply(Model model,String sealApplyId) {
        //查询原有印章信息
        SealApply sealApply_1 = sealService.findSealApplyById(sealApplyId);


        return "seal/seal_repeat";
    }

    /**
     *注册UK
     * @param
     */
    @RequestMapping(value="/messageMethodType_2.html", method = RequestMethod.GET)
    @ResponseBody
    public String messageMethodType_2(String messageNo) throws UnsupportedEncodingException {

        ResultMessageBeen messageBeen = new ResultMessageBeen();
        messageBeen.setMessage("success");
        messageBeen.setBody(null);

        Message message = new Message();
        message.setMessageNo(messageNo);
        message.setState(1);
        message = messageService.findMessageOnly(message);

        SealApply sealApply = sealService.findSealApplyById(message.getApplyInfoId());


        Seal seal  = sealService.findSealById(sealApply.getSealId());

        //新印章设置印章有效
        seal.setSealState(Constant.SEAL_STATE_VALID);
        sealService.updateSeal(seal);

        messageBeen.setBody(seal);

        return new String(FastJsonUtil.toJSONString(messageBeen).getBytes("utf8"),"iso8859-1");
    }

    /**
     *注销信息
     * @param
     */
    @RequestMapping(value="/destroyMessage.html", method = RequestMethod.GET)
    @ResponseBody
    public String destroyMessage(String messageNo) throws UnsupportedEncodingException {

        ResultMessageBeen messageBeen = new ResultMessageBeen();
        messageBeen.setMessage("success");
        messageBeen.setBody(null);

        Message message = new Message();
        message.setMessageNo(messageNo);
        //注销记录
        message.setState(0);
        messageService.updateMessage(message);

        return new String(FastJsonUtil.toJSONString(messageBeen).getBytes("utf8"),"iso8859-1");

    }
    /**
     *重做
     * @param
     */
    @RequestMapping(value="/messageMethodType_3.html", method = RequestMethod.GET)
    @ResponseBody
    public String messageMethodType_3(Model model,String messageNo) {
//        destroyMessage(messageNo);
        Message message = new Message();
        message.setMessageNo(messageNo);
        message.setState(0);
        //返回seal信息
        List<Message> messageList = messageService.findMessage(message);
        SealApply sealApply = sealService.findSealApplyById(messageList.get(0).getApplyInfoId());

        Seal seal  = sealService.findSealById(sealApply.getSealId());

        return seal.getSealId();
    }
    /**
     *授权延期
     * @param messageNo
     */
    @RequestMapping(value="/messageMethodType_4.html", method = RequestMethod.GET)
    @ResponseBody
    public String messageMethodType_4(String messageNo) throws UnsupportedEncodingException {

        ResultMessageBeen messageBeen = new ResultMessageBeen();
        messageBeen.setMessage("success");
        messageBeen.setBody(null);

        Message message = new Message();
        message.setMessageNo(messageNo);
        message.setState(1);
        message = messageService.findMessageOnly(message);

        SealApply sealApply = sealService.findSealApplyById(message.getApplyInfoId());
        if(sealApply==null){
            messageBeen.setMessage("error");
        }else{
            Seal seal  = sealService.findSealById(sealApply.getSealId());
            seal.setSealStartTime(sealApply.getSealStartTime());
            seal.setSealEndTime(sealApply.getSealEndTime());

            //新印章设置印章有效
//        seal.setSealState(Constant.SEAL_STATE_VALID);
            sealService.updateSeal(seal);

            messageBeen.setBody(seal);
        }

        return new String(FastJsonUtil.toJSONString(messageBeen).getBytes("utf8"),"iso8859-1");

    }
    /**
     *证书延期
     * @param
     */
    @RequestMapping(value="/messageMethodType_5.html", method = RequestMethod.GET)
    @ResponseBody
    public String messageMethodType_5(Model model,String messageNo) {

        Message message = new Message();
        message.setMessageNo(messageNo);
        message.setState(1);
        List<Message> messageList = messageService.findMessage(message);
        message = messageList.get(0);

        SealApply sealApply = new SealApply();
        sealApply.setSealApplyId(message.getApplyInfoId());
        List<SealApply> sealApplyList = sealService.findSealApply(sealApply);
        sealApply = sealApplyList.get(0);

        //更新证书数据
        //制作新证书
        //设置证书颁发单位
        //设置证书有效期生成和到期时间
        Certificate certificate_1 =sealApply.getCertificate();
        //原有颁发单位
        certificate_1.setIssuerUnitId(sealApply.getCerIssuer());
        if(sealApply.getIsUK()==1){

        }else{
            certificate_1.setFileState(Constant.FILE_STATE_CERANDPFX);
            //生成证书
            Map<String, String> cerAndPfxMap =  certificateService.createCerFileAndPfx(certificate_1);
            certificate_1.setPfxBase64(cerAndPfxMap.get("pfxBase64"));
            certificate_1.setCerBase64(cerAndPfxMap.get("cerBase64"));
            certificateService.updateCertificate(certificate_1);
        }
        //此处将seal的证书Id修改
        Seal seal = new Seal();
        seal.setSealId(sealApply.getSealId());
        seal.setCerId(certificate_1.getCertificateId());

        sealService.updateSeal(seal);
        //注销记录
        message.setState(0);
        messageService.updateMessage(message);

        return sealApply.getIsUK()+"@"+sealApply.getKeyId()+"@"+sealApply.getSealId()+"@"+certificate_1.getCertificateId();
    }

}
