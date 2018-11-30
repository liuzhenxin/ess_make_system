package com.clt.ess.web;

import com.clt.ess.base.Constant;
import com.clt.ess.bean.ResultMessageBeen;
import com.clt.ess.dao.IPersonDao;
import com.clt.ess.entity.*;
import com.clt.ess.service.*;
import com.clt.ess.utils.FastJsonUtil;
import com.clt.ess.utils.FileUtil;
import com.clt.ess.utils.PowerUtil;
import com.clt.ess.utils.ReadPfx;
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
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.clt.ess.utils.StringUtils.isNull;
import static com.clt.ess.utils.dateUtil.getDate;
import static com.clt.ess.utils.uuidUtil.getEssUUID;
import static com.clt.ess.utils.uuidUtil.getUUID;


@Controller
@RequestMapping("/apply")
public class ApplyController {
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;

    @Autowired
    private ISealService sealService;
    @Autowired
    private IUnitService unitService;
    @Autowired
    private ISealImgService sealImgService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IMessageService messageService;
    @Autowired
    private IRoleAndPowerService roleAndPowerService;
    @Autowired
    private ILogService logService;
    @Autowired
    private IPowerService powerService;
    @Autowired
    private IFileTypeService fileTypeService;
    @Autowired
    private IApplyService applyService;
    @Autowired
    private IErrorLogService errorLogService;
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
     * @param unitId 单位id
     */
    @RequestMapping(value="/list.html", method = RequestMethod.GET)
    public ModelAndView list(Model model, String unitId) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("apply/apply_list");

        SealApply sealApply = new SealApply();
        sealApply.setUnitId(unitId);
        //申请信息列表里只显示登录人申请的信息
        sealApply.setApplyUserId(PowerUtil.getLoginUser(session).getUserId());
        //类别：提交申请
        sealApply.setApplyState(Constant.SUBMIT_APPLICATION);
        List<SealApply> sealApplyList = sealService.findSealApply(sealApply);
        //审核驳回的信息
        sealApply.setApplyState(Constant.REVIEW_NO_THROUGH);
        List<SealApply> sealApplyList_no_through = sealService.findSealApply(sealApply);
        //将驳回的信息添加到list里
        sealApplyList.addAll(sealApplyList_no_through);

        modelAndView.addObject("sealApplyList",sealApplyList);
        modelAndView.addObject("unit",unitService.findUnitById(unitId));

        return modelAndView;
    }
    /**
     *访问印章申请添加页面
     * @param unitId 单位id
     */
    @RequestMapping(value="/add.html", method = RequestMethod.GET)
    public ModelAndView add(String unitId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("apply/apply_add");
        //非空判断
        if("".equals(unitId)||unitId ==null){
            //如果空的话，返回error页面
            modelAndView.addObject("message", "访问的数据错误！");
            modelAndView.setViewName("error");
            return modelAndView;
        }
        //获取当前单位的一级单位
        Unit TopUnit = unitService.findTopUnit(unitId);
        //根据一级单位获得印章类型
        SealType sealType = new SealType();
        sealType.setTopUnitId(TopUnit.getUnitId());

        List<SealType> sealTypes = sealService.findSealType(sealType);
        modelAndView.addObject("sealTypes", sealTypes);

        //查询当前单位对应的顶级单位支持的授权类型
        List<FileType> fileTypeList = fileTypeService.findFileTypeListByTop(unitId);

        modelAndView.addObject("fileTypeList",  fileTypeList);

        modelAndView.addObject("unit",  unitService.findUnitById(unitId));

        //申请类型：新印章
        modelAndView.addObject("applyType", Constant.APPLYTYPE_NEW);

        return modelAndView;
    }
    /**
     *访问印章申请添加页面
     * @param sealApply 申请信息
     *
     */
    @RequestMapping(value="/add_do.html", method = RequestMethod.POST)
    @ResponseBody
    public String add_do(SealApply sealApply, MultipartFile attachmentFile, Certificate c) {
        //补全证书单位名称
        ResultMessageBeen messageBeen = new ResultMessageBeen();
        messageBeen.setMessage("success");
        messageBeen.setBody(null);

        // 判断文件是否为空
        if (!attachmentFile.isEmpty()) {
            String filename = FileUtil.saveFile(attachmentFile);
            if(filename ==null){
                errorLogService.addErrorLog("ApplyController-add_do-上传附件保存到磁盘时出现异常！");
                messageBeen.setMessage("error");
            }else{
                sealApply.setAttachment(filename);
            }
        }else{
            errorLogService.addErrorLog("ApplyController-add_do-上传附件为空-null！");
            messageBeen.setMessage("error");
        }
        boolean result = applyService.addSealApply(sealApply,c,session);
        if(!result){
            messageBeen.setMessage("error");
        }
        return FastJsonUtil.toJSONString(messageBeen);
    }

    /**
     *访问注册UK申请添加页面
     * @param unitId 单位ID
     */
    @RequestMapping(value="/register_uk.html", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView register_uk(String unitId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("apply/apply_register_uk");

        //单位信息
        modelAndView.addObject("unit",  unitService.findUnitById(unitId));

        SealApply sealApply = new SealApply();
        //设置申请信息类别
        sealApply.setApplyType(Constant.APPLYTYPE_REGISTER_UK);

        modelAndView.addObject("sealApply", sealApply);
        modelAndView.addObject("applyType", Constant.APPLYTYPE_REGISTER_UK);

        //获取一级单位的印章类型
        //获取当前单位的一级单位
        Unit TopUnit = unitService.findTopUnit(unitId);

        //根据一级单位获得印章类型
        SealType sealType = new SealType();
        sealType.setTopUnitId(TopUnit.getUnitId());
        List<SealType> sealTypes = sealService.findSealType(sealType);

        modelAndView.addObject("sealTypes", sealTypes);

        return modelAndView;
    }

    /**
     * 注册uk请求
     * @param sealApply 申请信息
     * @param attachmentFile 上传的附件
     * @param attachmentPath 附件的文件名（此处是指读取UK后客户端上传附件所获得文件名）
     */
    @RequestMapping(value="/register_do.html", method = RequestMethod.POST)
    @ResponseBody
    public String register_do(SealApply sealApply, MultipartFile attachmentFile,String attachmentPath,
                Certificate c) {
        //返回结果对象
        ResultMessageBeen messageBeen = new ResultMessageBeen();
        //结果类型
        messageBeen.setMessage("success");
        //携带数据对象
        messageBeen.setBody(null);
        //先判断是否上传文件，如果没有按照附件路径找到文件。
        //如果有，将上传文件作为附件。
        // 判断附件地址是否为空
        if("".equals(attachmentPath)){
            //附件对象是否为空
            if (!attachmentFile.isEmpty()) {
                 //上传文件名称
                String filename = FileUtil.saveFile(attachmentFile);

                if(filename == null){
                    errorLogService.addErrorLog("ApplyController-add_do-上传附件保存到磁盘时出现异常！");
                    messageBeen.setMessage("error");
                }else{
                    sealApply.setAttachment(filename);
                }
            }else{
                errorLogService.addErrorLog("ApplyController-add_do-上传附件和附件地址都为空！");
                messageBeen.setMessage("error");
            }
        }else{
            sealApply.setAttachment(attachmentPath);
        }
        //添加申请信息
        boolean result = applyService.addSealApply(sealApply,c,session);
        if(!result){
            messageBeen.setMessage("error");
        }
        return FastJsonUtil.toJSONString(messageBeen);
    }

    /**
     * 获取人员信息
     * @param keyword 关键词
     */
    @RequestMapping(value="/findPerson.html", method = RequestMethod.GET)
    @ResponseBody
    public String findPerson(String keyword) {
        //结果对象
        ResultMessageBeen messageBeen = new ResultMessageBeen();
        //判断关键词是为空
        if(!"".equals(keyword) && keyword!=null){
            //根据关键词查找人员信息
            List<Person> personList = userService.findPersonListByKeyword(keyword);
            //添加数据到返回结果中
            messageBeen.setBody(personList);
            //返回结果类型为成功
            messageBeen.setMessage("ESSSUCCESS");
        }else {
            messageBeen.setMessage("ESSERROR");
            messageBeen.setBody("关键字不可为空！");
        }
        return new String(FastJsonUtil.toJSONString(messageBeen).getBytes(StandardCharsets.UTF_8),StandardCharsets.ISO_8859_1);
    }

    /**
     *解析pfx证书
     * @param pfxBase64 pfx证书base64编码
     * @param cerPsw 证书密码
     */
    @RequestMapping(value="/get_pfx_info.html", method = RequestMethod.POST)
    @ResponseBody
    public String getPfxInfo(String pfxBase64, String cerPsw){

        ResultMessageBeen messageBeen = new ResultMessageBeen();
        messageBeen.setMessage("success");
        messageBeen.setBody(null);
        if(!isNull(pfxBase64)&&!isNull(cerPsw)){
            //当证书和密码都不为空时
            Map<String,String> cerInfo = new HashMap<>();
            try {
                //解析pfx证书的信息
                cerInfo = ReadPfx.GetCertInfoFromPfxBase64(pfxBase64,cerPsw);
            } catch (Exception e) {
                errorLogService.addErrorLog("ApplyController-getPfxInfo-解析pfx证书出错-null！");
                messageBeen.setMessage("error");
                e.printStackTrace();
            }
            //证书所有人信息
            String owner = cerInfo.get("owner");

            String[] a = owner.split(", ");
            Map<String,String> ownerInfo = new HashMap<>();
            ownerInfo.put(a[a.length-1].split("=")[0],a[a.length-1].split("=")[1]);
            ownerInfo.put(a[a.length-2].split("=")[0],a[a.length-2].split("=")[1]);
            ownerInfo.put(a[a.length-3].split("=")[0],a[a.length-3].split("=")[1]);
            ownerInfo.put(a[a.length-4].split("=")[0],a[a.length-4].split("=")[1]);
            ownerInfo.put(a[a.length-5].split("=")[0],a[a.length-5].split("=")[1]);
            ownerInfo.put(a[a.length-6].split("=")[0],a[a.length-6].split("=")[1]);

            messageBeen.setBody(ownerInfo);
        }else{
            messageBeen.setMessage("error");
        }
        return new String(FastJsonUtil.toJSONString(messageBeen).getBytes(StandardCharsets.UTF_8),StandardCharsets.ISO_8859_1);
    }

    /**
     *删除申请信息
     * @param sealApplyId 申请信息ID
     */
    @RequestMapping(value="/delete.html", method = RequestMethod.GET)
    @ResponseBody
    public String sealApply_delete(String sealApplyId){

        ResultMessageBeen messageBeen = new ResultMessageBeen();
        messageBeen.setMessage("success");
        messageBeen.setBody(null);
        //要删除的申请信息ID
        SealApply sealApply  = new SealApply();
        sealApply.setSealApplyId(sealApplyId);
        //执行删除动作
        boolean result = sealService.delSealApply(sealApply);
        if(!result){
            messageBeen.setMessage("error");
            errorLogService.addErrorLog("ApplyController-sealApply_delete-删除申请信息时出错！");
        }
        return FastJsonUtil.toJSONString(messageBeen);
    }

    /**
     *访问印章申请重做页面
     * @param sealId 印章id
     */
    @RequestMapping(value="/repeat.html", method = RequestMethod.GET)
    public ModelAndView repeat(String sealId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("apply/apply_repeat");

        //查询原有印章信息
        Seal seal = sealService.findSealById(sealId);
       if(seal!=null){
           //根据原有印章信息建立新的印章申请信息
           SealApply sealApply = new SealApply();
           //设置重做申请的印章Id
           sealApply.setSealId(seal.getSealId());
           //设置重做申请的申请了类别
           sealApply.setApplyType(Constant.APPLYTYPE_REPEAT);
           //设置重做申请印章类型
           sealApply.setSealTypeId(seal.getSealTypeId());
           //设置重做申请印章名称
           sealApply.setSealName(seal.getSealName());
           //设置重做申请印章单位
           sealApply.setUnitId(seal.getUnitId());
           sealApply.setUnit(seal.getUnit());
           //设置重做申请的身份证信息（手签）
           sealApply.setSealHwUserIdNum(seal.getSealHwIdNum());
           //设置重做申请的授权类型代码
           sealApply.setFileTypeNum(seal.getFileTypeNum());

           modelAndView.addObject("sealApply", sealApply);
           //查询当前单位对应的顶级单位支持的授权类型
           List<FileType> fileTypeList = fileTypeService.findFileTypeListByTop(seal.getUnitId());

           modelAndView.addObject("fileTypeList",  fileTypeList);

           //获取一级单位的印章类型
           //获取当前单位的一级单位
           Unit TopUnit = unitService.findTopUnit(seal.getUnitId());
           //根据一级单位获得印章类型
           SealType sealType = new SealType();
           sealType.setTopUnitId(TopUnit.getUnitId());
           List<SealType> sealTypes = sealService.findSealType(sealType);
           modelAndView.addObject("sealTypes", sealTypes);
       }else{
           modelAndView.setViewName("error");
           modelAndView.addObject("message", "印章不存在");
       }
        return modelAndView;
    }
    /**
     *访问印章申请添加页面
     * @param
     */
    @RequestMapping(value="/repeat_do.html", method = RequestMethod.POST)
    @ResponseBody
    public String repeat_do(SealApply sealApply, MultipartFile attachmentFile, Certificate c) {
        //补全证书单位名称
        ResultMessageBeen messageBeen = new ResultMessageBeen();
        messageBeen.setMessage("success");
        messageBeen.setBody(null);
        // 判断文件是否为空
        if (!attachmentFile.isEmpty()) {
            try {
                // 文件保存路径
                String fileName = attachmentFile.getOriginalFilename();
                String fileType = fileName.split("\\.")[1];
                String UUID = getUUID();
                String filePath = Constant.ATTACHMENT_PATH + UUID+"."+fileType;
                // 转存文件
                attachmentFile.transferTo(new File(filePath));
                sealApply.setAttachment(UUID+"."+fileType);
            } catch (Exception e) {
                e.printStackTrace();
                messageBeen.setMessage("error");
            }
        }else{
            messageBeen.setMessage("error");
        }

        boolean result = applyService.addSealApply(sealApply,c,session);
        if(!result){
            messageBeen.setMessage("error");
        }
        return FastJsonUtil.toJSONString(messageBeen);
    }


    /**
     *访问注册UK申请添加页面
     * @param
     */
    @RequestMapping(value="/delay.html", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView delay(String sealId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("apply/apply_delay");

        Seal seal = sealService.findSealById(sealId);
        if(seal==null){
            modelAndView.setViewName("error");
            modelAndView.addObject("message", "印章不存在！");
        }
        modelAndView.addObject("seal", seal);

        return modelAndView;
    }
    /**
     *访问印章延期动作
     * @param sealId
     */
    @RequestMapping(value="/delay_do.html", method = RequestMethod.POST)
    @ResponseBody
    public String seal_delay_do(String sealId, String sealStartTime,String sealEndTime) {
        //补全证书单位名称
        ResultMessageBeen messageBeen = new ResultMessageBeen();
        messageBeen.setMessage("success");
        messageBeen.setBody(null);

        Seal seal = sealService.findSealById(sealId);
        if(seal!=null){
            //根据印章延期申请制作新的申请信息
            SealApply sealApply = new SealApply();
            //设置新申请信息id
            sealApply.setSealApplyId(getEssUUID(seal.getUnitId()));
            //设置申请的印章Id
            sealApply.setSealId(sealId);
            //设置申请的类别 印章延期
            sealApply.setApplyType(Constant.APPLYTYPE_DELAY_AUTH);
            //设置延期时间 (新数据)
            sealApply.setSealStartTime(sealStartTime);
            sealApply.setSealEndTime(sealEndTime);

            sealApply.setUnitId(seal.getUnitId());
            sealApply.setSealTypeId(seal.getSealTypeId());
            sealApply.setSealImgId(seal.getSealImgId());
            sealApply.setSealName(seal.getSealName());
            sealApply.setCertificateId(seal.getCerId());
            sealApply.setIsUK(seal.getIsUK());
            sealApply.setKeyId(seal.getKeyId());
            sealApply.setSealHwUserIdNum(seal.getSealHwIdNum());
            sealApply.setFileTypeNum(seal.getFileTypeNum());
            sealApply.setCerIssuer(seal.getCertificate().getIssuerUnitId());

            sealApply.setAuthorizationInfo(seal.getAuthorizationInfo());
            sealApply.setAuthorizationTime(seal.getAuthorizationTime());

            //设置申请人信息
            //设置申请人和申请时间
            sealApply.setApplyUserId(((User) session.getAttribute("user")).getUserId());
            sealApply.setApplyTime(getDate());
            sealApply.setApplyState(Constant.SUBMIT_APPLICATION);
            //向数据库添加申请信息
            sealService.addSealApply(sealApply);
            User user =  (User) session.getAttribute("user");
            logService.addSystemLog("申请延期印章"+sealApply.getSealName(),"印章延期",
                    sealApply.getUnitId(),user.getUserId(),"");
        }
        return FastJsonUtil.toJSONString(messageBeen);
    }

}
