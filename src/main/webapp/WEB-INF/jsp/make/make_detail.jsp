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
        /*上传框样式*/
        .uploader { position:relative; display:inline-block; overflow:hidden; cursor:default; padding:0;
            margin:10px 0px; -moz-box-shadow:0px 0px 5px #ddd; -webkit-box-shadow:0px 0px 5px #ddd;
            box-shadow:0px 0px 5px #ddd; -moz-border-radius:5px; -webkit-border-radius:5px; border-radius:5px; }
        .filename { float:left; display:inline-block; outline:0 none; height:32px; width:180px; margin:0; padding:8px 10px; overflow:hidden; cursor:default; border:1px solid; border-right:0; font:9pt/100% Arial, Helvetica, sans-serif; color:#777; text-shadow:1px 1px 0px #fff; text-overflow:ellipsis; white-space:nowrap; -moz-border-radius:5px 0px 0px 5px; -webkit-border-radius:5px 0px 0px 5px; border-radius:5px 0px 0px 5px; background:#f5f5f5; background:-moz-linear-gradient(top, #fafafa 0%, #eee 100%); background:-webkit-gradient(linear, left top, left bottom, color-stop(0%, #fafafa), color-stop(100%, #f5f5f5)); filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#fafafa', endColorstr='#f5f5f5', GradientType=0);
            border-color:#ccc; -moz-box-shadow:0px 0px 1px #fff inset; -webkit-box-shadow:0px 0px 1px #fff inset; box-shadow:0px 0px 1px #fff inset; -moz-box-sizing:border-box; -webkit-box-sizing:border-box; box-sizing:border-box; }
        .button1 { float:left; height:32px; display:inline-block; outline:0 none; padding:8px 12px; margin:0; cursor:pointer; border:1px solid; font:bold 9pt/100% Arial, Helvetica, sans-serif; -moz-border-radius:0px 5px 5px 0px; -webkit-border-radius:0px 5px 5px 0px; border-radius:0px 5px 5px 0px; -moz-box-shadow:0px 0px 1px #fff inset; -webkit-box-shadow:0px 0px 1px #fff inset; box-shadow:0px 0px 1px #fff inset; }
        .uploader input[type=file] { position:absolute; top:0; right:0; bottom:0; border:0; padding:0; margin:0;
            height:30px; cursor:pointer; filter:alpha(opacity=0); -moz-opacity:0; -khtml-opacity: 0; opacity:0; }
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
            <form method="post" class="form-x" action="${pageContext.request.contextPath}/make/make_do.html" id="make_from" enctype="multipart/form-data">
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
                    <div class="title f-w">查看附件：</div>
                    <div class="ub-f1" >
                        <a href="${pageContext.request.contextPath}/attachment.html?sealApplyId=${sealApply.sealApplyId}">
                            <span>点击下载</span>
                        </a>
                    </div>
                </div>
                <div class="ub option">
                    <div class="title f-w">印章图片：</div>
                    <div class="ub-f1" >
                        <div id="gif_show" style="float:left"></div>
                        <div id="jpg_show" style="float:left"></div>
                    </div>
                </div>
                <div class="ub option">
                    <div class="title f-w">上传图片：</div>
                    <div class="ub-f1">
                        <div class="uploader white">
                            <input type="button" name="file"  value="上传GIF标准图"/>
                            <input type="file" id="gifImg" name="gifImg"/>
                        </div>
                        <div class="uploader white">
                            <input type="button" name="file"  value="上传JPG高清图"/>
                            <input type="file" id="jpgImg"  name="jpgImg"/>
                        </div>
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
                    <div class="field"  id="fileType">

                    </div>
                </div>
                <c:choose>
                    <c:when test="${sealApply.isUK == '1'}">
                        <div class="ub option">
                            <div class="title f-w">选择UK类型：</div>
                            <div class="ub-f1">
                                <select id="cardType" name="cardType" class="w10">
                                    <option value="FTCX">飞天诚信</option>
                                </select>
                            </div>
                        </div>
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
                <div class="title f-w">国家：</div>
                <div class="ub-f1">
                    <input type="text" id="country" name="country" value="${sealApply.certificate.country}">
                </div>
            </div>
            <div class="ub option">
                <div class="title f-w">省份：</div>
                <div class="ub-f1">
                    <input type="text" id="province" name="province" value="${sealApply.certificate.province}">
                </div>
            </div>
            <div class="ub option">
                <div class="title f-w">城市：</div>
                <div class="ub-f1">
                    <input type="text" id="city" name="city" value="${sealApply.certificate.city}">
                </div>
            </div>
            <div class="ub option">
                <div class="title f-w">单位：</div>
                <div class="ub-f1">
                    <input type="text" id="certUnit" name="certUnit" value="${sealApply.certificate.certUnit}">
                </div>
            </div>
            <div class="ub option">
                <div class="title f-w">部门：</div>
                <div class="ub-f1">
                    <input type="text" id="certDepartment" name="certDepartment" value="${sealApply.certificate.certDepartment}">
                </div>
            </div>
            <div class="ub option">
                <div class="title f-w">公用名称：</div>
                <div class="ub-f1">
                    <input type="text" id="cerName" name="cerName" value="${sealApply.certificate.cerName}">
                </div>
            </div>
            <div class="ub option">
                <div class="title f-w">申请时间：</div>
                <div class="ub-f1" id="applyTime">${sealApply.certificate.applyTime}</div>
            </div>
            <c:choose>
                <c:when test="${sealApply.certificate.fileState == 1}">
                    <div class="ub option">
                        <div class="title f-w">有效期起始时间：</div>
                        <div class="ub-f1">
                            <input type="date" id="startTime" name="startTime" value="${sealApply.certificate.startTime}" />
                        </div>
                    </div>
                    <div class="ub option">
                        <div class="title f-w">有效期结束时间：</div>
                        <div class="field">
                            <select id="endTimeSelect">
                                <option value="1">一年</option>
                                <option value="2">两年</option>
                                <option value="3">三年</option>
                                <option value="5">五年</option>
                                <option value="10">十年</option>
                                <option value="15">十五年</option>
                                <option value="20">二十年</option>
                            </select>
                            <input type="hidden" id="endTime" name="endTime"  />
                        </div>
                    </div>
                </c:when>

                <c:when test="${sealApply.certificate.fileState == 4}">

                </c:when>
            </c:choose>

            <c:choose>
                <c:when test="${sealApply.certificate.fileState == 1}">
                    <div class="ub option">
                        <div class="title f-w">选择证书颁发机构：</div>
                        <div class="ub-f1" >
                            <select id="cerIssuer" name="cerIssuer" class="w10">
                                <c:forEach items="${issuerUnitList}" var="item"  varStatus="status">
                                    <option value="${item.issuerUnitId}">${item.issuerUnitName}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </c:when>
                <c:when test="${sealApply.certificate.fileState == 2}">
                    <div class="ub option">
                        <div class="title f-w">选择证书颁发机构：</div>
                        <div class="ub-f1" >${sealApply.cerIssuer}</div>
                    </div>
                </c:when>
                <c:when test="${sealApply.certificate.fileState == 4}">
                </c:when>
            </c:choose>
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
        /**
         * 根据已有信息设置选择框默认值
         */
        <%--var numbers = $("#isUK").find("option"); //获取select下拉框的所有值--%>
        <%--for (var j = 1; j < numbers.length; j++) {--%>
            <%--if ($(numbers[j]).val() == ${sealApply.isUK}) {--%>
                <%--$(numbers[j]).attr("selected", "selected");--%>
            <%--}--%>
        <%--}--%>


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
        $('#startTime').val(today);
        var endTime = dateOperator(today,1,"+");
        $("#endTime").val(endTime);

        $("#endTimeSelect").change(function(){

            var startTime = $("#startTime").val();
            var year = $("#endTimeSelect").val();
            var endTime = dateOperator(startTime,year,"+");
            $("#endTime").val(endTime);
        });

        $("#startTime").change(function(){

            var startTime = $("#startTime").val();
            var year = $("#endTimeSelect").val();
            var endTime = dateOperator(startTime,year,"+");
            $("#endTime").val(endTime);
        });

    });


    function reject(){
        var txt=  "请输入驳回理由：";
        window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.input,{
            onOk:function(v){
                $.ajax({
                    url: "${pageContext.request.contextPath}/make/make_reject.html",
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
    }

    function sealInfoSubmit(){
        $('body').dialogbox({
            type:"normal",title:"确认您提交的信息",
            buttons:[{
                Text:"确认制作",
                ClickToClose:true,
                callback:function (dialog){
                    var formData = new FormData();
                    var id = $('#cerIssuer').val();
                    formData.append('sealApplyId', $('#sealApplyId').val());
                    formData.append('gifImg', $('#gifImg')[0].files[0]);
                    formData.append('jpgImg', $('#jpgImg')[0].files[0]);
                    formData.append('country', $('#country').val());
                    formData.append('province', $('#province').val());
                    formData.append('city', $('#city').val());
                    formData.append('certUnit', $('#certUnit').val());
                    formData.append('certDepartment', $('#certDepartment').val());
                    formData.append('cerName', $('#cerName').val());
                    formData.append('startTime', $('#startTime').val());
                    formData.append('endTime', $('#endTime').val());
                    formData.append('cerIssuer', $('#cerIssuer').val());
                    formData.append('cardType', $('#cardType').val());
                    $.ajax({
                        url: "${pageContext.request.contextPath}/make/make_new_do.html",
                        type: "post",
                        data: formData,
                        cache: false,
                        processData: false,
                        contentType: false,
                        success: function (data) {
                            var obj = eval('('+ data +')');
                            if(obj.message=="success"){
                                if($("#isUK").text()=="是"){
                                    CreateNewCert(obj.body.sealImgId);
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
                                        message:"印章制作成功！"
                                    });
                                }
                            }else{
                                $('body').dialogbox({
                                    type:"normal",title:"系统提示",
                                    buttons:[{
                                        Text:"确认",
                                        ClickToClose:true,
                                        callback:function (dialog){
                                        }
                                    }],
                                    message:"服务器数据处理发生错误！请检查后重试！"
                                });
                            }

                        },
                        error:function (data) {
                            $('body').dialogbox({
                                type:"normal",title:"系统提示",
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

    function CreateNewCert(sealImgId){
        //UK执行写入
        var result = "";
        //传入信息：设备类型，颁发者ID，图片ID,图片类型，授权产品，授权到期时间
        var paramter = "";
        // 设备类型
        paramter += "CARDTYPE="+$("#cardType").val();
        //证书ID
        paramter += "&CERTID="+$("#certificateId").val();
        //图片ID
        paramter += "&TOKEN="+sealImgId;
        //图片类型
        paramter += "&SEALTYPE="+"gif";
        //授权产品
        paramter += "&PRODUCTS="+GetProductInfoFromAuthNumber("${sealApply.fileTypeNum}");
        //授权到期时间
        paramter += "&PRODUCTEND="+replaceAll($("#sealEndTime").val(),"-","");
        //是否写图片
        paramter += "&WP=YES";
        //是否写证书
        paramter += "&WC=YES";
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
                    var keyId = data[0].info+"";
                    //更新keyId
                    $.ajax({
                        url: "${pageContext.request.contextPath}/make/updateSealForKeyId.html",
                        type: "post",
                        data: {"sealId":$("#sealId").val(),"keyId":keyId},
                        success: function (data) {
                            var obj = eval('('+ data +')');
                            if(obj.message=="success"){
                                $('body').dialogbox({
                                    type:"normal",title:"系统提示",
                                    buttons:[{
                                        Text:"确认",
                                        ClickToClose:true,
                                        callback:function (dialog){
                                            window.location.href = document.referrer;
                                        }
                                    }],
                                    message:"印章制作成功！"
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
                                    message:"印章制作失败！原因为UK匹配，请联系管理员！"
                                });
                            }
                        }
                    });
                    result="OK"
                }else if(data[0].status == 1){
                    result = "请登录安全客户端"
                }else if(data[0].status == 2){
                    result = "发生错误"
                } else{
                    result = "未知错误"+data[0].status;
                }
                //返回结果
                if(result!="OK"){
                    $.ajax({
                        url: "${pageContext.request.contextPath}/make/sealApplyFail.html",
                        type: "post",
                        data: {"sealApplyId":$("#sealApplyId").val()},
                        dataType : "json",
                        success: function (data) {
                        }
                    });
                    //服务器成功，UK失败
                    $('body').dialogbox({
                        type:"normal",title:"UK操作失败",
                        buttons:[{
                            Text:"确认",
                            ClickToClose:true,
                            callback:function (dialog){
                                window.location.href = document.referrer;
                            }
                        }],
                        message:result
                    });

                }
            }
        });
    }

    //上传图片 转换base64 显示。
    $("#gifImg").change(function(){
        //清空图片内容
        $("#gif_show").empty();
        var fl=this.files.length;
        for(var i=0;i<fl;i++){
            var file = this.files[i];
            //上传文件格式
            var imgFileType = file.name.toLowerCase().split('.').splice(-1);
            var imgFileSize = file.size/1024 + 'kb';
            if("gif" == imgFileType){
                var reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = function(e){
                    var imgstr='<img style="width:70px;height:70px;" src="'+e.target.result+'"/>';
                    $("#gif_show").append(imgstr)
                };
            }else{
                alert("文件格式错误，请重新 上传");
                //重置file Input 值
                file.value = '';
            }
        }
    });
    //上传图片 转换base64 显示。
    $("#jpgImg").change(function(){
        //清空图片内容
        $("#jpg_show").empty();
        var fl=this.files.length;
        for(var i=0;i<fl;i++){
            var file = this.files[i];
            //上传文件格式
            var imgFileType = file.name.toLowerCase().split('.').splice(-1);
            if("jpg" == imgFileType){
                var reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = function(e){
                    var imgstr='<img style="width:70px;height:70px;" src="'+e.target.result+'"/>';
                    $("#jpg_show").append(imgstr)
                };
            }else{
                alert("文件格式错误，请重新 上传");
                //重置file Input 值
                file.value = '';
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
        arr.push("</td><td>所在国家：");
        arr.push($("#country").val());
        arr.push("</td></tr>");

        arr.push("<tr><td>名称：");
        arr.push($("#sealName").text());
        arr.push("</td><td>所在省份：");
        arr.push($("#province").val());
        arr.push("</td></tr>");

        arr.push("<tr><td>使用UK：");
        arr.push($("#isUK").text());
        arr.push("</td><td>所在城市：");
        arr.push($("#city").val());
        arr.push("</td></tr>");

        arr.push("<tr><td>印章起始时间：");
        arr.push($("#sealStartTime").val());
        arr.push("</td><td>单位名称：");
        arr.push($("#certUnit").val());
        arr.push("</td></tr>");

        arr.push("<tr><td>印章终止时间：");
        arr.push($("#sealEndTime").val());
        arr.push("</td><td>部门名称：");
        arr.push($("#certDepartment").val());
        arr.push("</td></tr>");


        arr.push("<tr><td>授权种类：");
        arr.push($("#fileType").text());
        arr.push("</td><td>公用名称：");
        arr.push($("#cerName").val());
        arr.push("</td></tr>");

        arr.push("<tr><td>证书颁发机构：");
        arr.push($("#cerIssuer option:selected").text());
        arr.push("</td><td>有效期起始时间：");
        arr.push($("#startTime").val());
        arr.push("</td></tr>");

        arr.push("<tr><td>");
        arr.push();
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
        });

        return iAuth;
    }
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