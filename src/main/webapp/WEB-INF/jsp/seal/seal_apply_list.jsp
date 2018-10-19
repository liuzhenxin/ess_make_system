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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/jquery-dialogbox/jquery.dialogbox-1.0.css">

    <script src="${pageContext.request.contextPath}/js/jquery-3.2.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/pintuer.js"></script>
    <script src="${pageContext.request.contextPath}/js/my.js"></script>
    <script src="${pageContext.request.contextPath}/jquery-dialogbox/jquery.dialogbox-1.0.js"></script>

    <style type="text/css">

    </style>

</head>
<body>
<form method="post" action="" id="listform">
    <div class="panel admin-panel">
        <div class="panel-head"><strong class="icon-reorder">部门列表</strong> <a href="" style="float:right;">${unit.unitName}</a></div>
        <div class="padding border-bottom">
            <ul class="search" style="padding-left:10px;">
                <li><a class="button border-main icon-plus-square-o" href="${pageContext.request.contextPath}/unit_page.html?toOpeUnitId=${unit.unitId}"> 返回</a></li>
                <li><a class="button border-main icon-plus-square-o" href="${pageContext.request.contextPath}/apply/add.html?unitId=${unit.unitId}"> 添加申请</a></li>
            </ul>
        </div>

        <table class="table table-hover text-center">
            <tr>
                <th width="10%">印章名称</th>
                <th width="10%">印章图片人</th>
                <th width="10%">申请类型</th>
                <th width="10%">申请人</th>
                <th width="10%">申请时间</th>
                <th width="10%">状态</th>
                <th width="10%">操作</th>
            </tr>

            <volist name="list" id="vo">
                <c:forEach items="${sealApplyList}" var="item"  varStatus="status">
                    <tr id="${item.sealApplyId}" >
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
                                <c:when test="${item.applyState == '3'}">
                                    审核人驳回
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
                                <c:choose>
                                    <c:when test="${item.applyState == '1'}">
                                        <a class="button border-red" href="javascript:sealApplyDelete('${item.sealApplyId}')">
                                            <span ></span> 撤销申请</a>
                                    </c:when>
                                    <c:when test="${item.applyState == '3'}">
                                        <a class="button border-red" href="javascript:sealApplyDelete('${item.sealApplyId}')">
                                            <span ></span> 撤销申请</a>
                                        <%--<a class="button border-red" href="javascript:sealReApply('${item.sealApplyId}')">--%>
                                            <%--<span ></span> 重新申请</a>--%>
                                    </c:when>
                                    <c:otherwise>

                                    </c:otherwise>
                                </c:choose>

                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </volist>
        </table>
    </div>
</form>

<script type="text/javascript">
    function sealApplyDelete(sealApplyId) {
        $('body').dialogbox({
            type:"normal",title:"确认撤销",
            buttons:[{
                Text:"确认",
                ClickToClose:true,
                callback:function (dialog){
                    $.ajax({
                        url: "${pageContext.request.contextPath}/seal/sealApply_delete.html",
                        type: "get",
                        data: sealApplyId,
                        success: function (data) {
                            var id = "#"+sealApplyId;
                            $(id).remove();
                        },
                        error: function (data) {
                            alert("操作失败");
                        }
                    });
                }
            }],
            message:"撤销的申请信息将被删除！"
        });
    }
    function sealReApply(sealApplyId) {
        $('body').dialogbox({
            type:"normal",title:"确认",
            buttons:[{
                Text:"确认",
                ClickToClose:true,
                callback:function (dialog){
                    $.ajax({
                        url: "${pageContext.request.contextPath}/seal/sealApply_delete.html",
                        type: "get",
                        data: sealApplyId,
                        success: function (data) {
                            var id = "#"+sealApplyId;
                            $(id).remove();
                        },
                        error: function (data) {
                            alert("操作失败");
                        }
                    });
                }
            }],
            message:"撤销的申请信息将被删除！"
        });
    }

</script>
</body>
</html>