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
            width: 60%;
            margin: 20px 0px;
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
            <span class="icon-pencil-square-o"></span> 制作印章-
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
        <div class="body-content  f-s-14 c-6">
            <form method="post" class="form-x" action="${pageContext.request.contextPath}/seal/seal_make_do.html" id="seal_review_from" enctype="multipart/form-data">
                <input type="hidden" id="sealApplyId" name="sealApplyId" value="${sealApply.sealApplyId}"/>
                <input type="hidden" id="cerFileState" name="cerFileState" value="${sealApply.certificate.fileState}"/>
                <input type="hidden" id="applyType" name="applyType" value="${sealApply.applyType}"/>
                <input type="hidden" id="sealId" name="sealId" value="${sealApply.sealId}"/>
                <input type="hidden" id="applyUserId" name="applyUserId" value="${sealApply.applyUserId}"/>
                <div class="ub option">
                    <div class="title f-w">所属单位：</div>
                    <div class="ub-f1" id="unitName">${sealApply.unit.unitName}</div>
                </div>
                <div class="ub option">
                    <div class="title f-w">印章名称：</div>
                    <div class="ub-f1" id="sealName">${sealApply.sealName}</div>
                </div>
                <div class="ub option">
                    <div class="title f-w">是否使用UK：</div>
                    <c:choose>
                        <c:when test="${sealApply.isUK == '1'}">
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
                        <img src="data:image/gif;base64,${sealApply.sealImg.sealThumbnailImgBase64}" width="70" height="70"/>
                    </div>
                </div>
                <div class="ub option">
                    <div class="title f-w">印章起始时间：</div>
                    <div class="ub-f1">
                        <input type="date" id="sealStartTime" name="sealStartTime"  value="${sealApply.sealStartTime}" readonly="readonly"/>
                    </div>
                </div>
                <div class="ub option">
                    <div class="title f-w">印章终止时间：</div>
                    <div class="ub-f1" name="sealEndTime">
                        <input type="date" id="sealEndTime" name="sealEndTime"  value="${sealApply.sealEndTime}" readonly="readonly"/>
                    </div>
                </div>
                <div class="ub option">
                    <div class="title f-w">
                        授权种类：
                    </div>
                    <div class="field"  id="fileTypeNum">
                    </div>
                </div>
                <c:choose>
                    <c:when test="${sealApply.certificate.fileState == 1}">
                        <div class="ub option">
                            <div class="title f-w">选择证书颁发机构：</div>
                            <div class="ub-f1" name="cerIssuer">
                                <select id="cerIssuer" name="cerIssuer" class="w10">
                                    <c:forEach items="${issuerUnitList}" var="item"  varStatus="status">
                                        <option value="${item.issuerUnitId}">${item.issuerUnitName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </c:when>
                    <c:when test="${sealApply.certificate.fileState == 2 || sealApply.certificate.fileState == 3}">
                        <div class="ub option">
                            <div class="title f-w">证书颁发机构：</div>
                            <div class="ub-f1" name="cerIssuer">${sealApply.cerIssuer}</div>
                        </div>
                    </c:when>
                    <c:when test="${sealApply.certificate.fileState == 4}">
                    </c:when>
                </c:choose>
                <div class="form-group">
                    <div class="label">
                        <label></label>
                    </div>
                    <div class="field">
                        <button type="button" class="button bg-main icon-check-square-o" onclick="sealInfoSubmit()">提交信息</button>
                        <button type="button" class="button bg-main icon-check-square-o" onclick="reject()">驳回</button>
                    </div>
                </div>
            </form>
        </div>

        <div class="rightContent ub-f1 ">
            <input type="hidden" id="certificateId" name="certificateId" value="${sealApply.certificate.certificateId}"/>
            <div class="ub option">
                <div class="title f-w">签名算法：</div>
                <div class="ub-f1" id="algorithm">${sealApply.certificate.algorithm}</div>
            </div>
            <div class="ub option">
                <div class="title f-w">证书用途：</div>
                <div class="ub-f1" id="cerClass">${sealApply.certificate.cerClass}</div>
            </div>
            <div class="ub option">
                <div class="title f-w">证书所有人：</div>
                <div class="ub-f1" id="owner">${sealApply.certificate.owner}</div>
            </div>
            <div class="ub option">
                <div class="title f-w">证书版本：</div>
                <div class="ub-f1" id="certificateVersion">${sealApply.certificate.certificateVersion}</div>
            </div>
            <div class="ub option">
                <div class="title f-w">申请时间：</div>
                <div class="ub-f1" id="applyTime">${sealApply.certificate.applyTime}</div>
            </div>
            <div class="ub option">
                <div class="title f-w">有效期起始时间：</div>
                <div class="ub-f1">
                    <input type="date" id="StarTime" name="StarTime" value="${sealApply.certificate.starTime}" readonly="readonly"/>
                </div>
            </div>
            <div class="ub option">
                <div class="title f-w">有效期结束时间：</div>
                <div class="ub-f1">
                    <input type="date" id="endTime" name="StarTime" value="${sealApply.certificate.endTime}" readonly="readonly"/>
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
        var checkBox = GetProductInfoFromAuthNumber("${sealApply.fileTypeNum}");
        $("#fileTypeNum").text(checkBox);




        $("#endTimeSelect").change(function(){

            var starTime = $("#StarTime").val();
            var year = $("#endTimeSelect").val();
            var endTime = dateOperator(starTime,year,"+");
            $("#endTime").val(endTime);
        });

        $("#StarTime").change(function(){

            var starTime = $("#StarTime").val();
            var year = $("#endTimeSelect").val();
            var endTime = dateOperator(starTime,year,"+");
            $("#endTime").val(endTime);
        });

    });

    //对时间进行年的加减操作
    function dateOperator(date,years) {
        date = date.replace(/-/g,"/"); //更改日期格式
        var nd = new Date(date);

        nd.setFullYear(nd.getFullYear()+parseInt(years));

        var y = nd.getFullYear();
        var m = nd.getMonth()+1;
        var d = nd.getDate();
        if(m <= 9) m = "0"+m;
        if(d <= 9) d = "0"+d;
        var cdate = y+"-"+m+"-"+d;
        return cdate;
    }

    function getTheCheckBoxValue() {
        var test = $("input[name='sProblem']:checked");
        var checkBoxValue = "";
        test.each(function () {
            checkBoxValue += $(this).val() + "@";
        });
        checkBoxValue = checkBoxValue.substring(0, checkBoxValue.length - 1);
        return checkBoxValue;
    }

    function reject(){
        var txt=  "请输入驳回理由：";
        window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.input,{
            onOk:function(v){
                $.ajax({
                    url: "${pageContext.request.contextPath}/seal/seal_make_reject.html",
                    type: "get",
                    data: {"sealApplyId":"${sealApply.sealApplyId}","message":v},
                    success: function (data)
                    {
                        window.history.go(-2);
                    }
                });
            }
        });
    }



    function sealInfoSubmit(){
        $('body').dialogbox({
            type:"normal",title:"确认您提交的信息",
            buttons:[{
                Text:"确认制作",
                ClickToClose:true,
                callback:function (dialog){
                    var submitDate = {
                        "sealApplyId":$("#sealApplyId").val(),
                        "sealStartTime":$("#sealStartTime").val()

                    };
                    $.ajax({
                        // url: "/seal/seal_make_do.html",
                        url: "${pageContext.request.contextPath}/seal/seal_make_do.html",
                        type: "post",
                        data: submitDate,
                        dataType : "json",
                        success: function (data)
                        {
                            var json_str = JSON.stringify(data);
                            $('body').dialogbox({
                                type:"normal",title:"印章制作成功",
                                buttons:[{
                                    Text:"确认",
                                    ClickToClose:true,
                                    callback:function (dialog){
                                        window.location.href = document.referrer;
                                    }
                                }],
                                message:'已根据您提交的信息成功制作印章！'
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

                }
            },
                {
                    Text:"返回修改",
                    ClickToClose:true,
                    callback:function (dialog){
                        // nothing
                    }
                }
            ],

            message:getDioMessage()
        });
    }



    /**
     * 检查输入值是否为空
     * @param data
     * @returns {boolean}
     */
    function isNull(val) {

        // var str = val.replace(/\s+/g,"");

        if (val == '' || val == undefined || val == null) {
            //空
            return true;
        } else {
            // 非空
            return false;
        }
    }

    //返回记录 提示
    function msgProcess(data){
        if(data&&data=='success'){
            // top.$.jBox.tip.mess=;
            //删除成功后重新加载表格当前页
            // top.$.jBox.tip("申请成功",'success',{persistent:true,opacity:0});
            alert("操作成功");
            window.history.go(-2);
        }else{
            // top.$.jBox.tip("操作失败",'error',{persistent:true,opacity:0});
            alert(data);
        }
    }

    $(function(){
        /**
         * 根据已有信息设置选择框默认值
         */

        var numbers = $("#isUK").find("option"); //获取select下拉框的所有值
        for (var j = 1; j < numbers.length; j++) {
            if ($(numbers[j]).val() == ${sealApply.isUK}) {
                $(numbers[j]).attr("selected", "selected");
            }
        }
    });

    //返回记录 提示
    function getDioMessage(){

        var arr=new Array();
        arr.push("<table class=\"table table-hover \">");
        arr.push("<tr><th width=\"200px\">印章信息</th><th width=\"200px\">证书信息</th></tr>");
        arr.push("<tr><td>单位：");
        arr.push($("#unitName").text());
        arr.push("</td><td>签名算法：");
        arr.push($("#algorithm").text());
        arr.push("</td></tr>");

        arr.push("<tr><td>名称：");
        arr.push($("#sealName").text());
        arr.push("</td><td>证书用途：");
        arr.push($("#cerClass").text());
        arr.push("</td></tr>");

        arr.push("<tr><td>使用UK：");
        arr.push($("#isUK").text());
        arr.push("</td><td>证书所有人：");
        arr.push($("#owner").text());
        arr.push("</td></tr>");

        arr.push("<tr><td>印章起始时间：");
        arr.push($("#sealStartTime").val());
        arr.push("</td><td>证书版本：");
        arr.push($("#certificateVersion").text());
        arr.push("</td></tr>");

        arr.push("<tr><td>印章终止时间：");
        arr.push($("#sealEndTime").val());
        arr.push("</td><td>证书申请时间：");
        arr.push($("#applyTime").text());
        arr.push("</td></tr>");

        arr.push("<tr><td>授权种类：");
        arr.push($("#fileTypeNum").text());
        arr.push("</td><td>有效期起始时间：");
        arr.push($("#StarTime").val());
        arr.push("</td></tr>");

        arr.push("<tr><td>证书颁发机构：");
        arr.push($("#cerIssuer option:selected").text());
        arr.push("</td><td>有效期结束时间：");
        arr.push($("#endTime").val());
        arr.push("</td></tr>");

        arr.push("</table>");
        var message=arr.join("");

        return message;
    }

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

        // checkBoxValue = checkBoxValue.substring(0, checkBoxValue.length - 1);
        return iAuth;
    }

    function GetProductInfoFromAuthNumber(iAuth){
        var sRet = "";
        if((iAuth & 1) != 0){
            sRet = sRet + "office annotation@" ;
        }
        if((iAuth & 2) != 0){
            sRet = sRet + "web annotation@" ;
        }
        if((iAuth & 4) != 0){
            sRet = sRet + "ESSWebSign@" ;
        }
        if((iAuth & 8) != 0){
            sRet = sRet + "ESSWordSign@" ;
        }
        if((iAuth & 16) != 0){
            sRet = sRet + "ESSExcelSign@" ;
        }
        if((iAuth & 32) != 0){
            sRet = sRet + "ESSPdfSign@" ;
        }
        if((iAuth & 64) != 0){
            sRet = sRet + "ESSMidWare@" ;
        }
        return sRet;
    }


</script>

</body></html>