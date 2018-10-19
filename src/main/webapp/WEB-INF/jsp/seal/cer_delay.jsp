
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


<div class="panel admin-panel">
    <div class="panel-head" id="add"><strong><span class="icon-pencil-square-o"></span>审核印章申请</strong></div>
    <div class="padding border-bottom">
        <ul class="search" style="padding-left:10px;">
            <li><a class="button border-main icon-plus-square-o" href="javaScript:window.history.go(-1)"> 返回</a></li>
        </ul>
    </div>
    <div class="body-content">
        <form method="post" class="form-x" action="" id="seal_apply_from" enctype="multipart/form-data">
            <input type="hidden" name="certificateId" value="${certificate.certificateId}"/>
            <%--可以修改--%>
            <div class="form-group"  id="seal_end_time_div" >
                <div class="label">
                    <label>证书起始时间：</label>
                </div>
                <div class="field">
                    <input type="date" id="StarTime" name="StarTime"  value="${certificate.starTime}"/>
                    <div class="tips"></div>
                </div>
            </div>
            <%--可以修改--%>
            <div class="form-group"  id="seal_end_time_div" >
                <div class="label">
                    <label>证书到期时间：</label>
                </div>
                <div class="field">
                    <input type="date" id="endTime" name="endTime"  value="${certificate.endTime}"/>
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
            url: "${pageContext.request.contextPath}/seal/cer_delay_do.html",
            type: "post",
            data: formData,
            // dataType : "json",
            success: function (data)
            {
                var txt=  "延期信息提交成功，返回上一页面";
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