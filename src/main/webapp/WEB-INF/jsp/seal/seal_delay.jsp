
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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pintuer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mask.css">
    <script src="${pageContext.request.contextPath}/js/jquery-3.2.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/pintuer.js"></script>
    <script src="${pageContext.request.contextPath}/js/my.js"></script>


<%--弹框插件--%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/xcConfirm/css/xcConfirm.css"/>
    <script src="${pageContext.request.contextPath}/xcConfirm/js/xcConfirm.js" type="text/javascript" charset="utf-8"></script>
    <style type="text/css">
        /*弹框样式*/
        .sgBtn{width: 135px; height: 35px; line-height: 35px; margin-left: 10px; margin-top: 10px; text-align: center;
            background-color: #0095D9; color: #FFFFFF; float: left; border-radius: 5px;}
    </style>
</head>
<body>

<object classid="clsid:8F6C59C0-BD5B-4F90-8827-526BFB5EBB44" id="ESSWebSign1" width="1  "height="1"
        style="position: absolute" CODEBASE="ESSWebSign.CAB#version=1,0,0,-1" align="baseline" hspace="200">
    <param name="_Version" value="65536">
    <param name="_ExtentX" value="-8202">
    <param name="_ExtentY" value="1058">
    <param name="_StockProps" value="0">
</object>

<div class="panel admin-panel">
    <div class="panel-head" id="add"><strong><span class="icon-pencil-square-o"></span>审核印章申请</strong></div>
    <div class="padding border-bottom">
        <ul class="search" style="padding-left:10px;">
            <li><a class="button border-main icon-plus-square-o" href="javaScript:window.location.href = document.referrer;"> 返回</a></li>
        </ul>
    </div>
    <div class="body-content">
        <form method="post" class="form-x" action="" id="seal_apply_from" enctype="multipart/form-data">
            <input type="hidden" name="sealId" value="${seal.sealId}"/>
            <%--可以修改--%>
            <div class="form-group"  id="seal_end_time_div" >
                <div class="label">
                    <label>授权起始时间：</label>
                </div>
                <div class="field">
                    ${seal.sealStartTime}
                </div>
            </div>
            <%--可以修改--%>
            <div class="form-group"  id="seal_end_time_div" >
                <div class="label">
                    <label>授权到期时间：</label>
                </div>
                <div class="field">
                    <input type="date" id="sealEndTime" name="sealEndTime"  value="${seal.sealEndTime}"/>
                    <div class="tips"></div>
                </div>
            </div>
            <div class="form-group">
                <div class="label">
                    <label></label>
                </div>
                <div class="field">
                    <button type="button" class="button bg-main icon-check-square-o" onclick="sealInfoSubmit()"> 延期申请</button>
                </div>
            </div>
        </form>
    </div>
</div>

<script>

    /**
     * submit数据
     * @param
     * @returns
     */
    function sealInfoSubmit(){
        // alert("上传服务器成功");
        var formData = $("#seal_apply_from").serializeArray();

        $.ajax({
            url: "${pageContext.request.contextPath}/seal/seal_delay_do.html",
            type: "post",
            data: formData,
            // dataType : "json",
            success: function (data)
            {
                $("#btn7").click(function(){

                });
                var txt=  "延期申请成功，返回上一页面";
                var option = {
                    title: "消息提示",
                    btn: parseInt("0011",2),
                    onOk: function(){
                        javaScript:window.history.go(-1);
                    }
                }
                window.wxc.xcConfirm(txt, "custom", option);
            }
        });
    }

</script>

</body></html>