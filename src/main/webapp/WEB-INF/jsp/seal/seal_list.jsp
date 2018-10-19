<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/11/27
  Time: 11:17
  To change this template use File | Settings | File Templates.
--%>
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
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/xcConfirm/css/xcConfirm.css"/>
    <script src="${pageContext.request.contextPath}/xcConfirm/js/xcConfirm.js" type="text/javascript" charset="utf-8"></script>
    <style type="text/css">
        .sgBtn{width: 135px; height: 35px; line-height: 35px; margin-left: 10px; margin-top: 10px; text-align: center; background-color: #0095D9; color: #FFFFFF; float: left; border-radius: 5px;}
    </style>
</head>
<body>
<form method="post" action="" id="listform">
    <div class="panel admin-panel">
        <div class="panel-head"><strong class="icon-reorder"> 印章列表</strong> <a href="" style="float:right;">${unit.unitName}</a></div>
        <div class="padding border-bottom">
            <ul class="search" style="padding-left:10px;">
                <li><a class="button border-main icon-plus-square-o" href="${pageContext.request.contextPath}/unit_page.html?toOpeUnitId=${unit.unitId}"> 返回</a></li>
            </ul>
        </div>
        <table class="table table-hover text-center">
            <tr>
                <th width="15%">印章名称</th>
                <th width="15%">印章图片</th>
                <th width="10%">是否UK</th>
                <th width="10%">到期时间</th>
                <th width="8%">印章状态</th>
                <th width="30%">操作</th>
                <th width="15%">详细</th>
            </tr>
            <volist name="list" id="vo">
                <c:forEach items="${sealList}" var="item"  varStatus="status">
                    <tr>
                        <td>${item.sealName}</td>
                        <td><img src="data:image/gif;base64,${item.sealImg.sealThumbnailImgBase64}" width="70" height="70"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${item.isUK == '1'}">
                                    是
                                </c:when>
                                <c:otherwise>
                                    否
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>${item.sealEndTime}</td>
                        <td>
                            <div id="${item.sealId}a">
                                <c:choose>
                                    <c:when test="${item.sealState == '1'}">
                                        有效
                                    </c:when>
                                    <c:when test="${item.sealState == '3'}">
                                        暂停
                                    </c:when>
                                </c:choose>
                            </div>
                        </td>
                        <td>
                            <div class="button-group">
                                <c:if test="${redoSealButton}">
                                    <a class="button border-green bg-white" href="${pageContext.request.contextPath}/apply/repeat.html?sealId=${item.sealId}">
                                        <span></span> 重做</a>
                                </c:if>
                                <c:if test="${stopButton}">
                                    <a class="button  border-green bg-white" href="javascript:seal_del('${item.sealId}')">
                                        <span></span> 注销</a>

                                </c:if>
                                <c:if test="${delayButton}">

                                    <a class="button  border-green bg-white" href="${pageContext.request.contextPath}/apply/delay.html?sealId=${item.sealId}">
                                        <span></span> 延期</a>
                                </c:if>

                                <c:if test="${pauseButton}">
                                    <c:choose>
                                        <c:when test="${item.sealState == '1'}">
                                            <input type="button" class="button  border-green bg-white" id="${item.sealId}b" value="悬挂" onclick="switch_pause('${item.sealId}','${item.sealState}')" />
                                        </c:when>
                                        <c:when test="${item.sealState == '3'}">
                                            <input type="button" class="button  border-green bg-white" id="${item.sealId}b" value="恢复" onclick="switch_pause('${item.sealId}','${item.sealState}')" />
                                        </c:when>
                                    </c:choose>
                                </c:if>
                            </div>
                        </td>
                        <td>
                            <a class="button  border-green bg-white" id="show"  href="${pageContext.request.contextPath}/seal/seal_detail.html?sealId=${item.sealId}">
                                详细</a>
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

    function switch_pause(sealId,sealState){
        var target_a = '#'+sealId+'a';
        var target = '#'+sealId+'b';
        $.ajax({
            url: "${pageContext.request.contextPath}/seal/seal_pause_switch.html",
            type: "get",
            data:  {"sealId":sealId,"sealState":sealState},
            success: function (data) {
                if($(target).val()=="恢复"){
                    $(target).val("悬挂");
                    $(target_a).text("有效");
                }else {
                    $(target).val("恢复");
                    $(target_a).text("暂停");
                }
            }
        });

    }


    function seal_del(sealId){

        $.ajax({
            url: "${pageContext.request.contextPath}/seal/seal_del.html",
            type: "get",
            data:  {"sealId":sealId},
            success: function (data)
            {
                location.reload();
                if(data == "success"){
                    var txt=  "操作成功";
                    var option = {
                        title: "自定义",
                        btn: parseInt("0011",2),
                        onOk: function(){

                            location.reload();
                        }
                    }
                    window.wxc.xcConfirm(txt, "custom", option);

                }

            }
        });

    }

    function seal_detail(sealId){

        $.ajax({
            url: "${pageContext.request.contextPath}/seal/seal_detail.html",
            type: "get",
            data:  {"sealId":sealId},
            success: function (data)
            {
                // location.reload();
                if(data == "success"){
                    var txt=  "操作成功";
                    var option = {
                        title: "自定义",
                        btn: parseInt("0011",2),
                        onOk: function(){

                            // location.reload();
                        }
                    }
                    window.wxc.xcConfirm(txt, "custom", option);

                }

            }
        });

    }
</script>
</body>
</html>