
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

    <script src="${pageContext.request.contextPath}/js/jquery-3.2.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/pintuer.js"></script>
    <script src="${pageContext.request.contextPath}/js/my.js"></script>

    <%--省市三级联动插件--%>
    <script src="${pageContext.request.contextPath}/distpicker/js/distpicker.data.js"></script>
    <script src="${pageContext.request.contextPath}/distpicker/js/distpicker.js"></script>
    <script src="${pageContext.request.contextPath}/distpicker/js/main.js"></script>
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
        /*弹框样式*/
        .sgBtn{width: 135px; height: 35px; line-height: 35px; margin-left: 10px; margin-top: 10px; text-align: center;
            background-color: #0095D9; color: #FFFFFF; float: left; border-radius: 5px;}
        /*输入框长度*/
        .w20{
            width: 300px;
            float:left;
        }
        .w10{
            width: 100px;
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
            <li><a class="button border-main icon-plus-square-o" href="${pageContext.request.contextPath}/unit_page.html?toOpeUnitId=${unit.unitId}"> 返回</a></li>
        </ul>
    </div>
    <div class="body-content up">
        <div class="ub-f1">
            <form method="post" class="form-x" action="" id="submit_from" >
                <%--申请信息类别--%>
                <input type="hidden" id="applyType" name="applyType" value="${sealApply.applyType}"/>
                <input type="hidden" id="sealId" name="sealId" value="${sealApply.sealId}"/>
                <div class="form-group">
                    <div class="label">
                        <label>所属单位：</label>
                    </div>
                    <div class="field">
                        <input type="text" id="unitName" class="input w50" name="unitName" value="${sealApply.unit.unitName}"  readonly="readonly" />
                        <input type="hidden" id="unitId" name="unitId" value="${sealApply.unit.unitId}"/>
                        <div class="tips"></div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="label">
                        <label>印章名称：</label>
                    </div>
                    <div class="field">
                        <input type="hidden" class="input w50" id="sealTypeId" name="sealTypeId" value="${sealApply.sealTypeId}" />
                        <input type="text" class="input w50" id="sealName" name="sealName" value="${sealApply.sealName}"  readonly="readonly"/>
                        <input type="hidden" id="sealHwUserIdNum" name="sealHwUserIdNum" value="${sealApply.sealHwUserIdNum}" />

                        <div class="tips"></div>
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
                        <div class="tips"></div>
                        <div class="field" id="cer_div" style="display: none">
                            <div class="uploader white">
                                <input type="text" class="filename" readonly/>
                                <input type="button" class="button1" value="上传..."/>
                                <input type="file" id="cerFile"  name="cerFile" size="30"/>
                                <input type="hidden" id="cerBase64" name="cerBase64"/>
                                <input type="hidden" id="fileState" name="fileState"/>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="form-group"  id="image_div" >
                    <div class="label">
                        <label>印章图片：</label>
                    </div>
                    <div class="field">
                        <div class="field">
                            <img id="sealImage" src="" alt="" width="70" height="70"/>
                            <input type="hidden" id="sealImgBase64" name="sealImgBase64"/>
                            <%--<input type="hidden" id="seaIImageUploadType" name="seaIImageUploadType"/>--%>
                            <div class="tips"></div>
                        </div>
                    </div>
                </div>
                <div class="form-group"  id="image_upload" >
                    <div class="label">
                        <label>上传图片：</label>
                    </div>
                    <div class="field">
                        <div class="uploader white">
                            <input type="text" class="filename" readonly/>
                            <input type="button" name="file" class="button1" value="上传..."/>
                            <input type="file" id="imageFile"  name="imageData" size="30"/>
                        </div>
                    </div>
                </div>

                <div class="form-group" >
                    <div class="label">
                        <label>印章起始时间：</label>
                    </div>
                    <div class="field">
                        <input type="date" class="input w20" id="sealStartTime" name="sealStartTime" />
                        <div class="tips"></div>
                    </div>
                </div>
                <div class="form-group"  >
                    <div class="label">
                        <label>印章终止时间：</label>
                    </div>
                    <div class="field">
                        <input type="date" class="input w20" id="sealEndTime" name="sealEndTime" />
                        <div class="tips"></div>
                    </div>
                </div>
                <div class="form-group"  >
                    <div class="label">
                        <label>授权种类：</label>
                    </div>
                    <div class="field" style="padding-top:8px;">
                        <input type="checkbox" value="word" name="sProblem">word
                        <input type="checkbox" value="excel" name="sProblem">excel
                        <input type="checkbox" value="web" name="sProblem">web
                        <input type="checkbox" value="pdf" name="sProblem">pdf
                        <input type="hidden" name="fileTypeId" id="fileTypeId">
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
                                    <select class="form-control input w20" id="province7" name="province" ></select>
                                    <select class="form-control input w20" id="city7" name="city" ></select>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="form-group" id="psw_div" >
                        <div class="label">
                            <label>证书密码：</label>
                        </div>
                        <div class="field">
                            <input type="password" name="cerPsw" id="cerPsw" class="input w20" value=""  />
                        </div>
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


    </div>

</div>

<script>

    function getTheCheckBoxValue() {
        var test = $("input[name='sProblem']:checked");
        var checkBoxValue = "";
        test.each(function () {
            checkBoxValue += $(this).val() + "@";
        });
        checkBoxValue = checkBoxValue.substring(0, checkBoxValue.length - 1);
        return checkBoxValue;
    }

    /**
     * submit 数据
     * @returns
     */
    function sealInfoSubmit(){
        $("#fileTypeId").val(getTheCheckBoxValue());
        // alert(getTheCheckBoxValue());

        var formData = $("#submit_from").serializeArray();
        var verificResult  = verification();

        if(verificResult == "true"){
            $.ajax({
                url: "${pageContext.request.contextPath}/seal/seal_apply_do.html",
                type: "post",
                data: formData,
                // dataType : "json",
                // contentType:"application",
                success: function (data)
                {
                    if(data == "success"){
                        // window.wxc.xcConfirm("申请成功", window.wxc.xcConfirm.typeEnum.success);
                        // location.reload();
                        window.history.go(-1)
                    }else{
                        window.wxc.xcConfirm("申请失败", window.wxc.xcConfirm.typeEnum.error);
                    }
                }
            });
        }else{
            var txt = verificResult;
            window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.confirm);
        }
    }

    function verification(){

        if(isNull($("#sealImgBase64").val())){
            return "请上传图片！";
        }
        if(isNull($("#sealStartTime").val())){
            return "请选择授权开始时间！";
        }
        if(isNull($("#sealEndTime").val())){
            return "请选择授权到期时间！";
        }
        if($("#isCer").val()==1 ){
            if(isNull($("#cerBase64").val())){
                return "请上传证书！";
            }
        }else if($("#isCer").val()==0){
            if(isNull($("#cerPsw").val())){
                return "请输入证书密码！！";
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
    $("#imageFile").change(function(){
        //上传文件格式
        var imgFileType = this.value.toLowerCase().split('.').splice(-1);
        //上传文件大小
        var imgFileSize1 = this.files[0].size/1024 + 'kb';

        if("gif" == imgFileType || "png" == imgFileType){
            var v1 = $(this).val();
            var reader = new FileReader();
            reader.readAsDataURL(this.files[0]);
            reader.onload = function(e){
                $('#sealImage').attr('src',e.target.result);

                var imgArray = e.target.result.split(",");

                $('#sealImgBase64').val(imgArray[1]);
                $('#seaIImageUploadType').val(1);
            };
        }else{
            alert("文件格式错误，请重新上传");
            //重置file Input 值
            this.files[0].value = '';
        }
    });


    //上传证书 转换base64
    $("#cerFile").change(function(){
        var cerFileType = this.value.toLowerCase().split('.').splice(-1)
        var cerFileSize = this.files[0].size/1024 + 'kb';
        if("cer" == cerFileType ||"pfx" == cerFileType ){
            var v2 = $(this).val();
            var reader = new FileReader();
            reader.readAsDataURL(this.files[0]);
            reader.onload = function(e){
                var cerArray = e.target.result.split(",");
                $('#cerBase64').val(cerArray[1]);
                if("cer" == cerFileType){
                    $('#fileState').val(2);
                }else if("pfx" == cerFileType){
                    $('#fileState').val(3);
                }
            };
        }else{
            alert("文件格式错误，请重新上传");
            this.files[0].value = '';
        }
    });



    $(function(){

        //授权种类选择框初始化
        var checkBox = "${sealApply.fileTypeId}";
        var checkBoxArray = checkBox.split("@");
        for(var i=0;i<checkBoxArray.length;i++){
            $("input[name='sProblem']").each(function(){
                if($(this).val()==checkBoxArray[i]){
                    $(this).attr("checked","checked");
                }
            })
        };


        //图片初始化
        $('#sealImage').attr('src','data:image/gif;base64,'+'${sealApply.sealImg.sealImgBase64}');
        $('#sealImgBase64').val('${sealApply.sealImg.sealImgBase64}');


        //是否提供证书 上传框  控制
        //设置证书状态为未生成证书
        $('#fileState').val(1);
        $("#isCer").change(function(){
            if($("#isCer").val()=='1'){
                $("#cer_div").show();
                $("#cer_info").hide();
            }else{
                $("#cer_div").hide();
                $("#cer_info").show();
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



</script>

</body></html>