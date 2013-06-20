<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>

<c:url value="/js/jquery-1.7.2.min.js" var="jquery_url"/>
<c:url value="/css/main.css" var="css_url"/>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="${css_url }" media="screen" rel="stylesheet" type="text/css" />

<title>GeoChat: Chat</title>

<script src="${jquery_url}" type="text/javascript"></script>
<script src="http://api-maps.yandex.ru/2.0/?load=package.full&lang=ru-RU" type="text/javascript"></script>
<script type="text/javascript">
	function submitMd5()
	{
		window.setInterval(function() {document.forms[0].submit();}, 10000);
		if (${md5Exist} == false)
			document.forms[0].submit();
	}
	function ctrlEnter(e){
		if (e.keyCode == 13 && e.ctrlKey == true) {
			document.forms[0].elements[6].value="true";
			document.forms[0].submit();
		}

		}
	</script>

<!-- Yandex Map -->
<script type="text/javascript">

ymaps.ready(function () { 
  var ymap = new ymaps.Map('ymap', {
	    center: [${userLat}, ${userLng}],
	    zoom: 10,
	    behaviors: ['default', 'scrollZoom']
	  }, 
	  {balloonMaxWidth: 200}
	);
	ymap.setBounds([[${seBound.latitude}, ${seBound.longitude}],[${nwBound.latitude}, ${nwBound.longitude}]]);    
	// Добавляем радиус на карту.
	var circle = new ymaps.Circle([[${userLat}, ${userLng}], ${userRadius}], {}, {
    geodesic: true});
	ymap.geoObjects.add(circle);
	// Создаем метки с юзерами
	<c:forEach var="user" items="${usersInRadius}">
		var placemark = new ymaps.Placemark([${user.userGeo.latitude}, ${user.userGeo.longitude}],
		{balloonContent: '${user.userLogin}'}, 
		{balloonCloseButton: true});
		ymap.geoObjects.add(placemark);
	</c:forEach>
	ymap.balloon.open([${userLat}, ${userLng}], {contentHeader: 'Я здесь!'});
});
</script>	
	
<style type="text/css">
<!--
.outMessageField {
	height: 100px;
	width: 100%;
}
.chatTextField {
	height: 400px;
	width: 100%;
}
.submitButton {
	height: 100px;
	width: 100px;
}
.yMap {
	height: 600px;
	width: 600px;
	border: 1px solid gray; 
}
-->
</style>
</head>
<body>
    <h2>Logged in as ${userLogin}</h2>
    <table width="100%"  border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td>
			<f:form name="chatForm" action="/chat/index" modelAttribute="chatForm" method="post">
			<table width="400px" border="0" align="center" cellspacing="10">
				<tr align="center" valign="top">
				  <td colspan="2">
					<textarea name="chatText" disabled class="chatTextField">${messagesListString}</textarea>
				  </td>
			  </tr>
				<tr align="center" valign="middle">
				  <td width="750">
					<f:textarea class="outMessageField" path="outMessage" onkeyup="ctrlEnter(event);" />
				  </td>
				  <td>
					<f:input type="submit" path="submitButton" class="submitButton" value="Submit"/>
				  </td>
			  </tr>
			  <tr>
			  	<td align="right">Only Followers: <f:checkbox path="isPrivate" value="true" /></td>
		      	<td><f:input type="submit" path="changeGeo" value="Change geo"/></td>
		      </tr>
			</table>
			<f:input type="hidden" path="submitByCtrl" value="false"/>
			<f:input type="hidden" path="md5" value="${md5}"/>
			</f:form>
		</td>
        <td><div class="yMap" id="ymap"></div></td>
      </tr>
    </table>
  </div>
</body>
<script type="text/javascript">submitMd5();</script>
</html>
