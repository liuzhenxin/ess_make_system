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
    <%--弹框插件--%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/xcConfirm/css/xcConfirm.css"/>
    <script src="${pageContext.request.contextPath}/xcConfirm/js/xcConfirm.js" type="text/javascript" charset="utf-8"></script>

    <script src="${pageContext.request.contextPath}/js/pintuer.js"></script>

</head>
<body>
<form method="post" action="" id="listform">
    <div class="panel admin-panel">
        <div class="panel-head"><strong class="icon-reorder">部门列表</strong> <a href="" style="float:right;">${unit.unitName}</a></div>


        <div class="padding border-bottom">
            <ul class="search" style="padding-left:10px;">

                <li><a class="button border-main icon-plus-square-o" href="${pageContext.request.contextPath}/apply/list.html?unitId=${unit.unitId}"> 申请印章</a></li>
                <c:if test="${registerButton}">
                    <li><a class="button border-main icon-plus-square-o" href="${pageContext.request.contextPath}/apply/register_uk.html?unitId=${unit.unitId}"> 注册UK印章</a></li>
                </c:if>
                <c:if test="${reviewButton}">
                    <li><a class="button border-main icon-plus-square-o" href="${pageContext.request.contextPath}/review/list.html?unitId=${unit.unitId}"> 审核印章</a></li>
                </c:if>
                <c:if test="${makeButton}">
                    <li><a class="button border-main icon-plus-square-o" href="${pageContext.request.contextPath}/make/list.html?unitId=${unit.unitId}"> 制作印章</a></li>
                </c:if>
                <li><a class="button border-main icon-plus-square-o" href="${pageContext.request.contextPath}/seal/seal_list.html?unitId=${unit.unitId}"> 查看单位公章</a></li>

            </ul>
        </div>
        <table class="table table-hover text-center">
            <tr>
                <th width="15%">部门</th>
                <th width="10%">部门创建人</th>
                <th width="15%">创建时间</th>
                <th width="25%">操作</th>
            </tr>
            <volist name="list" id="vo">
                <c:forEach items="${departmentList}" var="item"  varStatus="status">
                    <tr>
                        <td>${item.depName}</td>
                        <td>${item.user.person.personName}</td>
                        <td>${item.inputTime}</td>
                        <td>
                            <div class="button-group">
                                    <%--<a class="button border-main" href="add.html"><span class="icon-edit"></span> 修改</a>--%>
                                <a class="button border-blue" href="${pageContext.request.contextPath}/seal/sign_list.html?depId=${item.depId}">
                                    <span></span> 手签列表</a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </volist>
        </table>
    </div>
</form>

<script type="text/javascript">

    function registerUK(){
        // var txt=  "提示文字，提示文字，提示文字，提示文字，提示文字，提示文字";
        // window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.info);
        $.ajax({
            url: "/seal/register_uk.html",
            type: "get",
            data: "name",
            success: function (data)
            {
                document.write(data);
            }
        });
    }

</script>
</body>
</html>