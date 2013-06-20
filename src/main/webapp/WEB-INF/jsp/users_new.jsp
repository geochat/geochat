<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>

<c:url value="/js/jquery-1.7.2.min.js" var="jquery_url"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>GeoChat: Users</title>
</head>
<body>

<style>
.error {
	color: #ff0000;
	font-style: italic;
}
#container1 {
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
<div id="container1" align="center">
  <h2>Registration:</h2>
  <table>
	  <f:form action="/users/new" modelAttribute="user" method="POST">
	  <tr>
	  	<td>
	    	Login:
	    </td>
	    <td> 
	    	<f:input path="login" /> 
	    </td>
	    <td>
	    	<f:errors path="login" cssClass="error"/>
	    </td>
	</tr>
	<tr>
		<td>
	    	Password:
	    </td>
	    <td> 
	    	<f:password path="password" /> 
	    </td>
	    <td>
	    	<f:errors path="password" cssClass="error"/>
	    </td>
	</tr>
	<tr>
		<td colspan="2" align="center">
			<f:button>submit</f:button>
		</td>
	</tr>
	</f:form>
</table>
	<font color="red">${errorMessage}</font>
	</div>
</body>
</html>