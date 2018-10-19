
<%@ page contentType="text/html;charset=GBK" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta name="renderer" content="webkit">
    <title></title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pintuer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mask.css">

    <script src="${pageContext.request.contextPath}/js/jquery-3.2.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/pintuer.js"></script>
    <script src="${pageContext.request.contextPath}/js/my.js"></script>


    <link rel="stylesheet" href="${pageContext.request.contextPath}/jquery-dialogbox/jquery.dialogbox-1.0.css">
    <script src="${pageContext.request.contextPath}/jquery-dialogbox/jquery.dialogbox-1.0.js"></script>

    <%--弹框插件--%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/xcConfirm/css/xcConfirm.css"/>
    <script src="${pageContext.request.contextPath}/xcConfirm/js/xcConfirm.js" type="text/javascript" charset="utf-8"></script>
    <style type="text/css">

        /*上传框样式*/
        .uploader { position:relative; display:inline-block; overflow:hidden; cursor:default; padding:0;
            margin:10px 0px; -moz-box-shadow:0px 0px 5px #ddd; -webkit-box-shadow:0px 0px 5px #ddd;
            box-shadow:0px 0px 5px #ddd; -moz-border-radius:5px; -webkit-border-radius:5px; border-radius:5px; }
        .filename { float:left; display:inline-block; outline:0 none; height:32px; width:180px; margin:0; padding:8px 10px; overflow:hidden; cursor:default; border:1px solid; border-right:0; font:9pt/100% Arial, Helvetica, sans-serif; color:#777; text-shadow:1px 1px 0px #fff; text-overflow:ellipsis; white-space:nowrap; -moz-border-radius:5px 0px 0px 5px; -webkit-border-radius:5px 0px 0px 5px; border-radius:5px 0px 0px 5px; background:#f5f5f5; background:-moz-linear-gradient(top, #fafafa 0%, #eee 100%); background:-webkit-gradient(linear, left top, left bottom, color-stop(0%, #fafafa), color-stop(100%, #f5f5f5)); filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#fafafa', endColorstr='#f5f5f5', GradientType=0);
            border-color:#ccc; -moz-box-shadow:0px 0px 1px #fff inset; -webkit-box-shadow:0px 0px 1px #fff inset; box-shadow:0px 0px 1px #fff inset; -moz-box-sizing:border-box; -webkit-box-sizing:border-box; box-sizing:border-box; }
        .button1 { float:left; height:32px; display:inline-block; outline:0 none; padding:8px 12px; margin:0; cursor:pointer; border:1px solid; font:bold 9pt/100% Arial, Helvetica, sans-serif; -moz-border-radius:0px 5px 5px 0px; -webkit-border-radius:0px 5px 5px 0px; border-radius:0px 5px 5px 0px; -moz-box-shadow:0px 0px 1px #fff inset; -webkit-box-shadow:0px 0px 1px #fff inset; box-shadow:0px 0px 1px #fff inset; }
        .uploader input[type=file] { position:absolute; top:0; right:0; bottom:0; border:0; padding:0; margin:0;
            height:30px; cursor:pointer; filter:alpha(opacity=0); -moz-opacity:0; -khtml-opacity: 0; opacity:0; }

        .w20 { width:50%; float:left;}
        .body-content{
            width: 50%;
            margin: 20px 20px;
            box-shadow: 3px -3px 3px #dedede, -3px 3px 3px #dedede, -3px -3px 3px #dedede;
            background-color: #f7f7f7;
        }
        .option{
            padding: 10px 40px;

        }
        .title{
            color: #333;
            width: 130px;
        }
        /*弹框样式*/
        .sgBtn{width: 135px; height: 35px; line-height: 35px; margin-left: 10px; margin-top: 10px; text-align: center;
            background-color: #0095D9; color: #FFFFFF; float: left; border-radius: 5px;}
        .rightContent{
            width: 40%;
            /*border: 1px #dedede solid;*/
            margin: 20px 20px;
            box-shadow: 3px -3px 3px #dedede, -3px 3px 3px #dedede, -3px -3px 3px #dedede;
            background-color: #f7f7f7;
        }
    </style>

</head>
<body>


<div class="panel admin-panel">
    <div class="panel-head" id="add">
        <strong>
            <span class="icon-pencil-square-o"></span> 审核印章申请-
            <c:choose>
                <c:when test="${sealApply.applyType == '1'}">
                    申请新印章
                </c:when>
                <c:when test="${sealApply.applyType == '2'}">
                    UK注册
                </c:when>
                <c:when test="${sealApply.applyType == '3'}">
                    授权延期
                </c:when>
                <c:when test="${sealApply.applyType == '4'}">
                    证书延期
                </c:when>
                <c:when test="${sealApply.applyType == '5'}">
                    印章重做
                </c:when>
                <c:otherwise>
                    未知
                </c:otherwise>
            </c:choose>
        </strong>
    </div>
    <div class="padding border-bottom">
        <ul class="search" style="padding-left:10px;">
            <li><a class="button border-main icon-plus-square-o" href="javaScript:window.location.href = document.referrer;"> 返回</a></li>
        </ul>
    </div>
    <div class="ub">
        <div class="body-content f-s-14 c-6">
            <form method="post" class="form-x" action="" id="seal_review_from" enctype="multipart/form-data">
                <input type="hidden" name="applyType" value="2"/>
                <div class="ub option">
                    <div class="title f-w">所属单位：</div>
                    <div class="ub-f1">
                        <input type="text" class="w20" id="unitName" name="" value="${unit.unitName}"  readonly="readonly" />
                        <input type="hidden" id="unitId" name="unitId" value="${unit.unitId}"/>
                    </div>
                </div>
                <div class="ub option">
                    <div class="title f-w">印章类型：</div>
                    <div class="ub-f1" >
                        <select id="sealType" name="sealTypeId" class="w20">
                            <c:forEach items="${sealTypes}" var="item"  varStatus="status">
                                <option value="${item.sealTypeId}">${item.sealTypeName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="ub option">
                    <div class="title f-w">印章名称：</div>
                    <div class="ub-f1" >
                        <input type="text"  id="sealName" name="sealName" class="w20"/>
                        <input type="hidden" id="sealHwUserIdNum" name="sealHwUserIdNum" />
                    </div>
                </div>
                <div id="personChoose" style="display: none">
                    <div class="ub option">
                        <div class="title f-w">
                            员工搜索：
                        </div>
                        <div class="ub-f1">
                            <input type="text" name="empSearchMsg" id="empSearchMsg" value="" data-validate="required:请输入手机号" class="w20"/>
                            <div class="tips" id="employeTip">
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <a  class="button border-main icon-search"  href='javascript:void(0)' onclick="searchEmp()" > 搜索</a>
                                &nbsp;&nbsp;&nbsp;
                                *输入员工手机号进行搜索
                            </div>
                        </div>
                    </div>
                    <div class="ub option">
                        <div class="title f-w">
                            员工信息：
                        </div>
                        <div class="field" id="empInfoDiv">
                            <div class="tips"></div>
                        </div>
                    </div>
                </div>
                <div class="ub option">
                    <div class="title f-w">
                        印章图片：
                    </div>
                    <div class="ub-f1" >
                        <div id="gif_show"></div>
                        <div id="jpg_show"></div>
                    </div>
                </div>
                <div class="ub option">
                    <div class="title f-w">
                        上传图片：
                    </div>
                    <div class="ub-f1">
                        <div class="uploader white">
                            <input type="button" name="file" class="button bg-main" value="上传GIF标准图"/>
                            <input type="file" id="gifImg" name="gifImg"/>
                        </div>
                        <div class="uploader white">
                            <input type="button" name="file" class="button bg-main" value="上传JPG高清图"/>
                            <input type="file" id="jpgImg"  name="jpgImg"/>
                        </div>
                    </div>
                </div>

                <div class="ub option">
                    <div class="title f-w">印章到期时间：</div>
                    <div class="ub-f1" >
                        <input type="date" id="authorizationTime" name="authorizationTime" value="2018-09-04" readonly class="w20"/>
                        <input type="hidden" id="authorizationInfo" name="authorizationInfo" value="" readonly />
                    </div>
                </div>
                <input type="hidden" id="sealImgId" name="sealImgId" />
                <input type="hidden" id="cerBase64" name="cerBase64"/>
                <input type="hidden" id="keyId" name="keyId"/>
                <div class="form-group">
                    <div class="label">
                        <label></label>
                    </div>
                    <div class="field">
                        <button type="button" class="button bg-main " onclick=" GetUKInfo()"> 读取UK</button>
                        <button type="button" class="button bg-main " onclick="sealInfoSubmit()" > 注册</button>
                    </div>
                </div>
            </form>
        </div>
        <div class="rightContent ub-f1 ">
            <div class="ub option">
                <div class="title f-w">证书版本：</div>
                <div class="ub-f1" id="cerVersion"></div>
            </div>
            <div class="ub option">
                <div class="title f-w">签名算法：</div>
                <div class="ub-f1" id="cerAlgorithm"></div>
            </div>

            <div class="ub option">
                <div class="title f-w">证书使用者：</div>
                <div class="ub-f1" id="cerCn"></div>
            </div>
            <div class="ub option">
                <div class="title f-w">证书颁发者：</div>
                <div class="ub-f1" id="cerIssuer"></div>
            </div>
            <div class="ub option">
                <div class="title f-w">有效期起始：</div>
                <div class="ub-f1" id="cerStartTime"></div>
            </div>
            <div class="ub option">
                <div class="title f-w">有效期结束：</div>
                <div class="ub-f1" id="cerEndTime"></div>
            </div>
        </div>
    </div>
</div>


</body>
<script>
    /**
     * submit数据
     * @param
     * @returns
     */
    function sealInfoSubmit(){
        // alert("上传服务器成功");
        var formData = $("#seal_review_from").serializeArray();
        var verificResult  = verification();

        if(verificResult == "true"){
            $.ajax({
                url: "${pageContext.request.contextPath}/seal/seal_apply_do.html",
                type: "post",
                data: new FormData($("#seal_review_from")[0]),
                cache: false,
                processData: false,
                contentType: false,
                // dataType : "json",
                success: function (data)
                {
                    if(data == "success"){

                        $('body').dialogbox({
                            type:"normal",title:"操作成功",
                            buttons:[{
                                Text:"确认",
                                ClickToClose:true,
                                callback:function (dialog){
                                    window.location.href = document.referrer;
                                }
                            }],
                            message:'已审核通过该印章申请！'
                        });
                    }else{
                        $('body').dialogbox({
                            type:"normal",title:"操作失败",
                            buttons:[{
                                Text:"确认",
                                ClickToClose:true,
                                callback:function (dialog){
                                }
                            }],
                            message:'申请提交失败，请您重试！'
                        });
                    }
                }
            });
        }else{
            var txt = verificResult;
            window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.confirm);
        }
    }


    function verification(){

        return "true";

    }

    /**
     * 检查输入值是否为空
     * @param data
     * @returns {boolean}
     */
    function isNull(val) {
        if (val == '' || val == undefined || val == null) {
            //空
            return true;
        } else {
            // 非空
            return false;
        }
    }

    //上传图片 转换base64 显示。
    $("#gifImg").change(function(){
        //清空图片内容
        $("#gif_show").empty();
        var fl=this.files.length;
        for(var i=0;i<fl;i++){
            var file = this.files[i];
            //上传文件格式
            var imgFileType = file.name.toLowerCase().split('.').splice(-1);
            var imgFileSize = file.size/1024 + 'kb';
            if("gif" == imgFileType){
                var reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = function(e){
                    var imgstr='<img style="width:70px;height:70px;" src="'+e.target.result+'"/>';
                    $("#gif_show").append(imgstr);
                };
            }else{
                alert("文件格式错误，请重新 上传");
                //重置file Input 值
                file.value = '';
            }
        }
    });
    //上传图片 转换base64 显示。
    $("#jpgImg").change(function(){
        //清空图片内容
        $("#jpg_show").empty();
        var fl=this.files.length;
        for(var i=0;i<fl;i++){
            var file = this.files[i];
            //上传文件格式
            var imgFileType = file.name.toLowerCase().split('.').splice(-1);
            if("jpg" == imgFileType){
                var reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = function(e){
                    var imgstr='<img style="width:70px;height:70px;" src="'+e.target.result+'"/>';
                    $("#jpg_show").append(imgstr);
                };
            }else{
                alert("文件格式错误，请重新 上传");
                //重置file Input 值
                file.value = '';
            }
        }
    });

    $(function(){

        //印章名称相关JS
        $("#sealName").val($("#unitName").val()+$("#sealType").find("option:selected").text());
        $("#sealType").change(function(){

            if($("#sealType").find("option:selected").text()=='手签'){

                $("#personChoose").show();
                $("#sealName").val("");
            }else{

                $("#personChoose").hide();
                $("#sealName").val($("#unitName").val()+$("#sealType").find("option:selected").text());
            }

        });



        //文件上传输入框
        $("input[type=file]").change(function(){
            $(this).parents(".uploader").find(".filename").val($(this).val());
        });

        $("input[type=file]").each(function(){
            if($(this).val()==""){
                $(this).parents(".uploader").find(".filename").val("请选择文件...");
            }
        });
    });

    //根据用户输入的数据搜索用户数据  返回值格式：personId@personName@personPhone@personIdNum@@@personId@personName@...
    function searchEmp(){
        var empSearchMsg = $("#empSearchMsg").val();
        $.ajax({
            url: "${pageContext.request.contextPath}/seal/findPerson.html",
            type: "post",
            data: {"phone":empSearchMsg},
            dateType: "json",
            success: function (data)
            {

                var result = data;
                if(result.meg=="success"){
                    $("#empInfoDiv").html("");
                    //设置手签人身份证号
                    $("#sealHwUserIdNum").val(result.body.idNum);
                    //设置手签印章名称
                    $("#sealName").val(result.body.personName);
                    alert(result.body.personName);
                    $("#empInfoDiv").append(
                        "<label>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                        +"<font size='2'><strong>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名:&nbsp;</strong></font>"+result.body.personName+"<br/>"
                        +"<font size='2'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>手&nbsp;机&nbsp;&nbsp;号:&nbsp;</strong></font>"+result.body.phone+"<br/>"
                        +"<font size='2'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>身份证号:&nbsp;</strong></font>"+result.body.idNum+" </label>" +
                        "<br/><hr style='width:260px'/>"
                    );
                }else{
                    window.wxc.xcConfirm(result.meg, window.wxc.xcConfirm.typeEnum.info);
                }
            }
        });
    }


    function GetUKInfo(){
        clientIsRunning = false;
        $.getJSON("http://127.0.0.1:10102?jsoncallback=?", {
                ACT:"Q_UKINFOFORREG",
                VAL:"CARDTYPE=FTCX"
            },
            function(data) {
                if(data[0].status == 0){
                    var s = data[0].info;
                    var infos = s.split("#");

                    // alert("UK编号:" +　infos[0]);
                    $("#keyId").val(infos[0]);
                    // alert("证书内容:" +　infos[1]);
                    $("#cerBase64").val(infos[1]);
                    //	alert("授权值：:" +　infos[2]);
                    // alert("上传的得到的印章UUID:" +　infos[3]);
                    $("#sealImgId").val(infos[3]);
                    // alert("印章GIF:" +　infos[4]);
                    var imgstr='<img style="width:70px;height:70px;" src="data:image/gif;base64,'+infos[4]+'"/>';
                    $("#gif_show").append(imgstr);
                    // alert("证书版本:" +　infos[5]);
                    $("#cerVersion").text(infos[5]);
                    // alert("证书序列号:" +　infos[6]);

                    // alert("证书算法:" +　infos[7]);
                    $("#cerAlgorithm").text(infos[7]);
                    // alert("颁发者信息:" +　infos[8]);
                    $("#cerIssuer").text(infos[8]);
                    // alert("证书有效期起始:" +　infos[9]);
                    $("#cerStartTime").text(infos[9]);
                    // alert("证书有效期终止:" +　infos[10]);
                    $("#cerEndTime").text(infos[10]);
                    // alert("证书主题:" +　infos[11]);
                    $("#cerCn").text(infos[11]);
                }
                else if(data[0].status == 1){
                    alert("请登录安全客户端");
                }else if(data[0].status == 2){
                    alert("发生错误");
                }
                else{
                    alert(data[0].status);
                }
            }
        );
    }

</script>
</html>