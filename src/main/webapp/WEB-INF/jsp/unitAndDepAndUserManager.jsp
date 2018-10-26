<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta name="renderer" content="webkit">
    <title>ESS印章制作系统</title>  <!--  -->
    <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath }/images/icon.ico" media="screen" /> 
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pintuer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    
    <!--导航条插件样式-->
	<link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/font-awesome.min.css" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/ace.min.css" />
	<style>
		.ace-nav {
			height: auto;
		}
		.admin {
			position: relative;
			top:0;
			left: auto;
		}
		select{
			height: 45px;
			color: #fff !important;
			background-color: #62a8d1 !important;
			border: 0 solid #d5d5d5 !important;
			border-radius: 0 !important;
		}
		select:active{
			height: 45px;
			color: #fff !important;
			background-color: #62a8d1 !important;
			border: 0 solid #d5d5d5 !important;
			border-radius: 0 !important;
		}
		select:hover{
			height: 45px;
			color: #fff !important;
			background-color: #62a8d1 !important;
			border: 0 solid #d5d5d5 !important;
			border-radius: 0 !important;
		}
		span{
			font-size: 14px !important;
		}
		.ztree li a.curSelectedNode {
			background-color: #dedede !important;
			color: #333 !important;
			height: 28px;
			border: 1px #dedede solid !important;
			opacity: 0.8 !important;
			padding: 5px 10px !important;
			border-radius: 4px !important;
		}
		.ztree li a {
			padding: 5px 10px !important;
			margin: 0 !important;
			cursor: pointer !important;
			height: 30px !important;
			color: #333 !important;
		}
		.ztree li span.button.switch {
			margin-top: 4px !important;
		}
		.ztree li span.button.ico_docu {
			margin-right: 5px !important;
		}
		.homeIconSize{
			width: 17px;
			height: 18px;
		}
		
		li{
			list-style-type: none !important;
		}
		
	</style>

	<script src="${pageContext.request.contextPath}/js/jquery-2.0.3.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>

	<%--<link rel="stylesheet" href="${pageContext.request.contextPath}/css/demo.css" type="text/css">--%>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/zTreeStyle/zTreeStyle.css" type="text/css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.ztree.core-3.5.js"></script>
	
	<!-- 弹框的相关文件 -->
	<link href="${pageContext.request.contextPath}/css/addAlertCss/main.css" rel="stylesheet" type="text/css"/>
	<script src="${pageContext.request.contextPath}/js/addAlertJs/qikoo.js"></script>
</head>
<!-- #f2f9fd #FAFAFA -->
<body class="b-f">

	<input type="hidden" id="powerRange" value="${user.powerRange}"/>
	<input type="hidden" id="userLevel" value="${user.level}"/>
	<input type="hidden" id="toOpeUnitId" value="${user.unitId}"/>
	
	<!--导航条 - 公共部分-->
	<div class="navbar navbar-default" id="navbar">
		<script type="text/javascript">
			try{ace.settings.check('navbar' , 'fixed')}catch(e){}
		</script>
		<div class="navbar-container" id="navbar-container">
			<div class="navbar-header pull-left">
				<a href="javascript:void(0)" class="navbar-brand" onclick="toPowerIndex()">
					<small>
						<img alt="" src="${pageContext.request.contextPath}/images/icon.png" style="height:25px;width:27px"/>
						ESS印章制作系统
					</small>
				</a><!-- /.brand -->
			</div><!-- /.navbar-header -->


			<div class="navbar-header pull-right" role="navigation">
				<ul class="ace-nav">
					<c:if test="${fn:length(userList)>1 }">
						<li style="background-color: #62a8d1 !important;">
							<select id="otherUnit" name="otherUnit" onchange="toSealMakePasgeByUserId()">
								<option value="">--请选择--</option>
								<c:forEach items="${userList}" var="item">
									<option value="${item.userId }">${item.unit.unitName }</option>
								</c:forEach>
							</select>
						</li>
					</c:if>
					<li class="green">
						<a data-toggle="dropdown" class="dropdown-toggle" href="#">
							<i class="icon-envelope icon-animated-vertical" style="margin-top: 15px;" ></i>
							<span class="badge badge-success"></span>
						</a>
						<ul class="pull-right dropdown-navbar dropdown-menu dropdown-caret dropdown-close">
							<li>
								<a href="javascript:toMessage()">
									查看所有消息
									<i class="icon-arrow-right"></i>
								</a>
							</li>
						</ul>
					</li>

					<li class="grey">
						<a data-toggle="dropdown" class="dropdown-toggle" href="#">
							<i class="icon-tasks" style="margin-top: 15px;"></i>
							<span class="badge badge-grey"></span>
						</a>
						<ul class="pull-right dropdown-navbar navbar-pink dropdown-menu dropdown-caret dropdown-close">
							<li>
								<a onclick="toSealCenterIndexByAuth()"  href="javascript:void(0)">
									<div class="clearfix">
										<span class="pull-left">
											<i class="btn btn-xs no-hover" style="background-color: #3dc0b7 !important;border-color: #3dc0b7 !important;">
												<img src="${pageContext.request.contextPath}/images/home_icon_qianzhang.png" class="homeIconSize">
											</i>
											签章管理系统
										</span>
									</div>
								</a>
							</li>

							<li>
								<a onclick="toPersonManagerPageByAuth()"  href="javascript:void(0)">
									<div class="clearfix">
										<span class="pull-left">
											<i class="btn btn-xs no-hover" style="background-color: #fcae35 !important;border-color: #fcae35 !important;">
												<img src="${pageContext.request.contextPath}/images/home_icon_jigou.png" class="homeIconSize">
											</i>
											机构、人员管理
										</span>
									</div>
								</a>
							</li>

							<li>
								<a onclick="toSealMakePasgeByAuth()"  href="javascript:void(0)">
									<div class="clearfix">
										<span class="pull-left">
											<i class="btn btn-xs no-hover" style="background-color: #7167ef !important;border-color: #7167ef !important;">
												<img src="${pageContext.request.contextPath}/images/home_icon_zhizhang.png" class="homeIconSize">
											</i>
											制章系统
										</span>
									</div>
								</a>
							</li>

							<li>
								<a onclick="toLogIndexPageByAuth()"  href="javascript:void(0)">
									<div class="clearfix">
										<span class="pull-left">
											<i class="btn btn-xs no-hover" style="background-color: #5091f2 !important;border-color: #5091f2 !important;">
												<img src="${pageContext.request.contextPath}/images/home_icon_rizhi.png" class="homeIconSize">
											</i>
											日志系统
										</span>
									</div>
								</a>
							</li>

							<li>
								<a onclick="toUnitAndDepAndUserManagerPageByAuth()"  href="javascript:void(0)">
									<div class="clearfix">
										<span class="pull-left">
											<i class="btn btn-xs no-hover" style="background-color: #e5676e !important;border-color: #e5676e !important;">
												<img src="${pageContext.request.contextPath}/images/home_icon_renyuan.png" class="homeIconSize" />
											</i>
											基础人员信息管理
										</span>
									</div>
								</a>
							</li>
							<li>
								<a onclick="toSetUpPage()"  href="javascript:void(0)" >
									<div class="clearfix">
											<span class="pull-left">
												<i class="btn btn-xs no-hover" style="background-color: #228B22 !important;border-color: #228B22 !important;">
													<img src="${pageContext.request.contextPath}/images/setup2.png" class="homeIconSize">
												</i>
												设置
											</span>
									</div>
								</a>
							</li>
						</ul>
					</li>

					<li class="light-blue">
						<a data-toggle="dropdown" href="#" class="dropdown-toggle">
							<c:if test="${user.person.personImgBase64!='' }">
								<img class="nav-user-photo" src="data:image/gif;base64,${user.person.personImgBase64}" />
								<span class="user-info">
									<small>欢迎光临,</small>
									${user.person.personName}
								</span>
								<i class="icon-caret-down"></i>
							</c:if>
							<c:if test="${user.person.personImgBase64=='' }">
								<img class="nav-user-photo" src="${pageContext.request.contextPath}/images/home_icon_zhizhang.png"  />
								<span class="user-info">
									<small>欢迎光临,</small>
									${user.person.personName}
								</span>
								<i class="icon-caret-down"></i>
							</c:if>
						</a>

						<ul class="user-menu pull-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
							<li class="divider"></li>

							<li>
								<a href="${pageContext.request.contextPath}/system_out.html">
									<i class="icon-off"></i>
									退出
								</a>
							</li>
						</ul>
					</li>
				</ul><!-- /.ace-nav -->
			</div><!-- /.navbar-header -->
		</div><!-- /.container -->
	</div>

	<div class="ub b-f7" style="height: calc(100% - 45px);">
		<!--ztree之前版本-->
		<div class="zTreeDemoBackground left" style="width: 15%;height:100%;overflow:auto">
			<ul id="treeDemo" class="ztree" style=" height:100%;width:100%"></ul>
		</div>

		<div class="admin" style="width: 85%;overflow: auto; height:100%">
			<!--${pageContext.request.contextPath}/unit/showInfoPage-->
			<iframe scrolling="auto" rameborder="0" src="" name="right" id="mainiframe" width="100%" height="100%"></iframe>
		</div>
		<!--<div style="text-align:center;">-->
			<!--<p>来源:<a href="" target="_blank">北京诚利通数码技术有限公司</a></p>-->
		<!--</div>-->
	</div>

<script type="text/javascript">

	$(function setIframeHeight() {
		var iframe ;//高度初始化600，为了14寸笔记本
		iframe =document.getElementById('mainiframe');
		iframe.height="100%";
		//iframe.height="calc(100% - 30px)";

	});
	// 根据当前登陆用户的personId和用户选择的unitId刷新本页
	function toOtherUnitBySelectUnitId(){
		var selectUnitId = $("#otherUnit").val();
		if(selectUnitId != ""){
			window.location.href="${pageContext.request.contextPath}/unit/toUnitAndDepAndUserManagerPage?unitId="+selectUnitId+"";
		}
	}

	//点击节点，在右侧的主frame中显示
	function onclickNode(unitId){
		window.right.location.href="${pageContext.request.contextPath}/unit_page.html?toOpeUnitId="+unitId+"";
	}

	var setting = {
		data: {
			simpleData: {
				enable: true
			}
		}
	};

	$(function(){
		// 所属单位数量
		var unitSize = $("#unitSize").val();

		if(unitSize == 1){
			$("[name='otherUnit']").hide();
			$("[name='otherUnitBut']").hide();
		}
		// ZTreede的初始化
		var zNodes = ${unit_menu};
		$.fn.zTree.init($("#treeDemo"), setting, zNodes);

		$(".leftnav h2").click(function(){
			$(this).next().slideToggle(200);
			$(this).toggleClass("on");
	  	});

		$(".leftnav ul li a").click(function(){
			$("#a_leader_txt").text($(this).text());
			$(".leftnav ul li a").removeClass("on");
			$(this).addClass("on");
		})

		var toOpeUnitId = $("#toOpeUnitId").val();
		//alert(toOpeUnitId == "");
		if(toOpeUnitId != ""){
			window.right.location.href="${pageContext.request.contextPath}/unit_index.html";
		}

	});

	//点击消息中心，在右侧的主frame中显示
	function toMessage(){
		window.right.location.href="${pageContext.request.contextPath}/message/message_list.html";
	}

	// 调用验证方法获取相应参数
	function toAuth(url){
		$.getJSON("http://127.0.0.1:10102?jsoncallback=?", {
				ACT:"Q_AUTHTOKEN",
				VAL:""
			},
			function(data) {
				clientIsRunning = true;
				if(data[0].status == 0){
					var authToken = data[0].info;
					// 根据tokenId调用,获取personId和tokenId的状态
                    window.location.href = url+"?token="+authToken;
				}
				else if(data[0].status == 1){
					alert("请登录安全客户端");
				}else if(data[0].status == 2){
					alert("发生错误");
				}
				else{
					alert("无法解析返回值");
				}
			}
		);

	}

	//跳转到单位部门用户管理的界面
	function toUnitAndDepAndUserManagerPageByAuth(){

		// 校验是否有跳转到单位、部门、用户管理界面的权限
		$.post(
			"${pageContext.request.contextPath}/check_auth.html",
			{"isNeedCheckRange":"0","powerId":"sealTemplatPower_unitAndDepAndUserManager","currentUnitId":null},
			function(data){
				if(data.msg == 1){
					toAuth("http://192.168.1.113:8080/EssSealPlatform/unit/toUnitAndDepAndUserManagerPage");
					//window.location.href="${pageContext.request.contextPath}/unit/toUnitAndDepAndUserManagerPage";
				}else{
					qikoo.dialog.alert("您没有进入该模块的权限！");
				}
			},
			"json"
		);
	}


	//跳转到制章系统的界面
	function toSealMakePasgeByAuth(){

		// 校验是否有跳转到单位、部门、用户管理界面的权限
		$.post(
			"${pageContext.request.contextPath}/check_auth.html",
			{"isNeedCheckRange":"0","powerId":"makeSealPower_makeSealSys","currentUnitId":null},
			function(data){

				if(data.msg == 1){
					toAuth("http://192.168.1.113:8081/ess_make_system/index.html");
				}else{

					qikoo.dialog.alert("您没有进入该模块的权限！");
				}

			},
			"json"

		);
	}

    //跳转到制章系统的界面
    function toSealMakePasgeByUserId(){

		var userId =  $("#otherUnit").val();
        // 校验是否有跳转到单位、部门、用户管理界面的权限
        $.post(
            "${pageContext.request.contextPath}/check_auth.html",
            {"isNeedCheckRange":"0","powerId":"makeSealPower_makeSealSys","currentUnitId":null},
            function(data){
                if(data.msg == 1){
                    $.getJSON("http://127.0.0.1:10102?jsoncallback=?", {
                            ACT:"Q_AUTHTOKEN",
                            VAL:""
                        },
                        function(data) {
                            clientIsRunning = true;
                            if(data[0].status == 0){
                                var authToken = data[0].info;
                                // 根据tokenId调用,获取personId和tokenId的状态
                                window.location.href = "http://192.168.1.113:8081/ess_make_system/index.html"+"?token="+authToken+"&userId"+userId;
                            }
                            else if(data[0].status == 1){
                                alert("请登录安全客户端");
                            }else if(data[0].status == 2){
                                alert("发生错误");
                            }
                            else{
                                alert("无法解析返回值");
                            }
                        }
                    );

                }else{
                    qikoo.dialog.alert("您没有进入该模块的权限！");
                }

            },
            "json"

        );
    }

	// 跳转到人员管理的界面
	function toPersonManagerPageByAuth(){


		// 校验是否有跳转到人员管理管理界面的权限
		$.post(
			"${pageContext.request.contextPath}/check_auth.html",
			{"isNeedCheckRange":"0","powerId":"sealTemplatPower_personManager","currentUnitId":null},
			function(data){

				if(data.msg == 1){

					toAuth("http://192.168.1.113:8080/EssSealPlatform/person/toPersonManagerPage");
					<%----%>

				}else{
					qikoo.dialog.alert("您没有进入该模块的权限！");
				}
			},
			"json"

		);

	}

	function toSealCenterIndexByAuth(){

		// 校验是否有跳转到签章管理系统的权限
		$.post(
			"${pageContext.request.contextPath}/check_auth.html",
			{"isNeedCheckRange":"0","powerId":"sealCenterPower_sealCenterSys","currentUnitId":null},
			function(data){
				if(data.msg == 1){
					toAuth("http://192.168.1.113:8082/SealCenter/unit/toUserJurAndSealManagerPage");
				}else{
					qikoo.dialog.alert("您没有进入该模块的权限！");
				}
			},
			"json"
		);
	}
	// 跳转到日志系统
	function toLogIndexPageByAuth(){
		var personId = $("#personId").val();
		// 校验是否有跳转到签章管理系统的权限
		$.post(
			"${pageContext.request.contextPath}/check_auth.html",
			{"isNeedCheckRange":"0","powerId":"sealTemplatPower_viewLogIndex","currentUnitId":null},
			function(data){
				if(data.msg == 1){
					toAuth("http://192.168.1.113:8080/EssSealPlatform/log/toLogIndexPage");
				}else{
					qikoo.dialog.alert("您没有进入该模块的权限！");
				}
			},
			"json"
		);
	}
    // 跳转到设置页面
    function toSetUpPage(){

        // 校验是否有跳转到单位、部门、用户管理界面的权限
        $.post(

            "${pageContext.request.contextPath}/check_auth.html",
            {"isNeedCheckRange":"0","powerId":"sealTemplatPower_setup","currentUnitId":null},
            function(data){

                if(data.msg == 1){

                    toAuth("http://192.168.1.113:8080/EssSealPlatform/unit/toSetUpIndex");

                }else{

                    qikoo.dialog.alert("您没有进入该模块的权限！");

                }

            },
            "json"

        );

    }
//跳转到首页
function toPowerIndex(){
    toAuth("http://192.168.1.113:8080/EssSealPlatform/user/toPowerIndex");
}
</script>


</body>
</html>