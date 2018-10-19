
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
    <style type="text/css">

        .body-content{
            width: 40%;
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
        .rightContent{
            width: 40%;
            /*border: 1px #dedede solid;*/
            margin: 20px 40px;
            box-shadow: 3px -3px 3px #dedede, -3px 3px 3px #dedede, -3px -3px 3px #dedede;
            background-color: #f7f7f7;
        }
    </style>
    <script>
        $(function(){
            //授权种类选择框初始化
            var checkBox = GetProductInfoFromAuthNumber("${sealApply.fileTypeNum}");

            $("#fileType").text(checkBox);

            $("#submit").click(function(){

                var submitDate = {
                    "sealApplyId":$("#sealApplyId").val(),
                    "sealStartTime":$("#sealStartTime").val(),
                    "sealEndTime":$("#sealEndTime").val()
                };

                $.ajax({
                    url: "${pageContext.request.contextPath}/seal/seal_review_do.html",
                    type: "post",
                    data: submitDate,
                    dataType : "json",
                    success: function (data) {
                        $('body').dialogbox({
                            type:"normal",title:"操作成功",
                            buttons:[{
                                Text:"确认",
                                ClickToClose:true,
                                callback:function (dialog){
                                    window.location.href = document.referrer;
                                }
                            }],
                            message:'已审核通过该印章申请！'
                        });
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
                            url: "${pageContext.request.contextPath}/seal/seal_review_reject.html",
                            type: "get",
                            data: {"sealApplyId":"${sealApply.sealApplyId}","message":v},
                            success: function (data)
                            {
                                window.history.go(-2);
                            }
                        });
                    }
                });
            });
        });

        function getTheCheckBoxValue() {
            var test = $("input[name='sProblem']:checked");
            var checkBoxValue = "";
            var iAuth = 0;
            test.each(function () {
                var value = $(this).val();
                if(value.indexOf("office") != -1 && value.indexOf("annotation") != -1 )
                {
                    iAuth = iAuth | 1;
                }else if(value.indexOf("web") != -1 && value.indexOf("annotation") != -1 ){
                    iAuth = iAuth | 2;
                }else if(value.indexOf("ESSWebSign") != -1){
                    iAuth = iAuth | 4;
                }else if(value.indexOf("ESSWordSign") != -1){
                    iAuth = iAuth | 8;
                }else if(value.indexOf("ESSExcelSign") != -1){
                    iAuth = iAuth | 16;
                }else if(value.indexOf("ESSPdfSign") != -1){
                    iAuth = iAuth | 32;
                }else if(value.indexOf("ESSMidWare") != -1){
                    iAuth = iAuth | 64;
                }else{

                }
                // checkBoxValue += $(this).val() + "@";
            });

            return iAuth;
        }

        function GetProductInfoFromAuthNumber(iAuth){
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
        <div class="body-content">
            <form method="post" class="form-x" action="${pageContext.request.contextPath}/seal/seal_review_do.html" id="seal_review_from" enctype="multipart/form-data">
                <input type="hidden" id="sealApplyId" name="sealApplyId" value="${sealApply.sealApplyId}"/>
                <div class="ub option">
                    <div class="title f-w">所属单位：</div>
                    <div class="ub-f1">${sealApply.unit.unitName}</div>
                </div>
                <div class="ub option">
                    <div class="title f-w">印章名称：</div>
                    <div class="ub-f1" name="title">${sealApply.sealName}</div>
                </div>
                <div class="ub option">
                    <div class="title f-w">是否使用UK：</div>
                    <c:choose>
                        <c:when test="${sealApply.isUK == '1'}">
                            <div class="ub-f1" name="title">是</div>
                        </c:when>
                        <c:otherwise>
                            <div class="ub-f1" name="title">否</div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="ub option">
                    <div class="title f-w">印章图片：</div>
                    <div class="ub-f1" >
                        <img src="data:image/gif;base64,${sealApply.sealImg.sealThumbnailImgBase64}" width="70" height="70"/>
                    </div>
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
                    <div class="title f-w">授权种类：</div>
                    <div class="ub-f1" id="fileType">
                        ${sealApply.fileTypeNum}
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
            <input type="hidden" value="${sealApply.certificate.certificateId}"/>
            <div class="ub option">
                <div class="title f-w">签名算法：</div>
                <div class="ub-f1">${sealApply.certificate.algorithm}</div>
            </div>
            <div class="ub option">
                <div class="title f-w">证书用途：</div>
                <div class="ub-f1">${sealApply.certificate.cerClass}</div>
            </div>
            <div class="ub option">
                <div class="title f-w">证书所有人：</div>
                <div class="ub-f1">${sealApply.certificate.owner}</div>
            </div>
            <div class="ub option">
                <div class="title f-w">证书版本：</div>
                <div class="ub-f1">${sealApply.certificate.certificateVersion}</div>
            </div>
            <div class="ub option">
                <div class="title f-w">申请时间：</div>
                <div class="ub-f1">${sealApply.certificate.applyTime}</div>
            </div>
            <div class="ub option">
                <div class="title f-w">有效期起始时间：</div>
                    <div class="ub-f1">${sealApply.certificate.starTime}</div>
            </div>
            <div class="ub option">
                <div class="title f-w">有效期结束时间：</div>
                    <div class="ub-f1">${sealApply.certificate.endTime}</div>
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
</body>
</html>