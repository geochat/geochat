<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>

<c:url value="/js/jquery-1.7.2.min.js" var="jquery_url"/>
<c:url value="/css/main.css" var="css_url"/>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="${css_url}" media="screen" rel="stylesheet" type="text/css" />

<title>GeoChat: Location</title>

<script src="${jquery_url}" type="text/javascript"></script>
<script src="http://api-maps.yandex.ru/2.0/?load=package.full&lang=ru-RU" type="text/javascript"></script>
<script type="text/javascript">
	function submitMd5()
	{
		window.setInterval(function() {document.forms[0].submit();}, 30000);
		if (${md5Exist} == false)
		{
			document.forms[0].submit();
		}
	}
	</script>
<!-- Yandex Map -->
<script type="text/javascript">

ymaps.ready(function () { 
  var latitude=59.938779;
  var longitude=30.314321;
  if (${geoExist}) {
  	latitude = ${lat};
  	longitude = ${lng};  	 
  }
  var ymap = new ymaps.Map('ymap', {
	    center: [latitude, longitude],
	    zoom: 10,
	    behaviors: ['default', 'scrollZoom']
	  }, 
	  {
	      balloonMaxWidth: 200
	  }
	);
	
	if (${geoExist}) {
		ymap.balloon.open(ymap.getCenter(), {
         contentHeader: 'Я здесь!',
         contentBody: '<p>Местоположение: ' + [ymap.getCenter()[0].toPrecision(6), ymap.getCenter()[1].toPrecision(6)].join(', ') + '</p>',
         contentFooter: '<sup>GeoChat Locator</sup>'
       });
	}
       	// Создаем метки с юзерами
	<c:forEach var="user" items="${usersOnline}">
		var placemark = new ymaps.Placemark([${user.userGeo.latitude}, ${user.userGeo.longitude}],
		{balloonContent: '${user.userLogin}'}, 
		{balloonCloseButton: true});
		ymap.geoObjects.add(placemark);
	</c:forEach> 
  ymap.events.add('click', function (e) {
      if (ymap.balloon.isOpen()) {
    	  ymap.balloon.close();
      }
      
      var coords = e.get('coordPosition');
      ymap.balloon.open(coords, {
        contentHeader: 'Я здесь!',
        contentBody: '<p>Местоположение: ' + [coords[0].toPrecision(6), coords[1].toPrecision(6)].join(', ') + '</p>',
        contentFooter: '<sup>GeoChat Locator</sup>'
      });
      $('#latitude').val(coords[0].toPrecision(6));
      $('#longitude').val(coords[1].toPrecision(6));
  });
  
});
</script>
<style type="text/css">
<!--
.yMap {
	height: 500px;
	width: 500px;
	border: 1px solid gray; 
}
-->
</style>
</head>
<body>

  <h2>Location page</h2>
  <h3>Click on map to set your location</h3>
  
    <table width="100%" border="0" align="center" cellpadding="5" cellspacing="0">
      <tr align="left" valign="top">
        <td width=500px><div class="yMap" id="ymap"></div></td>
        <td>
		<f:form name="locationForm" action="/location/index" modelAttribute="location" method="post">
		  <table width="100px" border="0" align="left" cellpadding="1" cellspacing="0">
		    <tr>
		      <td>Широта:</td>
		      <td>
		        <f:input path="latitude" readonly="true"/>
		      </td>
		    </tr>
		    <tr>
		      <td>Долгота:</td>
		      <td>
		        <f:input path="longitude" readonly="true"/>
		      </td>
		    </tr>
			<tr>
		      <td>Радиус (км):</td>
		      <td>
		        <f:input path="radius"/>
		      </td>
		    </tr>
			<tr>
		      <td>Ретрансляция:</td>
		      <td><f:checkbox path="canRedirect" value="redirectOn" />
		      </td>
		    </tr>
		    <tr>
				<td><f:input type="submit" path="enterChatButton" value="Enter chat"/></td>
			</tr>
		  </table>
		  <f:input type="hidden" path="md5" value="${md5}"/>

    	</f:form>
    	</td>
      </tr>
</table>

</body>
<script type="text/javascript">submitMd5();</script>
</html>
