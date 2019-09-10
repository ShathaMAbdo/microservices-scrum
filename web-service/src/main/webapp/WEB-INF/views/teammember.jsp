<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>User</title>
	    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
	</head>
	<body>
	<div class="container">
                    <nav class="navbar navbar-default">
                        <div class="container-fluid">
                            <div class="navbar-header">
                                <ul class="nav navbar-nav">
                                    <li><a class="navbar-brand" href="/" th:href="@{/}">Home</a></li>
                                    <li><a href="/api/user/users"style="color:red;" th:href="@{/api/user/users}">USERS</a></li>
                                    <li><a href="/api/team/teams" style="color:red;"th:href="@{/api/team/teams}">TEAMS</a></li>
                                    <li><a href="/api/sprint/sprints"style="color:red;" th:href="@{api/sprint/sprints}">SPRINTS</a></li>
                                    <li><a href="/api/sprint/add"style="color:red;" th:href="@{/api/sprint/add}">Create Sprint</a></li>
                                </ul>

                            </div>
                        </div>
                    </nav>
             </div>
		<div class="container">
			<h2 id="article_header" class="text-warning" align="center">All members</h2>
	    	<div>&nbsp;</div>
          <form:form id="team_form" modelAttribute="team" >
	        	<form:hidden path="id"  />
	    	<!-- Table to display the user list from the mongo database -->
	    	<table id="members_table" class="table">
	        	<thead>
	            	<tr align="center">
	            		<th>Id</th>
	            		<th>Name</th>
	            		<th colspan="2"></th>

	            	</tr>
	        	</thead>
	        	<tbody>
	            	<c:forEach items="${members}" var="user">
	                	<tr align="left">
	                    	<td><c:out value="${user.id}" /></td>
	                    	<td><c:out value="${user.name}" /></td>
	                    	<td>
	                        	<c:url var="addUrl" value="/api/team/addmember?id=${user.id}&teamid=${team.id}" /><a id="add" href="${addUrl}" class="btn btn-success">Select</a>
	                    	</td>
	                	</tr>
	            	</c:forEach>
	        	</tbody>
	    	</table>
		</div>
	</body>
</html>