
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

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pintuer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mask.css">

    <script src="${pageContext.request.contextPath}/js/jquery-3.2.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/pintuer.js"></script>
    <script src="${pageContext.request.contextPath}/js/my.js"></script>
    <style type="text/css">
        .uploader { position:relative; display:inline-block; overflow:hidden; cursor:default; padding:0;
            margin:10px 0px; -moz-box-shadow:0px 0px 5px #ddd; -webkit-box-shadow:0px 0px 5px #ddd;
            box-shadow:0px 0px 5px #ddd; -moz-border-radius:5px; -webkit-border-radius:5px; border-radius:5px; }
        .filename { float:left; display:inline-block; outline:0 none; height:32px; width:180px; margin:0; padding:8px 10px; overflow:hidden; cursor:default; border:1px solid; border-right:0; font:9pt/100% Arial, Helvetica, sans-serif; color:#777; text-shadow:1px 1px 0px #fff; text-overflow:ellipsis; white-space:nowrap; -moz-border-radius:5px 0px 0px 5px; -webkit-border-radius:5px 0px 0px 5px; border-radius:5px 0px 0px 5px; background:#f5f5f5; background:-moz-linear-gradient(top, #fafafa 0%, #eee 100%); background:-webkit-gradient(linear, left top, left bottom, color-stop(0%, #fafafa), color-stop(100%, #f5f5f5)); filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#fafafa', endColorstr='#f5f5f5', GradientType=0);
            border-color:#ccc; -moz-box-shadow:0px 0px 1px #fff inset; -webkit-box-shadow:0px 0px 1px #fff inset; box-shadow:0px 0px 1px #fff inset; -moz-box-sizing:border-box; -webkit-box-sizing:border-box; box-sizing:border-box; }
        .button1 { float:left; height:32px; display:inline-block; outline:0 none; padding:8px 12px; margin:0; cursor:pointer; border:1px solid; font:bold 9pt/100% Arial, Helvetica, sans-serif; -moz-border-radius:0px 5px 5px 0px; -webkit-border-radius:0px 5px 5px 0px; border-radius:0px 5px 5px 0px; -moz-box-shadow:0px 0px 1px #fff inset; -webkit-box-shadow:0px 0px 1px #fff inset; box-shadow:0px 0px 1px #fff inset; }
        .uploader input[type=file] { position:absolute; top:0; right:0; bottom:0; border:0; padding:0; margin:0;
            height:30px; cursor:pointer; filter:alpha(opacity=0); -moz-opacity:0; -khtml-opacity: 0; opacity:0; }

        .body-content{
            width: 50%;
            margin: 20px 40px;
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
    </style>
</head>
<body>

<div class="panel admin-panel">
    <div class="panel-head" id="add"><strong><span class="icon-pencil-square-o"></span>审核印章申请</strong></div>
    <div class="padding border-bottom">
        <ul class="search m-l-10">
            <li><a class="button border-main icon-plus-square-o" href="javaScript:window.history.go(-1)"> 返回</a></li>
        </ul>
    </div>
    <div class="body-content f-s-14 c-6">
        <form method="post" class="form-x" action="" id="seal_review_from" enctype="multipart/form-data">
            <input type="hidden" value="${certificate.certificateId}"/>
            <div class="ub option">
                <div class="title">证书名称：</div>
                <div class="ub-f1">${certificate.cerName}</div>
            </div>
            <div class="ub option">
                <div class="title">签名算法：</div>
                <div class="ub-f1">${certificate.algorithm}</div>
            </div>
            <div class="ub option">
                <div class="title">证书用途：</div>
                <div class="ub-f1">${certificate.cerClass}</div>
            </div>
            <div class="ub option">
                <div class="title">颁发者：</div>
                <div class="ub-f1">${certificate.issuerUnit.issuerUnitName}</div>
            </div>
            <div class="ub option">
                <div class="title">证书所有人：</div>
                <div class="ub-f1">${certificate.owner}</div>
            </div>
            <div class="ub option">
                <div class="title">证书版本：</div>
                <div class="ub-f1">${certificate.certificateVersion}</div>
            </div>
            <div class="ub option">
                <div class="title">申请时间：</div>
                <div class="ub-f1">${certificate.applyTime}</div>
            </div>
            <div class="ub option">
                <div class="title">有效期起始时间：</div>
                <div class="ub-f1">${certificate.starTime}</div>
            </div>
            <div class="ub option">
                <div class="title">有效期结束时间：</div>
                <div class="ub-f1">${certificate.endTime}</div>
            </div>
            <div class="ub option">
                <div class="title">证书状态：</div>
                <div class="ub-f1 c-base">
                    <c:choose>
                        <c:when test="${certificate.fileState == '1'}">证书未生成</c:when>
                        <c:when test="${certificate.fileState == '2'}">cer证书</c:when>
                        <c:when test="${certificate.fileState == '3'}">pfx证书</c:when>
                        <c:when test="${certificate.fileState == '4'}">pfx证书和cer证书</c:when>
                        <c:otherwise>未知</c:otherwise>
                    </c:choose>
                </div>
            </div>
        </form>
    </div>

</div>

<script>

</script>

</body></html>