<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>

<c:url value="/js/jquery-1.7.2.min.js" var="jquery_url"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>GeoChat: Users</title>
<style>
#container {
    width:400px; /*Ширина блока*/
    height:200px; /*Высота блока*/
    margin:0 auto;
    overflow:visible;
    position:absolute;
    left:50%;
    top:50%;
    margin-left:-200px; /*Смещаем блок на половину всей ширины влево*/
    margin-top:-100px; /*Смещаем блок на половину высоты вверх*/
    background:#ffffff;
}
</style>
</head>
<body>
	<div id="container" align="center">
  <h2>Please login!</h2>
  <f:form action="/users/users_login" modelAttribute="user" method="POST">
    name: <f:input path="login" /> <br/>
    password: <f:password path="password" /> <br/>
    <br/><f:button>submit</f:button>
  </f:form>
	<font color="red">${errorMessage}</font>
	</div>
</body>
</html>