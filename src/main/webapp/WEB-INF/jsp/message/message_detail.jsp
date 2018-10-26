
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


    <link rel="stylesheet" href="${pageContext.request.contextPath}/jquery-dialogbox/jquery.dialogbox-1.0.css">
    <script src="${pageContext.request.contextPath}/jquery-dialogbox/jquery.dialogbox-1.0.js"></script>

    <!--弹框插件-->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/xcConfirm/css/xcConfirm.css"/>
    <script src="${pageContext.request.contextPath}/xcConfirm/js/xcConfirm.js" type="text/javascript" charset="utf-8"></script>
    <script src="${pageContext.request.contextPath}/js/pintuer.js"></script>
    <style type="text/css">
        /*弹框样式*/
        .sgBtn{width: 135px; height: 35px; line-height: 35px; margin-left: 10px; margin-top: 10px; text-align: center;
            background-color: #0095D9; color: #FFFFFF; float: left; border-radius: 5px;}
        .contentClass{
            padding: 20px;
        }
        .option{
            line-height: 40px;
            height: 40px;
        }
        .title{
            width: 70px;
        }
        .cardClass{
            padding: 20px;
            width: 56%;
            box-shadow: 3px -3px 3px #dedede, -3px 3px 3px #dedede, -3px -3px 3px #dedede;
            background-color: #f7f7f7;
        }
    </style>

</head>
<body>

<div class="panel admin-panel">
    <div class="panel-head"><strong><span class="icon-pencil-square-o"></span> 消息内容</strong></div>
    <div class="padding border-bottom">
        <ul class="search" style="padding-left:10px;">
            <li><a class="button border-main icon-plus-square-o" href="javaScript:window.history.go(-1)"> 返回</a></li>
        </ul>
    </div>
    <div class="contentClass">
        <form method="post" class="form-x" action="">
            <div class="cardClass">
                <div class="ub option">
                    <div class="title f-w">标题：</div>
                    <div class="ub-f1">${message.messageTitle}</div>
                </div>
                <div class="ub option">
                    <div class="title f-w">发送时间：</div>
                    <div class="ub-f1" >${message.sendTime}</div>
                </div>
                <div class="ub option">
                    <div class="title f-w">内容：</div>
                    <div class="ub-f1" >${message.messageContent}</div>
                </div>
            </div>
        </form>
    </div>
    <div class="ub option m-l-20 m-b-20">
        <div class="field">
            <c:choose>
                <c:when test="${message.messageType == '1' && message.readState ==2}">
                    <%--<button class="button bg-main icon-check-square-o" onclick="messageMethodType_1('${message.messageNo}')"> 重新申请</button>--%>
                </c:when>
                <c:when test="${message.messageType == '2'&& message.readState=='2'}">
                    <button class="button bg-main icon-check-square-o" onclick="messageMethodType_2('${message.messageNo}')"> 完成注册</button>
                </c:when>
                <c:when test="${message.messageType == '3' && message.readState=='2'}">
                    <%--<button class="button bg-main icon-check-square-o" onclick="messageMethodType_3('${message.messageNo}')"> 重做确认</button>--%>
                    <button class="button bg-main icon-check-square-o" onclick="messageMethodType_4('${message.messageNo}')"> 查看印章</button>
                </c:when>
                <c:when test="${message.messageType == '4' && message.readState=='2'}">
                    <button class="button bg-main icon-check-square-o" onclick="messageMethodType_4('${message.messageNo}')"> 延期生效</button>
                </c:when>
                <c:when test="${message.messageType == '5' && message.readState=='2'}">
                    <button class="button bg-main icon-check-square-o" onclick="messageMethodType_5('${message.messageNo}')"> 延期生效</button>
                </c:when>
            </c:choose>
        </div>
    </div>
</div>
<script type="text/javascript">
    //类型：驳回
    function messageMethodType_1(messageNo) {
        $.ajax({
            url: "${pageContext.request.contextPath}/message/messageMethodType_1.html",
            type: "get",
            data:  {"messageNo":messageNo},
            success: function (data)
            {
                $('body').dialogbox({
                    type:"normal",title:"选择操作",
                    buttons:[{
                        Text:"返回列表",
                        ClickToClose:true,
                        callback:function (dialog){
                            window.location.href = document.referrer;
                        }
                    }],
                    message:'申请信息已注销！'
                });

            }
        });
    }
    //类型：注册
    function messageMethodType_2(messageNo) {
        $.ajax({
            url: "${pageContext.request.contextPath}/message/messageMethodType_2.html",
            type: "get",
            data:  {"messageNo":messageNo},
            success: function (data)
            {
                var obj = eval('('+ data +')');
                if(obj.message == "success"){
                    $('body').dialogbox({
                        type:"normal",title:"系统实体",
                        buttons:[{
                            Text:"返回列表",
                            ClickToClose:true,
                            callback:function (dialog){
                                window.location.href = document.referrer;
                            }
                        },
                            {
                                Text:"查看印章",
                                ClickToClose:true,
                                callback:function (dialog){
                                    window.location.href="${pageContext.request.contextPath}/seal/seal_detail.html?sealId="+obj.body.sealId;
                                    // window.location.href="http://192.168.1.112:8081/ess_make_system/seal/seal_detail.html?sealId="+obj.body.sealId;
                                }
                            }
                        ],
                        message:'注册确认成功！'
                    });
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
            }
        });
    }

    //类型：重做
    function messageMethodType_3(messageNo) {
        $.ajax({
            url: "${pageContext.request.contextPath}/message/messageMethodType_3.html",
            type: "get",
            data:  {"messageNo":messageNo},
            success: function (data)
            {
                var obj = eval('('+ data +')');
                if(obj.message == "success"){
                    $('body').dialogbox({
                        type:"normal",title:"系统实体",
                        buttons:[{
                            Text:"返回列表",
                            ClickToClose:true,
                            callback:function (dialog){
                                window.location.href = document.referrer;
                            }
                        },
                            {
                                Text:"查看印章",
                                ClickToClose:true,
                                callback:function (dialog){
                                    window.location.href="${pageContext.request.contextPath}/seal/seal_detail.html?sealId="+obj.body.sealId;
                                }
                            }
                        ],
                        message:'注册确认成功！'
                    });
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
            }
        });

    }
    //类型：授权延期
    function messageMethodType_4(messageNo) {
        $.ajax({
            url: "${pageContext.request.contextPath}/message/messageMethodType_4.html",
            type: "get",
            data:  {"messageNo":messageNo},
            success: function (data)
            {
                var obj = eval('('+ data +')');
                if(obj.message == "success"){
                    if(obj.body.isUK==1){
                        //UK执行写入
                        var result = "";
                        //传入信息：设备类型，颁发者ID，图片ID,图片类型，授权产品，授权到期时间
                        var paramter = "";
                        // 设备类型
                        paramter += "CARDTYPE="+$("#cardType").val();
                        //授权到期时间
                        paramter += "&PRODUCTEND="+replaceAll($("#sealEndTime").val(),"-","");
                        //写授权
                        paramter += "&WA=YES";
                        //无意义
                        paramter += "&ORGNAME=CLTESS";
                        $.ajax({
                            url: "http://127.0.0.1:10102?jsoncallback=?",
                            data: {
                                ACT:"CERT_CREATENEW",
                                VAL:paramter
                            },
                            dataType: "json",
                            success: function (data) {
                                //写入UK判断start
                                if(data[0].status == 0){
                                    result="OK"
                                }else if(data[0].status == 1){
                                    result = "请登录安全客户端"
                                }else if(data[0].status == 2){
                                    result = "发生错误"
                                } else{
                                    result = "未知错误"+data[0].status;
                                }
                                if(result=="OK"){
                                    $('body').dialogbox({
                                        type:"normal",title:"系统实体",
                                        buttons:[{
                                            Text:"返回列表",
                                            ClickToClose:true,
                                            callback:function (dialog){

                                                window.location.href = document.referrer;
                                            }
                                        },
                                            {
                                                Text:"查看印章",
                                                ClickToClose:true,
                                                callback:function (dialog){
                                                    window.location.href="${pageContext.request.contextPath}/seal/seal_detail.html?sealId="+obj.body.sealId;
                                                }
                                            }
                                        ],
                                        message:'注册确认成功！'
                                    });
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
                                        message:'UK写入失败！'
                                    });
                                }
                            }
                        });
                    }else{
                        $('body').dialogbox({
                            type:"normal",title:"系统实体",
                            buttons:[{
                                Text:"返回列表",
                                ClickToClose:true,
                                callback:function (dialog){

                                    window.location.href = document.referrer;
                                }
                            },
                                {
                                    Text:"查看印章",
                                    ClickToClose:true,
                                    callback:function (dialog){

                                        window.location.href="${pageContext.request.contextPath}/seal/seal_detail.html?sealId="+obj.body.sealId;
                                    }
                                }
                            ],
                            message:'注册确认成功！'
                        });
                    }

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
            }
        });
    }

    function CreateNewCert(sealImgId){
        //UK执行写入
        var result = "";
        //传入信息：设备类型，颁发者ID，图片ID,图片类型，授权产品，授权到期时间
        var paramter = "";
        // 设备类型
        paramter += "CARDTYPE="+$("#cardType").val();
        //授权到期时间
        paramter += "&PRODUCTEND="+replaceAll($("#sealEndTime").val(),"-","");
        //写授权
        paramter += "&WA=YES";
        //无意义
        paramter += "&ORGNAME=CLTESS";
        $.ajax({
            url: "http://127.0.0.1:10102?jsoncallback=?",
            data: {
                ACT:"CERT_CREATENEW",
                VAL:paramter
            },
            dataType: "json",
            success: function (data) {
                //写入UK判断start
                if(data[0].status == 0){
                    result="OK"
                }else if(data[0].status == 1){
                    result = "请登录安全客户端"
                }else if(data[0].status == 2){
                    result = "发生错误"
                } else{
                    result = "未知错误"+data[0].status;
                }
            }
        });
    }


    //类型：证书延期
    function messageMethodType_5(messageNo) {
        $.ajax({
            url: "${pageContext.request.contextPath}/message/messageMethodType_5.html",
            type: "get",
            data:  {"messageNo":messageNo},
            success: function (data)
            {
                var s = data.split("@");
                if(s[0]=1){
                    //传入信息：设备类型，颁发者ID，图片ID,图片类型，授权产品，授权到期时间
                    var paramter = "";
                    // 设备类型
                    paramter += "CARDTYPE=FTCX";
                    //授权到期时间
                    paramter += "&CERTID="+s[3];
                    //ukid
                    paramter += "&UKID="+s[1];
                    //是否写证书
                    paramter += "&WC=YES";
                    //对比UKID
                    paramter += "&CU=YES";
                    //无意义
                    paramter += "&ORGNAME=CLTESS";
                    $.getJSON("http://127.0.0.1:10102?jsoncallback=?", {
                            ACT:"CERT_CREATENEW",
                            VAL:paramter
                        },
                        function(data) {
                            //写入UK判断start
                            if(data[0].status == 0){
                                $('body').dialogbox({
                                    type:"normal",title:"选择操作",
                                    buttons:[{
                                        Text:"返回列表",
                                        ClickToClose:true,
                                        callback:function (dialog){
                                            window.location.href = document.referrer;
                                        }
                                    }],
                                    buttons:[{
                                        Text:"查看印章",
                                        ClickToClose:true,
                                        callback:function (dialog){
                                            window.location.href="${pageContext.request.contextPath}/seal/seal_detail.html?sealId="+obj.body.sealId;
                                        }
                                    }],
                                    message:'申请信息已注销！'
                                });
                                result_uk = "OK"

                            }else{
                                $('body').dialogbox({
                                    type:"normal",title:"服务器操作失败",
                                    buttons:[{
                                        Text:"确认",
                                        ClickToClose:true,
                                        callback:function (dialog){

                                        }
                                    }],
                                    message:'您提交的信息有误,错误代码！'+data[0].status
                                });
                            }
                        }
                    );
                }
            }
        });
    }

    function replaceAll(str,str1,str2) {

        re = new RegExp(str1,"g"); //定义正则表达式
        //第一个参数是要替换掉的内容，第二个参数"g"表示替换全部（global）。
        var Newstr = str.replace(re, str2); //第一个参数是正则表达式。
        //本例会将全部匹配项替换为第二个参数。
        return Newstr;
    }
</script>
</body>
</html>