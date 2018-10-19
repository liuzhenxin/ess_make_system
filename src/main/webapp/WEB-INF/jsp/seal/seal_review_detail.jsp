
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
                        <input type="date" id="sealStartTime" name="sealStartTime" value="" />
                    </div>
                </div>
                <div class="ub option">
                    <div class="title f-w">印章使用时间：</div>
                    <div class="field">
                        <select id="sealEndTimeSelect"  >
                            <option value="1">一年</option>
                            <option value="2">两年</option>
                            <option value="3">三年</option>
                            <option value="5">五年</option>
                            <option value="10">十年</option>
                            <option value="15">十五年</option>
                            <option value="20">二十年</option>
                        </select>
                        <input type="hidden" id="sealEndTime" name="sealEndTime" value="" />
                    </div>
                </div>
                <div class="ub option">
                    <div class="title f-w">
                        授权种类
                    </div>
                    <div class="field">
                        <c:forEach items="${fileTypeList}" var="item"  varStatus="status">
                            <input type="checkbox" value="${item.fileTypeValue}" name="sProblem">${item.fileTypeName}
                        </c:forEach>
                        <input type="hidden" name="fileTypeNum" id="fileTypeNum">
                        <br/>
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
                <div class="title f-w">证书版本：</div>
                <div class="ub-f1">${sealApply.certificate.certificateVersion}</div>
            </div>
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
                <div class="ub-f1">${sealApply.certificate.cerName}</div>
            </div>
            <div class="ub option">
                <div class="title f-w">申请时间：</div>
                <div class="ub-f1">${sealApply.certificate.applyTime}</div>
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
        var checkBoxArray = checkBox.split("@");
        for(var i=0;i<checkBoxArray.length;i++){
            $("input[name='sProblem']").each(function(){
                if($(this).val()==checkBoxArray[i]){
                    $(this).attr("checked","checked");
                }
            })
        };

        $("#submit").click(function(){
            var submitDate = {
                "sealApplyId":$("#sealApplyId").val(),
                "sealStartTime":$("#sealStartTime").val(),
                "sealEndTime":$("#sealEndTime").val(),
                "fileTypeNum":getTheCheckBoxValue()
            };
            $.ajax({
                url: "${pageContext.request.contextPath}/seal/seal_review_do.html",
                type: "post",
                data: submitDate,
                dataType : "json",
                success: function (data)
                {
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


        //时间模块初始化
        // 给input  date设置默认值
        var now = new Date();
        //格式化日，如果小于9，前面补0
        var day = ("0" + now.getDate()).slice(-2);
        //格式化月，如果小于9，前面补0
        var month = ("0" + (now.getMonth() + 1)).slice(-2);
        //拼装完整日期格式
        var today = now.getFullYear()+"-"+(month)+"-"+(day) ;
        //完成赋值
        $('#sealStartTime').val(today);
        var endTime = dateOperator(today,1,"+");
        $("#sealEndTime").val(endTime);

        $("#sealEndTimeSelect").change(function(){

            var starTime = $("#sealStartTime").val();
            var year = $("#sealEndTimeSelect").val();
            var endTime = dateOperator(starTime,year,"+");
            $("#sealEndTime").val(endTime);
        });
        $("#sealStartTime").change(function(){
            var starTime = $("#sealStartTime").val();
            var year = $("#sealEndTimeSelect").val();
            var endTime = dateOperator(starTime,year,"+");
            $("#sealEndTime").val(endTime);
        });

    });

    //返回记录 提示
    function msgProcess(data){
        if(data&&data=='success'){
            // top.$.jBox.tip.mess=;
            //删除成功后重新加载表格当前页
            // top.$.jBox.tip("申请成功",'success',{persistent:true,opacity:0});
            alert("审核通过操作成功");
            window.history.go(-2);
        }else{
            // top.$.jBox.tip("操作失败",'error',{persistent:true,opacity:0});
            alert(data);
        }
    }
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

        });

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