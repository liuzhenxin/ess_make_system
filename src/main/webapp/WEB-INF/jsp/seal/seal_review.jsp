<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

</head>
<body>
<form method="post" action="" id="listform">
    <div class="panel admin-panel">
        <div class="panel-head"><strong class="icon-reorder"> 印章制作申请列表</strong> <a href="" style="float:right;">${unit.unitName}</a></div>

        <div class="padding border-bottom">
            <ul class="search" style="padding-left:10px;">
                <li><a class="button border-main icon-plus-square-o" href="${pageContext.request.contextPath}/unit_page.html?toOpeUnitId=${unit.unitId}"> 返回</a></li>
            </ul>
        </div>
        <table class="table table-hover text-center">
            <tr>
                <th width="10%">印章名称</th>
                <th width="10%">印章图片</th>
                <th width="10%">申请类型</th>
                <th width="10%">申请人</th>
                <th width="10%">申请时间</th>
                <th width="10%">状态</th>
                <th width="10%">操作</th>
            </tr>
            <volist name="list" id="vo">
                <c:forEach items="${sealApplyList}" var="item"  varStatus="status">
                    <tr>
                        <td>${item.sealName}</td>
                        <td><img src="data:image/gif;base64,${item.sealImg.sealThumbnailImgBase64}" width="70" height="70"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${item.applyType == '1'}">
                                    申请新印章
                                </c:when>
                                <c:when test="${item.applyType == '2'}">
                                    UK注册
                                </c:when>
                                <c:when test="${item.applyType == '3'}">
                                    授权延期
                                </c:when>
                                <c:when test="${item.applyType == '4'}">
                                    证书延期
                                </c:when>
                                <c:when test="${item.applyType == '5'}">
                                    印章重做
                                </c:when>
                                <c:otherwise>
                                    未知
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>${item.applyUser.person.personName}</td>
                        <td>${item.applyTime}</td>
                        <td>
                            <c:choose>
                                <c:when test="${item.applyState == '1'}">
                                    未审核
                                </c:when>
                                <c:when test="${item.applyState == '2'}">
                                    已审核
                                </c:when>
                                <c:when test="${item.applyState == '4'}">
                                    制作人驳回
                                </c:when>
                                <c:otherwise>
                                    未知
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <div class="button-group">
                                <a class="button border-red" href="${pageContext.request.contextPath}/seal/seal_review_detail.html?sealApplyId=${item.sealApplyId}">
                                    <span ></span> 审核</a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </volist>
        </table>
    </div>
</form>

<script type="text/javascript">
    $(document).ready(function(){

    });
</script>
</body>
</html>