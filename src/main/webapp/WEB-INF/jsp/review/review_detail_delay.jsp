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

    <%--弹框插件--%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/xcConfirm/css/xcConfirm.css"/>
    <script src="${pageContext.request.contextPath}/xcConfirm/js/xcConfirm.js" type="text/javascript" charset="utf-8"></script>
    <style type="text/css">

        .body-content{
            width: 60%;
            margin: 20px 10px;
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
        .w150{
            width: 150px;
            float:left;
        }
        /*弹框样式*/
        .sgBtn{width: 135px; height: 35px; line-height: 35px; margin-left: 10px; margin-top: 10px; text-align: center;
            background-color: #0095D9; color: #FFFFFF; float: left; border-radius: 5px;}
        .rightContent{
            width: 30%;
            /*border: 1px #dedede solid;*/
            margin: 20px 10px;
            box-shadow: 3px -3px 3px #dedede, -3px 3px 3px #dedede, -3px -3px 3px #dedede;
            background-color: #f7f7f7;
        }
    </style>
</head>
<body>
<div class="panel admin-panel">
    <div class="panel-head" id="add">
        <strong>
            <span class="icon-pencil-square-o"></span> 审核印章申请-
            <c:choose>
                <c:when test="${sealApply.applyType == '1'}">
                    申请新印章
                </c:when>
                <c:when test="${sealApply.applyType == '2'}">
                    UK注册
                </c:when>
                <c:when test="${sealApply.applyType == '3'}">
                    授权延期
                </c:when>
                <c:when test="${sealApply.applyType == '4'}">
                    证书延期
                </c:when>
                <c:when test="${sealApply.applyType == '5'}">
                    印章重做
                </c:when>
                <c:otherwise>
                    未知
                </c:otherwise>
            </c:choose>
        </strong>
    </div>
    <div class="padding border-bottom">
        <ul class="search" style="padding-left:10px;">
            <li><a class="button border-main icon-plus-square-o" href="javaScript:window.location.href = document.referrer;"> 返回</a></li>
        </ul>
    </div>
    <div class="ub">
        <div class="body-content f-s-14 c-6">
            <form method="post" class="form-x" action="" id="seal_review_from" enctype="multipart/form-data">

                <input type="hidden" id="sealApplyId" name="sealApplyId" value="${sealApply.sealApplyId}"/>
                <div class="ub option">
                    <div class="title f-w">所属单位：</div>
                    <div class="ub-f1">${sealApply.unit.unitName}</div>
                </div>
                <div class="ub option">
                    <div class="title f-w">印章名称：</div>
                    <div class="ub-f1" >${sealApply.sealName}</div>
                </div>
                <div class="ub option">
                    <div class="title f-w">是否使用UK：</div>
                    <c:choose>
                        <c:when test="${sealApply.isUK == '1'}">
                            <div class="ub-f1" >是</div>
                        </c:when>
                        <c:otherwise>
                            <div class="ub-f1" >否</div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="ub option">
                    <div class="title f-w">印章起始时间：</div>
                    <div class="ub-f1" >
                        <input type="date" id="sealStartTime" name="sealStartTime" value="${sealApply.sealStartTime}" />
                    </div>
                </div>
                <div class="ub option">
                    <div class="title f-w">印章终止时间：</div>
                    <div class="ub-f1">
                        <input type="date" id="sealEndTime" name="sealEndTime" value="${sealApply.sealEndTime}" />
                    </div>
                </div>
                <div class="ub option">
                    <div class="title f-w">
                        授权种类：
                    </div>
                    <div class="field"  id="fileType">

                    </div>
                </div>
                <div class="form-group">
                    <div class="label">
                        <label></label>
                    </div>
                    <div class="field">
                        <button type="button" class="button bg-main icon-check-square-o"  id="submit" > 通过</button>
                        <button type="button" class="button bg-main icon-check-square-o" id="reject"> 驳回</button>
                    </div>
                </div>
            </form>
        </div>

        <div class="rightContent ub-f1 ">
            <input type="hidden" id="certificateId" value="${sealApply.certificate.certificateId}"/>
            <div class="ub option">
                <div class="title f-w">国家：</div>
                <div class="ub-f1">
                    ${sealApply.certificate.country}
                </div>
            </div>
            <div class="ub option">
                <div class="title f-w">省份：</div>
                <div class="ub-f1">
                    ${sealApply.certificate.province}
                </div>
            </div>
            <div class="ub option">
                <div class="title f-w">城市：</div>
                <div class="ub-f1">
                    ${sealApply.certificate.city}
                </div>
            </div>
            <div class="ub option">
                <div class="title f-w">单位：</div>
                <div class="ub-f1">
                    ${sealApply.certificate.certUnit}
                </div>
            </div>
            <div class="ub option">
                <div class="title f-w">部门：</div>
                <div class="ub-f1">
                    ${sealApply.certificate.certDepartment}
                </div>
            </div>
            <div class="ub option">
                <div class="title f-w">公用名称：</div>
                <div class="ub-f1">
                    ${sealApply.certificate.cerName}
                </div>
            </div>
            <div class="ub option">
                <div class="title f-w">证书状态：</div>
                <div class="ub-f1 c-base">
                    <c:choose>
                        <c:when test="${sealApply.certificate.fileState == '1'}">证书未生成</c:when>
                        <c:when test="${sealApply.certificate.fileState == '2'}">cer证书</c:when>
                        <c:when test="${sealApply.certificate.fileState == '3'}">pfx证书</c:when>
                        <c:when test="${sealApply.certificate.fileState == '4'}">pfx证书和cer证书</c:when>
                        <c:otherwise>未知</c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    $(function(){
        //授权种类选择框初始化
        var checkBox = GetProductInfoFromAuthNumber_1("${sealApply.fileTypeNum}");
        $("#fileType").text(checkBox);
    });

    $("#submit").click(function(){
        var submitDate = {
            "sealApplyId":$("#sealApplyId").val(),
            "sealStartTime":$("#sealStartTime").val(),
            "sealEndTime":$("#sealEndTime").val()

        };

        $.ajax({
            url: "${pageContext.request.contextPath}/review/review_delay_do.html",
            type: "post",
            data: submitDate,
            success: function (data) {
                var obj = eval('('+ data +')');
                if(obj.message == "success"){
                    $('body').dialogbox({
                        type:"normal",title:"审核操作成功",
                        buttons:[{
                            Text:"确认",
                            ClickToClose:true,
                            callback:function (dialog){
                                window.location.href = document.referrer;
                            }
                        }],
                        message:'申请信息已经审核完毕！'
                    });
                }else{
                    $('body').dialogbox({
                        type:"normal",title:"审核操作失败",
                        buttons:[{
                            Text:"确认",
                            ClickToClose:true,
                            callback:function (dialog){
                                // window.history.go(-1)
                            }
                        }],
                        message:'审核操作失败，请您重试！'
                    });
                }
            },
            error:function (data) {
                $('body').dialogbox({
                    type:"normal",title:"操作失败",
                    buttons:[{
                        Text:"确认",
                        ClickToClose:true,
                        callback:function (dialog){

                        }
                    }],
                    message:'您提交的信息有误！'
                });
            }
        });
    });

    $("#reject").click(function(){
        var txt=  "请输入驳回理由：";
        window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.input,{
            onOk:function(v){
                $.ajax({
                    url: "${pageContext.request.contextPath}/review/review_reject.html",
                    type: "get",
                    data: {"sealApplyId":"${sealApply.sealApplyId}","message":v},
                    success: function (data) {
                        var obj = eval('('+ data +')');
                        if(obj.message == "success"){
                            $('body').dialogbox({
                                type:"normal",title:"驳回成功",
                                buttons:[{
                                    Text:"确认",
                                    ClickToClose:true,
                                    callback:function (dialog){
                                        window.location.href = document.referrer;
                                    }
                                }],
                                message:'驳回成功，驳回理由将发送申请人！'
                            });
                        }else{
                            $('body').dialogbox({
                                type:"normal",title:"驳回失败",
                                buttons:[{
                                    Text:"确认",
                                    ClickToClose:true,
                                    callback:function (dialog){
                                        // window.history.go(-1)
                                    }
                                }],
                                message:'驳回信息提交失败，请您重试！'
                            });
                        }


                    }
                });
            }
        });
    });

    function GetProductInfoFromAuthNumber_1(iAuth){
        var sRet = "";
        if((iAuth & 1) != 0){
            sRet = sRet + "OFFICE 全文批注、" ;
        }
        if((iAuth & 2) != 0){
            sRet = sRet + "网页批注、" ;
        }
        if((iAuth & 4) != 0){
            sRet = sRet + "网页签章、" ;
        }
        if((iAuth & 8) != 0){
            sRet = sRet + "WORD签章、" ;
        }
        if((iAuth & 16) != 0){
            sRet = sRet + "EXCEL签章、" ;
        }
        if((iAuth & 32) != 0){
            sRet = sRet + "PDF签章、" ;
        }
        if((iAuth & 64) != 0){
            sRet = sRet + "中间件" ;
        }
        return sRet;
    }
</script>

</body></html>