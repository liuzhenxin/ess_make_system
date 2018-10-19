<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta name="renderer" content="webkit">
    <title></title>
    <%--MUI--%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pintuer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mask.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/jquery-dialogbox/jquery.dialogbox-1.0.css">

    <script src="${pageContext.request.contextPath}/js/jquery-3.2.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/pintuer.js"></script>
    <script src="${pageContext.request.contextPath}/js/my.js"></script>
    <script src="${pageContext.request.contextPath}/jquery-dialogbox/jquery.dialogbox-1.0.js"></script>

    <%--省市三级联动插件--%>
    <script src="${pageContext.request.contextPath}/distpicker/js/distpicker.data.js"></script>
    <script src="${pageContext.request.contextPath}/distpicker/js/distpicker.js"></script>
    <script src="${pageContext.request.contextPath}/distpicker/js/main.js"></script>

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

        /*输入框长度*/
        .w20{
            width: 300px;
            float:left;
        }
        .w10{
            width: 150px;
            float:left;
        }
        .form-x .form-group .label {
            width: 170px;
        }
        .rightContent{
            width:600px;
            /*border: 1px #dedede solid;*/
            margin: 20px 40px;
        }
        .form-x .form-group .field {
            width: calc(100% - 170px);
        }
    </style>
</head>
<body>
<div class="panel admin-panel">
    <div class="panel-head" id="add"><strong><span class="icon-pencil-square-o"></span>申请制作新印章</strong></div>
    <div class="padding border-bottom">
        <ul class="search" style="padding-left:10px;">
            <li><a class="button border-main icon-plus-square-o" href="javaScript:window.location.href = document.referrer;"> 返回</a></li>
        </ul>
    </div>
    <div class="body-content up">
        <div class="ub-f1">
            <form method="post" class="form-x" action="" id="submit_from" enctype="multipart/form-data">
                <%--申请信息类别--%>
                <input type="hidden" id="applyType" name="applyType" value="${applyType}"/>
                <div class="form-group">
                    <div class="label">
                        <label>所属单位：</label>
                    </div>
                    <div class="field">
                        <input type="text" id="unitName" class="input w20" name="unitName" value="${unit.unitName}"  readonly />
                        <input type="hidden" id="unitId" name="unitId" value="${unit.unitId}"/>
                        <div class="tips"></div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="label">
                        <label>印章类型：</label>
                    </div>
                    <div class="field">
                        <select id="sealType" name="sealTypeId" class="input w20">
                            <c:forEach items="${sealTypes}" var="item"  varStatus="status">
                                <option value="${item.sealTypeId}">${item.sealTypeName}</option>
                            </c:forEach>
                        </select>
                        <div class="tips"></div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="label">
                        <label>印章名称：</label>
                    </div>
                    <div class="field">
                        <input type="text" class="input w20" id="sealName" name="sealName" />
                        <input type="hidden" id="sealHwUserIdNum" name="sealHwUserIdNum" />
                        <div class="tips"></div>
                    </div>
                </div>

                <div id="personChoose" style="display: none">
                    <div class="form-group">
                        <div class="label">
                            <label><strong>员工搜索：</strong></label>
                        </div>
                        <div class="field">
                            <input type="text" name="empSearchMsg" id="empSearchMsg" class="input w20" value="" data-validate="required:请输入手机号" />
                            <div class="tips" id="employeTip">
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <a  class="button border-main icon-search"  href='javascript:void(0)' onclick="searchEmp()" > 搜索</a>
                                &nbsp;&nbsp;&nbsp;
                                *输入员工手机号进行搜索
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="label">
                            <label><strong>员工信息：</strong></label>
                        </div>
                        <div class="field" id="empInfoDiv">
                            <div class="tips"></div>
                        </div>
                    </div>
                </div>

                <div class="form-group" >
                    <div class="label">
                        <label>是否申请UK印章：</label>
                    </div>
                    <div class="field">
                        <select id="isUk" name="isUK" class="input w20">
                            <option value=2>否</option>
                            <option value=1>是</option>
                        </select>
                        <div class="tips"></div>
                    </div>
                </div>
                <div class="form-group" >
                    <div class="label">
                        <label>是否提供证书：</label>
                    </div>
                    <div class="field">
                        <select id="isCer" name="isCer" class="input w20">
                            <option value="0">否</option>
                            <option value="1">是</option>
                        </select>
                        <div class="field" id="cer_div" style="display: none">
                            <div class="uploader white">
                                <input type="text" class="filename input w20" readonly/>
                                <input type="button" class="button1" value="上传..."/>
                                <input type="file" id="cerFile"  name="cerFile" size="30"/>
                                <input type="hidden" id="pfxBase64" name="pfxBase64"/>
                                <input type="hidden" id="fileState" name="fileState"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="label">
                        <label>印章图片：</label>
                    </div>
                    <div class="field" >
                        <div id="gif_show"></div>
                        <div id="jpg_show"></div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="label">
                        <label>上传图片：</label>
                    </div>
                    <div class="field">
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
                <div class="form-group"  >
                    <div class="label">
                        <label>授权种类：</label>
                    </div>
                    <div class="field" >
                        <c:forEach items="${fileTypeList}" var="item"  varStatus="status">
                            <input type="checkbox" value="${item.fileTypeValue}" name="sProblem">${item.fileTypeName}
                        </c:forEach>
                        <input type="hidden" name="fileTypeNum" id="fileTypeNum">
                        <br/>
                    </div>
                </div>
                <div id="cer_info">
                    <div class="form-group" id="distpicker_div" >
                        <div class="label">
                            <label>证书归属地信息：</label>
                        </div>
                        <div class="field">
                            <div data-toggle="distpicker">
                                <div class="form-group">
                                    <select class="form-control input w10" id="province7" name="province" ></select>
                                    <select class="form-control input w10" id="city7" name="city" ></select>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group" id="psw_div" style="display: none">
                    <div class="label">
                        <label>软证书密码：</label>
                    </div>
                    <div class="field">
                        <input type="password" name="cerPsw" id="cerPsw" class="input w20" value=""  />
                    </div>
                </div>
                <div class="form-group">
                    <div class="label">
                        <label></label>
                    </div>
                    <div class="field">
                        <button type="button" class="button bg-main icon-check-square-o" onclick="sealInfoSubmit()"> 申请</button>
                    </div>
                </div>
            </form>
        </div>
        <div class="rightContent">
            右边内容
        </div>
    </div>

</div>

<script>

    function getTheCheckBoxValue() {
        var test = $("input[name='sProblem']:checked");
        var checkBoxValue = "";
        var iAuth = 0;
        test.each(function () {
            var value = $(this).val();
            if(value.indexOf("office") != -1 && value.indexOf("annotation") != -1 )
            {
                iAuth = iAuth | 1;
            }else if(value.indexOf("web") != -1 && value.indexOf("annotation") != -1 ){
                iAuth = iAuth | 2;
            }else if(value.indexOf("ESSWebSign") != -1){
                iAuth = iAuth | 4;
            }else if(value.indexOf("ESSWordSign") != -1){
                iAuth = iAuth | 8;
            }else if(value.indexOf("ESSExcelSign") != -1){
                iAuth = iAuth | 16;
            }else if(value.indexOf("ESSPdfSign") != -1){
                iAuth = iAuth | 32;
            }else if(value.indexOf("ESSMidWare") != -1){
                iAuth = iAuth | 64;
            }else{

            }
        });
        return iAuth;
    }

    /**
     * submit 数据
     * @returns
     */
    function sealInfoSubmit(){
        $("#fileTypeNum").val(getTheCheckBoxValue());

        var formData = $("#submit_from").serializeArray();
        var verificResult  = verification();

        if(verificResult == "true"){
            $.ajax({
                url: "${pageContext.request.contextPath}/seal/seal_apply_do.html",
                type: "post",
                data: new FormData($("#submit_from")[0]),
                cache: false,
                processData: false,
                contentType: false,
                success: function (data)
                {
                    if(data == "success"){
                        $('body').dialogbox({
                            type:"normal",title:"申请成功",
                            buttons:[{
                                Text:"确认",
                                ClickToClose:true,
                                callback:function (dialog){
                                    window.location.href = document.referrer;
                                }
                            }],
                            message:'已根据您提交的信息成功提交印章申请，请您等候审核制作！'
                        });
                    }else{
                        $('body').dialogbox({
                            type:"normal",title:"申请失败",
                            buttons:[{
                                Text:"确认",
                                ClickToClose:true,
                                callback:function (dialog){
                                    // window.history.go(-1)
                                }
                            }],
                            message:'申请提交失败，请您重试！'
                        });
                    }
                }
            });
        }else{
            $('body').dialogbox({
                type:"normal",title:"信息缺失",
                buttons:[{
                    Text:"确认",
                    ClickToClose:true,
                    callback:function (dialog){
                        // window.history.go(-1)
                    }
                }],
                message:verificResult
            });
        }
    }

    function verification(){
        if(isNull($("#sealName").val())){
            return "印章名称不可为空！";
        }

        if(isNull($("#gifImg").val()) &&isNull($("#jpgImg").val())){
            return "请上传图片！";
        }
        if($("#isCer").val()==1 ){
            if(isNull($("#pfxBase64").val())){
                return "请上传证书！";
            }
            if(isNull($("#cerPsw").val())){
                return "请输入密码！";
            }
        }
        //上传证书和uk印章不可相同
        if($("#isCer").val()==1 ){
            if($("#isUK").val()==1 ){
                return "pfx证书不适用于UK印章申请！";
            }
        }

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
                    $("#gif_show").append(imgstr)
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
                    $("#jpg_show").append(imgstr)
                };
            }else{
                alert("文件格式错误，请重新 上传");
                //重置file Input 值
                file.value = '';
            }
        }
    });

    //上传证书 转换base64
    $("#cerFile").change(function(){
        var cerFileType = this.value.toLowerCase().split('.').splice(-1)
        var cerFileSize = this.files[0].size/1024 + 'kb';
        if("pfx" == cerFileType ){
            var v2 = $(this).val();
            var reader = new FileReader();
            reader.readAsDataURL(this.files[0]);
            reader.onload = function(e){
                var cerArray = e.target.result.split(",");
                $('#pfxBase64').val(cerArray[1]);
                $('#fileState').val(3);
            };
        }else{
            alert("文件格式错误，请重新上传");
            this.files[0].value = '';
        }
    });
    $(function(){
        //是否提供证书 上传框  控制
        //设置证书状态为未生成证书
        $('#fileState').val(1);
        $("#isCer").change(function(){
            if($("#isCer").val()=='1'){
                $("#cer_div").show();
                $("#cer_info").hide();
                $("#psw_div").show();
            }else{
                $("#cer_div").hide();
                $("#cer_info").show();
                $("#psw_div").hide();
            }
        });
        //印章名称相关JS
        $("#sealName").val($("#unitName").val()+$("#sealType").find("option:selected").text());
        $("#sealType").change(function(){

            if(($("#sealType").val()).indexOf("st7")!=-1){

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
        var phone = $("#empSearchMsg").val();
        $.ajax({
            url: "${pageContext.request.contextPath}/seal/findPerson.html",
            type: "get",
            data: {phone:phone},
            contentType : "application/x-www-form-urlencoded",
            // dateType: "json",
            success: function (data) {

                // var jsonStr = JSON.stringify(data);
                var resultArray = data.split("@");
                if(resultArray.indexOf("error")==0){
                    alert(resultArray[1]);
                }else{
                    var idNum = resultArray[0];
                    var phone = resultArray[1];
                    var personName = resultArray[2];

                    $("#empInfoDiv").html("");
                    //设置手签人身份证号
                    $("#sealHwUserIdNum").val(idNum);
                    //设置手签印章名称
                    $("#sealName").val(personName);

                    $("#empInfoDiv").append(
                        "<label>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                        +"<font size='2'><strong>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名:&nbsp;</strong></font>"+personName+"<br/>"
                        +"<font size='2'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>手&nbsp;机&nbsp;&nbsp;号:&nbsp;</strong></font>"+phone+"<br/>"
                        +"<font size='2'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>身份证号:&nbsp;</strong></font>"+idNum+" </label>" +
                        "<br/><hr style='width:260px'/>"
                    );
                }
            },
            error: function (data) {
                alert("请求错误！")
            }
        });
    }
</script>

</body></html>