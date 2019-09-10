<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
	<head>
	    <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <meta name="description" content="">
        <meta name="author" content="">

         <title>User profile</title>

        <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="${contextPath}/resources/css/common.css" rel="stylesheet">

        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->

		</head>
	<body>
	    <div class="container">
	        <h3 id="form_header" class="text-warning" align="center">User Form</h3>
	        <div>&nbsp;</div>

			<!-- User input form to add a new user or update the existing user-->
	        <c:url var="saveUrl" value="/api/user/save" />
	        <form:form id="user_form" modelAttribute="userAttr" method="POST" action="${saveUrl}">
	        	<form:hidden path="id" />
	            <label for="user_name">Enter Name: </label>
	            <form:input id="user_name" cssClass="form-control" path="name" />
	            <label for="user_name">Enter Email: </label>
	            <form:input type="email" id="user_name" cssClass="form-control"  path="email" />
	            <label for="user_name">Enter Phone: </label>
                <form:input type="number" id="user_name" cssClass="form-control"   path="phone" />
                <label for="user_name">Enter City: </label>
                <form:input id="user_name" cssClass="form-control"  path="city" />
	            <div>&nbsp;</div>

	            <button id="saveBtn" type="submit" class="btn btn-primary">Save</button>
	        </form:form>
	    </div>
	    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
        <script src="${contextPath}/resources/js/bootstrap.min.js"></script>

	</body>
</html>