package com.clt.ess.web;

import com.clt.ess.base.Constant;
import com.clt.ess.bean.ZTree;
import com.clt.ess.dao.IIssuerUnitDao;
import com.clt.ess.entity.*;
import com.clt.ess.service.*;
import com.clt.ess.utils.FastJsonUtil;
import com.clt.ess.utils.PowerUtil;

import com.clt.ess.utils.WSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import static com.clt.ess.utils.CertUtils.signCertByIssuerUnit;
import static com.clt.ess.utils.dateUtil.strToDate;
import static com.multica.crypt.MuticaCrypt.ESSGetBase64Encode;

@Controller
@RequestMapping("") // url:/模块/资源/{id}/细分 /seckill/list
public class MainController {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;

    @Autowired
    private IUserService userService;
    @Autowired
    private IUnitService unitService;
    @Autowired
    private IRoleAndPowerService roleAndPowerService;
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private IPowerService powerService;
    @Autowired
    private ISealImgService sealImgService;
    @Autowired
    private IIssuerUnitDao iIssuerUnitDao;
    @Autowired
    private ICertificateService certificateService;
    @Autowired
    private ISealService sealService;
    @Autowired
    private IErrorLogService errorLogService;
    /**
     * 每次拦截到请求会先访问此函数，
     * 获取request,session,response等实例
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
     * 制章系统入口
     * @param token 验证口令 网页访问PC客户端得到的令牌
     * @param userId 登录人ID可为空 在personId拥有多个登录身份时，在页面内切换身份时userId不为空
     * @return 视图信息
     */
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    private ModelAndView index(String token,String userId)  {

        //视图对象
        ModelAndView modelAndView = new ModelAndView();
        //设置返回视图
        modelAndView.setViewName("unitAndDepAndUserManager");
//        //授权信息
//        String authMsg = null;
//
//        try{
//            //通过webservice,根据token获取授权信息
//            authMsg = WSUtil.getLoginUserInfo(token);
//
//        }catch (MalformedURLException e){
//            //抓取异常 记录错误日志
//            errorLogService.addErrorLog("MainController-index-webservice获取登录人授权信息出错！");
//            modelAndView.setViewName("error");
//        }
//        //判断授权信息 判断是否为空，完整性
//        if(authMsg == null || !authMsg.contains("ESSYES") || !authMsg.contains("ESSEND")){
//            errorLogService.addErrorLog("MainController-index-authMsg授权信息为空或者不完整！");
//            model.addAttribute("message", "获取人员信息出错");
//            modelAndView.setViewName("error");
//        }
//        //解析授权信息 根据 @@@@ 分割字符串
//        String[] split = authMsg.split("@@@@");
//        if(split.length == 0){
//            errorLogService.addErrorLog("MainController-index-authMsg授权信息出错！");
//            modelAndView.setViewName("error");
//        }
//        //获取到登录人的personId
//        String personId = split[0].substring(6);
        //根据personId查找用户表
        User user = new User();
        user.setPersonId(token);
        //状态有效的登录身份
        user.setState(Constant.STATE_YES);
        //查找符合条件的用户
        List<User> userList =  userService.findLoginUserByPersonId(user);

        if(userList.size()!=0 ){
            //判断访问参数userId是否为空
            if("".equals(userId)||userId==null){
                //userId为空时 是 第一次访问。
                //此时自动分配第一个用户身份
                user = userList.get(0);
            }else{
                //userId不为空时，为拥有多个身份的用户切换时
                //根据其身份列表选出其切换的身份。
                for(User nav :userList){
                    if(nav.getUserId()==userId){
                        user = nav;
                    }
                }
            }
            //将用户身份存入session，作为登录信息。
            session.setAttribute("user",user);
            modelAndView.addObject("user", user);
            modelAndView.addObject("userList", userList);

            //根据当前登录用户获取其单位列表结构
            List<ZTree> tree =  unitService.queryUnitMenu(user.getUnitId());
            if(tree==null){
                errorLogService.addErrorLog("MainController-index-unitNameChain获取单位列表结构出错！");
                modelAndView.setViewName("error");
            }else{
                modelAndView.addObject("unit_menu", FastJsonUtil.toJSONString(tree));
            }
            //单位名称chain
            String unitNameChain = unitService.getUnitNameChain(user.getUnitId());
            if("".equals(unitNameChain)||unitNameChain==null){
                errorLogService.addErrorLog("MainController-index-unitNameChain获取单位名称链出错！");
                modelAndView.setViewName("error");
            }else{
                //放入当前单位名称chain，先清除。此处放入单位名称chain是为了写入系统日志是方便取用
                session.removeAttribute("unitNameChain");
                session.setAttribute("unitNameChain", unitNameChain);
            }

        }else{
            errorLogService.addErrorLog("MainController-index-userList根据personId查询的user为空！");
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }

    /**
     * 获取左侧单位列表json数据
     * @param unitId 单位ID
     */
    @RequestMapping(value="/unit_menu.html", method = RequestMethod.GET)
    @ResponseBody
    public String unit_menu(String unitId) throws UnsupportedEncodingException {

        List<ZTree> tree =  unitService.queryUnitMenu(unitId);
        return new String(FastJsonUtil.toJSONString(tree).getBytes("utf8"),"iso8859-1");
//        toJSONString
//        FastJsonUtil.write_object(response, tree);
    }
    /**
     * 单位展示页默认页面
     */
    @RequestMapping(value="/unit_index.html", method = RequestMethod.GET)
    public ModelAndView unit_index() {
        //视图对象
        ModelAndView modelAndView = new ModelAndView();
        //设置返回视图
        modelAndView.setViewName("unit/showInfoPage");
        return modelAndView;
    }

    /**
     * 单位详情页面
     * @param toOpeUnitId 要打开的单位Id
     * @return s
     */
    @RequestMapping(value="/unit_page.html", method = RequestMethod.GET)
    public ModelAndView unit_page(String toOpeUnitId) {
        //视图对象
        ModelAndView modelAndView = new ModelAndView();
        //设置返回视图
        modelAndView.setViewName("unit/unit_page");
        //判断是否有进入当前单位的权限
        if(powerService.checkIsHaveThisRangePower(session,unitService.findUnitById(toOpeUnitId),Constant.topUnitLevel)){
            //放入当前单位名称chain，先清除。此处放入单位名称chain是为了写入系统日志是方便取用
            session.removeAttribute("unitNameChain");
            session.setAttribute("unitNameChain", unitService.getUnitNameChain(toOpeUnitId));

            //查询要进入的单位数据
            modelAndView.addObject("unit",  unitService.findUnitById(toOpeUnitId));

            //根据要打开的单位ID查询当前单位下的部门列表
            Department department = new Department();
            department.setUnitId(toOpeUnitId);
            department.setState(Constant.STATE_YES);
            modelAndView.addObject("departmentList",  departmentService.findDepartmentList(department));

            //判断当前登录用户对于各个按钮的权限
            //审核
            if(powerService.getPowerForButton(session,"makeSealPower_auditSeal")){
                modelAndView.addObject("reviewButton",  true);
            }else {
                modelAndView.addObject("reviewButton",  false);
            }
            //制作
            if(powerService.getPowerForButton(session,"makeSealPower_makeSeal")){
                modelAndView.addObject("makeButton",  true);
            }else {
                modelAndView.addObject("makeButton",  false);
            }
            //注册UK
            if(powerService.getPowerForButton(session,"makeSealPower_registerTheExistingUkSeal")){
                modelAndView.addObject("registerButton",  true);
            }else {
                modelAndView.addObject("registerButton",  false);
            }
        }else {
            modelAndView.addObject("message",  "您不能访问此单位");
            modelAndView.setViewName("error");
        }
        return modelAndView;

    }
    /**
     * 退出登录
     */
    @RequestMapping(value="/system_out.html", method = RequestMethod.GET)
    public ModelAndView system_out() {
        PowerUtil.delLoginUserFromSession(session);
        //TODO 网址替换为 管理系统的IP
        return new ModelAndView("redirect:http://192.168.1.113:8080/EssSealPlatform/unit/logout");
    }

//    @RequestMapping(value="/upload.html", method = RequestMethod.GET)
//    public String upload(HttpServletRequest request) {
//        return "upload";
//    }

    /**
     * 对上传的证书进行签名
     * @param cert 证书cer文件
     * @param certId 证书ID
     * @throws IOException io异常
     */
    @RequestMapping(value="signCert.do", method = RequestMethod.POST)
    public void signCert(@RequestParam("cert") MultipartFile cert,String certId) throws IOException {
        //查询证书对象
        Certificate certificate = new Certificate();
        certificate.setCertificateId(certId);
        certificate.setState(1);
        List<Certificate> certificateList = certificateService.findCertificate(certificate);
        //要返回的证书文件二进制
        byte[] certByte = null;

        if(certificateList.get(0)!=null){
            // 判断文件是否为空
            if (!cert.isEmpty()) {
                try {
                    //得到证书信息对象
                    certificate = certificateList.get(0);
                    //证书颁发单位Id
                    String issuerUnitId = certificate.getIssuerUnitId();
                    //获取证书颁发单位
                    IssuerUnit issuerUnit = iIssuerUnitDao.findIssuerUnitById(issuerUnitId);

                    //算法
                    String algorithm = certificate.getAlgorithm();
                    //所在省份
                    String sS = certificate.getProvince();
                    //所在城市
                    String sL = certificate.getCity();
                    //单位
                    String sO = certificate.getCertUnit();
                    //部门
                    String sOU = certificate.getCertDepartment();
                    //公用名称
                    String sDN = certificate.getCerName();

                    //起始时间  当前时间减一天
                    Date dateStart = strToDate(certificate.getStartTime());
                    Date dateEnd  = strToDate(certificate.getEndTime());
                    //对上传的证书进行签名
                    certByte = signCertByIssuerUnit(cert.getBytes(),issuerUnit,sDN,sOU,sO,sL,sS,algorithm,dateStart,dateEnd);
                    //更新证书对象的证书数据
                    certificate.setCerBase64(ESSGetBase64Encode(certByte));
                    certificateService.updateCertificate(certificate);
                } catch (Exception e) {
                    e.printStackTrace();
                    errorLogService.addErrorLog("MainController-signCert-对上传证书进行签名时发生异常！");
                }
            }
        }

        if(certByte == null){
            //设置MIME类型
            response.setContentType("application/json");
            ServletOutputStream outputStream=response.getOutputStream();
            outputStream.write(("ESSNOO").getBytes());
            outputStream.close();
        }else{
            response.setContentType("application/json");
            ServletOutputStream outputStream=response.getOutputStream();
            String result = "ESSYES"+ESSGetBase64Encode(certByte)+"ESSEND";
            outputStream.write(result.getBytes());
            outputStream.close();
        }
    }
//    /**
//     * 通过url请求返回图像的字节流
//     */
//    @RequestMapping(value="img.html", method = RequestMethod.GET)
//    public void getIcon(String imgId) throws IOException {
//
//        File file = new File("e:/001.gif");
//        FileInputStream inputStream = new FileInputStream(file);
//        byte[] data = new byte[(int)file.length()];
//        int length = inputStream.read(data);
//        inputStream.close();
//        SealImg sealImg = sealImgService.findSealImgById(imgId);
//        response.setContentType("image/gif");
//        OutputStream stream = response.getOutputStream();
//        stream.write((byte[]) sealImg.getSealImgJpg());
//        stream.flush();
//        stream.close();
//    }

//    /**
//     * 通过url请求返回图像的字节流
//     */
//    @RequestMapping(value="seal_img.html", method = RequestMethod.GET)
//    public void getSealImg(String uuid,String type) throws IOException {
//
//        File file  = new File("e:/001.gif");
//        //设置MIME类型
//        response.setContentType("application/octet-stream");
//        //或者为response.setContentType("application/x-msdownload");
//
//        //设置头信息,设置文件下载时的默认文件名，同时解决中文名乱码问题
//        response.addHeader("Content-disposition", "attachment;filename="+new String("e:/001.gif".getBytes(), "ISO-8859-1"));
//
//        InputStream inputStream=new FileInputStream(file);
//        ServletOutputStream outputStream=response.getOutputStream();
//        byte[] bs=new byte[1024];
//        while((inputStream.read(bs)>0)){
//            outputStream.write(bs);
//        }
//        outputStream.close();
//        inputStream.close();
//    }

    /**
     *错误页面
     */
    @RequestMapping(value="error.html", method = RequestMethod.GET)
    public String error(Model model) {
        model.addAttribute("message",  "请登录安全客户端");
        return "error";
    }

    /**
     *下载附件
     * @param sealApplyId 申请信息Id
     */
    @RequestMapping(value="attachment.html", method = RequestMethod.GET)
    public void attachment(String sealApplyId) throws IOException {
        File file = null;
        SealApply sealApply = sealService.findSealApplyById(sealApplyId);
        if(sealApply==null){
            //设置MIME类型
            response.setContentType("application/json");
            ServletOutputStream outputStream=response.getOutputStream();
            outputStream.write(("error！").getBytes());
            outputStream.close();
        }else{
            file = new File(Constant.ATTACHMENT_PATH+sealApply.getAttachment());
            //设置MIME类型
            response.setContentType("application/octet-stream");
            //或者为response.setContentType("application/x-msdownload");
            //设置头信息,设置文件下载时的默认文件名，同时解决中文名乱码问题
            response.addHeader("Content-disposition", "attachment;filename="+new String(sealApply.getAttachment().getBytes(),
                    StandardCharsets.ISO_8859_1));

            InputStream inputStream=new FileInputStream(file);
            ServletOutputStream outputStream=response.getOutputStream();
            byte[] bs=new byte[1024];
            while((inputStream.read(bs)>0)){
                outputStream.write(bs);
            }
            outputStream.close();
            inputStream.close();
        }

    }

    /**
     * 判断当前登录用户对某个系统是否有权限
     * 2018.05.24
     * @param isNeedCheckRange(1判断,0不判断)  是否需要判断范围,判断某个角色能否进入系统时,则不需要判断范围
     * @param powerId 权限Id
     * @param currentUnitId	当前点击的单位id,不需要传值时,传null即可
     */
    @RequestMapping(value="/check_auth.html", method = RequestMethod.POST)
    public void checkAuth(Integer isNeedCheckRange,String powerId,String currentUnitId){
        // 判断是否能进入某个系统时不需要判断范围
        if(isNeedCheckRange == 0){
            if(PowerUtil.checkUserIsHavaThisPower(session, powerId, roleAndPowerService, unitService,
                    Constant.topUnitLevel,Constant.companyLevel) ){
                FastJsonUtil.write_json(response, "{\"msg\":\""+"1"+"\"}");
            }else{
                FastJsonUtil.write_json(response, "{\"msg\":\""+"0"+"\"}");
            }
        }else{
            // 需要先判断管理范围再判断功能权限
            // 需要写入错误日志表
            if(currentUnitId == null){
//                errorLogService.addErrorLog("签章平台", "userController-checkAuth-获取到的currentUnitId为null");
                FastJsonUtil.write_json(response, "{\"msg\":\""+"2"+"\"}");
            }
            // 获取当前点击的单位对象
            Unit currentUnit = unitService.findUnitById(currentUnitId);
            // 需要写入错误日志表
            if(currentUnit == null){
//                errorLogService.addErrorLog("签章平台", "userController-checkAuth-获取到的单位对象为null");
                FastJsonUtil.write_json(response, "{\"msg\":\""+"2"+"\"}");
            }

            if(PowerUtil.checkUserIsHaveThisRangePower(session, currentUnit, Constant.topUnitLevel)){
                // 如果管理范围包含当前层级,则继续判断对应的功能
                if(PowerUtil.checkUserIsHavaThisPower(session, powerId, roleAndPowerService, unitService, Constant.topUnitLevel,
                        Constant.companyLevel) ){
                    FastJsonUtil.write_json(response, "{\"msg\":\""+"1"+"\"}");
                }else{
                    FastJsonUtil.write_json(response, "{\"msg\":\""+"0"+"\"}");
                }
            }else{
                // 如果管理范围不包含当前层级,则表示没有权限
                FastJsonUtil.write_json(response, "{\"msg\":\""+"0"+"\"}");
            }
        }
    }

}
