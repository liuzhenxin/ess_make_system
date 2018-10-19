package com.clt.ess.web;

import com.clt.ess.base.Constant;
import com.clt.ess.dao.IPersonDao;
import com.clt.ess.entity.*;
import com.clt.ess.service.*;
import com.clt.ess.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.clt.ess.utils.PowerUtil.getLoginUser;
import static com.clt.ess.utils.dateUtil.getDate;
import static com.clt.ess.utils.uuidUtil.getEssUUID;
import static com.clt.ess.utils.uuidUtil.getUUID;
import static com.multica.crypt.MuticaCrypt.ESSGetBase64Encode;


@Controller
@RequestMapping("/seal")
public class SealController {
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;

    @Autowired
    private IPersonDao personDao;
    @Autowired
    private ISealService sealService;
    @Autowired
    private IUnitService unitService;
    @Autowired
    private ICertificateService certificateService;
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
     *访问单位公章列表
     * @param unitId
     */
    @RequestMapping(value="/seal_list.html", method = RequestMethod.GET)
    public String seal_list(Model model, String unitId) {
        Seal seal = new Seal();
        seal.setUnitId(unitId);
        List<Seal> sealList = sealService.findSealList(seal);
        //遍历印章数据
        Iterator<Seal> it = sealList.iterator();
        while(it.hasNext()){
            Seal x = it.next();
            //去掉手签
            if("025st7".equals(x.getSealTypeId())){
                it.remove();
            }

        }
        while(it.hasNext()){
            Seal x = it.next();
            //去掉注销的印章
            if(x.getSealState()==Constant.SEAL_STATE_INVALID){
                it.remove();
            }
        }

        model.addAttribute("unit",  unitService.findUnitById(unitId));
        model.addAttribute("sealList",  sealList);
        //暂停与恢复
        if(powerService.getPowerForButton(session,"makeSealPower_pauseSeal")){
            model.addAttribute("pauseButton",  true);
        }else {
            model.addAttribute("pauseButton",  false);
        }
        //重做
        if(powerService.getPowerForButton(session,"makeSealPower_redoSeal")){
            model.addAttribute("redoSealButton",  true);
        }else {
            model.addAttribute("redoSealButton",  false);
        }
        //注销
        if(powerService.getPowerForButton(session,"makeSealPower_stopSeal")){
            model.addAttribute("stopButton",  true);
        }else {
            model.addAttribute("stopButton",  false);
        }
        //延期
        if(powerService.getPowerForButton(session,"makeSealPower_sealDelay")){
            model.addAttribute("delayButton",  true);
        }else {
            model.addAttribute("delayButton",  false);
        }
        return "seal/seal_list";
    }

    /**
     *访问手签列表
     * @param depId 部门iD
     */
    @RequestMapping(value="/sign_list.html", method = RequestMethod.GET)
    public String sign_list(Model model, String depId) {
        //查询部门人员
        User user = new User();
        user.setDepId(depId);
        user.setState(Constant.STATE_YES);
        List<User> userList = userService.findUserList(user);
        //根据部门人员的身份证号查询是否拥有手签
        List<String> personIdNums = new ArrayList<>();
        if(userList.size()>=1){
            //获取部门人员的身份证号list
            for(User u :userList){
                String idm = u.getPerson().getIdNum();
                personIdNums.add(idm);
            }

            //根据List查询印章信息
            List<Seal> sealList = sealService.findSealListByIdNums(personIdNums);
            //遍历印章数据
            Iterator<Seal> it = sealList.iterator();
            while(it.hasNext()){
                Seal x = it.next();
                //去掉注销的印章
                if(x.getSealState()==Constant.SEAL_STATE_INVALID){
                    it.remove();
                }
            }

            model.addAttribute("sealList",  sealList);
        }
        String unitId = ((User) session.getAttribute("user")).getUnitId();
        model.addAttribute("unit",  unitService.findUnitById(unitId));
        //权限
        //暂停与恢复
        if(powerService.getPowerForButton(session,"makeSealPower_pauseSeal")){
            model.addAttribute("pauseButton",  true);
        }else {
            model.addAttribute("pauseButton",  false);
        }
        //重做
        if(powerService.getPowerForButton(session,"makeSealPower_redoSeal")){
            model.addAttribute("redoSealButton",  true);
        }else {
            model.addAttribute("redoSealButton",  false);
        }
        //注销
        if(powerService.getPowerForButton(session,"makeSealPower_stopSeal")){
            model.addAttribute("stopButton",  true);
        }else {
            model.addAttribute("stopButton",  false);
        }
        //延期
        if(powerService.getPowerForButton(session,"makeSealPower_sealDelay")){
            model.addAttribute("delayButton",  true);
        }else {
            model.addAttribute("delayButton",  false);
        }
        return "seal/seal_list";
    }

    /**
     *访问印章申请列表
     * @param unitId
     */
    @RequestMapping(value="/seal_apply_list.html", method = RequestMethod.GET)
    public String seal_apply_list(Model model, String unitId) {

        SealApply sealApply = new SealApply();
        sealApply.setUnitId(unitId);
        sealApply.setApplyUserId(PowerUtil.getLoginUser(session).getUserId());
        //提交申请的信息
        sealApply.setApplyState(Constant.SUBMIT_APPLICATION);
        List<SealApply> sealApplyList = sealService.findSealApply(sealApply);
        //审核驳回的信息
        sealApply.setApplyState(Constant.REVIEW_NO_THROUGH);
        List<SealApply> sealApplyList_no_through = sealService.findSealApply(sealApply);

        sealApplyList.addAll(sealApplyList_no_through);

        model.addAttribute("sealApplyList",sealApplyList);
        model.addAttribute("unit",unitService.findUnitById(unitId));

        return "seal/seal_apply_list";
    }


    /**
     *访问印章申请页面
     * @param unitId
     */
    @RequestMapping(value="/seal_apply.html", method = RequestMethod.GET)
    public String seal_apply(Model model, String unitId) {
        //获取一级单位的印章类型
        //获取当前单位的一级单位
        Unit TopUnit = unitService.findTopUnit(unitId);

        //根据一级单位获得印章类型
        SealType sealType = new SealType();
        sealType.setTopUnitId(TopUnit.getUnitId());
        List<SealType> sealTypes = sealService.findSealType(sealType);

        //查询当前单位对应的顶级单位支持的授权类型
        List<FileType> fileTypeList = fileTypeService.findFileTypeListByTop(unitId);
        model.addAttribute("fileTypeList",  fileTypeList);
        model.addAttribute("unit",  unitService.findUnitById(unitId));
        model.addAttribute("sealTypes", sealTypes);
        model.addAttribute("applyType", Constant.APPLYTYPE_NEW);
        return "seal/seal_apply";
    }

    /**
     *申请添加新印章
     */
    @RequestMapping(value="/seal_apply_do.html", method = RequestMethod.POST)
    @ResponseBody
    public String seal_apply_do(SealApply sealApply, MultipartFile gifImg,
            MultipartFile jpgImg, Certificate c){

        SealImg sealImg =  sealImgService.createSealImg(sealApply,gifImg,jpgImg);

//        boolean result = applyService.addSealApply(sealApply,sealImg,c,session);
//        if(result){
//            return "success";
//        }
        return "error";
    }

    /**
     *删除申请信息
     * @param sealApplyId 申请信息ID
     */
    @RequestMapping(value="/sealApply_delete.html", method = RequestMethod.GET)
    @ResponseBody
    public String sealApply_delete(Model model, String sealApplyId) {
        SealApply sealApply  = new SealApply();
        sealApply.setSealApplyId(sealApplyId);
        sealService.delSealApply(sealApply);
        return "success";
    }

    /**
     * 获取人员信息
     * @param phone 手机号
     */
    @RequestMapping(value="/findPerson.html", method = RequestMethod.GET)
    @ResponseBody
    public void findPerson(String phone) {
        Person person = new Person();
        person.setState(1);
        person.setPhone(phone);
        String result_1 ="";
        if(!"".equals(phone) && StringUtils.isInteger(phone) && phone.length()==11){
            Person p = personDao.findPerson(person);
            ESSGetBase64Encode(p.getPersonName().getBytes());
            result_1 = p.getIdNum()+"@"+p.getPhone()+"@"+p.getPersonName();
        }else {
            result_1 =  "error@手机号输入错误，请检查！";
        }

        try {
            PrintWriter out = response.getWriter();  //输出中文，这一句一定要放到前面两句的后面，否则中文返回到页面是乱码
            out.print(new String(result_1.getBytes("utf8"),"iso8859-1"));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *访问印章申请列表
     * @param unitId 单位ID
     */
    @RequestMapping(value="/seal_review.html", method = RequestMethod.GET)
    public String seal_review(Model model, String unitId) {
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

        return "seal/seal_review";
    }

    /**
     *审核详细页面
     * @param sealApplyId
     */
    @RequestMapping(value="/seal_review_detail.html", method = RequestMethod.GET)
    public String seal_review_detail(Model model, String sealApplyId) {
        //根据 不同的申请，进入不同的页面
        SealApply sealApply = new SealApply();
        sealApply.setSealApplyId(sealApplyId);
        sealApply = sealService.findSealApplyById(sealApplyId);
        model.addAttribute("sealApply",  sealApply);
        //查询当前单位对应的顶级单位支持的授权类型

        List<FileType> fileTypeList = fileTypeService.findFileTypeListByTop(sealApply.getUnitId());
        model.addAttribute("fileTypeList",  fileTypeList);
        String back_page = "error";
        //判断申请信息类别
        switch(sealApply.getApplyType()) {
            //申请新印章
            case Constant.APPLYTYPE_NEW:
                back_page = "seal/seal_review_detail";
                break;
            //注册UK
            case Constant.APPLYTYPE_REGISTER_UK:
                back_page = "seal/seal_review_detail_register_uk";
                break;
            //印章重做
            case Constant.APPLYTYPE_REPEAT:
                back_page = "seal/seal_review_detail";
                break;
            //印章授权延期
            case Constant.APPLYTYPE_DELAY_AUTH:
                back_page = "seal/seal_review_detail_delay";
                break;
        }
        return back_page;
    }

    /**
     *审核通过
     * @param sealApply
     */
    @RequestMapping(value="/seal_review_do.html", method = RequestMethod.POST)
    @ResponseBody
    public String seal_review_do(SealApply sealApply,Certificate c) {
        //更新信息
        //状态修改为审核通过
        sealApply.setApplyState(Constant.REVIEW_THROUGH);
        //设置审核人和审核时间
        sealApply.setReviewUserId(((User) session.getAttribute("user")).getUserId());
        sealApply.setReviewTime(getDate());

        sealService.updateSealApply(sealApply);

        //查出完整信息
        sealApply = sealService.findSealApplyById(sealApply.getSealApplyId());
        //修改证书信息
        if(c.getCertificateId()!=null && "".equals(c.getCertificateId())){
            certificateService.updateCertificate(c);
        }
        //添加日志
        User user =  (User) session.getAttribute("user");
        logService.addSystemLog("通过了"+sealApply.getSealName()+"的制作申请","印章申请审核通过",
                sealApply.getUnitId(),user.getUserId(),"");
        return "{\"success\": \"success\"}";
    }
    /**
     *审核驳回
     * @param sealApplyId 申请信息ID
     */
    @RequestMapping(value="/seal_review_reject.html", method = RequestMethod.GET)
    @ResponseBody
    public String seal_review_reject(String sealApplyId, String message) {
        //注销申请信息
        SealApply sealApply = new SealApply();
        sealApply.setSealApplyId(sealApplyId);

        List<SealApply> sealApplyList = sealService.findSealApply(sealApply);
        if(sealApplyList.size()>=1){
            sealApply = sealApplyList.get(0);
        }

        //向消息中心添加数据
        Message message_new  = new Message();
        message_new.setMessageNo(Constant.JIANGSU_CODE+getUUID());
        message_new.setSender(((User) session.getAttribute("user")).getUserId());
        message_new.setReceiver(sealApply.getApplyUserId());
        message_new.setSendTime(getDate());
        message_new.setMessageType("驳回信息");
        message_new.setMessageTitle(sealApply.getSealName()+"申请被驳回");
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

        return "success";
    }

    /**
     *访问印章制作页面
     * @param unitId 单位ID
     */
    @RequestMapping(value="/seal_make.html", method = RequestMethod.GET)
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

        return "seal/seal_make";

    }

    /**
     *访问印章制作
     * @param sealApplyId 申请信息ID
     */
    @RequestMapping(value="/seal_make_detail.html", method = RequestMethod.GET)
    public String seal_make_detail(Model model, String sealApplyId) {
        //根据申请信息ID获取详细信息

        SealApply sealApply = sealService.findSealApplyById(sealApplyId);
        if(sealApply==null){
            return "error";
        }
        model.addAttribute("sealApply",sealApply);
        //根据申请类别确定到达哪一个页面


        //查询当前单位对应的顶级单位支持的授权类型
        List<FileType> fileTypeList = fileTypeService.findFileTypeListByTop(sealApply.getUnitId());

        model.addAttribute("fileTypeList",  fileTypeList);

        String back_page = "error";
        //判断申请信息类别
        switch(sealApply.getApplyType()){
            //申请新印章
            case Constant.APPLYTYPE_NEW:
                //查询当前单位对应的顶级单位支持的证书颁发机构
                Unit unit = unitService.findTopUnit(sealApply.getUnitId());
                if(!"".equals(sealApply.getCerIssuer())){
                    //获取证书颁发机构
                    List<IssuerUnit> issuerUnitList = unitService.findIssuerUnitByUnitId(unit.getUnitId());
                    model.addAttribute("issuerUnitList", issuerUnitList);
                }
                back_page =  "seal/seal_make_detail";
                break;
            //注册UK
            case Constant.APPLYTYPE_REGISTER_UK:
                back_page =  "seal/seal_make_detail_UK";
                break;
            //印章重做
            case Constant.APPLYTYPE_REPEAT:

                //查询当前单位对应的顶级单位支持的证书颁发机构
                Unit unit_1 = unitService.findTopUnit(sealApply.getUnitId());
                if(!"".equals(sealApply.getCerIssuer())){
                    //获取证书颁发机构
                    List<IssuerUnit> issuerUnitList = unitService.findIssuerUnitByUnitId(unit_1.getUnitId());
                    model.addAttribute("issuerUnitList", issuerUnitList);
                }
                back_page =  "seal/seal_make_detail";
                //...;
                break;
            //印章授权延期
            case Constant.APPLYTYPE_DELAY_AUTH:

                back_page =  "seal/seal_make_detail_delay_auth";
                //...;
                break;
            //证书授权延期
            case Constant.APPLYTYPE_DELAY_CER:
                //查询当前单位对应的顶级单位支持的证书颁发机构
                Unit unit_2 = unitService.findTopUnit(sealApply.getUnitId());
                //获取证书颁发机构
                List<IssuerUnit> issuerUnitList_2 = unitService.findIssuerUnitByUnitId(unit_2.getUnitId());
                model.addAttribute("issuerUnitList", issuerUnitList_2);
                back_page =  "seal/seal_make_detail_delay_cer";
                //...;
                break;
            default:
                //...;
                break;
        }
        return back_page;

    }

    /**
     *访问印章制作
     * @param sealApply
     */
    @RequestMapping(value="/seal_make_do.html", method = RequestMethod.POST)
    @ResponseBody
    public String seal_make_do(Model model, SealApply sealApply,Certificate c) {

        //更新修改的信息
        sealService.updateSealApply(sealApply);

        //修改证书信息
        if(!(c.getCertificateId()==null||"".equals(c.getCertificateId()))){
            certificateService.updateCertificate(c);
        }

        //查出完整信息
        sealApply = sealService.findSealApplyById(sealApply.getSealApplyId());

//        if(sealService.isAddSeal(sealApply.getUnitId())!=3){
//            return "{\"message\": \"error\"}";
//        }

        User user =  getLoginUser(session);
        //判断申请信息类别
        switch(sealApply.getApplyType()){
            //申请新印章
            case Constant.APPLYTYPE_NEW:

                Seal result_seal = makeSeal(sealApply);
                if(result_seal==null){
                    return "{\"message\": \"error\"}";
                }
                //新印章设置印章直接有效
                result_seal.setSealState(Constant.SEAL_STATE_VALID);
                sealService.updateSeal(result_seal);

                logService.addSystemLog("制作了"+result_seal.getSealName(),"印章制作",
                        result_seal.getUnitId(),user.getUserId(),"");

//                return FastJsonUtil.toJSONString(sealService.findSealList(result_seal).get(0));
                return "{\"message\": \"success\"}";
            //注册UK
            case Constant.APPLYTYPE_REGISTER_UK:
                Seal result_seal1 = makeSeal(sealApply);
                if(result_seal1==null){
                    return "{\"message\": \"error\"}";
                }
                //向消息中心发消息
                Message message_new  = new Message();
                message_new.setMessageNo(getEssUUID(result_seal1.getUnitId()));
                message_new.setSender(((User) session.getAttribute("user")).getUserId());
                message_new.setReceiver(sealApply.getApplyUserId());
                message_new.setSendTime(getDate());
                message_new.setMessageType("2");
                message_new.setMessageTitle("注册成功,请确认！");
                message_new.setMessageContent(sealApply.getSealName()+"UK注册已审核制作完成，请确认！");
                message_new.setApplyInfoId(sealApply.getSealApplyId());
                message_new.setReadState(2);
                message_new.setState(1);
                messageService.addMessage(message_new);

                logService.addSystemLog("制作了"+result_seal1.getSealName(),"印章制作",
                        result_seal1.getUnitId(),user.getUserId(),"");
//                return FastJsonUtil.toJSONString(sealService.findSealList(result_seal1).get(0));
                return "{\"message\": \"success\"}";
            //印章重做
            case Constant.APPLYTYPE_REPEAT:
                //删除原来的印章
                Seal seal = new Seal();
                if(seal==null){
                    return "{\"message\": \"error\"}";
                }
                seal.setSealId(sealApply.getSealId());
                sealService.delSeal(seal);
                //制作新的印章，印章id不变
                Seal result_seal2 = makeSeal(sealApply);

                //新印章设置印章直接有效
                result_seal2.setSealState(Constant.SEAL_STATE_VALID);
                sealService.updateSeal(result_seal2);

                //向消息中心发消息
                Message message_new_1  = new Message();
                message_new_1.setMessageNo(getEssUUID(result_seal2.getUnitId()));
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

                logService.addSystemLog("制作了"+result_seal2.getSealName(),"印章制作",
                        result_seal2.getUnitId(),user.getUserId(),"");

//                return FastJsonUtil.toJSONString(sealService.findSealList(result_seal2).get(0));
                return "{\"message\": \"success\"}";
            //印章授权延期
            case Constant.APPLYTYPE_DELAY_AUTH:

                //状态修改为制作完成
                sealApply.setApplyState(Constant.MAKE_COMPLETION);
                //设置制作人和制作时间
                sealApply.setMakeUserId(((User) session.getAttribute("user")).getUserId());
                sealApply.setMakeTime(getDate());
                //更新申请信息数据
                sealService.updateSealApply(sealApply);

                //向消息中心发消息
                Message message_new_2  = new Message();
                message_new_2.setMessageNo(getEssUUID(sealApply.getUnitId()));
                message_new_2.setSender(((User) session.getAttribute("user")).getUserId());
                message_new_2.setReceiver(sealApply.getApplyUserId());
                message_new_2.setSendTime(getDate());
                message_new_2.setMessageType("4");
                message_new_2.setMessageTitle("授权延期确认");
                message_new_2.setMessageContent("您申请"+sealApply.getSealName()+"的授权延期已审核制作完成,请您确认生效！");
                message_new_2.setApplyInfoId(sealApply.getSealApplyId());
                message_new_2.setReadState(2);
                message_new_2.setState(1);
                messageService.addMessage(message_new_2);

                logService.addSystemLog("制作了"+sealApply.getSealName(),"印章制作",
                        sealApply.getUnitId(),user.getUserId(),"");

                return "{\"message\": \"success\"}";
            //证书授权延期
            case Constant.APPLYTYPE_DELAY_CER:

                    //状态修改为制作完成
                sealApply.setApplyState(Constant.MAKE_COMPLETION);
                    //设置制作人和制作时间
                sealApply.setMakeUserId(((User) session.getAttribute("user")).getUserId());
                sealApply.setMakeTime(getDate());

                    //新的授权单位
                sealApply.setCerIssuer(sealApply.getCerIssuer());
                    //更新申请信息数据
                sealService.updateSealApply(sealApply);

                if(sealApply.getIsUK()==1){
                    //uk印章
                    //向消息中心发消息
                    Message message_new_3  = new Message();
                    message_new_3.setMessageNo(getEssUUID(sealApply.getUnitId()));
                    message_new_3.setSender(((User) session.getAttribute("user")).getUserId());
                    message_new_3.setReceiver(sealApply.getApplyUserId());
                    message_new_3.setSendTime(getDate());
                    message_new_3.setMessageType("5");
                    message_new_3.setMessageTitle("证书延期确认");
                    message_new_3.setMessageContent("您申请"+sealApply.getSealName()+"的证书延期已审核制作完成,请您确认生效！！");
                    message_new_3.setApplyInfoId(sealApply.getSealApplyId());
                    message_new_3.setReadState(2);
                    message_new_3.setState(1);
                    messageService.addMessage(message_new_3);

                }else {
                    //非UK
                    //更新证书数据
                    //制作新证书
                    //设置证书颁发单位
                    //设置证书有效期生成和到期时间
                    Certificate certificate_1 =sealApply.getCertificate();
                    //原有颁发单位
                    certificate_1.setIssuerUnitId(sealApply.getCerIssuer());


                    certificate_1.setFileState(Constant.FILE_STATE_CERANDPFX);
                    //生成证书
                    Map<String, String> cerAndPfxMap =  certificateService.createCerFileAndPfx(certificate_1);

                    certificate_1.setPfxBase64(cerAndPfxMap.get("pfxBase64"));
                    certificate_1.setCerBase64(cerAndPfxMap.get("cerBase64"));

                    certificateService.updateCertificate(certificate_1);
                    //此处将seal的证书Id修改
                    Seal seal_1 = new Seal();
                    seal_1.setSealId(sealApply.getSealId());
                    seal_1.setCerId(certificate_1.getCertificateId());
                    sealService.updateSeal(seal_1);
                }
                logService.addSystemLog("制作了"+sealApply.getSealName(),"印章制作",
                        sealApply.getUnitId(),user.getUserId(),"");

                return "{\"message\": \"success\"}";

        }
        return "{\"message\": \"error\"}";
    }
    public Seal makeSeal(SealApply sealApply){
        Seal seal = null;
            //状态修改为制作完成
        sealApply.setApplyState(Constant.MAKE_COMPLETION);
        //设置制作人和制作时间
        sealApply.setMakeUserId(((User) session.getAttribute("user")).getUserId());
        sealApply.setMakeTime(getDate());
        if(sealApply.getCertificate().getFileState()==1){
            //证书未生成 申请新UK或者重做
            //设置证书颁发单位
            //设置证书有效期生成和到期时间
            Certificate certificate_1 =sealApply.getCertificate();
            certificate_1.setIssuerUnitId(sealApply.getCerIssuer());
            if(sealApply.getIsUK()==1){
//                //是
                certificate_1.setFileState(Constant.FILE_STATE_CER);
                boolean result_cer = certificateService.updateCertificate(certificate_1);
            }else {
                //否
                certificate_1.setCerPsw(StringUtils.getEncryptPwd());
                certificate_1.setFileState(Constant.FILE_STATE_CERANDPFX);
                //生成证书
                Map<String, String> cerAndPfxMap =  certificateService.createCerFileAndPfx(certificate_1);

                certificate_1.setPfxBase64(cerAndPfxMap.get("pfxBase64"));
                certificate_1.setCerBase64(cerAndPfxMap.get("cerBase64"));

                boolean result_cer = certificateService.updateCertificate(certificate_1);
            }
        }
        if(sealApply.getCertificate().getFileState()==2){

        }
        boolean result = sealService.updateSealApply(sealApply);
        if (result) {
            //添加新印章
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
            //keyId
            newSeal.setKeyId(sealApply.getKeyId());
            //是否UK印章
            newSeal.setIsUK(sealApply.getIsUK());
            //印章状态有效
            newSeal.setSealState(Constant.SEAL_STATE_INVALID);
            //插入数据库
            boolean result1 = sealService.addSeal(newSeal);
            if(result1){
                seal = newSeal;
            }
        }
        return seal;
    }


    /**
     *制作驳回
     * @param sealApplyId 申请信息ID
     */
    @RequestMapping(value="/seal_make_reject.html", method = RequestMethod.GET)
    @ResponseBody
    public String seal_make_reject(Model model, String sealApplyId, String message) {
        //注销申请信息
        SealApply sealApply = new SealApply();
        sealApply.setSealApplyId(sealApplyId);

        List<SealApply> sealApplyList = sealService.findSealApply(sealApply);
        if(sealApplyList.size()>=1){
            sealApply = sealApplyList.get(0);
        }

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

        return "success";
    }
    /**
     * 印章重做只保留原有印章的单位，名称，类型，id
     *访问印章重做页面
     * @param sealId
     */
    @RequestMapping(value="/seal_repeat.html", method = RequestMethod.GET)
    public String seal_repeat(Model model, String sealId) {

        //查询原有印章信息
        Seal seal = new Seal();
        seal.setSealId(sealId);
        List<Seal> sealList = sealService.findSealList(seal);
        if(sealList.size()==1){
            seal = sealList.get(0);
        }
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
        //设置重做申请的印章图像
        sealApply.setSealImg(seal.getSealImg());
        sealApply.setSealImgId(seal.getSealImgId());
        //设置重做的证书信息
        sealApply.setCertificate(seal.getCertificate());
        //设置重做申请的身份证信息（手签）
        sealApply.setSealHwUserIdNum(seal.getSealHwIdNum());

        sealApply.setFileTypeNum(seal.getFileTypeNum());

        model.addAttribute("sealApply", sealApply);

        return "seal/seal_repeat";

    }

    /**
     *访问注册UK页面
     * @param
     */
    @RequestMapping(value="/register_uk.html", method = RequestMethod.GET)
    public String register_uk(Model model, String unitId) {

        //单位信息
        model.addAttribute("unit",  unitService.findUnitById(unitId));

        SealApply sealApply = new SealApply();

        //设置申请信息类别
        sealApply.setApplyType(Constant.APPLYTYPE_REGISTER_UK);

        model.addAttribute("sealApply", sealApply);

        //获取一级单位的印章类型
        //获取当前单位的一级单位
        Unit TopUnit = unitService.findTopUnit(unitId);

        //根据一级单位获得印章类型
        SealType sealType = new SealType();
        sealType.setTopUnitId(TopUnit.getUnitId());
        List<SealType> sealTypes = sealService.findSealType(sealType);

        model.addAttribute("sealTypes", sealTypes);

        return "seal/seal_register_uk";
    }

    /**
     *访问印章延期页面
     * @param sealId
     */
    @RequestMapping(value="/seal_delay.html", method = RequestMethod.GET)
    public String seal_delay(Model model, String sealId) {

        Seal seal  = new Seal();
        seal.setSealId(sealId);
        List<Seal> sealList = sealService.findSealList(seal);
        if(sealList.size()==1){
            seal = sealList.get(0);
        }

        model.addAttribute("seal", seal);

        return "seal/seal_delay";
    }
    /**
     *访问印章延期动作
     * @param sealId
     */
    @RequestMapping(value="/seal_delay_do.html", method = RequestMethod.POST)
    @ResponseBody
    public String seal_delay_do(Model model, String sealId, String sealStartTime,String sealEndTime) {

        Seal seal  = new Seal();
        seal.setSealId(sealId);
        List<Seal> sealList = sealService.findSealList(seal);
        if(sealList.size()==1){
            seal = sealList.get(0);
        }
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
        return "success";
    }


    /**
     *访问证书延期页面
     * @param certificateId
     */
    @RequestMapping(value="/cer_delay.html", method = RequestMethod.GET)
    public String cer_delay(Model model, String certificateId) {

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
        //设置申请的类别 印章延期
        sealApply.setApplyType(Constant.APPLYTYPE_DELAY_CER);

        //设置延期起始时间 (新数据)
        sealApply.setSealStartTime(seal.getSealStartTime());
        //设置延期到期时间 (新数据)
        sealApply.setSealEndTime(seal.getSealEndTime());

        sealApply.setUnitId(seal.getUnitId());
        sealApply.setSealTypeId(seal.getSealTypeId());
        sealApply.setSealImgId(seal.getSealImgId());
        sealApply.setSealName(seal.getSealName());

        Certificate new_c = seal.getCertificate();
        new_c.setCertificateId(getEssUUID(seal.getUnitId()));
        new_c.setFileState(Constant.FILE_STATE_NULL);
//        new_c.setStarTime(c.getStarTime());
//        new_c.setEndTime(c.getEndTime());
        new_c.setApplyTime(getDate());
        String cId = certificateService.addCertificate(new_c);

        sealApply.setCertificateId(cId);


        sealApply.setIsUK(seal.getIsUK());
        sealApply.setKeyId(seal.getKeyId());
        sealApply.setSealHwUserIdNum(seal.getSealHwIdNum());
        sealApply.setFileTypeNum(seal.getFileTypeNum());
        sealApply.setCerIssuer(seal.getCertificate().getIssuerUnitId());

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

        return "redirect:/seal/seal_make_detail.html?sealApplyId="+sealApply.getSealApplyId();
    }


    /**
     *删除申请
     * @param sealApplyId
     */
    @RequestMapping(value="/seal_apply_del.html", method = RequestMethod.GET)

    public String seal_apply_del(Model model, String sealApplyId) {
        SealApply sealApply = new SealApply();
        sealApply.setSealApplyId(sealApplyId);
        sealService.delSealApply(sealApply);
        return "index";
    }

    /**
     *注销印章
     * @param sealId
     */
    @RequestMapping(value="/seal_del.html", method = RequestMethod.GET)
    @ResponseBody
    public String seal_del(Model model, String sealId) {
        Seal seal = new Seal();
        seal.setSealId(sealId);
        seal.setSealState(2);
        sealService.updateSeal(seal);

        seal = sealService.findSealList(seal).get(0);
        User user =  (User) session.getAttribute("user");
        logService.addSystemLog("注销了"+seal.getSealName(),"印章注销",
                seal.getUnitId(),user.getUserId(),"");

        return "success";
    }

    /**
     *暂停和恢复印章
     * @param sealId
     */
    @RequestMapping(value="/seal_pause_switch.html", method = RequestMethod.GET)
    @ResponseBody
    public String seal_pause_switch(Model model, String sealId, int sealState) {

        String result = null;

        Seal seal = new Seal();
        seal.setSealId(sealId);

        if(sealState == 1){
            sealState = 3;
        }else{
            sealState = 1;
        }
        User user =  (User) session.getAttribute("user");
        logService.addSystemLog(result+seal.getSealName(),"印章"+result,
                sealService.findSealList(seal).get(0).getUnitId(),user.getUserId(),"");

        seal.setSealState(sealState);
        sealService.updateSeal(seal);
        return "success";
    }


    /**
     *查看证书详情
     * @param certificateId
     */
    @RequestMapping(value="/cer_detail.html", method = RequestMethod.GET)
    public String cer_detail(Model model, String certificateId) {

        Certificate certificate = new Certificate();
        certificate.setCertificateId(certificateId);
        certificate.setState(Constant.STATE_YES);
        List<Certificate> certificateList = certificateService.findCertificate(certificate);
        if(certificateList.size()!=0){
            model.addAttribute("certificate", certificateList.get(0));
        }else{
            model.addAttribute("message", "证书不存在！");
            return "error";
        }
        return "seal/cer_detail";
    }
    /**
     *查看印章详情
     * @param sealId
     */
    @RequestMapping(value="/seal_detail.html", method = RequestMethod.GET)
    public String seal_detail(Model model, String sealId) {

        Seal seal = sealService.findSealById(sealId);
        model.addAttribute("seal", seal);
        //延期
        if(powerService.getPowerForButton(session,"makeSealPower_cerDelay")){
            model.addAttribute("delayButton",  true);
        }else {
            model.addAttribute("delayButton",  false);
        }
        return "seal/seal_detail";
    }

    /**
     *更新印章keyID
     * @param seal
     */
    @RequestMapping(value="/updateSealForKeyId.html", method = RequestMethod.POST)
    @ResponseBody
    public String updateSealForKeyId(Model model, Seal seal) {

        sealService.updateSeal(seal);

        return "{\"success\":\"success\"}";
    }
    /**
     *更新印章ID
     * @param sealApplyId
     */
    @RequestMapping(value="/sealApplyFail.html", method = RequestMethod.POST)
    @ResponseBody
    public String sealApplyFail(Model model, String sealApplyId) {

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

        return "{\"success\":\"success\"}";
    }


}
