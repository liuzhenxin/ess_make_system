package com.clt.ess.web;

import com.clt.ess.base.Constant;
import com.clt.ess.bean.ResultMessageBeen;
import com.clt.ess.entity.*;
import com.clt.ess.service.*;
import com.clt.ess.utils.FastJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static com.clt.ess.utils.dateUtil.getDate;
import static com.clt.ess.utils.uuidUtil.getUUID;

@Controller
@RequestMapping("/review")
public class ReviewController {

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
     *访问印章申请列表
     * @param unitId 单位ID
     */
    @RequestMapping(value="/list.html", method = RequestMethod.GET)
    public String list(Model model, String unitId) {

        SealApply sealApply = new SealApply();
        sealApply.setUnitId(unitId);
        //提交申请的信息
        sealApply.setApplyState(Constant.SUBMIT_APPLICATION);
        List<SealApply> sealApplyList =  sealService.findSealApply(sealApply);
        //审核通过的信息
        sealApply.setApplyState(Constant.REVIEW_THROUGH);
        List<SealApply> sealApplyList_through =  sealService.findSealApply(sealApply);
        //制作人驳回的信息
        sealApply.setApplyState(Constant.MAKE_NO_THROUGH);
        List<SealApply> sealApplyList_no_through =  sealService.findSealApply(sealApply);

        sealApplyList.addAll(sealApplyList_no_through);
        sealApplyList.addAll(sealApplyList_through);

        model.addAttribute("unit",  unitService.findUnitById(unitId));
        model.addAttribute("sealApplyList",  sealApplyList);

        return "review/review_list";

    }
    /**
     *审核详细页面
     * @param sealApplyId 申请ID
     */
    @RequestMapping(value="/review_detail.html", method = RequestMethod.GET)
    public ModelAndView seal_review_detail(Model model, String sealApplyId) {
        ModelAndView modelAndView = new ModelAndView();
        //根据 不同的申请，进入不同的页面
        SealApply sealApply = sealService.findSealApplyById(sealApplyId);
        model.addAttribute("sealApply",  sealApply);

        //当前单位对应的顶级单位支持的授权类型
        List<FileType> fileTypeList = null;

        modelAndView.setViewName("error");
        //判断申请信息类别
        switch(sealApply.getApplyType()) {
            //申请新印章
            case Constant.APPLYTYPE_NEW:
                fileTypeList = fileTypeService.findFileTypeListByTop(sealApply.getUnitId());
                modelAndView.addObject("fileTypeList",  fileTypeList);
                modelAndView.setViewName("review/review_detail");
                break;
            //注册UK
            case Constant.APPLYTYPE_REGISTER_UK:
                //查询当前单位对应的顶级单位支持的证书颁发机构
                Unit unit = unitService.findTopUnit(sealApply.getUnitId());
                String fileTypeConfig = fileTypeService.findFileTypeConfigByTop(unit.getUnitId());
                if("1".equals(fileTypeConfig)){
                    //根据平台
                    fileTypeList = fileTypeService.findFileTypeListByTop(sealApply.getUnitId());
                    modelAndView.addObject("fileTypeList",  fileTypeList);
                }else{
                    //根据UK授权值获取支持的授权文档类型
                    fileTypeList = fileTypeService.GetProductInfoFromAuthNumber(sealApply.getAuthorizationInfo());
                    modelAndView.addObject("fileTypeList",  fileTypeList);
                }
                modelAndView.setViewName("review/review_detail_uk");
                break;
            //印章重做
            case Constant.APPLYTYPE_REPEAT:
                fileTypeList = fileTypeService.findFileTypeListByTop(sealApply.getUnitId());
                modelAndView.addObject("fileTypeList",  fileTypeList);
                modelAndView.setViewName("review/review_detail_repeat");
                break;
            //印章授权延期
            case Constant.APPLYTYPE_DELAY_AUTH:
                fileTypeList = fileTypeService.findFileTypeListByTop(sealApply.getUnitId());
                modelAndView.addObject("fileTypeList",  fileTypeList);
                modelAndView.setViewName("review/review_detail_delay");
                break;
        }
        return modelAndView;
    }


    /**
     *审核通过 审核通过
     * @param sealApply 申请信息
     */
    @RequestMapping(value="/review_do.html", method = RequestMethod.POST)
    @ResponseBody
    public String seal_review_do(SealApply sealApply,Certificate c) {
        ResultMessageBeen messageBeen = new ResultMessageBeen();
        messageBeen.setMessage("success");
        messageBeen.setBody(null);
        if(sealApply.getSealApplyId()==null || "".equals(sealApply.getSealApplyId())){
            messageBeen.setMessage("error");
        }else{
            //更新信息
            //状态修改为审核通过
            sealApply.setApplyState(Constant.REVIEW_THROUGH);
            //设置审核人和审核时间
            sealApply.setReviewUserId(((User) session.getAttribute("user")).getUserId());
            sealApply.setReviewTime(getDate());
            sealService.updateSealApply(sealApply);
            //查出完整信息
            sealApply = sealService.findSealApplyById(sealApply.getSealApplyId());
        }
        //修改证书信息
        if(c.getCertificateId()==null || "".equals(c.getCertificateId())){
            messageBeen.setMessage("error");
        }else{
            certificateService.updateCertificate(c);
            messageBeen.setMessage("success");
        }
        if("success".equals(messageBeen.getMessage())){
            //添加日志
            User user =  (User) session.getAttribute("user");
            logService.addSystemLog("通过了"+sealApply.getSealName()+"的制作申请","印章申请审核通过",
                    sealApply.getUnitId(),user.getUserId(),"");
        }
        return FastJsonUtil.toJSONString(messageBeen);
    }

    /**
     *审核通过
     * @param sealApply 申请信息
     */
    @RequestMapping(value="/review_uk_do.html", method = RequestMethod.POST)
    @ResponseBody
    public String review_uk_do(SealApply sealApply) {
        ResultMessageBeen messageBeen = new ResultMessageBeen();
        messageBeen.setMessage("success");
        messageBeen.setBody(null);

        if(sealApply.getSealApplyId()==null || "".equals(sealApply.getSealApplyId())){
            messageBeen.setMessage("error");
        }else{
            //更新信息
            //状态修改为审核通过
            sealApply.setApplyState(Constant.REVIEW_THROUGH);
            //设置审核人和审核时间
            sealApply.setReviewUserId(((User) session.getAttribute("user")).getUserId());
            sealApply.setReviewTime(getDate());
            sealService.updateSealApply(sealApply);
            //查出完整信息
            sealApply = sealService.findSealApplyById(sealApply.getSealApplyId());
        }
        if("success".equals(messageBeen.getMessage())){
            //添加日志
            User user =  (User) session.getAttribute("user");
            logService.addSystemLog("通过了"+sealApply.getSealName()+"的制作申请","印章申请审核通过",
                    sealApply.getUnitId(),user.getUserId(),"");
        }
        return FastJsonUtil.toJSONString(messageBeen);
    }


    /**
     *审核通过
     * @param sealApply 申请信息
     */
    @RequestMapping(value="/review_repeat_do.html", method = RequestMethod.POST)
    @ResponseBody
    public String review_repeat_do(SealApply sealApply,Certificate c) {
        ResultMessageBeen messageBeen = new ResultMessageBeen();
        messageBeen.setMessage("success");
        messageBeen.setBody(null);
        if(sealApply.getSealApplyId()==null || "".equals(sealApply.getSealApplyId())){
            messageBeen.setMessage("error");
        }else{
            //更新信息
            //状态修改为审核通过
            sealApply.setApplyState(Constant.REVIEW_THROUGH);
            //设置审核人和审核时间
            sealApply.setReviewUserId(((User) session.getAttribute("user")).getUserId());
            sealApply.setReviewTime(getDate());
            sealService.updateSealApply(sealApply);
            //查出完整信息
            sealApply = sealService.findSealApplyById(sealApply.getSealApplyId());
        }
        //修改证书信息
        if(c.getCertificateId()==null || "".equals(c.getCertificateId())){
            messageBeen.setMessage("error");
        }else{
            certificateService.updateCertificate(c);
            messageBeen.setMessage("success");
        }
        if("success".equals(messageBeen.getMessage())){
            //添加日志
            User user =  (User) session.getAttribute("user");
            logService.addSystemLog("通过了"+sealApply.getSealName()+"的制作申请","印章申请审核通过",
                    sealApply.getUnitId(),user.getUserId(),"");
        }
        return FastJsonUtil.toJSONString(messageBeen);
    }


    /**
     *审核通过
     * @param sealApply 申请信息
     */
    @RequestMapping(value="/review_delay_do.html", method = RequestMethod.POST)
    @ResponseBody
    public String review_delay_do(SealApply sealApply) {
        ResultMessageBeen messageBeen = new ResultMessageBeen();
        messageBeen.setMessage("success");
        messageBeen.setBody(null);
        if(sealApply.getSealApplyId()==null || "".equals(sealApply.getSealApplyId())){
            messageBeen.setMessage("error");
        }else{
            //更新信息
            //状态修改为审核通过
            sealApply.setApplyState(Constant.REVIEW_THROUGH);
            //设置审核人和审核时间
            sealApply.setReviewUserId(((User) session.getAttribute("user")).getUserId());
            sealApply.setReviewTime(getDate());
            sealService.updateSealApply(sealApply);
            //查出完整信息
            sealApply = sealService.findSealApplyById(sealApply.getSealApplyId());
        }

        if("success".equals(messageBeen.getMessage())){
            //添加日志
            User user =  (User) session.getAttribute("user");
            logService.addSystemLog("通过了"+sealApply.getSealName()+"的制作申请","印章申请审核通过",
                    sealApply.getUnitId(),user.getUserId(),"");
        }
        return FastJsonUtil.toJSONString(messageBeen);
    }


    /**
     *审核驳回
     * @param sealApplyId 申请信息ID
     */
    @RequestMapping(value="/review_reject.html", method = RequestMethod.GET)
    @ResponseBody
    public String seal_review_reject(String sealApplyId, String message) {

        ResultMessageBeen messageBeen = new ResultMessageBeen();
        messageBeen.setMessage("success");
        messageBeen.setBody(null);

        //注销申请信息
        SealApply sealApply = sealService.findSealApplyById(sealApplyId);
        if(sealApply!=null){
            messageBeen.setMessage("error");
        }
        //向消息中心添加数据
        Message message_new  = new Message();
        message_new.setMessageNo(Constant.JIANGSU_CODE+getUUID());
        message_new.setSender(((User) session.getAttribute("user")).getUserId());
        if (sealApply != null) {
            message_new.setReceiver(sealApply.getApplyUserId());
        }else{
            messageBeen.setMessage("error");
        }
        message_new.setSendTime(getDate());
        message_new.setMessageType("驳回信息");
        if (sealApply != null) {
            message_new.setMessageTitle(sealApply.getSealName()+"申请被驳回");
        }else{
            messageBeen.setMessage("error");
        }
        message_new.setMessageContent(message);
        message_new.setApplyInfoId(sealApply.getSealApplyId());
        message_new.setReadState(2);
        message_new.setState(1);
        messageService.addMessage(message_new);

        //信息状态修改为审核人驳回
        sealApply.setApplyState(Constant.REVIEW_NO_THROUGH);
        sealService.updateSealApply(sealApply);
        //添加日志
        User user =  (User) session.getAttribute("user");
        logService.addSystemLog("驳回了"+sealApply.getSealName()+"的制作申请","印章申请驳回",
                sealApply.getUnitId(),user.getUserId(),"");

        return FastJsonUtil.toJSONString(messageBeen);

    }



}

