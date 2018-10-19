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

    <link rel="stylesheet" href="${pageContext.request.contextPath}/jquery-dialogbox/jquery.dialogbox-1.0.css">
    <script src="${pageContext.request.contextPath}/jquery-dialogbox/jquery.dialogbox-1.0.js"></script>
    <%--弹框插件--%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/xcConfirm/css/xcConfirm.css"/>
    <script src="${pageContext.request.contextPath}/xcConfirm/js/xcConfirm.js" type="text/javascript" charset="utf-8"></script>
    <script src="${pageContext.request.contextPath}/js/pintuer.js"></script>
    <style type="text/css">
        /*弹框样式*/
        .sgBtn{width: 135px; height: 35px; line-height: 35px; margin-left: 10px; margin-top: 10px; text-align: center;
            background-color: #0095D9; color: #FFFFFF; float: left; border-radius: 5px;}
    </style>

</head>
<body>
<form method="post" action="" id="listform">
    <div class="panel admin-panel">
        <div class="panel-head"><strong class="icon-reorder">消息列表</strong> </div>
        <div class="padding border-bottom">
            <ul class="search" style="padding-left:10px;">
                <li><a class="button border-main icon-plus-square-o" href="javaScript:window.history.go(-1)"> 返回</a></li>
            </ul>
        </div>
        <table class="table table-hover text-center">
            <tr>
                <th width="15%">标题</th>
                <th width="15%">发送时间</th>
                <th width="15%">发送人</th>
                <th width="15%">操作</th>
            </tr>
            <volist name="list" id="vo">
                <c:forEach items="${messageList}" var="item"  varStatus="status">
                    <tr id="${item.messageNo}">
                        <td>${item.messageTitle}</td>
                        <td>${item.sendTime}</td>
                        <td>${item.senderUser.person.personName}</td>
                        <td>
                            <div class="button-group">
                                <a class="button border-green" href="${pageContext.request.contextPath}/message/message_detail.html?messageNo=${item.messageNo}">
                                    <span></span> 查看</a>
                                <a class="button border-red" href="javascript:destroyMessage('${item.messageNo}')">
                                    <span></span> 删除</a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </volist>
        </table>
    </div>
</form>

<script type="text/javascript">

    function destroyMessage(messageNo) {
        $('body').dialogbox({
            type:"normal",title:"确认撤销",
            buttons:[{
                Text:"确认",
                ClickToClose:true,
                callback:function (dialog){
                    $.ajax({
                        url: "${pageContext.request.contextPath}/message/destroyMessage.html",
                        type: "get",
                        data: {"messageNo":messageNo},
                        success: function (data) {
                            var obj = eval('('+ data +')');
                            if(obj.message == "success"){
                                var id = "#"+messageNo;
                                $(id).remove();
                            }else{
                                $('body').dialogbox({
                                    type:"normal",title:"系统提示",
                                    buttons:[{
                                        Text:"确认",
                                        ClickToClose:true,
                                        callback:function (dialog){
                                            window.location.href = document.referrer;
                                        }
                                    }],
                                    message:'服务器操作失败！'
                                });
                            }

                        },
                        error: function (data) {
                            $('body').dialogbox({
                                type:"normal",title:"系统提示",
                                buttons:[{
                                    Text:"确认",
                                    ClickToClose:true,
                                    callback:function (dialog){
                                        window.location.href = document.referrer;
                                    }
                                }],
                                message:'浏览器操作失败！'
                            });
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