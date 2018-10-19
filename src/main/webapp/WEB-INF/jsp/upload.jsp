
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Insert title here</title>
</head>
<body>
<form method="post" enctype="multipart/form-data" action="signCert.do">
    <h1>使用spring mvc提供的类的方法上传文件</h1>
    <input type="file" name="files" >
    <input type="hidden" name="issuerUnitId" value="001">
    <input type="submit" value="upload" />
</form>
</body>
</html>