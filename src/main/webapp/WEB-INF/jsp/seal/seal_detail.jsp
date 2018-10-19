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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/jquery-dialogbox/jquery.dialogbox-1.0.css">


    <script src="${pageContext.request.contextPath}/js/jquery-3.2.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/pintuer.js"></script>
    <script src="${pageContext.request.contextPath}/js/my.js"></script>
    <script src="${pageContext.request.contextPath}/jquery-dialogbox/jquery.dialogbox-1.0.js"></script>


    <%--弹框插件--%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/xcConfirm/css/xcConfirm.css"/>
    <script src="${pageContext.request.contextPath}/xcConfirm/js/xcConfirm.js" type="text/javascript" charset="utf-8"></script>



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

        .w10{
            width: 100px;
            float:left;
        }

        /*弹框样式*/
        .sgBtn{width: 135px; height: 35px; line-height: 35px; margin-left: 10px; margin-top: 10px; text-align: center;
            background-color: #0095D9; color: #FFFFFF; float: left; border-radius: 5px;}
        .rightContent{
            width: 40%;
            /*border: 1px #dedede solid;*/
            margin: 20px 40px;
            box-shadow: 3px -3px 3px #dedede, -3px 3px 3px #dedede, -3px -3px 3px #dedede;
            background-color: #f7f7f7;
        }


    </style>
</head>
<body>


<div class="panel admin-panel">
    <div class="panel-head" id="add"><strong><span class="icon-pencil-square-o"></span>制作印章</strong></div>
    <div class="padding border-bottom">
        <ul class="search" style="padding-left:10px;">

            <li><a class="button border-main icon-plus-square-o" href="javaScript:window.location.href = document.referrer;"> 返回</a></li>
        </ul>
    </div>
    <div class="ub">
        <div class="body-content  f-s-14 c-6">
            <form method="post" class="form-x" action="${pageContext.request.contextPath}/seal/seal_make_do.html" id="seal_review_from" enctype="multipart/form-data">

                <div class="ub option">
                    <div class="title f-w">所属单位：</div>
                    <div class="ub-f1" id="unitName">${seal.unit.unitName}</div>
                </div>
                <div class="ub option">
                    <div class="title f-w">印章名称：</div>
                    <div class="ub-f1" id="sealName">${seal.sealName}</div>
                </div>
                <div class="ub option">
                    <div class="title f-w">是否使用UK：</div>
                    <c:choose>
                        <c:when test="${seal.isUK == '1'}">
                            <div class="ub-f1" id="isUK">是</div>
                        </c:when>
                        <c:otherwise>
                            <div class="ub-f1" id="isUK">否</div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="ub option">
                    <div class="title f-w">印章图片：</div>
                    <div class="ub-f1" >
                        <img src="data:image/gif;base64,${seal.sealImg.sealThumbnailImgBase64}" width="70" height="70"/>
                    </div>
                </div>
                <div class="ub option">
                    <div class="title f-w">印章起始时间：</div>

                    <div class="ub-f1" id="sealStartTime" >${seal.sealStartTime}</div>

                </div>
                <div class="ub option">
                    <div class="title f-w">印章终止时间：</div>

                    <div class="ub-f1" id="sealEndTime">${seal.sealEndTime}</div>

                </div>
                <div class="ub option">
                    <div class="title f-w">
                        授权种类：
                    </div>
                    <div class="ub-f1" id="fileType" ></div>
                </div>

            </form>
        </div>


        <div class="rightContent ub-f1 ">
            <input type="hidden" id="certificateId" name="certificateId" value="${seal.certificate.certificateId}"/>
            <div class="ub option">
                <div class="title f-w">国家：</div>
                <div class="ub-f1" id="country">
                    ${seal.certificate.country}
                </div>
            </div>
            <div class="ub option">
                <div class="title f-w">省份：</div>
                <div class="ub-f1" id="province">
                    ${seal.certificate.province}
                </div>
            </div>
            <div class="ub option">
                <div class="title f-w">城市：</div>
                <div class="ub-f1" id="city">
                    ${seal.certificate.city}
                </div>
            </div>
            <div class="ub option">
                <div class="title f-w">单位：</div>
                <div class="ub-f1" id="certUnit">
                    ${seal.certificate.certUnit}
                </div>
            </div>
            <div class="ub option">
                <div class="title f-w">部门：</div>
                <div class="ub-f1" id="certDepartment">
                    ${seal.certificate.certDepartment}
                </div>
            </div>
            <div class="ub option">
                <div class="title f-w">公用名称：</div>
                <div class="ub-f1" id="cerName">
                    ${seal.certificate.cerName}
                </div>
            </div>
            <div class="ub option">
                <div class="title f-w">申请时间：</div>
                <div class="ub-f1" id="applyTime">${seal.certificate.applyTime}</div>
            </div>
            <div class="ub option">
                <div class="title f-w">有效期起始时间：</div>

                <div class="ub-f1" id="StarTime">${seal.certificate.startTime}</div>

            </div>
            <div class="ub option">
                <div class="title f-w">有效期结束时间：</div>

                <div class="ub-f1" id="endTime">${seal.certificate.endTime}</div>

            </div>
            <div class="ub option">
                <div class="title f-w">证书状态：</div>
                <div class="ub-f1 c-base">
                    <c:choose>
                        <c:when test="${seal.certificate.fileState == '1'}">证书未生成</c:when>
                        <c:when test="${seal.certificate.fileState == '2'}">cer证书</c:when>
                        <c:when test="${seal.certificate.fileState == '3'}">pfx证书</c:when>
                        <c:when test="${seal.certificate.fileState == '4'}">pfx证书和cer证书</c:when>
                        <c:otherwise>未知</c:otherwise>
                    </c:choose>
                </div>
            </div>
            <div class="ub option">
                <div class="title"></div>
                <div class="ub-f1">
                    <a class="button border-main icon-plus-square-o" href="${pageContext.request.contextPath}/make/cer_delay.html?certificateId=${seal.certificate.certificateId}"> 证书延期</a>
                </div>
            </div>
            <c:choose>

                <c:when test="${seal.certificate.fileState == '4' || seal.certificate.fileState == '3'}">
                    <c:if test="delayButton">
                        <div class="ub option">
                            <div class="title"></div>
                            <div class="ub-f1">
                                <a class="button border-main icon-plus-square-o" href="${pageContext.request.contextPath}/make/cer_delay.html?certificateId=${seal.certificate.certificateId}"> 证书延期</a>
                            </div>
                        </div>
                    </c:if>
                </c:when>
            </c:choose>
        </div>
    </div>
</div>
<script>
    $(function(){

        //授权种类选择框初始化
        var checkBox = GetProductInfoFromAuthNumber("${seal.fileTypeNum}");
        $("#fileType").text(checkBox);
    });

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
</body></html>